package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A ProcedureVisitRanking.
 */
@Entity
@Table(name = "procedure_visit_ranking")
public class ProcedureVisitRanking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "puntuation")
    private Double puntuation;

    @Column(name = "deleted")
    private Integer deleted;

    @ManyToOne
    private ProcedureVisits procedureVisits;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public ProcedureVisitRanking question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Double getPuntuation() {
        return puntuation;
    }

    public ProcedureVisitRanking puntuation(Double puntuation) {
        this.puntuation = puntuation;
        return this;
    }

    public void setPuntuation(Double puntuation) {
        this.puntuation = puntuation;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public ProcedureVisitRanking deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public ProcedureVisits getProcedureVisits() {
        return procedureVisits;
    }

    public ProcedureVisitRanking procedureVisits(ProcedureVisits procedureVisits) {
        this.procedureVisits = procedureVisits;
        return this;
    }

    public void setProcedureVisits(ProcedureVisits procedureVisits) {
        this.procedureVisits = procedureVisits;
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
        ProcedureVisitRanking procedureVisitRanking = (ProcedureVisitRanking) o;
        if (procedureVisitRanking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureVisitRanking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureVisitRanking{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", puntuation=" + getPuntuation() +
            ", deleted=" + getDeleted() +
            "}";
    }
}
