package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the MacroVisit entity.
 */
public class MacroVisitDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String identificationnumber;

    @NotNull
    private String lastname;

    @NotNull
    private String secondlastname;

    private String licenseplate;

    @NotNull
    private Integer status;

    @NotNull
    private ZonedDateTime arrivaltime;

    private ZonedDateTime departuretime;

    private String destiny;

    private Long macroCondominiumId;

    private String macroCondominiumName;

    private Long companyId;

    private String companyName;

    private Long houseId;

    private String houseHousenumber;

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

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
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

    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ZonedDateTime getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(ZonedDateTime arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public ZonedDateTime getDeparturetime() {
        return departuretime;
    }

    public void setDeparturetime(ZonedDateTime departuretime) {
        this.departuretime = departuretime;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public Long getMacroCondominiumId() {
        return macroCondominiumId;
    }

    public void setMacroCondominiumId(Long macroCondominiumId) {
        this.macroCondominiumId = macroCondominiumId;
    }

    public String getMacroCondominiumName() {
        return macroCondominiumName;
    }

    public void setMacroCondominiumName(String macroCondominiumName) {
        this.macroCondominiumName = macroCondominiumName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getHouseHousenumber() {
        return houseHousenumber;
    }

    public void setHouseHousenumber(String houseHousenumber) {
        this.houseHousenumber = houseHousenumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MacroVisitDTO macroVisitDTO = (MacroVisitDTO) o;
        if(macroVisitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), macroVisitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MacroVisitDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", identificationnumber='" + getIdentificationnumber() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", secondlastname='" + getSecondlastname() + "'" +
            ", licenseplate='" + getLicenseplate() + "'" +
            ", status=" + getStatus() +
            ", arrivaltime='" + getArrivaltime() + "'" +
            ", departuretime='" + getDeparturetime() + "'" +
            ", destiny='" + getDestiny() + "'" +
            "}";
    }
}
