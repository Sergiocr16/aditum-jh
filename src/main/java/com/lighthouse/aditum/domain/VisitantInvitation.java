package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A VisitantInvitation.
 */
@Entity
@Table(name = "visitant_invitation")
public class VisitantInvitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "secondlastname", nullable = true)
    private String secondlastname;

    @Column(name = "identificationnumber")
    private String identificationnumber;

    @Column(name = "invitationstartingtime")
    private ZonedDateTime invitationstartingtime;

    @Column(name = "invitationlimittime")
    private ZonedDateTime invitationlimittime;

    @Column(name = "licenseplate")
    private String licenseplate;

    @Column(name = "hasschedule")
    private Integer hasschedule;

    @Column(name = "destiny")
    private String destiny;

    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "proveedor")
    private String proveedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public VisitantInvitation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public VisitantInvitation lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public VisitantInvitation secondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
        return this;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public VisitantInvitation identificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
        return this;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public ZonedDateTime getInvitationstartingtime() {
        return invitationstartingtime;
    }

    public VisitantInvitation invitationstartingtime(ZonedDateTime invitationstartingtime) {
        this.invitationstartingtime = invitationstartingtime;
        return this;
    }

    public void setInvitationstartingtime(ZonedDateTime invitationstartingtime) {
        this.invitationstartingtime = invitationstartingtime;
    }

    public ZonedDateTime getInvitationlimittime() {
        return invitationlimittime;
    }

    public VisitantInvitation invitationlimittime(ZonedDateTime invitationlimittime) {
        this.invitationlimittime = invitationlimittime;
        return this;
    }

    public void setInvitationlimittime(ZonedDateTime invitationlimittime) {
        this.invitationlimittime = invitationlimittime;
    }

    public String getLicenseplate() {
        return licenseplate;
    }

    public VisitantInvitation licenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
        return this;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public Integer getHasschedule() {
        return hasschedule;
    }

    public VisitantInvitation hasschedule(Integer hasschedule) {
        this.hasschedule = hasschedule;
        return this;
    }

    public void setHasschedule(Integer hasschedule) {
        this.hasschedule = hasschedule;
    }

    public String getDestiny() {
        return destiny;
    }

    public VisitantInvitation destiny(String destiny) {
        this.destiny = destiny;
        return this;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public Long getHouseId() {
        return houseId;
    }

    public VisitantInvitation houseId(Long houseId) {
        this.houseId = houseId;
        return this;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public VisitantInvitation companyId(Long companyId) {
        this.companyId = companyId;
        return this;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public VisitantInvitation adminId(Long adminId) {
        this.adminId = adminId;
        return this;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public VisitantInvitation scheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
        return this;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getStatus() {
        return status;
    }

    public VisitantInvitation status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProveedor() {
        return proveedor;
    }

    public VisitantInvitation proveedor(String proveedor) {
        this.proveedor = proveedor;
        return this;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VisitantInvitation visitantInvitation = (VisitantInvitation) o;
        if (visitantInvitation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), visitantInvitation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VisitantInvitation{" +
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
