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

    @Column(name = "days_tobe_defaulter")
    private Integer daysTobeDefaulter;

    @NotNull
    @Column(name = "has_subcharges", nullable = false)
    private Boolean hasSubcharges;

    @Column(name = "subcharge_percentage")
    private Double subchargePercentage;

    @Column(name = "subcharge_ammount")
    private Double subchargeAmmount;

    @Column(name = "days_to_send_email_before_be_defaulter")
    private Integer daysToSendEmailBeforeBeDefaulter;

    @Column(name = "using_subcharge_percentage")
    private Boolean usingSubchargePercentage;

    @Column(name = "book_common_area")
    private Boolean bookCommonArea;

    @Column(name = "income_statement")
    private Boolean incomeStatement;

    @Column(name = "monthly_income_statement")
    private Boolean monthlyIncomeStatement;

    @Column(name = "egress_report")
    private Boolean egressReport;

    @Column(name = "income_folio")
    private Boolean incomeFolio;

    @Column(name = "egress_folio")
    private Boolean egressFolio;

    @Column(name = "egress_folio_serie")
    private String egressFolioSerie;

    @Column(name = "egress_folio_number")
    private String egressFolioNumber;

    @Column(name = "initial_configuration")
    private Integer initialConfiguration;

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

    public Integer getDaysTobeDefaulter() {
        return daysTobeDefaulter;
    }

    public AdministrationConfiguration daysTobeDefaulter(Integer daysTobeDefaulter) {
        this.daysTobeDefaulter = daysTobeDefaulter;
        return this;
    }

    public void setDaysTobeDefaulter(Integer daysTobeDefaulter) {
        this.daysTobeDefaulter = daysTobeDefaulter;
    }

    public Boolean isHasSubcharges() {
        return hasSubcharges;
    }

    public AdministrationConfiguration hasSubcharges(Boolean hasSubcharges) {
        this.hasSubcharges = hasSubcharges;
        return this;
    }

    public void setHasSubcharges(Boolean hasSubcharges) {
        this.hasSubcharges = hasSubcharges;
    }

    public Double getSubchargePercentage() {
        return subchargePercentage;
    }

    public AdministrationConfiguration subchargePercentage(Double subchargePercentage) {
        this.subchargePercentage = subchargePercentage;
        return this;
    }

    public void setSubchargePercentage(Double subchargePercentage) {
        this.subchargePercentage = subchargePercentage;
    }

    public Double getSubchargeAmmount() {
        return subchargeAmmount;
    }

    public AdministrationConfiguration subchargeAmmount(Double subchargeAmmount) {
        this.subchargeAmmount = subchargeAmmount;
        return this;
    }

    public void setSubchargeAmmount(Double subchargeAmmount) {
        this.subchargeAmmount = subchargeAmmount;
    }

    public Integer getDaysToSendEmailBeforeBeDefaulter() {
        return daysToSendEmailBeforeBeDefaulter;
    }

    public AdministrationConfiguration daysToSendEmailBeforeBeDefaulter(Integer daysToSendEmailBeforeBeDefaulter) {
        this.daysToSendEmailBeforeBeDefaulter = daysToSendEmailBeforeBeDefaulter;
        return this;
    }

    public void setDaysToSendEmailBeforeBeDefaulter(Integer daysToSendEmailBeforeBeDefaulter) {
        this.daysToSendEmailBeforeBeDefaulter = daysToSendEmailBeforeBeDefaulter;
    }

    public Boolean isUsingSubchargePercentage() {
        return usingSubchargePercentage;
    }

    public AdministrationConfiguration usingSubchargePercentage(Boolean usingSubchargePercentage) {
        this.usingSubchargePercentage = usingSubchargePercentage;
        return this;
    }

    public void setUsingSubchargePercentage(Boolean usingSubchargePercentage) {
        this.usingSubchargePercentage = usingSubchargePercentage;
    }

    public Boolean isBookCommonArea() {
        return bookCommonArea;
    }

    public AdministrationConfiguration bookCommonArea(Boolean bookCommonArea) {
        this.bookCommonArea = bookCommonArea;
        return this;
    }

    public void setBookCommonArea(Boolean bookCommonArea) {
        this.bookCommonArea = bookCommonArea;
    }

    public Boolean isIncomeStatement() {
        return incomeStatement;
    }

    public AdministrationConfiguration incomeStatement(Boolean incomeStatement) {
        this.incomeStatement = incomeStatement;
        return this;
    }

    public void setIncomeStatement(Boolean incomeStatement) {
        this.incomeStatement = incomeStatement;
    }

    public Boolean isMonthlyIncomeStatement() {
        return monthlyIncomeStatement;
    }

    public AdministrationConfiguration monthlyIncomeStatement(Boolean monthlyIncomeStatement) {
        this.monthlyIncomeStatement = monthlyIncomeStatement;
        return this;
    }

    public void setMonthlyIncomeStatement(Boolean monthlyIncomeStatement) {
        this.monthlyIncomeStatement = monthlyIncomeStatement;
    }

    public Boolean isEgressReport() {
        return egressReport;
    }

    public AdministrationConfiguration egressReport(Boolean egressReport) {
        this.egressReport = egressReport;
        return this;
    }

    public void setEgressReport(Boolean egressReport) {
        this.egressReport = egressReport;
    }

    public Boolean isIncomeFolio() {
        return incomeFolio;
    }

    public AdministrationConfiguration incomeFolio(Boolean incomeFolio) {
        this.incomeFolio = incomeFolio;
        return this;
    }

    public void setIncomeFolio(Boolean incomeFolio) {
        this.incomeFolio = incomeFolio;
    }

    public Boolean isEgressFolio() {
        return egressFolio;
    }

    public AdministrationConfiguration egressFolio(Boolean egressFolio) {
        this.egressFolio = egressFolio;
        return this;
    }

    public void setEgressFolio(Boolean egressFolio) {
        this.egressFolio = egressFolio;
    }

    public String getEgressFolioSerie() {
        return egressFolioSerie;
    }

    public AdministrationConfiguration egressFolioSerie(String egressFolioSerie) {
        this.egressFolioSerie = egressFolioSerie;
        return this;
    }

    public void setEgressFolioSerie(String egressFolioSerie) {
        this.egressFolioSerie = egressFolioSerie;
    }

    public String getEgressFolioNumber() {
        return egressFolioNumber;
    }

    public AdministrationConfiguration egressFolioNumber(String egressFolioNumber) {
        this.egressFolioNumber = egressFolioNumber;
        return this;
    }

    public void setEgressFolioNumber(String egressFolioNumber) {
        this.egressFolioNumber = egressFolioNumber;
    }

    public Integer getInitialConfiguration() {
        return initialConfiguration;
    }

    public AdministrationConfiguration initialConfiguration(Integer initialConfiguration) {
        this.initialConfiguration = initialConfiguration;
        return this;
    }

    public void setInitialConfiguration(Integer initialConfiguration) {
        this.initialConfiguration = initialConfiguration;
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
            ", initialConfiguration=" + getInitialConfiguration() +
            "}";
    }
}
