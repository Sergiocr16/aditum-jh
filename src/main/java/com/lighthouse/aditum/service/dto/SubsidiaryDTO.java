package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Subsidiary entity.
 */
public class SubsidiaryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer deleted;

    private String description;

    private Long subsidiaryTypeId;

    private Long houseId;

    private SubsidiaryTypeDTO type;

    public SubsidiaryTypeDTO getType() {
        return type;
    }

    public void setType(SubsidiaryTypeDTO type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSubsidiaryTypeId() {
        return subsidiaryTypeId;
    }

    public void setSubsidiaryTypeId(Long subsidiaryTypeId) {
        this.subsidiaryTypeId = subsidiaryTypeId;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SubsidiaryDTO subsidiaryDTO = (SubsidiaryDTO) o;
        if(subsidiaryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subsidiaryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubsidiaryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", deleted=" + getDeleted() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
