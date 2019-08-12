package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the VisitantInvitation entity.
 */
public class VisitantInvitationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    @NotNull
    private String secondlastname;

    private String identificationnumber;

    private ZonedDateTime invitationstartingtime;

    private ZonedDateTime invitationlimittime;

    private String licenseplate;

    private Integer hasschedule;

    private String destiny;

    private Long houseId;

    private Long companyId;

    private Long adminId;

    private Long scheduleId;

    private Integer status;

    private String proveedor;

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

    public ZonedDateTime getInvitationstartingtime() {
        return invitationstartingtime;
    }

    public void setInvitationstartingtime(ZonedDateTime invitationstartingtime) {
        this.invitationstartingtime = invitationstartingtime;
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

    public Integer getHasschedule() {
        return hasschedule;
    }

    public void setHasschedule(Integer hasschedule) {
        this.hasschedule = hasschedule;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
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

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VisitantInvitationDTO visitantInvitationDTO = (VisitantInvitationDTO) o;
        if(visitantInvitationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), visitantInvitationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VisitantInvitationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", secondlastname='" + getSecondlastname() + "'" +
            ", identificationnumber='" + getIdentificationnumber() + "'" +
            ", invitationstartingtime='" + getInvitationstartingtime() + "'" +
            ", invitationlimittime='" + getInvitationlimittime() + "'" +
            ", licenseplate='" + getLicenseplate() + "'" +
            ", hasschedule=" + getHasschedule() +
            ", destiny='" + getDestiny() + "'" +
            ", houseId=" + getHouseId() +
            ", companyId=" + getCompanyId() +
            ", adminId=" + getAdminId() +
            ", scheduleId=" + getScheduleId() +
            ", status=" + getStatus() +
            ", proveedor='" + getProveedor() + "'" +
            "}";
    }
}
