package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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

    @Column(name = "has_blocks")
    private Integer hasBlocks;

    @Column(name = "has_days_before_to_reserve")
    private Integer hasDaysBeforeToReserve;

    @Column(name = "days_before_to_reserve")
    private Integer daysBeforeToReserve;

    @Column(name = "has_days_to_reserve_if_free")
    private Integer hasDaysToReserveIfFree;

    @Column(name = "days_to_reserve_if_free")
    private String daysToReserveIfFree;

    @Column(name = "has_distance_between_reservations")
    private Integer hasDistanceBetweenReservations;

    @Column(name = "distance_between_reservations")
    private Integer distanceBetweenReservations;

    @Column(name = "needs_approval")
    private Integer needsApproval;

    @Column(name = "has_validity_time")
    private Integer hasValidityTime;

    @Column(name = "validity_time_hours")
    private Integer validityTimeHours;

    @Column(name = "has_reservations_limit")
    private Integer hasReservationsLimit;

    @Column(name = "period_begin")
    private ZonedDateTime periodBegin;

    @Column(name = "period_times")
    private Integer periodTimes;

    @Column(name = "period_month_end")
    private Integer periodMonthEnd;

    @Column(name = "limit_people_per_reservation")
    private Integer limitPeoplePerReservation;

    @Column(name = "limit_active_reservations")
    private Integer limitActiveReservations;

    @Column(name = "has_maximun_days_in_advance")
    private Boolean hasMaximunDaysInAdvance;

    @Column(name = "maximun_days_in_advance")
    private Integer maximunDaysInAdvance;

    @Column(name = "has_define_people_quantity")
    private Boolean hasDefinePeopleQuantity;

    @Column(name = "quantity_guest_limit")
    private Integer quantityGuestLimit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public Integer getHasBlocks() {
        return hasBlocks;
    }

    public CommonArea hasBlocks(Integer hasBlocks) {
        this.hasBlocks = hasBlocks;
        return this;
    }

    public void setHasBlocks(Integer hasBlocks) {
        this.hasBlocks = hasBlocks;
    }

    public Integer getHasDaysBeforeToReserve() {
        return hasDaysBeforeToReserve;
    }

    public CommonArea hasDaysBeforeToReserve(Integer hasDaysBeforeToReserve) {
        this.hasDaysBeforeToReserve = hasDaysBeforeToReserve;
        return this;
    }

    public void setHasDaysBeforeToReserve(Integer hasDaysBeforeToReserve) {
        this.hasDaysBeforeToReserve = hasDaysBeforeToReserve;
    }

    public Integer getDaysBeforeToReserve() {
        return daysBeforeToReserve;
    }

    public CommonArea daysBeforeToReserve(Integer daysBeforeToReserve) {
        this.daysBeforeToReserve = daysBeforeToReserve;
        return this;
    }

    public void setDaysBeforeToReserve(Integer daysBeforeToReserve) {
        this.daysBeforeToReserve = daysBeforeToReserve;
    }

    public Integer getHasDaysToReserveIfFree() {
        return hasDaysToReserveIfFree;
    }

    public CommonArea hasDaysToReserveIfFree(Integer hasDaysToReserveIfFree) {
        this.hasDaysToReserveIfFree = hasDaysToReserveIfFree;
        return this;
    }

    public void setHasDaysToReserveIfFree(Integer hasDaysToReserveIfFree) {
        this.hasDaysToReserveIfFree = hasDaysToReserveIfFree;
    }

    public String getDaysToReserveIfFree() {
        return daysToReserveIfFree;
    }

    public CommonArea daysToReserveIfFree(String daysToReserveIfFree) {
        this.daysToReserveIfFree = daysToReserveIfFree;
        return this;
    }

    public void setDaysToReserveIfFree(String daysToReserveIfFree) {
        this.daysToReserveIfFree = daysToReserveIfFree;
    }

    public Integer getHasDistanceBetweenReservations() {
        return hasDistanceBetweenReservations;
    }

    public CommonArea hasDistanceBetweenReservations(Integer hasDistanceBetweenReservations) {
        this.hasDistanceBetweenReservations = hasDistanceBetweenReservations;
        return this;
    }

    public void setHasDistanceBetweenReservations(Integer hasDistanceBetweenReservations) {
        this.hasDistanceBetweenReservations = hasDistanceBetweenReservations;
    }

    public Integer getDistanceBetweenReservations() {
        return distanceBetweenReservations;
    }

    public CommonArea distanceBetweenReservations(Integer distanceBetweenReservations) {
        this.distanceBetweenReservations = distanceBetweenReservations;
        return this;
    }

    public void setDistanceBetweenReservations(Integer distanceBetweenReservations) {
        this.distanceBetweenReservations = distanceBetweenReservations;
    }

    public Integer getNeedsApproval() {
        return needsApproval;
    }

    public CommonArea needsApproval(Integer needsApproval) {
        this.needsApproval = needsApproval;
        return this;
    }

    public void setNeedsApproval(Integer needsApproval) {
        this.needsApproval = needsApproval;
    }

    public Integer getHasValidityTime() {
        return hasValidityTime;
    }

    public CommonArea hasValidityTime(Integer hasValidityTime) {
        this.hasValidityTime = hasValidityTime;
        return this;
    }

    public void setHasValidityTime(Integer hasValidityTime) {
        this.hasValidityTime = hasValidityTime;
    }

    public Integer getValidityTimeHours() {
        return validityTimeHours;
    }

    public CommonArea validityTimeHours(Integer validityTimeHours) {
        this.validityTimeHours = validityTimeHours;
        return this;
    }

    public void setValidityTimeHours(Integer validityTimeHours) {
        this.validityTimeHours = validityTimeHours;
    }

    public Integer getHasReservationsLimit() {
        return hasReservationsLimit;
    }

    public CommonArea hasReservationsLimit(Integer hasReservationsLimit) {
        this.hasReservationsLimit = hasReservationsLimit;
        return this;
    }

    public void setHasReservationsLimit(Integer hasReservationsLimit) {
        this.hasReservationsLimit = hasReservationsLimit;
    }

    public ZonedDateTime getPeriodBegin() {
        return periodBegin;
    }

    public CommonArea periodBegin(ZonedDateTime periodBegin) {
        this.periodBegin = periodBegin;
        return this;
    }

    public void setPeriodBegin(ZonedDateTime periodBegin) {
        this.periodBegin = periodBegin;
    }

    public Integer getPeriodTimes() {
        return periodTimes;
    }

    public CommonArea periodTimes(Integer periodTimes) {
        this.periodTimes = periodTimes;
        return this;
    }

    public void setPeriodTimes(Integer periodTimes) {
        this.periodTimes = periodTimes;
    }

    public Integer getPeriodMonthEnd() {
        return periodMonthEnd;
    }

    public CommonArea periodMonthEnd(Integer periodMonthEnd) {
        this.periodMonthEnd = periodMonthEnd;
        return this;
    }

    public void setPeriodMonthEnd(Integer periodMonthEnd) {
        this.periodMonthEnd = periodMonthEnd;
    }

    public Integer getLimitPeoplePerReservation() {
        return limitPeoplePerReservation;
    }

    public CommonArea limitPeoplePerReservation(Integer limitPeoplePerReservation) {
        this.limitPeoplePerReservation = limitPeoplePerReservation;
        return this;
    }

    public void setLimitPeoplePerReservation(Integer limitPeoplePerReservation) {
        this.limitPeoplePerReservation = limitPeoplePerReservation;
    }

    public Integer getLimitActiveReservations() {
        return limitActiveReservations;
    }

    public CommonArea limitActiveReservations(Integer limitActiveReservations) {
        this.limitActiveReservations = limitActiveReservations;
        return this;
    }

    public void setLimitActiveReservations(Integer limitActiveReservations) {
        this.limitActiveReservations = limitActiveReservations;
    }

    public Boolean isHasMaximunDaysInAdvance() {
        return hasMaximunDaysInAdvance;
    }

    public CommonArea hasMaximunDaysInAdvance(Boolean hasMaximunDaysInAdvance) {
        this.hasMaximunDaysInAdvance = hasMaximunDaysInAdvance;
        return this;
    }

    public void setHasMaximunDaysInAdvance(Boolean hasMaximunDaysInAdvance) {
        this.hasMaximunDaysInAdvance = hasMaximunDaysInAdvance;
    }

    public Integer getMaximunDaysInAdvance() {
        return maximunDaysInAdvance;
    }

    public CommonArea maximunDaysInAdvance(Integer maximunDaysInAdvance) {
        this.maximunDaysInAdvance = maximunDaysInAdvance;
        return this;
    }

    public void setMaximunDaysInAdvance(Integer maximunDaysInAdvance) {
        this.maximunDaysInAdvance = maximunDaysInAdvance;
    }

    public Boolean isHasDefinePeopleQuantity() {
        return hasDefinePeopleQuantity;
    }

    public CommonArea hasDefinePeopleQuantity(Boolean hasDefinePeopleQuantity) {
        this.hasDefinePeopleQuantity = hasDefinePeopleQuantity;
        return this;
    }

    public void setHasDefinePeopleQuantity(Boolean hasDefinePeopleQuantity) {
        this.hasDefinePeopleQuantity = hasDefinePeopleQuantity;
    }

    public Integer getQuantityGuestLimit() {
        return quantityGuestLimit;
    }

    public CommonArea quantityGuestLimit(Integer quantityGuestLimit) {
        this.quantityGuestLimit = quantityGuestLimit;
        return this;
    }

    public void setQuantityGuestLimit(Integer quantityGuestLimit) {
        this.quantityGuestLimit = quantityGuestLimit;
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
            ", chargeRequired=" + getChargeRequired() +
            ", reservationWithDebt=" + getReservationWithDebt() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", maximunHours=" + getMaximunHours() +
            ", deleted=" + getDeleted() +
            ", companyId=" + getCompanyId() +
            ", hasBlocks=" + getHasBlocks() +
            ", hasDaysBeforeToReserve=" + getHasDaysBeforeToReserve() +
            ", daysBeforeToReserve=" + getDaysBeforeToReserve() +
            ", hasDaysToReserveIfFree=" + getHasDaysToReserveIfFree() +
            ", daysToReserveIfFree='" + getDaysToReserveIfFree() + "'" +
            ", hasDistanceBetweenReservations=" + getHasDistanceBetweenReservations() +
            ", distanceBetweenReservations=" + getDistanceBetweenReservations() +
            ", needsApproval=" + getNeedsApproval() +
            ", hasValidityTime=" + getHasValidityTime() +
            ", validityTimeHours=" + getValidityTimeHours() +
            ", hasReservationsLimit=" + getHasReservationsLimit() +
            ", periodBegin='" + getPeriodBegin() + "'" +
            ", periodTimes=" + getPeriodTimes() +
            ", periodMonthEnd=" + getPeriodMonthEnd() +
            ", limitPeoplePerReservation=" + getLimitPeoplePerReservation() +
            ", limitActiveReservations=" + getLimitActiveReservations() +
            ", hasMaximunDaysInAdvance='" + isHasMaximunDaysInAdvance() + "'" +
            ", maximunDaysInAdvance=" + getMaximunDaysInAdvance() +
            ", hasDefinePeopleQuantity='" + isHasDefinePeopleQuantity() + "'" +
            ", quantityGuestLimit=" + getQuantityGuestLimit() +
            "}";
    }
}
