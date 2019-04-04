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

    private Boolean bookCommonArea;

    private Boolean incomeStatement;

    private Boolean monthlyIncomeStatement;

    private Boolean egressReport;

    private Boolean incomeFolio;

    private Boolean egressFolio;

    private String egressFolioSerie;

    private String egressFolioNumber;

    private Long companyId;

    private int saveInBitacora;

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

    public Boolean isBookCommonArea() {
        return bookCommonArea;
    }

    public void setBookCommonArea(Boolean bookCommonArea) {
        this.bookCommonArea = bookCommonArea;
    }

    public Boolean isIncomeStatement() {
        return incomeStatement;
    }

    public void setIncomeStatement(Boolean incomeStatement) {
        this.incomeStatement = incomeStatement;
    }

    public Boolean isMonthlyIncomeStatement() {
        return monthlyIncomeStatement;
    }

    public void setMonthlyIncomeStatement(Boolean monthlyIncomeStatement) {
        this.monthlyIncomeStatement = monthlyIncomeStatement;
    }

    public Boolean isEgressReport() {
        return egressReport;
    }

    public void setEgressReport(Boolean egressReport) {
        this.egressReport = egressReport;
    }

    public Boolean isIncomeFolio() {
        return incomeFolio;
    }

    public void setIncomeFolio(Boolean incomeFolio) {
        this.incomeFolio = incomeFolio;
    }

    public Boolean isEgressFolio() {
        return egressFolio;
    }

    public void setEgressFolio(Boolean egressFolio) {
        this.egressFolio = egressFolio;
    }

    public String getEgressFolioSerie() {
        return egressFolioSerie;
    }

    public void setEgressFolioSerie(String egressFolioSerie) {
        this.egressFolioSerie = egressFolioSerie;
    }

    public String getEgressFolioNumber() {
        return egressFolioNumber;
    }

    public void setEgressFolioNumber(String egressFolioNumber) {
        this.egressFolioNumber = egressFolioNumber;
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
            ", bookCommonArea='" + isBookCommonArea() + "'" +
            ", incomeStatement='" + isIncomeStatement() + "'" +
            ", monthlyIncomeStatement='" + isMonthlyIncomeStatement() + "'" +
            ", egressReport='" + isEgressReport() + "'" +
            ", incomeFolio='" + isIncomeFolio() + "'" +
            ", egressFolio='" + isEgressFolio() + "'" +
            ", egressFolioSerie='" + getEgressFolioSerie() + "'" +
            ", egressFolioNumber='" + getEgressFolioNumber() + "'" +
            "}";
    }

    public int getSaveInBitacora() {
        return saveInBitacora;
    }

    public void setSaveInBitacora(int saveInBitacora) {
        this.saveInBitacora = saveInBitacora;
    }
}
