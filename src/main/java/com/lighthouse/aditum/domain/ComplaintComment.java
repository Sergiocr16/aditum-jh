package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A ComplaintComment.
 */
@Entity
@Table(name = "complaint_comment")
public class ComplaintComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "edited_date")
    private ZonedDateTime editedDate;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @ManyToOne
    private Resident resident;

    @ManyToOne
    private AdminInfo adminInfo;

    @ManyToOne(optional = false)
    @NotNull
    private Complaint complaint;

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

    public ComplaintComment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public ComplaintComment creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getEditedDate() {
        return editedDate;
    }

    public ComplaintComment editedDate(ZonedDateTime editedDate) {
        this.editedDate = editedDate;
        return this;
    }

    public void setEditedDate(ZonedDateTime editedDate) {
        this.editedDate = editedDate;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public ComplaintComment deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Resident getResident() {
        return resident;
    }

    public ComplaintComment resident(Resident resident) {
        this.resident = resident;
        return this;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public AdminInfo getAdminInfo() {
        return adminInfo;
    }

    public ComplaintComment adminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
        return this;
    }

    public void setAdminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public ComplaintComment complaint(Complaint complaint) {
        this.complaint = complaint;
        return this;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
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
        ComplaintComment complaintComment = (ComplaintComment) o;
        if (complaintComment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), complaintComment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComplaintComment{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", editedDate='" + getEditedDate() + "'" +
            ", deleted=" + getDeleted() +
            "}";
    }
}
