package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A Complaint.
 */
@Entity
@Table(name = "complaint")
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "complaint_type", nullable = false)
    private String complaintType;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "resolution_date")
    private ZonedDateTime resolutionDate;

    @Column(name = "file_url")
    private String fileUrl;

    @NotNull
    @Column(name = "complaint_category", nullable = false)
    private Integer complaintCategory;

    @Column(name = "subject")
    private String subject;

    @ManyToOne(optional = false)
    @NotNull
    private House house;

    @ManyToOne(optional = false)
    @NotNull
    private Company company;

    @ManyToOne(optional = false)
    @NotNull
    private Resident resident;

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

    public Complaint description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public Complaint complaintType(String complaintType) {
        this.complaintType = complaintType;
        return this;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public Integer getStatus() {
        return status;
    }

    public Complaint status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Complaint deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Complaint creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getResolutionDate() {
        return resolutionDate;
    }

    public Complaint resolutionDate(ZonedDateTime resolutionDate) {
        this.resolutionDate = resolutionDate;
        return this;
    }

    public void setResolutionDate(ZonedDateTime resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Complaint fileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getComplaintCategory() {
        return complaintCategory;
    }

    public Complaint complaintCategory(Integer complaintCategory) {
        this.complaintCategory = complaintCategory;
        return this;
    }

    public void setComplaintCategory(Integer complaintCategory) {
        this.complaintCategory = complaintCategory;
    }

    public String getSubject() {
        return subject;
    }

    public Complaint subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public House getHouse() {
        return house;
    }

    public Complaint house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public Complaint company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Resident getResident() {
        return resident;
    }

    public Complaint resident(Resident resident) {
        this.resident = resident;
        return this;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
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
        Complaint complaint = (Complaint) o;
        if (complaint.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), complaint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Complaint{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", complaintType='" + getComplaintType() + "'" +
            ", status=" + getStatus() +
            ", deleted=" + getDeleted() +
            ", creationDate='" + getCreationDate() + "'" +
            ", resolutionDate='" + getResolutionDate() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", complaintCategory=" + getComplaintCategory() +
            ", subject='" + getSubject() + "'" +
            "}";
    }
}
