package de.martinclan.fulltextacrossdatasources.services;

import com.github.javafaker.Faker;
import de.martinclan.fulltextacrossdatasources.entities.patients.Patient;
import de.martinclan.fulltextacrossdatasources.repositories.patients.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    private final Faker faker = new Faker();

    public void generatePatients(long n) {
        EntityGenerator.generate("patients", n, patientRepository, () -> {
            Patient patient = new Patient();
            // Generate random Patient
            com.github.javafaker.Name fakerName = faker.name();
            patient.setPrename(fakerName.firstName());
            patient.setSurname(fakerName.lastName());
            patient.setBirthdate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            return patient;
        });
    }
}
