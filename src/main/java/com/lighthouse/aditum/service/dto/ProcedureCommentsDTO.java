package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProcedureComments entity.
 */
public class ProcedureCommentsDTO implements Serializable {

    private Long id;

    @NotNull
    private String comment;

    private Integer deleted;

    private String creationDate;

    private Long proceduresId;

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Long getProceduresId() {
        return proceduresId;
    }

    public void setProceduresId(Long proceduresId) {
        this.proceduresId = proceduresId;
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

        ProcedureCommentsDTO procedureCommentsDTO = (ProcedureCommentsDTO) o;
        if(procedureCommentsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureCommentsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureCommentsDTO{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", deleted=" + getDeleted() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
