package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A HistoricalPositive.
 */
@Entity
@Table(name = "historical_positive")
public class HistoricalPositive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total")
    private String total;

    @Column(name = "housenumber")
    private String housenumber;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @ManyToOne
    private Company company;

    @ManyToOne
    private House house;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public HistoricalPositive total(String total) {
        this.total = total;
        return this;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public HistoricalPositive housenumber(String housenumber) {
        this.housenumber = housenumber;
        return this;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public HistoricalPositive date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Company getCompany() {
        return company;
    }

    public HistoricalPositive company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public House getHouse() {
        return house;
    }

    public HistoricalPositive house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
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
        HistoricalPositive historicalPositive = (HistoricalPositive) o;
        if (historicalPositive.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historicalPositive.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoricalPositive{" +
            "id=" + getId() +
            ", total='" + getTotal() + "'" +
            ", housenumber='" + getHousenumber() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
