package com.lighthouse.aditum.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AnnouncementComment entity.
 */
public class AnnouncementCommentDTO implements Serializable {

    private Long id;

    @NotNull
    private String comment;

    @NotNull
    private ZonedDateTime creationDate;


    private ZonedDateTime editedDate;

    @NotNull
    private Integer deleted;

    private Long residentId;

    private ResidentDTO resident;

    private Long announcementId;

    private Long adminInfoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }

    public Long getAdminInfoId() {
        return adminInfoId;
    }

    public void setAdminInfoId(Long adminInfoId) {
        this.adminInfoId = adminInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnouncementCommentDTO announcementCommentDTO = (AnnouncementCommentDTO) o;
        if (announcementCommentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), announcementCommentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnnouncementCommentDTO{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", resident=" + getResidentId() +
            ", announcement=" + getAnnouncementId() +
            ", admininfo=" + getAdminInfoId() +
            "}";
    }

    public ResidentDTO getResident() {
        return resident;
    }

    public void setResident(ResidentDTO resident) {
        this.resident = resident;
    }

    public ZonedDateTime getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(ZonedDateTime editedDate) {
        this.editedDate = editedDate;
    }

    @NotNull
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(@NotNull Integer deleted) {
        this.deleted = deleted;
    }
}
