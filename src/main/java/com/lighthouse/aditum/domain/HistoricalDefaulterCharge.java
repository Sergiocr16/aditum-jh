package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A HistoricalDefaulterCharge.
 */
@Entity
@Table(name = "historical_defaulter_charge")
public class HistoricalDefaulterCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "concept")
    private String concept;

    @Column(name = "consecutive")
    private String consecutive;

    @Column(name = "ammount")
    private String ammount;

    @Column(name = "left_to_pay")
    private String leftToPay;

    @Column(name = "abonado")
    private String abonado;

    @Column(name = "defaulters_day")
    private String defaultersDay;

    @ManyToOne
    private HistoricalDefaulter historicalDefaulter;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public HistoricalDefaulterCharge type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public HistoricalDefaulterCharge date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getConcept() {
        return concept;
    }

    public HistoricalDefaulterCharge concept(String concept) {
        this.concept = concept;
        return this;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getConsecutive() {
        return consecutive;
    }

    public HistoricalDefaulterCharge consecutive(String consecutive) {
        this.consecutive = consecutive;
        return this;
    }

    public void setConsecutive(String consecutive) {
        this.consecutive = consecutive;
    }

    public String getAmmount() {
        return ammount;
    }

    public HistoricalDefaulterCharge ammount(String ammount) {
        this.ammount = ammount;
        return this;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getLeftToPay() {
        return leftToPay;
    }

    public HistoricalDefaulterCharge leftToPay(String leftToPay) {
        this.leftToPay = leftToPay;
        return this;
    }

    public void setLeftToPay(String leftToPay) {
        this.leftToPay = leftToPay;
    }

    public String getAbonado() {
        return abonado;
    }

    public HistoricalDefaulterCharge abonado(String abonado) {
        this.abonado = abonado;
        return this;
    }

    public void setAbonado(String abonado) {
        this.abonado = abonado;
    }

    public String getDefaultersDay() {
        return defaultersDay;
    }

    public HistoricalDefaulterCharge defaultersDay(String defaultersDay) {
        this.defaultersDay = defaultersDay;
        return this;
    }

    public void setDefaultersDay(String defaultersDay) {
        this.defaultersDay = defaultersDay;
    }

    public HistoricalDefaulter getHistoricalDefaulter() {
        return historicalDefaulter;
    }

    public HistoricalDefaulterCharge historicalDefaulter(HistoricalDefaulter historicalDefaulter) {
        this.historicalDefaulter = historicalDefaulter;
        return this;
    }

    public void setHistoricalDefaulter(HistoricalDefaulter historicalDefaulter) {
        this.historicalDefaulter = historicalDefaulter;
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
        HistoricalDefaulterCharge historicalDefaulterCharge = (HistoricalDefaulterCharge) o;
        if (historicalDefaulterCharge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historicalDefaulterCharge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoricalDefaulterCharge{" +
            "id=" + getId() +
            ", type=" + getType() +
            ", date='" + getDate() + "'" +
            ", concept='" + getConcept() + "'" +
            ", consecutive='" + getConsecutive() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", leftToPay='" + getLeftToPay() + "'" +
            ", abonado='" + getAbonado() + "'" +
            ", defaultersDay='" + getDefaultersDay() + "'" +
            "}";
    }
}
