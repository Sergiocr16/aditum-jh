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

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    @NotNull
    private String secondlastname;

    @NotNull
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

    private Long userId;

    private String userLogin;

    private Long companyId;

    private Long houseId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResidentDTO residentDTO = (ResidentDTO) o;

        if ( ! Objects.equals(id, residentDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ResidentDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastname='" + lastname + "'" +
            ", secondlastname='" + secondlastname + "'" +
            ", identificationnumber='" + identificationnumber + "'" +
            ", phonenumber='" + phonenumber + "'" +
            ", image='" + image + "'" +
            ", email='" + email + "'" +
            ", isOwner='" + isOwner + "'" +
            ", enabled='" + enabled + "'" +
            '}';
    }
}
