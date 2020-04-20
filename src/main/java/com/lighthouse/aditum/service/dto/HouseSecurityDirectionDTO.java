package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the HouseSecurityDirection entity.
 */
public class HouseSecurityDirectionDTO implements Serializable {

    private Long id;

    private String directionDescription;

    private String latitude;

    private String longitude;

    private String houseDescription;

    private String aditionalNotes;

    private String housePictureUrl;

    private String smallDirection;

    private Long houseId;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirectionDescription() {
        return directionDescription;
    }

    public void setDirectionDescription(String directionDescription) {
        this.directionDescription = directionDescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getHouseDescription() {
        return houseDescription;
    }

    public void setHouseDescription(String houseDescription) {
        this.houseDescription = houseDescription;
    }

    public String getAditionalNotes() {
        return aditionalNotes;
    }

    public void setAditionalNotes(String aditionalNotes) {
        this.aditionalNotes = aditionalNotes;
    }

    public String getHousePictureUrl() {
        return housePictureUrl;
    }

    public void setHousePictureUrl(String housePictureUrl) {
        this.housePictureUrl = housePictureUrl;
    }

    public String getSmallDirection() {
        return smallDirection;
    }

    public void setSmallDirection(String smallDirection) {
        this.smallDirection = smallDirection;
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

        HouseSecurityDirectionDTO houseSecurityDirectionDTO = (HouseSecurityDirectionDTO) o;
        if(houseSecurityDirectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), houseSecurityDirectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HouseSecurityDirectionDTO{" +
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
