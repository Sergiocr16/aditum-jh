package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A RevisionTask.
 */
@Entity
@Table(name = "revision_task")
public class RevisionTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "done")
    private Boolean done;

    @Column(name = "observations")
    private String observations;

    @Column(name = "observation_file")
    private String observationFile;

    @Column(name = "has_observations")
    private Integer hasObservations;

    @ManyToOne
    private Revision revision;

    @ManyToOne
    private RevisionTaskCategory revisionTaskCategory;

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

    public RevisionTask description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isDone() {
        return done;
    }

    public RevisionTask done(Boolean done) {
        this.done = done;
        return this;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getObservations() {
        return observations;
    }

    public RevisionTask observations(String observations) {
        this.observations = observations;
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getObservationFile() {
        return observationFile;
    }

    public RevisionTask observationFile(String observationFile) {
        this.observationFile = observationFile;
        return this;
    }

    public void setObservationFile(String observationFile) {
        this.observationFile = observationFile;
    }

    public Integer getHasObservations() {
        return hasObservations;
    }

    public RevisionTask hasObservations(Integer hasObservations) {
        this.hasObservations = hasObservations;
        return this;
    }

    public void setHasObservations(Integer hasObservations) {
        this.hasObservations = hasObservations;
    }

    public Revision getRevision() {
        return revision;
    }

    public RevisionTask revision(Revision revision) {
        this.revision = revision;
        return this;
    }

    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    public RevisionTaskCategory getRevisionTaskCategory() {
        return revisionTaskCategory;
    }

    public RevisionTask revisionTaskCategory(RevisionTaskCategory revisionTaskCategory) {
        this.revisionTaskCategory = revisionTaskCategory;
        return this;
    }

    public void setRevisionTaskCategory(RevisionTaskCategory revisionTaskCategory) {
        this.revisionTaskCategory = revisionTaskCategory;
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
        RevisionTask revisionTask = (RevisionTask) o;
        if (revisionTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revisionTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevisionTask{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", done='" + isDone() + "'" +
            ", observations='" + getObservations() + "'" +
            ", observationFile='" + getObservationFile() + "'" +
            ", hasObservations=" + getHasObservations() +
            "}";
    }
}
