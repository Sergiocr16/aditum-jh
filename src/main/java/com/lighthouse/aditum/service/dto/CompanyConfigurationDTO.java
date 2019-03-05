package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the CompanyConfiguration entity.
 */
public class CompanyConfigurationDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quantityhouses;

    @NotNull
    private Integer quantityadmins;

    @NotNull
    private Integer quantityaccessdoor;

    @NotNull
    private Integer hasContability;

    private ZonedDateTime minDate;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantityhouses() {
        return quantityhouses;
    }

    public void setQuantityhouses(Integer quantityhouses) {
        this.quantityhouses = quantityhouses;
    }

    public Integer getQuantityadmins() {
        return quantityadmins;
    }

    public void setQuantityadmins(Integer quantityadmins) {
        this.quantityadmins = quantityadmins;
    }

    public Integer getQuantityaccessdoor() {
        return quantityaccessdoor;
    }

    public void setQuantityaccessdoor(Integer quantityaccessdoor) {
        this.quantityaccessdoor = quantityaccessdoor;
    }

    public Integer getHasContability() {
        return hasContability;
    }

    public void setHasContability(Integer hasContability) {
        this.hasContability = hasContability;
    }

    public ZonedDateTime getMinDate() {
        return minDate;
    }

    public void setMinDate(ZonedDateTime minDate) {
        this.minDate = minDate;
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

        CompanyConfigurationDTO companyConfigurationDTO = (CompanyConfigurationDTO) o;
        if(companyConfigurationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyConfigurationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyConfigurationDTO{" +
            "id=" + getId() +
            ", quantityhouses=" + getQuantityhouses() +
            ", quantityadmins=" + getQuantityadmins() +
            ", quantityaccessdoor=" + getQuantityaccessdoor() +
            ", hasContability=" + getHasContability() +
            ", minDate='" + getMinDate() + "'" +
            "}";
    }
}
