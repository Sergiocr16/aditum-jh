package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Vehicule.
 */
@Entity
@Table(name = "vehicule")
public class Vehicule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "licenseplate", nullable = false)
    private String licenseplate;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "color", nullable = false)
    private String color;

    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "enabled")
    private Integer enabled;

    @ManyToOne
    private House house;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseplate() {
        return licenseplate;
    }

    public Vehicule licenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
        return this;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public String getBrand() {
        return brand;
    }

    public Vehicule brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public Vehicule color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public Vehicule enabled(Integer enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public House getHouse() {
        return house;
    }

    public Vehicule house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public Vehicule company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vehicule vehicule = (Vehicule) o;
        if (vehicule.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, vehicule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Vehicule{" +
            "id=" + id +
            ", licenseplate='" + licenseplate + "'" +
            ", brand='" + brand + "'" +
            ", color='" + color + "'" +
            ", enabled='" + enabled + "'" +
            '}';
    }
}
