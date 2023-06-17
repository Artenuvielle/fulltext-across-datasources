package de.martinclan.fulltextacrossdatasources.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import de.martinclan.fulltextacrossdatasources.entities.addresses.Address;
import de.martinclan.fulltextacrossdatasources.entities.elasticsearch.CombinedDataEntity;
import de.martinclan.fulltextacrossdatasources.entities.patients.Patient;
import de.martinclan.fulltextacrossdatasources.repositories.addresses.AddressRepository;
import de.martinclan.fulltextacrossdatasources.repositories.elasticsearch.CombinedDataRepository;
import de.martinclan.fulltextacrossdatasources.repositories.patients.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CombinedDataService {
    private final Logger logger = LoggerFactory.getLogger(CombinedDataService.class);

    @Autowired private CombinedDataRepository combinedDataRepository;
    @Autowired private ElasticsearchClient elasticsearchClient;
    @Autowired private PatientRepository patientRepository;
    @Autowired private AddressRepository addressRepository;

    public void fetchAndIndexData() {
        logger.info("Creating index");

        // Fetch data from both data sources
        List<Patient> patients = patientRepository.findAll();
        List<Address> addresses = addressRepository.findAll();
        logger.info("Retrieval of data for index complete");

        // Merge data into CombinedDataEntities
        Collection<CombinedDataEntity> combinedData = mergeData(patients, addresses);
        logger.info("Merging data for index complete");

        combinedDataRepository.deleteAll();
        logger.info("Deleted previous index");

        // Index data into Elasticsearch
        combinedDataRepository.saveAll(combinedData);
        logger.info("Index created successfully");
    }

    private Collection<CombinedDataEntity> mergeData(List<Patient> patients, List<Address> addresses) {
        Map<Long, CombinedDataEntity> combinedDataMap = patients.stream()
                .collect(Collectors.toMap(Patient::getId, Patient::asCombinedEntity));
        for (Address address : addresses) {
            CombinedDataEntity entity = combinedDataMap.get(address.getPatientId());
            if (entity != null) {
                entity.setAddressId(address.getId());
                entity.setStreet(address.getStreet());
                entity.setCity(address.getCity());
                entity.setPostalCode(address.getPostalCode());
                combinedDataMap.put(address.getPatientId(), entity);
            } else {
                logger.error("Cannot combine address with id " + address.getId());
            }
        }
        return combinedDataMap.values();
    }

    public HitsMetadata<CombinedDataEntity> searchAndPaginate(
            String search,
            int pageNumber,
            int pageSize,
            String sortField,
            Boolean sortDesc
    ) throws IOException {
        SearchResponse<CombinedDataEntity> response = elasticsearchClient.search(s -> {
            SearchRequest.Builder res = s
                    .index("combined_data")
                    .from(pageSize * pageNumber)
                    .size(pageSize);
            if (search != null)
                res = res.query(q -> q.multiMatch(mm -> mm
                                .fields(
                                        "prename",
                                        "surname",
                                        "patient_id",
                                        "street",
                                        "city",
                                        "postal_code"
                                )
                                .fuzziness("AUTO")
                                .type(TextQueryType.BestFields)
                                .query(search)
                ));
            if (sortField != null)
                res = res.sort(b -> b.field(a -> a
                        .field(sortField)
                        .order(sortDesc ? SortOrder.Desc : SortOrder.Asc)
                ));
            return res;
        }, CombinedDataEntity.class);
        return response.hits();
    }
}
