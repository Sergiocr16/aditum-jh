package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Revision entity.
 */
public class RevisionDTO implements Serializable {

    private Long id;

    private String name;

    private ZonedDateTime executionDate;

    private String observations;

    private Integer status;

    private Long companyId;

    private List<RevisionTaskDTO> revisionTasks;

    public List<RevisionTaskDTO> getRevisionTasks() {
        return revisionTasks;
    }

    public void setRevisionTasks(List<RevisionTaskDTO> revisionTasks) {
        this.revisionTasks = revisionTasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(ZonedDateTime executionDate) {
        this.executionDate = executionDate;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RevisionDTO revisionDTO = (RevisionDTO) o;
        if(revisionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revisionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevisionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", executionDate='" + getExecutionDate() + "'" +
            ", observations='" + getObservations() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
