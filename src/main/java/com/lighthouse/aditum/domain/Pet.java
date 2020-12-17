package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Pet.
 */
@Entity
@Table(name = "pet")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "raze")
    private String raze;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "contact")
    private String contact;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "weight")
    private String weight;

    @Column(name = "vaccinated")
    private String vaccinated;

    @ManyToOne
    private House house;

    @ManyToOne
    private Company company;

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

    public Pet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Pet deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getRaze() {
        return raze;
    }

    public Pet raze(String raze) {
        this.raze = raze;
        return this;
    }

    public void setRaze(String raze) {
        this.raze = raze;
    }

    public String getType() {
        return type;
    }

    public Pet type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public Pet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public Pet contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Pet imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWeight() {
        return weight;
    }

    public Pet weight(String weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVaccinated() {
        return vaccinated;
    }

    public Pet vaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
        return this;
    }

    public void setVaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
    }

    public House getHouse() {
        return house;
    }

    public Pet house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public Pet company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        Pet pet = (Pet) o;
        if (pet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", deleted=" + getDeleted() +
            ", raze='" + getRaze() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", contact='" + getContact() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", weight='" + getWeight() + "'" +
            ", vaccinated='" + getVaccinated() + "'" +
            "}";
    }
}
