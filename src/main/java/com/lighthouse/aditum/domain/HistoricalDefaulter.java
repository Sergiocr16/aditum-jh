package com.lighthouse.aditum.domain;


import com.lighthouse.aditum.service.dto.HistoricalDefaulterChargeDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;


/**
 * A HistoricalDefaulter.
 */
@Entity
@Table(name = "historical_defaulter")
public class HistoricalDefaulter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total")
    private String total;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "categories")
    private String categories;

    @Column(name = "housenumber")
    private String housenumber;

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

    public HistoricalDefaulter total(String total) {
        this.total = total;
        return this;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public HistoricalDefaulter date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getCategories() {
        return categories;
    }

    public HistoricalDefaulter categories(String categories) {
        this.categories = categories;
        return this;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public HistoricalDefaulter housenumber(String housenumber) {
        this.housenumber = housenumber;
        return this;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public Company getCompany() {
        return company;
    }

    public HistoricalDefaulter company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public House getHouse() {
        return house;
    }

    public HistoricalDefaulter house(House house) {
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
        HistoricalDefaulter historicalDefaulter = (HistoricalDefaulter) o;
        if (historicalDefaulter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historicalDefaulter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoricalDefaulter{" +
            "id=" + getId() +
            ", total='" + getTotal() + "'" +
            ", date='" + getDate() + "'" +
            ", categories='" + getCategories() + "'" +
            ", housenumber='" + getHousenumber() + "'" +
            "}";
    }
}
