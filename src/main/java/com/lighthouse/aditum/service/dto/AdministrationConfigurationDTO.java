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
    private Boolean hasSubCharges;

    private Integer daysToBeDefaulter;

    private Integer daysToSendEmailBeforeBeDefaulter;

    private Integer increaseSubChargeDays;

    private Double subchargePercentage;

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

    public Boolean isHasSubCharges() {
        return hasSubCharges;
    }

    public void setHasSubCharges(Boolean hasSubCharges) {
        this.hasSubCharges = hasSubCharges;
    }

    public Integer getDaysToBeDefaulter() {
        return daysToBeDefaulter;
    }

    public void setDaysToBeDefaulter(Integer daysToBeDefaulter) {
        this.daysToBeDefaulter = daysToBeDefaulter;
    }

    public Integer getDaysToSendEmailBeforeBeDefaulter() {
        return daysToSendEmailBeforeBeDefaulter;
    }

    public void setDaysToSendEmailBeforeBeDefaulter(Integer daysToSendEmailBeforeBeDefaulter) {
        this.daysToSendEmailBeforeBeDefaulter = daysToSendEmailBeforeBeDefaulter;
    }

    public Integer getIncreaseSubChargeDays() {
        return increaseSubChargeDays;
    }

    public void setIncreaseSubChargeDays(Integer increaseSubChargeDays) {
        this.increaseSubChargeDays = increaseSubChargeDays;
    }

    public Double getSubchargePercentage() {
        return subchargePercentage;
    }

    public void setSubchargePercentage(Double subchargePercentage) {
        this.subchargePercentage = subchargePercentage;
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
            ", hasSubCharges='" + isHasSubCharges() + "'" +
            ", daysToBeDefaulter=" + getDaysToBeDefaulter() +
            ", daysToSendEmailBeforeBeDefaulter=" + getDaysToSendEmailBeforeBeDefaulter() +
            ", increaseSubChargeDays=" + getIncreaseSubChargeDays() +
            ", subchargePercentage=" + getSubchargePercentage() +
            "}";
    }
}
