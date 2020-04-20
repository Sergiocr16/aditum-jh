package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A HouseSecurityDirection.
 */
@Entity
@Table(name = "house_security_direction")
public class HouseSecurityDirection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direction_description")
    private String directionDescription;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "house_description")
    private String houseDescription;

    @Column(name = "aditional_notes")
    private String aditionalNotes;

    @Column(name = "house_picture_url")
    private String housePictureUrl;

    @Column(name = "small_direction")
    private String smallDirection;

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

    public String getDirectionDescription() {
        return directionDescription;
    }

    public HouseSecurityDirection directionDescription(String directionDescription) {
        this.directionDescription = directionDescription;
        return this;
    }

    public void setDirectionDescription(String directionDescription) {
        this.directionDescription = directionDescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public HouseSecurityDirection latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public HouseSecurityDirection longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getHouseDescription() {
        return houseDescription;
    }

    public HouseSecurityDirection houseDescription(String houseDescription) {
        this.houseDescription = houseDescription;
        return this;
    }

    public void setHouseDescription(String houseDescription) {
        this.houseDescription = houseDescription;
    }

    public String getAditionalNotes() {
        return aditionalNotes;
    }

    public HouseSecurityDirection aditionalNotes(String aditionalNotes) {
        this.aditionalNotes = aditionalNotes;
        return this;
    }

    public void setAditionalNotes(String aditionalNotes) {
        this.aditionalNotes = aditionalNotes;
    }

    public String getHousePictureUrl() {
        return housePictureUrl;
    }

    public HouseSecurityDirection housePictureUrl(String housePictureUrl) {
        this.housePictureUrl = housePictureUrl;
        return this;
    }

    public void setHousePictureUrl(String housePictureUrl) {
        this.housePictureUrl = housePictureUrl;
    }

    public String getSmallDirection() {
        return smallDirection;
    }

    public HouseSecurityDirection smallDirection(String smallDirection) {
        this.smallDirection = smallDirection;
        return this;
    }

    public void setSmallDirection(String smallDirection) {
        this.smallDirection = smallDirection;
    }

    public House getHouse() {
        return house;
    }

    public HouseSecurityDirection house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public HouseSecurityDirection company(Company company) {
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
        HouseSecurityDirection houseSecurityDirection = (HouseSecurityDirection) o;
        if (houseSecurityDirection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), houseSecurityDirection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HouseSecurityDirection{" +
            "id=" + getId() +
            ", directionDescription='" + getDirectionDescription() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", houseDescription='" + getHouseDescription() + "'" +
            ", aditionalNotes='" + getAditionalNotes() + "'" +
            ", housePictureUrl='" + getHousePictureUrl() + "'" +
            ", smallDirection='" + getSmallDirection() + "'" +
            "}";
    }
}
