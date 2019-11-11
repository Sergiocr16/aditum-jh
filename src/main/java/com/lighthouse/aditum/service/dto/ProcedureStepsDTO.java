package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProcedureSteps entity.
 */
public class ProcedureStepsDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private Integer number;

    @NotNull
    private Integer deleted;

    private Long proceduresId;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

        ProcedureStepsDTO procedureStepsDTO = (ProcedureStepsDTO) o;
        if(procedureStepsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureStepsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureStepsDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", number=" + getNumber() +
            ", deleted=" + getDeleted() +
            "}";
    }
}
