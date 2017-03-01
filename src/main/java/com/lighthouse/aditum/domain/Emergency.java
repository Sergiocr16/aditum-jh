package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Emergency.
 */
@Entity
@Table(name = "emergency")
public class Emergency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "is_attended", nullable = false)
    private Integer isAttended;

    @Column(name = "observation")
    private String observation;

    @ManyToOne
    private Company company;

    @ManyToOne
    private House house;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsAttended() {
        return isAttended;
    }

    public Emergency isAttended(Integer isAttended) {
        this.isAttended = isAttended;
        return this;
    }

    public void setIsAttended(Integer isAttended) {
        this.isAttended = isAttended;
    }

    public String getObservation() {
        return observation;
    }

    public Emergency observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Company getCompany() {
        return company;
    }

    public Emergency company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public House getHouse() {
        return house;
    }

    public Emergency house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Emergency emergency = (Emergency) o;
        if (emergency.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, emergency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Emergency{" +
            "id=" + id +
            ", isAttended='" + isAttended + "'" +
            ", observation='" + observation + "'" +
            '}';
    }
}
