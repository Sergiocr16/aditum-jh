package com.lighthouse.aditum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A House.
 */
@Entity
@Table(name = "house")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "housenumber", nullable = false)
    private String housenumber;

    @Column(name = "extension")
    private String extension;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "isdesocupated", nullable = false)
    private Integer isdesocupated;

    @Column(name = "desocupationinitialtime")
    private ZonedDateTime desocupationinitialtime;

    @Column(name = "desocupationfinaltime")
    private ZonedDateTime desocupationfinaltime;

    @Column(name = "security_key")
    private String securityKey;

    @Column(name = "emergency_key")
    private String emergencyKey;

    @Column(name = "login_code")
    private String loginCode;

    @Column(name = "code_status")
    private Integer codeStatus;

    @Column(name = "due")
    private String due;

    @Column(name = "square_meters")
    private String squareMeters;

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    private Set<Vehicule> vehicules = new HashSet<>();

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    private Set<Visitant> visitants = new HashSet<>();

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    private Set<Note> notes = new HashSet<>();

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    private Set<Resident> residents = new HashSet<>();

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    private Set<Emergency> emergencies = new HashSet<>();

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public House housenumber(String housenumber) {
        this.housenumber = housenumber;
        return this;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getExtension() {
        return extension;
    }

    public House extension(String extension) {
        this.extension = extension;
        return this;
    }

    public int getHouseNumberInt(){
        return Integer.parseInt(this.housenumber);
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getIsdesocupated() {
        return isdesocupated;
    }

    public House isdesocupated(Integer isdesocupated) {
        this.isdesocupated = isdesocupated;
        return this;
    }

    public void setIsdesocupated(Integer isdesocupated) {
        this.isdesocupated = isdesocupated;
    }

    public ZonedDateTime getDesocupationinitialtime() {
        return desocupationinitialtime;
    }

    public House desocupationinitialtime(ZonedDateTime desocupationinitialtime) {
        this.desocupationinitialtime = desocupationinitialtime;
        return this;
    }

    public void setDesocupationinitialtime(ZonedDateTime desocupationinitialtime) {
        this.desocupationinitialtime = desocupationinitialtime;
    }

    public ZonedDateTime getDesocupationfinaltime() {
        return desocupationfinaltime;
    }

    public House desocupationfinaltime(ZonedDateTime desocupationfinaltime) {
        this.desocupationfinaltime = desocupationfinaltime;
        return this;
    }

    public void setDesocupationfinaltime(ZonedDateTime desocupationfinaltime) {
        this.desocupationfinaltime = desocupationfinaltime;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public House securityKey(String securityKey) {
        this.securityKey = securityKey;
        return this;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getEmergencyKey() {
        return emergencyKey;
    }

    public House emergencyKey(String emergencyKey) {
        this.emergencyKey = emergencyKey;
        return this;
    }

    public void setEmergencyKey(String emergencyKey) {
        this.emergencyKey = emergencyKey;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public House loginCode(String loginCode) {
        this.loginCode = loginCode;
        return this;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public Integer getCodeStatus() {
        return codeStatus;
    }

    public House codeStatus(Integer codeStatus) {
        this.codeStatus = codeStatus;
        return this;
    }

    public void setCodeStatus(Integer codeStatus) {
        this.codeStatus = codeStatus;
    }

    public String getDue() {
        return due;
    }

    public House due(String due) {
        this.due = due;
        return this;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getSquareMeters() {
        return squareMeters;
    }

    public House squareMeters(String squareMeters) {
        this.squareMeters = squareMeters;
        return this;
    }

    public void setSquareMeters(String squareMeters) {
        this.squareMeters = squareMeters;
    }

    public Set<Vehicule> getVehicules() {
        return vehicules;
    }

    public House vehicules(Set<Vehicule> vehicules) {
        this.vehicules = vehicules;
        return this;
    }

    public House addVehicules(Vehicule vehicule) {
        this.vehicules.add(vehicule);
        vehicule.setHouse(this);
        return this;
    }

    public House removeVehicules(Vehicule vehicule) {
        this.vehicules.remove(vehicule);
        vehicule.setHouse(null);
        return this;
    }

    public void setVehicules(Set<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    public Set<Visitant> getVisitants() {
        return visitants;
    }

    public House visitants(Set<Visitant> visitants) {
        this.visitants = visitants;
        return this;
    }

    public House addVisitants(Visitant visitant) {
        this.visitants.add(visitant);
        visitant.setHouse(this);
        return this;
    }

    public House removeVisitants(Visitant visitant) {
        this.visitants.remove(visitant);
        visitant.setHouse(null);
        return this;
    }

    public void setVisitants(Set<Visitant> visitants) {
        this.visitants = visitants;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public House notes(Set<Note> notes) {
        this.notes = notes;
        return this;
    }

    public House addNotes(Note note) {
        this.notes.add(note);
        note.setHouse(this);
        return this;
    }

    public House removeNotes(Note note) {
        this.notes.remove(note);
        note.setHouse(null);
        return this;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Set<Resident> getResidents() {
        return residents;
    }

    public House residents(Set<Resident> residents) {
        this.residents = residents;
        return this;
    }

    public House addResidents(Resident resident) {
        this.residents.add(resident);
        resident.setHouse(this);
        return this;
    }

    public House removeResidents(Resident resident) {
        this.residents.remove(resident);
        resident.setHouse(null);
        return this;
    }

    public void setResidents(Set<Resident> residents) {
        this.residents = residents;
    }

    public Set<Emergency> getEmergencies() {
        return emergencies;
    }

    public House emergencies(Set<Emergency> emergencies) {
        this.emergencies = emergencies;
        return this;
    }

    public House addEmergencies(Emergency emergency) {
        this.emergencies.add(emergency);
        emergency.setHouse(this);
        return this;
    }

    public House removeEmergencies(Emergency emergency) {
        this.emergencies.remove(emergency);
        emergency.setHouse(null);
        return this;
    }

    public void setEmergencies(Set<Emergency> emergencies) {
        this.emergencies = emergencies;
    }

    public Company getCompany() {
        return company;
    }

    public House company(Company company) {
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
        House house = (House) o;
        if (house.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), house.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "House{" +
            "id=" + getId() +
            ", housenumber='" + getHousenumber() + "'" +
            ", extension='" + getExtension() + "'" +
            ", isdesocupated='" + getIsdesocupated() + "'" +
            ", desocupationinitialtime='" + getDesocupationinitialtime() + "'" +
            ", desocupationfinaltime='" + getDesocupationfinaltime() + "'" +
            ", securityKey='" + getSecurityKey() + "'" +
            ", emergencyKey='" + getEmergencyKey() + "'" +
            ", loginCode='" + getLoginCode() + "'" +
            ", codeStatus='" + getCodeStatus() + "'" +
            ", due='" + getDue() + "'" +
            ", squareMeters='" + getSquareMeters() + "'" +
            "}";
    }
}
