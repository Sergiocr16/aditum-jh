package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A MacroVisit.
 */
@Entity
@Table(name = "macro_visit")
public class MacroVisit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "identificationnumber", nullable = false)
    private String identificationnumber;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotNull
    @Column(name = "secondlastname", nullable = false)
    private String secondlastname;

    @Column(name = "licenseplate")
    private String licenseplate;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @NotNull
    @Column(name = "arrivaltime", nullable = false)
    private ZonedDateTime arrivaltime;

    @Column(name = "departuretime")
    private ZonedDateTime departuretime;

    @Column(name = "destiny")
    private String destiny;

    @ManyToOne(optional = false)
    @NotNull
    private MacroCondominium macroCondominium;

    @ManyToOne
    private Company company;

    @ManyToOne
    private House house;

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

    public MacroVisit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public MacroVisit identificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
        return this;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public String getLastname() {
        return lastname;
    }

    public MacroVisit lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public MacroVisit secondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
        return this;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getLicenseplate() {
        return licenseplate;
    }

    public MacroVisit licenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
        return this;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public Integer getStatus() {
        return status;
    }

    public MacroVisit status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ZonedDateTime getArrivaltime() {
        return arrivaltime;
    }

    public MacroVisit arrivaltime(ZonedDateTime arrivaltime) {
        this.arrivaltime = arrivaltime;
        return this;
    }

    public void setArrivaltime(ZonedDateTime arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public ZonedDateTime getDeparturetime() {
        return departuretime;
    }

    public MacroVisit departuretime(ZonedDateTime departuretime) {
        this.departuretime = departuretime;
        return this;
    }

    public void setDeparturetime(ZonedDateTime departuretime) {
        this.departuretime = departuretime;
    }

    public String getDestiny() {
        return destiny;
    }

    public MacroVisit destiny(String destiny) {
        this.destiny = destiny;
        return this;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public MacroCondominium getMacroCondominium() {
        return macroCondominium;
    }

    public MacroVisit macroCondominium(MacroCondominium macroCondominium) {
        this.macroCondominium = macroCondominium;
        return this;
    }

    public void setMacroCondominium(MacroCondominium macroCondominium) {
        this.macroCondominium = macroCondominium;
    }

    public Company getCompany() {
        return company;
    }

    public MacroVisit company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public House getHouse() {
        return house;
    }

    public MacroVisit house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
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
        MacroVisit macroVisit = (MacroVisit) o;
        if (macroVisit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), macroVisit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MacroVisit{" +
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
