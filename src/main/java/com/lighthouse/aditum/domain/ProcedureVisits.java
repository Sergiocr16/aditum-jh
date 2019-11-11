package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A ProcedureVisits.
 */
@Entity
@Table(name = "procedure_visits")
public class ProcedureVisits implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visit_date")
    private ZonedDateTime visitDate;

    @Column(name = "duration")
    private String duration;

    @Column(name = "puntuation")
    private Double puntuation;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "is_done")
    private Integer isDone;

    @ManyToOne
    private Procedures procedures;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getVisitDate() {
        return visitDate;
    }

    public ProcedureVisits visitDate(ZonedDateTime visitDate) {
        this.visitDate = visitDate;
        return this;
    }

    public void setVisitDate(ZonedDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public String getDuration() {
        return duration;
    }

    public ProcedureVisits duration(String duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getPuntuation() {
        return puntuation;
    }

    public ProcedureVisits puntuation(Double puntuation) {
        this.puntuation = puntuation;
        return this;
    }

    public void setPuntuation(Double puntuation) {
        this.puntuation = puntuation;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public ProcedureVisits deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getIsDone() {
        return isDone;
    }

    public ProcedureVisits isDone(Integer isDone) {
        this.isDone = isDone;
        return this;
    }

    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }

    public Procedures getProcedures() {
        return procedures;
    }

    public ProcedureVisits procedures(Procedures procedures) {
        this.procedures = procedures;
        return this;
    }

    public void setProcedures(Procedures procedures) {
        this.procedures = procedures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcedureVisits procedureVisits = (ProcedureVisits) o;
        if (procedureVisits.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureVisits.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureVisits{" +
            "id=" + getId() +
            ", visitDate='" + getVisitDate() + "'" +
            ", duration='" + getDuration() + "'" +
            ", puntuation=" + getPuntuation() +
            ", deleted=" + getDeleted() +
            ", isDone=" + getIsDone() +
            "}";
    }
}
