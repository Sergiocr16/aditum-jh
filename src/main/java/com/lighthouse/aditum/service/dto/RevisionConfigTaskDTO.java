package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the RevisionConfigTask entity.
 */
public class RevisionConfigTaskDTO implements Serializable {

    private Long id;

    private String description;

    private String observations;

    private Long revisionTaskCategoryId;

    private Long revisionConfigId;

    private int deleted;

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

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

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Long getRevisionTaskCategoryId() {
        return revisionTaskCategoryId;
    }

    public void setRevisionTaskCategoryId(Long revisionTaskCategoryId) {
        this.revisionTaskCategoryId = revisionTaskCategoryId;
    }

    public Long getRevisionConfigId() {
        return revisionConfigId;
    }

    public void setRevisionConfigId(Long revisionConfigId) {
        this.revisionConfigId = revisionConfigId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RevisionConfigTaskDTO revisionConfigTaskDTO = (RevisionConfigTaskDTO) o;
        if(revisionConfigTaskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revisionConfigTaskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevisionConfigTaskDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", observations='" + getObservations() + "'" +
            "}";
    }
}
