package com.lighthouse.aditum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AnnouncementComment.
 */
@Entity
@Table(name = "announcement_comment")
public class AnnouncementComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_comment", nullable = false)
    private String comment;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Resident resident;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Announcement announcement;

    @ManyToOne
    @JsonIgnoreProperties("")
    private AdminInfo adminInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public AnnouncementComment comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public AnnouncementComment creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Resident getResident() {
        return resident;
    }

    public AnnouncementComment resident(Resident resident) {
        this.resident = resident;
        return this;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public AnnouncementComment announcement(Announcement announcement) {
        this.announcement = announcement;
        return this;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    public AdminInfo getAdminInfo() {
        return adminInfo;
    }

    public AnnouncementComment adminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
        return this;
    }

    public void setAdminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
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
        AnnouncementComment announcementComment = (AnnouncementComment) o;
        if (announcementComment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), announcementComment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnnouncementComment{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
