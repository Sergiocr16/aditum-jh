package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AdministrationConfiguration.
 */
@Entity
@Table(name = "administration_configuration")
public class AdministrationConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "square_meters_price")
    private String squareMetersPrice;

    @Column(name = "folio_serie")
    private String folioSerie;

    @Column(name = "folio_number")
    private Integer folioNumber;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSquareMetersPrice() {
        return squareMetersPrice;
    }

    public AdministrationConfiguration squareMetersPrice(String squareMetersPrice) {
        this.squareMetersPrice = squareMetersPrice;
        return this;
    }

    public void setSquareMetersPrice(String squareMetersPrice) {
        this.squareMetersPrice = squareMetersPrice;
    }

    public String getFolioSerie() {
        return folioSerie;
    }

    public AdministrationConfiguration folioSerie(String folioSerie) {
        this.folioSerie = folioSerie;
        return this;
    }

    public void setFolioSerie(String folioSerie) {
        this.folioSerie = folioSerie;
    }

    public Integer getFolioNumber() {
        return folioNumber;
    }

    public AdministrationConfiguration folioNumber(Integer folioNumber) {
        this.folioNumber = folioNumber;
        return this;
    }

    public void setFolioNumber(Integer folioNumber) {
        this.folioNumber = folioNumber;
    }

    public Company getCompany() {
        return company;
    }

    public AdministrationConfiguration company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdministrationConfiguration administrationConfiguration = (AdministrationConfiguration) o;
        if (administrationConfiguration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), administrationConfiguration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdministrationConfiguration{" +
            "id=" + getId() +
            ", squareMetersPrice='" + getSquareMetersPrice() + "'" +
            ", folioSerie='" + getFolioSerie() + "'" +
            ", folioNumber='" + getFolioNumber() + "'" +
            "}";
    }
}
