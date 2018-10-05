package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CommonArea.
 */
@Entity
@Table(name = "common_area")
public class CommonArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "reservation_charge")
    private String reservationCharge;

    @Column(name = "devolution_ammount")
    private String devolutionAmmount;

    @Max(value = 1)
    @Column(name = "charge_required")
    private Integer chargeRequired;

    @Column(name = "reservation_with_debt")
    private Integer reservationWithDebt;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "maximun_hours")
    private Integer maximunHours;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "company_id")
    private Integer companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CommonArea name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public CommonArea description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReservationCharge() {
        return reservationCharge;
    }

    public CommonArea reservationCharge(String reservationCharge) {
        this.reservationCharge = reservationCharge;
        return this;
    }

    public void setReservationCharge(String reservationCharge) {
        this.reservationCharge = reservationCharge;
    }

    public String getDevolutionAmmount() {
        return devolutionAmmount;
    }

    public CommonArea devolutionAmmount(String devolutionAmmount) {
        this.devolutionAmmount = devolutionAmmount;
        return this;
    }

    public void setDevolutionAmmount(String devolutionAmmount) {
        this.devolutionAmmount = devolutionAmmount;
    }

    public Integer getChargeRequired() {
        return chargeRequired;
    }

    public CommonArea chargeRequired(Integer chargeRequired) {
        this.chargeRequired = chargeRequired;
        return this;
    }

    public void setChargeRequired(Integer chargeRequired) {
        this.chargeRequired = chargeRequired;
    }

    public Integer getReservationWithDebt() {
        return reservationWithDebt;
    }

    public CommonArea reservationWithDebt(Integer reservationWithDebt) {
        this.reservationWithDebt = reservationWithDebt;
        return this;
    }

    public void setReservationWithDebt(Integer reservationWithDebt) {
        this.reservationWithDebt = reservationWithDebt;
    }

    public byte[] getPicture() {
        return picture;
    }

    public CommonArea picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public CommonArea pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Integer getMaximunHours() {
        return maximunHours;
    }

    public CommonArea maximunHours(Integer maximunHours) {
        this.maximunHours = maximunHours;
        return this;
    }

    public void setMaximunHours(Integer maximunHours) {
        this.maximunHours = maximunHours;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public CommonArea deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public CommonArea companyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonArea commonArea = (CommonArea) o;
        if (commonArea.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commonArea.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommonArea{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", reservationCharge='" + getReservationCharge() + "'" +
            ", devolutionAmmount='" + getDevolutionAmmount() + "'" +
            ", chargeRequired='" + getChargeRequired() + "'" +
            ", reservationWithDebt='" + getReservationWithDebt() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + pictureContentType + "'" +
            ", maximunHours='" + getMaximunHours() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", companyId='" + getCompanyId() + "'" +
            "}";
    }
}
