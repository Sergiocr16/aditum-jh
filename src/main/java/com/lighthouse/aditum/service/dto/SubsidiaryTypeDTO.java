package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the SubsidiaryType entity.
 */
public class SubsidiaryTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private String size;

    private Double jointOwnershipPercentage;

    private String ammount;

    private Integer limit;

    private Integer subsidiaryType;

    private Integer deleted;

    private Long subsidiaryCategoryId;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getJointOwnershipPercentage() {
        return jointOwnershipPercentage;
    }

    public void setJointOwnershipPercentage(Double jointOwnershipPercentage) {
        this.jointOwnershipPercentage = jointOwnershipPercentage;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSubsidiaryType() {
        return subsidiaryType;
    }

    public void setSubsidiaryType(Integer subsidiaryType) {
        this.subsidiaryType = subsidiaryType;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getSubsidiaryCategoryId() {
        return subsidiaryCategoryId;
    }

    public void setSubsidiaryCategoryId(Long subsidiaryCategoryId) {
        this.subsidiaryCategoryId = subsidiaryCategoryId;
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

        SubsidiaryTypeDTO subsidiaryTypeDTO = (SubsidiaryTypeDTO) o;
        if(subsidiaryTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subsidiaryTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubsidiaryTypeDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", size='" + getSize() + "'" +
            ", jointOwnershipPercentage=" + getJointOwnershipPercentage() +
            ", ammount='" + getAmmount() + "'" +
            ", limit=" + getLimit() +
            ", subsidiaryType=" + getSubsidiaryType() +
            ", deleted=" + getDeleted() +
            "}";
    }
}
