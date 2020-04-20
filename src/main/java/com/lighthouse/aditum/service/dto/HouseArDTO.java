
package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the House entity.
 */
public class HouseArDTO implements Serializable {

    private Long id;

    @NotNull
    private String housenumber;

    private String extension;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer isdesocupated;

    private Boolean hasOwner;

    private String securityKey;

    private String emergencyKey;

    private String loginCode;

    private Integer codeStatus;

    private Long companyId;

    private String email;

    private String due;

    private HouseSecurityDirectionDTO ubication;

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public HouseSecurityDirectionDTO getUbication() {
        return ubication;
    }

    public void setUbication(HouseSecurityDirectionDTO ubication) {
        this.ubication = ubication;
    }

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

    public Integer getIsdesocupated() {
        return isdesocupated;
    }

    public void setIsdesocupated(Integer isdesocupated) {
        this.isdesocupated = isdesocupated;
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

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public Integer getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(Integer codeStatus) {
        this.codeStatus = codeStatus;
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

        HouseArDTO houseDTO = (HouseArDTO) o;
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
            ", isdesocupated='" + getIsdesocupated() + "'" +
            ", securityKey='" + getSecurityKey() + "'" +
            ", emergencyKey='" + getEmergencyKey() + "'" +
            ", loginCode='" + getLoginCode() + "'" +
            ", codeStatus='" + getCodeStatus() + "'" +
            "}";
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHasOwner() {
        return hasOwner;
    }

    public void setHasOwner(Boolean hasOwner) {
        this.hasOwner = hasOwner;
    }
}
