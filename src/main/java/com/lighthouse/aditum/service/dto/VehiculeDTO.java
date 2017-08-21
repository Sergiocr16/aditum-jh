package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Vehicule entity.
 */
public class VehiculeDTO implements Serializable {

    private Long id;

    @NotNull
    private String licenseplate;

    @NotNull
    private String brand;

    @NotNull
    private String color;

    @Min(value = 0)
    @Max(value = 1)
    private Integer enabled;

    private String type;

    private Long houseId;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
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

        VehiculeDTO vehiculeDTO = (VehiculeDTO) o;

        if ( ! Objects.equals(id, vehiculeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VehiculeDTO{" +
            "id=" + id +
            ", licenseplate='" + licenseplate + "'" +
            ", brand='" + brand + "'" +
            ", color='" + color + "'" +
            ", enabled='" + enabled + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
