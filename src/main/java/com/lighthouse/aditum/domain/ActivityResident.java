package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A ActivityResident.
 */
@Entity
@Table(name = "activity_resident")
public class ActivityResident implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "seen")
    private Integer seen;

    @Column(name = "image")
    private String image;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "jhi_user")
    private Long user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ActivityResident title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public ActivityResident description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public ActivityResident date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getSeen() {
        return seen;
    }

    public ActivityResident seen(Integer seen) {
        this.seen = seen;
        return this;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public String getImage() {
        return image;
    }

    public ActivityResident image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public ActivityResident type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUser() {
        return user;
    }

    public ActivityResident user(Long user) {
        this.user = user;
        return this;
    }

    public void setUser(Long user) {
        this.user = user;
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
        ActivityResident activityResident = (ActivityResident) o;
        if (activityResident.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityResident.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityResident{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", seen=" + getSeen() +
            ", image='" + getImage() + "'" +
            ", type='" + getType() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
