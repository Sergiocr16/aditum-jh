package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the House entity.
 */
public class HouseAccessDoorDTO implements Serializable {

    private Long id;

    @NotNull
    private String housenumber;

    private String extension;




    private String securityKey;

    private String emergencyKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }


    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getEmergencyKey() {
        return emergencyKey;
    }

    public void setEmergencyKey(String emergencyKey) {
        this.emergencyKey = emergencyKey;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HouseAccessDoorDTO houseDTO = (HouseAccessDoorDTO) o;
        if(houseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), houseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HouseDTO{" +
            "id=" + getId() +
            ", housenumber='" + getHousenumber() + "'" +
            ", extension='" + getExtension() + "'" +
            ", securityKey='" + getSecurityKey() + "'" +
            ", emergencyKey='" + getEmergencyKey() + "'" +
            "}";
    }
}
