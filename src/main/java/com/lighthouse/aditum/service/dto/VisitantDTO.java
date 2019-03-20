package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Visitant entity.
 */
public class VisitantDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    @NotNull
    private String secondlastname;


    private String identificationnumber;

    private String arrivalTimeFormatted;

    private ZonedDateTime arrivaltime;

    private ZonedDateTime departureTime;

    private ZonedDateTime invitationstaringtime;

    private ZonedDateTime invitationlimittime;

    private String licenseplate;

    @NotNull
    @Min(value = 0)
    @Max(value = 3)
    private Integer isinvited;

    private String responsableofficer;

    private Long houseId;

    private Long companyId;

    private String houseNumber;

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
    public ZonedDateTime getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(ZonedDateTime arrivaltime) {
        this.arrivaltime = arrivaltime;
    }
    public ZonedDateTime getInvitationstaringtime() {
        return invitationstaringtime;
    }

    public void setInvitationstaringtime(ZonedDateTime invitationstaringtime) {
        this.invitationstaringtime = invitationstaringtime;
    }
    public ZonedDateTime getInvitationlimittime() {
        return invitationlimittime;
    }

    public void setInvitationlimittime(ZonedDateTime invitationlimittime) {
        this.invitationlimittime = invitationlimittime;
    }
    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }
    public Integer getIsinvited() {
        return isinvited;
    }

    public void setIsinvited(Integer isinvited) {
        this.isinvited = isinvited;
    }
    public String getResponsableofficer() {
        return responsableofficer;
    }

    public void setResponsableofficer(String responsableofficer) {
        this.responsableofficer = responsableofficer;
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

        VisitantDTO visitantDTO = (VisitantDTO) o;

        if ( ! Objects.equals(id, visitantDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VisitantDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastname='" + lastname + "'" +
            ", secondlastname='" + secondlastname + "'" +
            ", identificationnumber='" + identificationnumber + "'" +
            ", arrivaltime='" + arrivaltime + "'" +
            ", invitationstaringtime='" + invitationstaringtime + "'" +
            ", invitationlimittime='" + invitationlimittime + "'" +
            ", licenseplate='" + licenseplate + "'" +
            ", isinvited='" + isinvited + "'" +
            ", responsableofficer='" + responsableofficer + "'" +
            '}';
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ZonedDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTimeFormatted() {
        return arrivalTimeFormatted;
    }

    public void setArrivalTimeFormatted(String arrivalTimeFormatted) {
        this.arrivalTimeFormatted = arrivalTimeFormatted;
    }
}
