package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the RevisionConfig entity.
 */
public class RevisionConfigDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer deleted;

    private Long companyId;

    private List<RevisionConfigTaskDTO> configTasks;

    public List<RevisionConfigTaskDTO> getConfigTasks() {
        return configTasks;
    }

    public void setConfigTasks(List<RevisionConfigTaskDTO> configTasks) {
        this.configTasks = configTasks;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

        RevisionConfigDTO revisionConfigDTO = (RevisionConfigDTO) o;
        if(revisionConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revisionConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevisionConfigDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", deleted=" + getDeleted() +
            "}";
    }
}
