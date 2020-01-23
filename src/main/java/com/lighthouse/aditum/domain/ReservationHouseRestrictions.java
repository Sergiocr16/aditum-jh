package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A ReservationHouseRestrictions.
 */
@Entity
@Table(name = "reservation_house_restrictions")
public class ReservationHouseRestrictions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_quantity")
    private Integer reservationQuantity;

    @Column(name = "last_time_reservation")
    private ZonedDateTime lastTimeReservation;

    @ManyToOne
    private House house;

    @ManyToOne
    private CommonArea commonArea;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReservationQuantity() {
        return reservationQuantity;
    }

    public ReservationHouseRestrictions reservationQuantity(Integer reservationQuantity) {
        this.reservationQuantity = reservationQuantity;
        return this;
    }

    public void setReservationQuantity(Integer reservationQuantity) {
        this.reservationQuantity = reservationQuantity;
    }

    public ZonedDateTime getLastTimeReservation() {
        return lastTimeReservation;
    }

    public ReservationHouseRestrictions lastTimeReservation(ZonedDateTime lastTimeReservation) {
        this.lastTimeReservation = lastTimeReservation;
        return this;
    }

    public void setLastTimeReservation(ZonedDateTime lastTimeReservation) {
        this.lastTimeReservation = lastTimeReservation;
    }

    public House getHouse() {
        return house;
    }

    public ReservationHouseRestrictions house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public CommonArea getCommonArea() {
        return commonArea;
    }

    public ReservationHouseRestrictions commonArea(CommonArea commonArea) {
        this.commonArea = commonArea;
        return this;
    }

    public void setCommonArea(CommonArea commonArea) {
        this.commonArea = commonArea;
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
        ReservationHouseRestrictions reservationHouseRestrictions = (ReservationHouseRestrictions) o;
        if (reservationHouseRestrictions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservationHouseRestrictions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReservationHouseRestrictions{" +
            "id=" + getId() +
            ", reservationQuantity=" + getReservationQuantity() +
            ", lastTimeReservation='" + getLastTimeReservation() + "'" +
            "}";
    }
}
