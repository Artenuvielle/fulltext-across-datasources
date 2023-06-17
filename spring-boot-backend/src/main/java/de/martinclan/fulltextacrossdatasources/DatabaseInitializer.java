package de.martinclan.fulltextacrossdatasources;

import de.martinclan.fulltextacrossdatasources.services.AddressService;
import de.martinclan.fulltextacrossdatasources.services.CombinedDataService;
import de.martinclan.fulltextacrossdatasources.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private static final long NUMBER_OF_PATIENTS = 100000;
    private static final long NUMBER_OF_ADDRESSES = 20;

    @Autowired private PatientService patientService;
    @Autowired private AddressService addressService;
    @Autowired private CombinedDataService combinedDataService;

    @Override
    public void run(String... args) {
        // Generate random data upon startup and create initial index
        patientService.generatePatients(NUMBER_OF_PATIENTS);
        addressService.generateAddresses(NUMBER_OF_ADDRESSES);
        combinedDataService.fetchAndIndexData();
    }
}
