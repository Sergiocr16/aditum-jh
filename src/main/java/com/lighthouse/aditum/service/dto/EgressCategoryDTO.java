package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the EgressCategory entity.
 */
public class EgressCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String group;

    @NotNull
    private String category;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

        EgressCategoryDTO egressCategoryDTO = (EgressCategoryDTO) o;
        if(egressCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), egressCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EgressCategoryDTO{" +
            "id=" + getId() +
            ", group='" + getGroup() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
