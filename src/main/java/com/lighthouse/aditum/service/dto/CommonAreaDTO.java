package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
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

    private Integer deleted;

    private Integer companyId;

    private Integer hasBlocks;

    private Integer hasDaysBeforeToReserve;

    private Integer daysBeforeToReserve;

    private Integer hasDaysToReserveIfFree;

    private String daysToReserveIfFree;

    private Integer hasDistanceBetweenReservations;

    private Integer distanceBetweenReservations;

    private Integer needsApproval;

    private Integer hasValidityTime;

    private Integer validityTimeHours;

    private Integer hasReservationsLimit;

    private ZonedDateTime periodBegin;

    private Integer periodTimes;

    private Integer periodMonthEnd;

    private Integer limitPeoplePerReservation;

    private Integer limitActiveReservations;

    private Boolean hasMaximunDaysInAdvance;

    private Integer maximunDaysInAdvance;

    private Boolean hasDefinePeopleQuantity;

    private Integer quantityGuestLimit;

    private Integer timesPerDay;

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getHasBlocks() {
        return hasBlocks;
    }

    public void setHasBlocks(Integer hasBlocks) {
        this.hasBlocks = hasBlocks;
    }

    public Integer getHasDaysBeforeToReserve() {
        return hasDaysBeforeToReserve;
    }

    public void setHasDaysBeforeToReserve(Integer hasDaysBeforeToReserve) {
        this.hasDaysBeforeToReserve = hasDaysBeforeToReserve;
    }

    public Integer getDaysBeforeToReserve() {
        return daysBeforeToReserve;
    }

    public void setDaysBeforeToReserve(Integer daysBeforeToReserve) {
        this.daysBeforeToReserve = daysBeforeToReserve;
    }

    public Integer getHasDaysToReserveIfFree() {
        return hasDaysToReserveIfFree;
    }

    public void setHasDaysToReserveIfFree(Integer hasDaysToReserveIfFree) {
        this.hasDaysToReserveIfFree = hasDaysToReserveIfFree;
    }

    public String getDaysToReserveIfFree() {
        return daysToReserveIfFree;
    }

    public void setDaysToReserveIfFree(String daysToReserveIfFree) {
        this.daysToReserveIfFree = daysToReserveIfFree;
    }

    public Integer getHasDistanceBetweenReservations() {
        return hasDistanceBetweenReservations;
    }

    public void setHasDistanceBetweenReservations(Integer hasDistanceBetweenReservations) {
        this.hasDistanceBetweenReservations = hasDistanceBetweenReservations;
    }

    public Integer getDistanceBetweenReservations() {
        return distanceBetweenReservations;
    }

    public void setDistanceBetweenReservations(Integer distanceBetweenReservations) {
        this.distanceBetweenReservations = distanceBetweenReservations;
    }

    public Integer getNeedsApproval() {
        return needsApproval;
    }

    public void setNeedsApproval(Integer needsApproval) {
        this.needsApproval = needsApproval;
    }

    public Integer getHasValidityTime() {
        return hasValidityTime;
    }

    public void setHasValidityTime(Integer hasValidityTime) {
        this.hasValidityTime = hasValidityTime;
    }

    public Integer getValidityTimeHours() {
        return validityTimeHours;
    }

    public void setValidityTimeHours(Integer validityTimeHours) {
        this.validityTimeHours = validityTimeHours;
    }

    public Integer getHasReservationsLimit() {
        return hasReservationsLimit;
    }

    public void setHasReservationsLimit(Integer hasReservationsLimit) {
        this.hasReservationsLimit = hasReservationsLimit;
    }

    public ZonedDateTime getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(ZonedDateTime periodBegin) {
        this.periodBegin = periodBegin;
    }

    public Integer getPeriodTimes() {
        return periodTimes;
    }

    public void setPeriodTimes(Integer periodTimes) {
        this.periodTimes = periodTimes;
    }

    public Integer getPeriodMonthEnd() {
        return periodMonthEnd;
    }

    public void setPeriodMonthEnd(Integer periodMonthEnd) {
        this.periodMonthEnd = periodMonthEnd;
    }

    public Integer getLimitPeoplePerReservation() {
        return limitPeoplePerReservation;
    }

    public void setLimitPeoplePerReservation(Integer limitPeoplePerReservation) {
        this.limitPeoplePerReservation = limitPeoplePerReservation;
    }

    public Integer getLimitActiveReservations() {
        return limitActiveReservations;
    }

    public void setLimitActiveReservations(Integer limitActiveReservations) {
        this.limitActiveReservations = limitActiveReservations;
    }

    public Boolean isHasMaximunDaysInAdvance() {
        return hasMaximunDaysInAdvance;
    }

    public void setHasMaximunDaysInAdvance(Boolean hasMaximunDaysInAdvance) {
        this.hasMaximunDaysInAdvance = hasMaximunDaysInAdvance;
    }

    public Integer getMaximunDaysInAdvance() {
        return maximunDaysInAdvance;
    }

    public void setMaximunDaysInAdvance(Integer maximunDaysInAdvance) {
        this.maximunDaysInAdvance = maximunDaysInAdvance;
    }

    public Boolean isHasDefinePeopleQuantity() {
        return hasDefinePeopleQuantity;
    }

    public void setHasDefinePeopleQuantity(Boolean hasDefinePeopleQuantity) {
        this.hasDefinePeopleQuantity = hasDefinePeopleQuantity;
    }

    public Integer getQuantityGuestLimit() {
        return quantityGuestLimit;
    }

    public void setQuantityGuestLimit(Integer quantityGuestLimit) {
        this.quantityGuestLimit = quantityGuestLimit;
    }

    public Integer getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(Integer timesPerDay) {
        this.timesPerDay = timesPerDay;
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
            ", chargeRequired=" + getChargeRequired() +
            ", reservationWithDebt=" + getReservationWithDebt() +
            ", picture='" + getPicture() + "'" +
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
            ", timesPerDay=" + getTimesPerDay() +
            "}";
    }
}
