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

    private Integer daysTobeDefaulter;

    @NotNull
    private Boolean hasSubcharges;

    private Double subchargePercentage;

    private Double subchargeAmmount;

    private Integer daysToSendEmailBeforeBeDefaulter;

    private Boolean usingSubchargePercentage;

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

    public Integer getDaysTobeDefaulter() {
        return daysTobeDefaulter;
    }

    public void setDaysTobeDefaulter(Integer daysTobeDefaulter) {
        this.daysTobeDefaulter = daysTobeDefaulter;
    }

    public Boolean isHasSubcharges() {
        return hasSubcharges;
    }

    public void setHasSubcharges(Boolean hasSubcharges) {
        this.hasSubcharges = hasSubcharges;
    }

    public Double getSubchargePercentage() {
        return subchargePercentage;
    }

    public void setSubchargePercentage(Double subchargePercentage) {
        this.subchargePercentage = subchargePercentage;
    }

    public Double getSubchargeAmmount() {
        return subchargeAmmount;
    }

    public void setSubchargeAmmount(Double subchargeAmmount) {
        this.subchargeAmmount = subchargeAmmount;
    }

    public Integer getDaysToSendEmailBeforeBeDefaulter() {
        return daysToSendEmailBeforeBeDefaulter;
    }

    public void setDaysToSendEmailBeforeBeDefaulter(Integer daysToSendEmailBeforeBeDefaulter) {
        this.daysToSendEmailBeforeBeDefaulter = daysToSendEmailBeforeBeDefaulter;
    }

    public Boolean isUsingSubchargePercentage() {
        return usingSubchargePercentage;
    }

    public void setUsingSubchargePercentage(Boolean usingSubchargePercentage) {
        this.usingSubchargePercentage = usingSubchargePercentage;
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
            ", daysTobeDefaulter=" + getDaysTobeDefaulter() +
            ", hasSubcharges='" + isHasSubcharges() + "'" +
            ", subchargePercentage=" + getSubchargePercentage() +
            ", subchargeAmmount=" + getSubchargeAmmount() +
            ", daysToSendEmailBeforeBeDefaulter=" + getDaysToSendEmailBeforeBeDefaulter() +
            ", usingSubchargePercentage='" + isUsingSubchargePercentage() + "'" +
            "}";
    }
}
