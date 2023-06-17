package de.martinclan.fulltextacrossdatasources.services;

import com.github.javafaker.Faker;
import de.martinclan.fulltextacrossdatasources.entities.addresses.Address;
import de.martinclan.fulltextacrossdatasources.entities.patients.Patient;
import de.martinclan.fulltextacrossdatasources.repositories.addresses.AddressRepository;
import de.martinclan.fulltextacrossdatasources.repositories.patients.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired private PatientRepository patientRepository;
    @Autowired private AddressRepository addressRepository;

    private final Faker faker = new Faker();

    public void generateAddresses(long n) {
        EntityGenerator.generate("addresses", n, addressRepository, () -> {
            Address address = new Address();
            // This can potentially create multiple addresses for the same patient, but
            // it is statistically unlikely for very few addresses on many patients
            Patient randomPatient = patientRepository.findAll(
                    PageRequest.of((int)(Math.random() * n), 1)
            ).stream().findFirst().get();
            com.github.javafaker.Address fakerAddress = faker.address();
            // Generate random Address
            address.setPatientId(randomPatient.getId());
            address.setStreet(fakerAddress.streetAddress());
            address.setCity(fakerAddress.city());
            address.setPostalCode(fakerAddress.zipCode());
            return address;
        });
    }
}
