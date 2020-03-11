package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Company entity.
 */
public class CompanyDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer active;

    private String logoUrl;

    private String direction;

    private String legalIdentification;

    private String legalName;

    private String adminLogoUrl;

    public String getAdminLogoUrl() {
        return adminLogoUrl;
    }

    public void setAdminLogoUrl(String adminLogoUrl) {
        this.adminLogoUrl = adminLogoUrl;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    private String phoneNumber;

    private String supportEmail;

    private String supportNumber;

    private String email;

    private List<HouseAccessDoorDTO> houses;

    public List<HouseAccessDoorDTO> getHouses() {
        return houses;
    }

    public void setHouses(List<HouseAccessDoorDTO> houses) {
        this.houses = houses;
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

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLegalIdentification() {
        return legalIdentification;
    }

    public void setLegalIdentification(String legalIdentification) {
        this.legalIdentification = legalIdentification;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getSupportNumber() {
        return supportNumber;
    }

    public void setSupportNumber(String supportNumber) {
        this.supportNumber = supportNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;
        if(companyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active=" + getActive() +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", direction='" + getDirection() + "'" +
            ", legalIdentification='" + getLegalIdentification() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", supportEmail='" + getSupportEmail() + "'" +
            ", supportNumber='" + getSupportNumber() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
