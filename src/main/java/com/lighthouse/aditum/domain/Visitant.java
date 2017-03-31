package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Visitant.
 */
@Entity
@Table(name = "visitant")
public class Visitant implements Serializable {

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

    @NotNull
    @Column(name = "secondlastname", nullable = false)
    private String secondlastname;

    @NotNull
    @Column(name = "identificationnumber", nullable = false)
    private String identificationnumber;

    @Column(name = "arrivaltime")
    private ZonedDateTime arrivaltime;

    @Column(name = "invitationstaringtime")
    private ZonedDateTime invitationstaringtime;

    @Column(name = "invitationlimittime")
    private ZonedDateTime invitationlimittime;

    @Column(name = "licenseplate")
    private String licenseplate;

    @NotNull
    @Min(value = 0)
    @Max(value = 3)
    @Column(name = "isinvited", nullable = false)
    private Integer isinvited;

    @Column(name = "responsableofficer")
    private String responsableofficer;

    @ManyToOne
    private House house;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Visitant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public Visitant lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public Visitant secondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
        return this;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public Visitant identificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
        return this;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public ZonedDateTime getArrivaltime() {
        return arrivaltime;
    }

    public Visitant arrivaltime(ZonedDateTime arrivaltime) {
        this.arrivaltime = arrivaltime;
        return this;
    }

    public void setArrivaltime(ZonedDateTime arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public ZonedDateTime getInvitationstaringtime() {
        return invitationstaringtime;
    }

    public Visitant invitationstaringtime(ZonedDateTime invitationstaringtime) {
        this.invitationstaringtime = invitationstaringtime;
        return this;
    }

    public void setInvitationstaringtime(ZonedDateTime invitationstaringtime) {
        this.invitationstaringtime = invitationstaringtime;
    }

    public ZonedDateTime getInvitationlimittime() {
        return invitationlimittime;
    }

    public Visitant invitationlimittime(ZonedDateTime invitationlimittime) {
        this.invitationlimittime = invitationlimittime;
        return this;
    }

    public void setInvitationlimittime(ZonedDateTime invitationlimittime) {
        this.invitationlimittime = invitationlimittime;
    }

    public String getLicenseplate() {
        return licenseplate;
    }

    public Visitant licenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
        return this;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public Integer getIsinvited() {
        return isinvited;
    }

    public Visitant isinvited(Integer isinvited) {
        this.isinvited = isinvited;
        return this;
    }

    public void setIsinvited(Integer isinvited) {
        this.isinvited = isinvited;
    }

    public String getResponsableofficer() {
        return responsableofficer;
    }

    public Visitant responsableofficer(String responsableofficer) {
        this.responsableofficer = responsableofficer;
        return this;
    }

    public void setResponsableofficer(String responsableofficer) {
        this.responsableofficer = responsableofficer;
    }

    public House getHouse() {
        return house;
    }

    public Visitant house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public Visitant company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Visitant visitant = (Visitant) o;
        if (visitant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, visitant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Visitant{" +
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
}
