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

    private Integer deleted;

    private Long houseId;

    private HouseDTO house;

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
        if(vehiculeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehiculeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehiculeDTO{" +
            "id=" + getId() +
            ", licenseplate='" + getLicenseplate() + "'" +
            ", brand='" + getBrand() + "'" +
            ", color='" + getColor() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", type='" + getType() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }

    public HouseDTO getHouse() {
        return house;
    }

    public void setHouse(HouseDTO house) {
        this.house = house;
    }
}
