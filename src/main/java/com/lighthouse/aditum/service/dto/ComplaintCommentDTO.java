package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ComplaintComment entity.
 */
public class ComplaintCommentDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private ZonedDateTime creationDate;

    private ZonedDateTime editedDate;

    private ResidentDTO resident;

    @NotNull
    private Integer deleted;

    private Long residentId;

    private Long adminInfoId;

    private Long complaintId;

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

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public Long getAdminInfoId() {
        return adminInfoId;
    }

    public void setAdminInfoId(Long adminInfoId) {
        this.adminInfoId = adminInfoId;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComplaintCommentDTO complaintCommentDTO = (ComplaintCommentDTO) o;
        if(complaintCommentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), complaintCommentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComplaintCommentDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", editedDate='" + getEditedDate() + "'" +
            ", deleted=" + getDeleted() +
            "}";
    }

    public ResidentDTO getResident() {
        return resident;
    }

    public void setResident(ResidentDTO resident) {
        this.resident = resident;
    }
}
