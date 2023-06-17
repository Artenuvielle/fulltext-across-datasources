package de.martinclan.fulltextacrossdatasources.repositories.elasticsearch;

import de.martinclan.fulltextacrossdatasources.entities.elasticsearch.CombinedDataEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombinedDataRepository extends ElasticsearchRepository<CombinedDataEntity, String> {}
