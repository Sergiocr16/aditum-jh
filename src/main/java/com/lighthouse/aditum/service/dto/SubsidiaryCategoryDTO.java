package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the SubsidiaryCategory entity.
 */
public class SubsidiaryCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer categoryType;

    private Long companyId;

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

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
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

        SubsidiaryCategoryDTO subsidiaryCategoryDTO = (SubsidiaryCategoryDTO) o;
        if(subsidiaryCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subsidiaryCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubsidiaryCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", categoryType=" + getCategoryType() +
            "}";
    }
}
