package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Pet entity.
 */
public class PetDTO implements Serializable {

    private Long id;

    private String name;

    private Integer deleted;

    private String raze;

    private String type;

    private String description;

    private String contact;

    private String imageUrl;

    private Long houseId;

    private String houseNumber;

    private Long companyId;

    private String weight;

    private String vaccinated;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getRaze() {
        return raze;
    }

    public void setRaze(String raze) {
        this.raze = raze;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PetDTO petDTO = (PetDTO) o;
        if(petDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), petDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", deleted=" + getDeleted() +
            ", raze='" + getRaze() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", contact='" + getContact() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }
}
