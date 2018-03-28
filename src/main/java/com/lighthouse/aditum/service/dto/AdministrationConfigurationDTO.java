package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the AdministrationConfiguration entity.
 */
public class AdministrationConfigurationDTO implements Serializable {

    private Long id;

    private String squareMetersPrice;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSquareMetersPrice() {
        return squareMetersPrice;
    }

    public void setSquareMetersPrice(String squareMetersPrice) {
        this.squareMetersPrice = squareMetersPrice;
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

        AdministrationConfigurationDTO administrationConfigurationDTO = (AdministrationConfigurationDTO) o;
        if(administrationConfigurationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), administrationConfigurationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdministrationConfigurationDTO{" +
            "id=" + getId() +
            ", squareMetersPrice='" + getSquareMetersPrice() + "'" +
            "}";
    }
}
