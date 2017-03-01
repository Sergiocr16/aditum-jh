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
 * A Watch.
 */
@Entity
@Table(name = "watch")
public class Watch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initialtime")
    private ZonedDateTime initialtime;

    @Column(name = "finaltime")
    private ZonedDateTime finaltime;

    @NotNull
    @Column(name = "responsableofficer", nullable = false)
    private String responsableofficer;

    @OneToMany(mappedBy = "watch")
    @JsonIgnore
    private Set<AccessDoor> accessDoors = new HashSet<>();

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getInitialtime() {
        return initialtime;
    }

    public Watch initialtime(ZonedDateTime initialtime) {
        this.initialtime = initialtime;
        return this;
    }

    public void setInitialtime(ZonedDateTime initialtime) {
        this.initialtime = initialtime;
    }

    public ZonedDateTime getFinaltime() {
        return finaltime;
    }

    public Watch finaltime(ZonedDateTime finaltime) {
        this.finaltime = finaltime;
        return this;
    }

    public void setFinaltime(ZonedDateTime finaltime) {
        this.finaltime = finaltime;
    }

    public String getResponsableofficer() {
        return responsableofficer;
    }

    public Watch responsableofficer(String responsableofficer) {
        this.responsableofficer = responsableofficer;
        return this;
    }

    public void setResponsableofficer(String responsableofficer) {
        this.responsableofficer = responsableofficer;
    }

    public Set<AccessDoor> getAccessDoors() {
        return accessDoors;
    }

    public Watch accessDoors(Set<AccessDoor> accessDoors) {
        this.accessDoors = accessDoors;
        return this;
    }

    public Watch addAccessDoor(AccessDoor accessDoor) {
        this.accessDoors.add(accessDoor);
        accessDoor.setWatch(this);
        return this;
    }

    public Watch removeAccessDoor(AccessDoor accessDoor) {
        this.accessDoors.remove(accessDoor);
        accessDoor.setWatch(null);
        return this;
    }

    public void setAccessDoors(Set<AccessDoor> accessDoors) {
        this.accessDoors = accessDoors;
    }

    public Company getCompany() {
        return company;
    }

    public Watch company(Company company) {
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
        Watch watch = (Watch) o;
        if (watch.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, watch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Watch{" +
            "id=" + id +
            ", initialtime='" + initialtime + "'" +
            ", finaltime='" + finaltime + "'" +
            ", responsableofficer='" + responsableofficer + "'" +
            '}';
    }
}
