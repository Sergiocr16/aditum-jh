package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the HouseLoginTracker entity.
 */
public class HouseLoginTrackerDTO implements Serializable {

    private Long id;

    private ZonedDateTime lastTime;

    private String user;

    private Long houseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(ZonedDateTime lastTime) {
        this.lastTime = lastTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HouseLoginTrackerDTO houseLoginTrackerDTO = (HouseLoginTrackerDTO) o;
        if(houseLoginTrackerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), houseLoginTrackerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HouseLoginTrackerDTO{" +
            "id=" + getId() +
            ", lastTime='" + getLastTime() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }
}
