package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ReservationHouseRestrictions entity.
 */
public class ReservationHouseRestrictionsDTO implements Serializable {

    private Long id;

    private Integer reservationQuantity;

    private ZonedDateTime lastTimeReservation;

    private Long houseId;

    private Long commonAreaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReservationQuantity() {
        return reservationQuantity;
    }

    public void setReservationQuantity(Integer reservationQuantity) {
        this.reservationQuantity = reservationQuantity;
    }

    public ZonedDateTime getLastTimeReservation() {
        return lastTimeReservation;
    }

    public void setLastTimeReservation(ZonedDateTime lastTimeReservation) {
        this.lastTimeReservation = lastTimeReservation;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getCommonAreaId() {
        return commonAreaId;
    }

    public void setCommonAreaId(Long commonAreaId) {
        this.commonAreaId = commonAreaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO = (ReservationHouseRestrictionsDTO) o;
        if(reservationHouseRestrictionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservationHouseRestrictionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReservationHouseRestrictionsDTO{" +
            "id=" + getId() +
            ", reservationQuantity=" + getReservationQuantity() +
            ", lastTimeReservation='" + getLastTimeReservation() + "'" +
            "}";
    }
}
