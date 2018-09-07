package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotNull
    @Column(name = "uses_fine_per_day", nullable = false)
    private Boolean usesFinePerDay;

    @Column(name = "fine_per_day")
    private String finePerDay;

    @Column(name = "days_to_be_defaulter")
    private Integer daysToBeDefaulter;

    @Column(name = "percentage_fine_per_day")
    private Double percentageFinePerDay;

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public Boolean isUsesFinePerDay() {
        return usesFinePerDay;
    }

    public AdministrationConfiguration usesFinePerDay(Boolean usesFinePerDay) {
        this.usesFinePerDay = usesFinePerDay;
        return this;
    }

    public void setUsesFinePerDay(Boolean usesFinePerDay) {
        this.usesFinePerDay = usesFinePerDay;
    }

    public String getFinePerDay() {
        return finePerDay;
    }

    public AdministrationConfiguration finePerDay(String finePerDay) {
        this.finePerDay = finePerDay;
        return this;
    }

    public void setFinePerDay(String finePerDay) {
        this.finePerDay = finePerDay;
    }

    public Integer getDaysToBeDefaulter() {
        return daysToBeDefaulter;
    }

    public AdministrationConfiguration daysToBeDefaulter(Integer daysToBeDefaulter) {
        this.daysToBeDefaulter = daysToBeDefaulter;
        return this;
    }

    public void setDaysToBeDefaulter(Integer daysToBeDefaulter) {
        this.daysToBeDefaulter = daysToBeDefaulter;
    }

    public Double getPercentageFinePerDay() {
        return percentageFinePerDay;
    }

    public AdministrationConfiguration percentageFinePerDay(Double percentageFinePerDay) {
        this.percentageFinePerDay = percentageFinePerDay;
        return this;
    }

    public void setPercentageFinePerDay(Double percentageFinePerDay) {
        this.percentageFinePerDay = percentageFinePerDay;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
            ", folioNumber=" + getFolioNumber() +
            ", usesFinePerDay='" + isUsesFinePerDay() + "'" +
            ", finePerDay='" + getFinePerDay() + "'" +
            ", daysToBeDefaulter=" + getDaysToBeDefaulter() +
            ", percentageFincePerDay=" + getPercentageFinePerDay() +
            "}";
    }
}
