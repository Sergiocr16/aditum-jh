package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A RevisionConfigTask.
 */
@Entity
@Table(name = "revision_config_task")
public class RevisionConfigTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "observations")
    private String observations;

    @ManyToOne
    private RevisionTaskCategory revisionTaskCategory;

    @ManyToOne
    private RevisionConfig revisionConfig;

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

    public RevisionConfigTask description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservations() {
        return observations;
    }

    public RevisionConfigTask observations(String observations) {
        this.observations = observations;
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public RevisionTaskCategory getRevisionTaskCategory() {
        return revisionTaskCategory;
    }

    public RevisionConfigTask revisionTaskCategory(RevisionTaskCategory revisionTaskCategory) {
        this.revisionTaskCategory = revisionTaskCategory;
        return this;
    }

    public void setRevisionTaskCategory(RevisionTaskCategory revisionTaskCategory) {
        this.revisionTaskCategory = revisionTaskCategory;
    }

    public RevisionConfig getRevisionConfig() {
        return revisionConfig;
    }

    public RevisionConfigTask revisionConfig(RevisionConfig revisionConfig) {
        this.revisionConfig = revisionConfig;
        return this;
    }

    public void setRevisionConfig(RevisionConfig revisionConfig) {
        this.revisionConfig = revisionConfig;
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
        RevisionConfigTask revisionConfigTask = (RevisionConfigTask) o;
        if (revisionConfigTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revisionConfigTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevisionConfigTask{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", observations='" + getObservations() + "'" +
            "}";
    }
}
