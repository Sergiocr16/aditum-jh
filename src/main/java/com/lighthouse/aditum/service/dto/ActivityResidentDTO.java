package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ActivityResident entity.
 */
public class ActivityResidentDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private ZonedDateTime date;

    private Integer seen;

    private String image;

    private String type;

    private Long user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getSeen() {
        return seen;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityResidentDTO activityResidentDTO = (ActivityResidentDTO) o;
        if(activityResidentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityResidentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityResidentDTO{" +
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
