package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the CommonArea entity.
 */
public class CommonAreaDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private String reservationCharge;

    private String devolutionAmmount;

    @Max(value = 1)
    private Integer chargeRequired;

    private Integer reservationWithDebt;

    @Lob
    private byte[] picture;
    private String pictureContentType;

    private Integer maximunHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReservationCharge() {
        return reservationCharge;
    }

    public void setReservationCharge(String reservationCharge) {
        this.reservationCharge = reservationCharge;
    }

    public String getDevolutionAmmount() {
        return devolutionAmmount;
    }

    public void setDevolutionAmmount(String devolutionAmmount) {
        this.devolutionAmmount = devolutionAmmount;
    }

    public Integer getChargeRequired() {
        return chargeRequired;
    }

    public void setChargeRequired(Integer chargeRequired) {
        this.chargeRequired = chargeRequired;
    }

    public Integer getReservationWithDebt() {
        return reservationWithDebt;
    }

    public void setReservationWithDebt(Integer reservationWithDebt) {
        this.reservationWithDebt = reservationWithDebt;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Integer getMaximunHours() {
        return maximunHours;
    }

    public void setMaximunHours(Integer maximunHours) {
        this.maximunHours = maximunHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommonAreaDTO commonAreaDTO = (CommonAreaDTO) o;
        if(commonAreaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commonAreaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommonAreaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", reservationCharge='" + getReservationCharge() + "'" +
            ", devolutionAmmount='" + getDevolutionAmmount() + "'" +
            ", chargeRequired='" + getChargeRequired() + "'" +
            ", reservationWithDebt='" + getReservationWithDebt() + "'" +
            ", picture='" + getPicture() + "'" +
            ", maximunHours='" + getMaximunHours() + "'" +
            "}";
    }
}
