package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the House entity.
 */
public class HouseDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer housenumber;

    private String extension;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer isdesocupated;

    private ZonedDateTime desocupationinitialtime;

    private ZonedDateTime desocupationfinaltime;

    private String securityKey;

    private String emergencyKey;

    private String loginCode;

    private Boolean codeStatus;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = Integer.parseInt(housenumber);
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
    public ZonedDateTime getDesocupationinitialtime() {
        return desocupationinitialtime;
    }

    public void setDesocupationinitialtime(ZonedDateTime desocupationinitialtime) {
        this.desocupationinitialtime = desocupationinitialtime;
    }
    public ZonedDateTime getDesocupationfinaltime() {
        return desocupationfinaltime;
    }

    public void setDesocupationfinaltime(ZonedDateTime desocupationfinaltime) {
        this.desocupationfinaltime = desocupationfinaltime;
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
    public Boolean getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(Boolean codeStatus) {
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

        HouseDTO houseDTO = (HouseDTO) o;

        if ( ! Objects.equals(id, houseDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HouseDTO{" +
            "id=" + id +
            ", housenumber='" + housenumber + "'" +
            ", extension='" + extension + "'" +
            ", isdesocupated='" + isdesocupated + "'" +
            ", desocupationinitialtime='" + desocupationinitialtime + "'" +
            ", desocupationfinaltime='" + desocupationfinaltime + "'" +
            ", securityKey='" + securityKey + "'" +
            ", emergencyKey='" + emergencyKey + "'" +
            ", loginCode='" + loginCode + "'" +
            ", codeStatus='" + codeStatus + "'" +
            '}';
    }
}
