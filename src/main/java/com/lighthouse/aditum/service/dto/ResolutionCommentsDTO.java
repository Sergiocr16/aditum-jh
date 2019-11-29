package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ResolutionComments entity.
 */
public class ResolutionCommentsDTO implements Serializable {

    private Long id;

    private String description;

    private ZonedDateTime creationDate;

    private ZonedDateTime editedDate;

    private Integer deleted;

    private Long adminInfoId;

    private Long resolutionId;

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

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(ZonedDateTime editedDate) {
        this.editedDate = editedDate;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getAdminInfoId() {
        return adminInfoId;
    }

    public void setAdminInfoId(Long adminInfoId) {
        this.adminInfoId = adminInfoId;
    }

    public Long getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(Long resolutionId) {
        this.resolutionId = resolutionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResolutionCommentsDTO resolutionCommentsDTO = (ResolutionCommentsDTO) o;
        if(resolutionCommentsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resolutionCommentsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResolutionCommentsDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", editedDate='" + getEditedDate() + "'" +
            ", deleted=" + getDeleted() +
            "}";
    }
}
