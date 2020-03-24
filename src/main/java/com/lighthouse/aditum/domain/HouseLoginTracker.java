package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A HouseLoginTracker.
 */
@Entity
@Table(name = "house_login_tracker")
public class HouseLoginTracker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_time")
    private ZonedDateTime lastTime;

    @Column(name = "jhi_user")
    private String user;

    @ManyToOne
    private House house;

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getLastTime() {
        return lastTime;
    }

    public HouseLoginTracker lastTime(ZonedDateTime lastTime) {
        this.lastTime = lastTime;
        return this;
    }

    public void setLastTime(ZonedDateTime lastTime) {
        this.lastTime = lastTime;
    }

    public String getUser() {
        return user;
    }

    public HouseLoginTracker user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public House getHouse() {
        return house;
    }

    public HouseLoginTracker house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public HouseLoginTracker company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        HouseLoginTracker houseLoginTracker = (HouseLoginTracker) o;
        if (houseLoginTracker.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), houseLoginTracker.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HouseLoginTracker{" +
            "id=" + getId() +
            ", lastTime='" + getLastTime() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }
}
