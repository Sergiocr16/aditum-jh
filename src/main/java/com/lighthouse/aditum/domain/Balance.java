package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Balance.
 */
@Entity
@Table(name = "balance")
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "extraordinary", nullable = false)
    private String extraordinary;

    @NotNull
    @Column(name = "common_areas", nullable = false)
    private String commonAreas;

    @NotNull
    @Column(name = "maintenance", nullable = false)
    private String maintenance;

    @Column(name = "water_charge")
    private String waterCharge;

    @Column(name = "others")
    private String others;

    @Column(name = "multa")
    private String multa;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private House house;

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtraordinary() {
        return extraordinary;
    }

    public Balance extraordinary(String extraordinary) {
        this.extraordinary = extraordinary;
        return this;
    }

    public void setExtraordinary(String extraordinary) {
        this.extraordinary = extraordinary;
    }

    public String getCommonAreas() {
        return commonAreas;
    }

    public Balance commonAreas(String commonAreas) {
        this.commonAreas = commonAreas;
        return this;
    }

    public void setCommonAreas(String commonAreas) {
        this.commonAreas = commonAreas;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public Balance maintenance(String maintenance) {
        this.maintenance = maintenance;
        return this;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getWaterCharge() {
        return waterCharge;
    }

    public Balance waterCharge(String waterCharge) {
        this.waterCharge = waterCharge;
        return this;
    }

    public void setWaterCharge(String waterCharge) {
        this.waterCharge = waterCharge;
    }

    public String getOthers() {
        return others;
    }

    public Balance others(String others) {
        this.others = others;
        return this;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getMulta() {
        return multa;
    }

    public Balance multa(String multa) {
        this.multa = multa;
        return this;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public House getHouse() {
        return house;
    }

    public Balance house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public Balance company(Company company) {
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
        Balance balance = (Balance) o;
        if (balance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), balance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Balance{" +
            "id=" + getId() +
            ", extraordinary='" + getExtraordinary() + "'" +
            ", commonAreas='" + getCommonAreas() + "'" +
            ", maintenance='" + getMaintenance() + "'" +
            ", waterCharge='" + getWaterCharge() + "'" +
            ", others='" + getOthers() + "'" +
            ", multa='" + getMulta() + "'" +
            "}";
    }
}
