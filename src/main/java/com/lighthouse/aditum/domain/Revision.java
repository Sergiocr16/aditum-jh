package com.lighthouse.aditum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Revision.
 */
@Entity
@Table(name = "revision")
public class Revision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "execution_date")
    private ZonedDateTime executionDate;

    @Column(name = "observations")
    private String observations;

    @Column(name = "status")
    private Integer status;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "file_url")
    private String fileUrl;

    @OneToMany(mappedBy = "revision")
    @JsonIgnore
    private Set<RevisionTask> revisionTasks = new HashSet<>();

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    public Revision name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getExecutionDate() {
        return executionDate;
    }

    public Revision executionDate(ZonedDateTime executionDate) {
        this.executionDate = executionDate;
        return this;
    }

    public void setExecutionDate(ZonedDateTime executionDate) {
        this.executionDate = executionDate;
    }

    public String getObservations() {
        return observations;
    }

    public Revision observations(String observations) {
        this.observations = observations;
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getStatus() {
        return status;
    }

    public Revision status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Revision fileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Set<RevisionTask> getRevisionTasks() {
        return revisionTasks;
    }

    public Revision revisionTasks(Set<RevisionTask> revisionTasks) {
        this.revisionTasks = revisionTasks;
        return this;
    }

    public Revision addRevisionTask(RevisionTask revisionTask) {
        this.revisionTasks.add(revisionTask);
        revisionTask.setRevision(this);
        return this;
    }

    public Revision removeRevisionTask(RevisionTask revisionTask) {
        this.revisionTasks.remove(revisionTask);
        revisionTask.setRevision(null);
        return this;
    }

    public void setRevisionTasks(Set<RevisionTask> revisionTasks) {
        this.revisionTasks = revisionTasks;
    }

    public Company getCompany() {
        return company;
    }

    public Revision company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        Revision revision = (Revision) o;
        if (revision.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revision.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Revision{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", executionDate='" + getExecutionDate() + "'" +
            ", observations='" + getObservations() + "'" +
            ", status=" + getStatus() +
            ", fileUrl='" + getFileUrl() + "'" +
            "}";
    }
}
