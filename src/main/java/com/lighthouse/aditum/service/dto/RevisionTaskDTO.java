package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the RevisionTask entity.
 */
public class RevisionTaskDTO implements Serializable {

    private Long id;

    private String description;

    private Boolean done;

    private String observations;

    private String observationFile;

    private Integer hasObservations;

    private Long revisionId;

    private Long revisionTaskCategoryId;

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

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getObservationFile() {
        return observationFile;
    }

    public void setObservationFile(String observationFile) {
        this.observationFile = observationFile;
    }

    public Integer getHasObservations() {
        return hasObservations;
    }

    public void setHasObservations(Integer hasObservations) {
        this.hasObservations = hasObservations;
    }

    public Long getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Long revisionId) {
        this.revisionId = revisionId;
    }

    public Long getRevisionTaskCategoryId() {
        return revisionTaskCategoryId;
    }

    public void setRevisionTaskCategoryId(Long revisionTaskCategoryId) {
        this.revisionTaskCategoryId = revisionTaskCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RevisionTaskDTO revisionTaskDTO = (RevisionTaskDTO) o;
        if(revisionTaskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revisionTaskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevisionTaskDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", done='" + isDone() + "'" +
            ", observations='" + getObservations() + "'" +
            ", observationFile='" + getObservationFile() + "'" +
            ", hasObservations=" + getHasObservations() +
            "}";
    }
}
