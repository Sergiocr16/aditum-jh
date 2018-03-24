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

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private House house;

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
            "}";
    }
}
