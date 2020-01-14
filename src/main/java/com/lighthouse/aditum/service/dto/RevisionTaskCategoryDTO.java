package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RevisionTaskCategory entity.
 */
public class RevisionTaskCategoryDTO implements Serializable {

    private Long id;

    private String description;

    private Integer deleted;

    private Integer order;

    private Integer type;

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RevisionTaskCategoryDTO revisionTaskCategoryDTO = (RevisionTaskCategoryDTO) o;
        if(revisionTaskCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revisionTaskCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevisionTaskCategoryDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", deleted=" + getDeleted() +
            ", order=" + getOrder() +
            ", type=" + getType() +
            "}";
    }
}
