package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Complaint entity.
 */
public class ComplaintDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private String complaintType;

    @NotNull
    private Integer status;

    @NotNull
    private Integer deleted;

    @NotNull
    private ZonedDateTime creationDate;

    private ZonedDateTime resolutionDate;

    private Long houseId;

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

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(ZonedDateTime resolutionDate) {
        this.resolutionDate = resolutionDate;
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

        ComplaintDTO complaintDTO = (ComplaintDTO) o;
        if(complaintDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), complaintDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComplaintDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", complaintType='" + getComplaintType() + "'" +
            ", status=" + getStatus() +
            ", deleted=" + getDeleted() +
            ", creationDate='" + getCreationDate() + "'" +
            ", resolutionDate='" + getResolutionDate() + "'" +
            "}";
    }
}
