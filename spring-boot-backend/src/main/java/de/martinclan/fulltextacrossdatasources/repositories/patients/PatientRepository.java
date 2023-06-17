package de.martinclan.fulltextacrossdatasources.repositories.patients;

import de.martinclan.fulltextacrossdatasources.entities.patients.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {}
