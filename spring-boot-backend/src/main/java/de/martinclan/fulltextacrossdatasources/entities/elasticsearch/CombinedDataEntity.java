package de.martinclan.fulltextacrossdatasources.entities.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Document(indexName = "combined_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CombinedDataEntity {
    @Id
    @Field(type = FieldType.Long)
    private Long patientId;

    @Field(type = FieldType.Long)
    private Long addressId;

    @Field(type = FieldType.Keyword)
    private String prename;

    @Field(type = FieldType.Keyword)
    private String surname;

    @Field(type = FieldType.Date)
    private LocalDate birthdate;

    @Field(type = FieldType.Text)
    private String street;

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Keyword)
    private String postalCode;

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
