package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A ResolutionComments.
 */
@Entity
@Table(name = "resolution_comments")
public class ResolutionComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "edited_date")
    private ZonedDateTime editedDate;

    @Column(name = "deleted")
    private Integer deleted;

    @ManyToOne
    private AdminInfo adminInfo;

    @ManyToOne
    private Resolution resolution;

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

    public ResolutionComments description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public ResolutionComments creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getEditedDate() {
        return editedDate;
    }

    public ResolutionComments editedDate(ZonedDateTime editedDate) {
        this.editedDate = editedDate;
        return this;
    }

    public void setEditedDate(ZonedDateTime editedDate) {
        this.editedDate = editedDate;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public ResolutionComments deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public AdminInfo getAdminInfo() {
        return adminInfo;
    }

    public ResolutionComments adminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
        return this;
    }

    public void setAdminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public ResolutionComments resolution(Resolution resolution) {
        this.resolution = resolution;
        return this;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
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
        ResolutionComments resolutionComments = (ResolutionComments) o;
        if (resolutionComments.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resolutionComments.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResolutionComments{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", editedDate='" + getEditedDate() + "'" +
            ", deleted=" + getDeleted() +
            "}";
    }
}
