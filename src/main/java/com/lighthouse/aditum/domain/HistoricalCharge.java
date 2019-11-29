package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A HistoricalCharge.
 */
@Entity
@Table(name = "historical_charge")
public class HistoricalCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "concept", nullable = false)
    private String concept;

    @NotNull
    @Column(name = "ammount", nullable = false)
    private String ammount;

    @Column(name = "abono")
    private String abono;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private Integer type;

    @NotNull
    @Column(name = "state", nullable = false)
    private Integer state;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @Column(name = "total")
    private String total;

    @ManyToOne(optional = false)
    @NotNull
    private House house;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public HistoricalCharge date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getConcept() {
        return concept;
    }

    public HistoricalCharge concept(String concept) {
        this.concept = concept;
        return this;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getAmmount() {
        return ammount;
    }

    public HistoricalCharge ammount(String ammount) {
        this.ammount = ammount;
        return this;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getAbono() {
        return abono;
    }

    public HistoricalCharge abono(String abono) {
        this.abono = abono;
        return this;
    }

    public void setAbono(String abono) {
        this.abono = abono;
    }

    public Integer getType() {
        return type;
    }

    public HistoricalCharge type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public HistoricalCharge state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public HistoricalCharge deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getTotal() {
        return total;
    }

    public HistoricalCharge total(String total) {
        this.total = total;
        return this;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public House getHouse() {
        return house;
    }

    public HistoricalCharge house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HistoricalCharge historicalCharge = (HistoricalCharge) o;
        if (historicalCharge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historicalCharge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoricalCharge{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", concept='" + getConcept() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", abono='" + getAbono() + "'" +
            ", type='" + getType() + "'" +
            ", state='" + getState() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", total='" + getTotal() + "'" +
            "}";
    }
}
