package de.martinclan.fulltextacrossdatasources.entities.patients;

import de.martinclan.fulltextacrossdatasources.entities.elasticsearch.CombinedDataEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "prename", nullable = false)
    private String prename;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public CombinedDataEntity asCombinedEntity() {
        CombinedDataEntity result = new CombinedDataEntity();
        result.setPatientId(id);
        result.setPrename(prename);
        result.setSurname(surname);
        result.setBirthdate(birthdate);
        return result;
    }
}
