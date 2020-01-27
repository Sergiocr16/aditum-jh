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

    private Integer status;

    private String chargeEmail;

    private Long egressId;

    private Long paymentId;

    private String paymentProof;

    private Long commonAreaId;

    private Long companyId;

    private Long chargeIdId;

    private int availability;

    private ResidentDTO resident;

    private ChargeDTO charge;

    private HouseDTO house;

    private CommonAreaDTO commonArea;

    private boolean sendPendingEmail;

    private EgressDTO egress;

    private PaymentDTO payment;

    private String emailTitle;

    private int userType;

    private String houseNumber;

    private String residentName;


    private int validityTimePassed;

    public int getValidityTimePassed() {
        return validityTimePassed;
    }

    public void setValidityTimePassed(int validityTimePassed) {
        this.validityTimePassed = validityTimePassed;
    }


    public boolean isSendPendingEmail() {
        return sendPendingEmail;
    }

    public void setSendPendingEmail(boolean sendPendingEmail) {
        this.sendPendingEmail = sendPendingEmail;
    }

    public EgressDTO getEgress() {
        return egress;
    }

    public void setEgress(EgressDTO egress) {
        this.egress = egress;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }


    public CommonAreaDTO getCommonArea() {
        return commonArea;
    }

    public void setCommonArea(CommonAreaDTO commonArea) {
        this.commonArea = commonArea;
    }

    public ResidentDTO getResident() {
        return resident;
    }

    public void setResident(ResidentDTO resident) {
        this.resident = resident;
    }

    public ChargeDTO getCharge() {
        return charge;
    }

    public void setCharge(ChargeDTO charge) {
        this.charge = charge;
    }

    public HouseDTO getHouse() {
        return house;
    }

    public void setHouse(HouseDTO house) {
        this.house = house;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

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

    public String getChargeEmail() {
        return chargeEmail;
    }

    public void setChargeEmail(String chargeEmail) {
        this.chargeEmail = chargeEmail;
    }

    public Long getEgressId() {
        return egressId;
    }

    public void setEgressId(Long egressId) {
        this.egressId = egressId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentProof() {
        return paymentProof;
    }

    public void setPaymentProof(String paymentProof) {
        this.paymentProof = paymentProof;
    }

    public Long getCommonAreaId() {
        return commonAreaId;
    }

    public void setCommonAreaId(Long commonAreaId) {
        this.commonAreaId = commonAreaId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getChargeIdId() {
        return chargeIdId;
    }

    public void setChargeIdId(Long chargeId) {
        this.chargeIdId = chargeId;
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
            ", houseId=" + getHouseId() +
            ", residentId=" + getResidentId() +
            ", initialTime='" + getInitialTime() + "'" +
            ", finalTime='" + getFinalTime() + "'" +
            ", comments='" + getComments() + "'" +
            ", initalDate='" + getInitalDate() + "'" +
            ", finalDate='" + getFinalDate() + "'" +
            ", reservationCharge=" + getReservationCharge() +
            ", devolutionAmmount=" + getDevolutionAmmount() +
            ", status=" + getStatus() +
            ", chargeEmail='" + getChargeEmail() + "'" +
            ", egressId=" + getEgressId() +
            ", paymentId=" + getPaymentId() +
            ", paymentProof='" + getPaymentProof() + "'" +
            "}";
    }
}
