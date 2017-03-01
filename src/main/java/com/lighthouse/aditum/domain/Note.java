package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "notetype", nullable = false)
    private Integer notetype;

    @NotNull
    @Column(name = "creationdate", nullable = false)
    private ZonedDateTime creationdate;

    @ManyToOne
    private House house;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Note description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNotetype() {
        return notetype;
    }

    public Note notetype(Integer notetype) {
        this.notetype = notetype;
        return this;
    }

    public void setNotetype(Integer notetype) {
        this.notetype = notetype;
    }

    public ZonedDateTime getCreationdate() {
        return creationdate;
    }

    public Note creationdate(ZonedDateTime creationdate) {
        this.creationdate = creationdate;
        return this;
    }

    public void setCreationdate(ZonedDateTime creationdate) {
        this.creationdate = creationdate;
    }

    public House getHouse() {
        return house;
    }

    public Note house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public Note company(Company company) {
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
        Note note = (Note) o;
        if (note.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", notetype='" + notetype + "'" +
            ", creationdate='" + creationdate + "'" +
            '}';
    }
}
