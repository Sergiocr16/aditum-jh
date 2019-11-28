package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Subsidiary.
 */
@Entity
@Table(name = "subsidiary")
public class Subsidiary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private SubsidiaryType subsidiaryType;

    @ManyToOne
    private House house;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Subsidiary name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Subsidiary deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public Subsidiary description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SubsidiaryType getSubsidiaryType() {
        return subsidiaryType;
    }

    public Subsidiary subsidiaryType(SubsidiaryType subsidiaryType) {
        this.subsidiaryType = subsidiaryType;
        return this;
    }

    public void setSubsidiaryType(SubsidiaryType subsidiaryType) {
        this.subsidiaryType = subsidiaryType;
    }

    public House getHouse() {
        return house;
    }

    public Subsidiary house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
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
        Subsidiary subsidiary = (Subsidiary) o;
        if (subsidiary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subsidiary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Subsidiary{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", deleted=" + getDeleted() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
