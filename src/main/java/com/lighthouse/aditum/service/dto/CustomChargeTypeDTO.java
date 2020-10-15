package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the CustomChargeType entity.
 */
public class CustomChargeTypeDTO implements Serializable {

    private Long id;

    private String description;

    private Integer type;

    private Integer status;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

        CustomChargeTypeDTO customChargeTypeDTO = (CustomChargeTypeDTO) o;
        if(customChargeTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customChargeTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomChargeTypeDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", type=" + getType() +
            ", status=" + getStatus() +
            "}";
    }
}
