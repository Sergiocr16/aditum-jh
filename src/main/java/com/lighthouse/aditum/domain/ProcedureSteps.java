package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A ProcedureSteps.
 */
@Entity
@Table(name = "procedure_steps")
public class ProcedureSteps implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "jhi_number", nullable = false)
    private Integer number;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @ManyToOne
    private Procedures procedures;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public ProcedureSteps description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumber() {
        return number;
    }

    public ProcedureSteps number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public ProcedureSteps deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Procedures getProcedures() {
        return procedures;
    }

    public ProcedureSteps procedures(Procedures procedures) {
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
        ProcedureSteps procedureSteps = (ProcedureSteps) o;
        if (procedureSteps.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureSteps.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureSteps{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", number=" + getNumber() +
            ", deleted=" + getDeleted() +
            "}";
    }
}
