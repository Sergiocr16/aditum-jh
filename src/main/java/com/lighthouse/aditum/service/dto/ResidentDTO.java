package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Resident entity.
 */
public class ResidentDTO implements Serializable {

    private Long id;

//    @NotNull
    private String name;

//    @NotNull
    private String lastname;

//    @NotNull
    private String secondlastname;

//    @NotNull
    private String identificationnumber;

    private String phonenumber;

    @Lob
    private byte[] image;
    private String imageContentType;

    private String email;

    @Min(value = 0)
    @Max(value = 1)
    private Integer isOwner;

    @Min(value = 0)
    @Max(value = 1)
    private Integer enabled;

    private String image_url;

    private Integer type;

    private Integer principalContact;

    private String companyName;

    private Integer deleted;

    private Boolean isCompany;

    private String legalIdentification;

    private boolean hasTokenToNotification;

    private String companyDirection;

    private String companyEmail;

    private Long userId;

    private String userLogin;

    private String companyPhone;

    private String legalCompanyName;

    private Long companyId;

    private Long houseId;

    private Set<HouseDTO> houses = new HashSet<>();

    private HouseAccessDoorDTO houseClean;

    private HouseDTO house;

    public boolean getHasTokenToNotification() {
        return hasTokenToNotification;
    }

    public void setHasTokenToNotification(boolean hasTokenToNotification) {
        this.hasTokenToNotification = hasTokenToNotification;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Integer isOwner) {
        this.isOwner = isOwner;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPrincipalContact() {
        return principalContact;
    }

    public void setPrincipalContact(Integer principalContact) {
        this.principalContact = principalContact;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Boolean isIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Boolean isCompany) {
        this.isCompany = isCompany;
    }

    public String getLegalIdentification() {
        return legalIdentification;
    }

    public void setLegalIdentification(String legalIdentification) {
        this.legalIdentification = legalIdentification;
    }

    public String getCompanyDirection() {
        return companyDirection;
    }

    public void setCompanyDirection(String companyDirection) {
        this.companyDirection = companyDirection;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Set<HouseDTO> getHouses() {
        return houses;
    }

    public HouseAccessDoorDTO getHouseClean() {
        return houseClean;
    }

    public void setHouseClean(HouseAccessDoorDTO houseClean) {
        this.houseClean = houseClean;
    }

    public HouseDTO getHouse() {
        return house;
    }

    public void setHouse(HouseDTO house) {
        this.house = house;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setHouses(Set<HouseDTO> houses) {
        this.houses = houses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResidentDTO residentDTO = (ResidentDTO) o;
        if(residentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), residentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResidentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", secondlastname='" + getSecondlastname() + "'" +
            ", identificationnumber='" + getIdentificationnumber() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", image='" + getImage() + "'" +
            ", email='" + getEmail() + "'" +
            ", isOwner=" + getIsOwner() +
            ", enabled=" + getEnabled() +
            ", image_url='" + getImage_url() + "'" +
            ", type=" + getType() +
            ", principalContact=" + getPrincipalContact() +
            ", deleted=" + getDeleted() +
            ", isCompany='" + isIsCompany() + "'" +
            ", legalIdentification='" + getLegalIdentification() + "'" +
            ", companyDirection='" + getCompanyDirection() + "'" +
            ", companyEmail='" + getCompanyEmail() + "'" +
            "}";
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getLegalCompanyName() {
        return legalCompanyName;
    }

    public void setLegalCompanyName(String legalCompanyName) {
        this.legalCompanyName = legalCompanyName;
    }
}
