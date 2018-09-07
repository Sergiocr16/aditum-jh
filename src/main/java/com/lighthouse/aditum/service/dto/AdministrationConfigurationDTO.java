package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the AdministrationConfiguration entity.
 */
public class AdministrationConfigurationDTO implements Serializable {

    private Long id;

    private String squareMetersPrice;

    private String folioSerie;

    private Integer folioNumber;

    @NotNull
    private Boolean usesFinePerDay;

    private String finePerDay;

    private Integer daysToBeDefaulter;

    private Double percentageFinePerDay;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSquareMetersPrice() {
        return squareMetersPrice;
    }

    public void setSquareMetersPrice(String squareMetersPrice) {
        this.squareMetersPrice = squareMetersPrice;
    }

    public String getFolioSerie() {
        return folioSerie;
    }

    public void setFolioSerie(String folioSerie) {
        this.folioSerie = folioSerie;
    }

    public Integer getFolioNumber() {
        return folioNumber;
    }

    public void setFolioNumber(Integer folioNumber) {
        this.folioNumber = folioNumber;
    }

    public Boolean isUsesFinePerDay() {
        return usesFinePerDay;
    }

    public void setUsesFinePerDay(Boolean usesFinePerDay) {
        this.usesFinePerDay = usesFinePerDay;
    }

    public String getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(String finePerDay) {
        this.finePerDay = finePerDay;
    }

    public Integer getDaysToBeDefaulter() {
        return daysToBeDefaulter;
    }

    public void setDaysToBeDefaulter(Integer daysToBeDefaulter) {
        this.daysToBeDefaulter = daysToBeDefaulter;
    }

    public Double getPercentageFinePerDay() {
        return percentageFinePerDay;
    }

    public void setPercentageFinePerDay(Double percentageFinePerDay) {
        this.percentageFinePerDay = percentageFinePerDay;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdministrationConfigurationDTO administrationConfigurationDTO = (AdministrationConfigurationDTO) o;
        if(administrationConfigurationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), administrationConfigurationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdministrationConfigurationDTO{" +
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
