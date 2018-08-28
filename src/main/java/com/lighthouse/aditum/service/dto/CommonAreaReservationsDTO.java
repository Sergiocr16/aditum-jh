package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the CommonAreaReservations entity.
 */
public class CommonAreaReservationsDTO implements Serializable {

    private Long id;

    @NotNull
    private Long houseId;

    @NotNull
    private Long residentId;

    @NotNull
    private String initialTime;

    @NotNull
    private String finalTime;

    private String comments;

    private ZonedDateTime initalDate;

    private ZonedDateTime finalDate;

    private Integer reservationCharge;

    private Integer devolutionAmmount;

    private boolean isAvailable;

    private Integer status;

    private Long commonAreaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public String getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(String initialTime) {
        this.initialTime = initialTime;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ZonedDateTime getInitalDate() {
        return initalDate;
    }

    public void setInitalDate(ZonedDateTime initalDate) {
        this.initalDate = initalDate;
    }

    public ZonedDateTime getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(ZonedDateTime finalDate) {
        this.finalDate = finalDate;
    }

    public Integer getReservationCharge() {
        return reservationCharge;
    }

    public void setReservationCharge(Integer reservationCharge) {
        this.reservationCharge = reservationCharge;
    }

    public Integer getDevolutionAmmount() {
        return devolutionAmmount;
    }

    public void setDevolutionAmmount(Integer devolutionAmmount) {
        this.devolutionAmmount = devolutionAmmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

        CommonAreaReservationsDTO commonAreaReservationsDTO = (CommonAreaReservationsDTO) o;
        if(commonAreaReservationsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commonAreaReservationsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommonAreaReservationsDTO{" +
            "id=" + getId() +
            ", houseId='" + getHouseId() + "'" +
            ", residentId='" + getResidentId() + "'" +
            ", initialTime='" + getInitialTime() + "'" +
            ", finalTime='" + getFinalTime() + "'" +
            ", comments='" + getComments() + "'" +
            ", initalDate='" + getInitalDate() + "'" +
            ", finalDate='" + getFinalDate() + "'" +
            ", reservationCharge='" + getReservationCharge() + "'" +
            ", devolutionAmmount='" + getDevolutionAmmount() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
