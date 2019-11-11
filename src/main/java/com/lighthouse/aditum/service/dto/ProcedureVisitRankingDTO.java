package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProcedureVisitRanking entity.
 */
public class ProcedureVisitRankingDTO implements Serializable {

    private Long id;

    private String question;

    private Double puntuation;

    private Integer deleted;

    private Long procedureVisitsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public Long getProcedureVisitsId() {
        return procedureVisitsId;
    }

    public void setProcedureVisitsId(Long procedureVisitsId) {
        this.procedureVisitsId = procedureVisitsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcedureVisitRankingDTO procedureVisitRankingDTO = (ProcedureVisitRankingDTO) o;
        if(procedureVisitRankingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureVisitRankingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureVisitRankingDTO{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", puntuation=" + getPuntuation() +
            ", deleted=" + getDeleted() +
            "}";
    }
}
