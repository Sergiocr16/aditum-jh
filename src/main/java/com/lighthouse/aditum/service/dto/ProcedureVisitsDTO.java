package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProcedureVisits entity.
 */
public class ProcedureVisitsDTO implements Serializable {

    private Long id;

    private ZonedDateTime visitDate;

    private String duration;

    private Double puntuation;

    private Integer deleted;

    private Integer isDone;

    private Long proceduresId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(ZonedDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getPuntuation() {
        return puntuation;
    }

    public void setPuntuation(Double puntuation) {
        this.puntuation = puntuation;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getIsDone() {
        return isDone;
    }

    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }

    public Long getProceduresId() {
        return proceduresId;
    }

    public void setProceduresId(Long proceduresId) {
        this.proceduresId = proceduresId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcedureVisitsDTO procedureVisitsDTO = (ProcedureVisitsDTO) o;
        if(procedureVisitsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureVisitsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureVisitsDTO{" +
            "id=" + getId() +
            ", visitDate='" + getVisitDate() + "'" +
            ", duration='" + getDuration() + "'" +
            ", puntuation=" + getPuntuation() +
            ", deleted=" + getDeleted() +
            ", isDone=" + getIsDone() +
            "}";
    }
}
