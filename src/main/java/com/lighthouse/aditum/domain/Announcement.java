package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Announcement.
 */
@Entity
@Table(name = "announcement")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "publishing_date", nullable = false)
    private ZonedDateTime publishingDate;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @ManyToOne(optional = false)
    @NotNull
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Announcement title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getPublishingDate() {
        return publishingDate;
    }

    public Announcement publishingDate(ZonedDateTime publishingDate) {
        this.publishingDate = publishingDate;
        return this;
    }

    public void setPublishingDate(ZonedDateTime publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getDescription() {
        return description;
    }

    public Announcement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public Announcement status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Company getCompany() {
        return company;
    }

    public Announcement company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Announcement announcement = (Announcement) o;
        if (announcement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), announcement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Announcement{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", publishingDate='" + getPublishingDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
