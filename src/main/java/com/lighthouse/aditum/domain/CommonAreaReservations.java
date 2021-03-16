package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A CommonAreaReservations.
 */
@Entity
@Table(name = "common_area_reservations")
public class CommonAreaReservations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "house_id", nullable = false)
    private Long houseId;

    @NotNull
    @Column(name = "resident_id", nullable = false)
    private Long residentId;

    @NotNull
    @Column(name = "initial_time", nullable = false)
    private String initialTime;

    @NotNull
    @Column(name = "final_time", nullable = false)
    private String finalTime;

    @Column(name = "comments")
    private String comments;

    @Column(name = "inital_date")
    private ZonedDateTime initalDate;

    @Column(name = "final_date")
    private ZonedDateTime finalDate;

    @Column(name = "reservation_charge")
    private Integer reservationCharge;

    @Column(name = "devolution_ammount")
    private Integer devolutionAmmount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "charge_email")
    private String chargeEmail;

    @Column(name = "egress_id")
    private Long egressId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "people_quantity")
    private int peopleQuantity;

    @Column(name = "payment_proof")
    private String paymentProof;

    @Column(name = "return_money")
    private Integer returnMoney;

    @ManyToOne
    private CommonArea commonArea;

    @ManyToOne
    private Company company;

    @OneToOne
    @JoinColumn(unique = true)
    private Charge chargeId;

    public Integer getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Integer returnMoney) {
        this.returnMoney = returnMoney;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseId() {
        return houseId;
    }

    public CommonAreaReservations houseId(Long houseId) {
        this.houseId = houseId;
        return this;
    }

    public int getPeopleQuantity() {
        return peopleQuantity;
    }

    public CommonAreaReservations setPeopleQuantity(int peopleQuantity) {
        this.peopleQuantity = peopleQuantity;
        return this;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getResidentId() {
        return residentId;
    }

    public CommonAreaReservations residentId(Long residentId) {
        this.residentId = residentId;
        return this;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public String getInitialTime() {
        return initialTime;
    }

    public CommonAreaReservations initialTime(String initialTime) {
        this.initialTime = initialTime;
        return this;
    }

    public void setInitialTime(String initialTime) {
        this.initialTime = initialTime;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public CommonAreaReservations finalTime(String finalTime) {
        this.finalTime = finalTime;
        return this;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getComments() {
        return comments;
    }

    public CommonAreaReservations comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ZonedDateTime getInitalDate() {
        return initalDate;
    }

    public CommonAreaReservations initalDate(ZonedDateTime initalDate) {
        this.initalDate = initalDate;
        return this;
    }

    public void setInitalDate(ZonedDateTime initalDate) {
        this.initalDate = initalDate;
    }

    public ZonedDateTime getFinalDate() {
        return finalDate;
    }

    public CommonAreaReservations finalDate(ZonedDateTime finalDate) {
        this.finalDate = finalDate;
        return this;
    }

    public void setFinalDate(ZonedDateTime finalDate) {
        this.finalDate = finalDate;
    }

    public Integer getReservationCharge() {
        return reservationCharge;
    }

    public CommonAreaReservations reservationCharge(Integer reservationCharge) {
        this.reservationCharge = reservationCharge;
        return this;
    }

    public void setReservationCharge(Integer reservationCharge) {
        this.reservationCharge = reservationCharge;
    }

    public Integer getDevolutionAmmount() {
        return devolutionAmmount;
    }

    public CommonAreaReservations devolutionAmmount(Integer devolutionAmmount) {
        this.devolutionAmmount = devolutionAmmount;
        return this;
    }

    public void setDevolutionAmmount(Integer devolutionAmmount) {
        this.devolutionAmmount = devolutionAmmount;
    }

    public Integer getStatus() {
        return status;
    }

    public CommonAreaReservations status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getChargeEmail() {
        return chargeEmail;
    }

    public CommonAreaReservations chargeEmail(String chargeEmail) {
        this.chargeEmail = chargeEmail;
        return this;
    }

    public void setChargeEmail(String chargeEmail) {
        this.chargeEmail = chargeEmail;
    }

    public Long getEgressId() {
        return egressId;
    }

    public CommonAreaReservations egressId(Long egressId) {
        this.egressId = egressId;
        return this;
    }

    public void setEgressId(Long egressId) {
        this.egressId = egressId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public CommonAreaReservations paymentId(Long paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentProof() {
        return paymentProof;
    }

    public CommonAreaReservations paymentProof(String paymentProof) {
        this.paymentProof = paymentProof;
        return this;
    }

    public void setPaymentProof(String paymentProof) {
        this.paymentProof = paymentProof;
    }

    public CommonArea getCommonArea() {
        return commonArea;
    }

    public CommonAreaReservations commonArea(CommonArea commonArea) {
        this.commonArea = commonArea;
        return this;
    }

    public void setCommonArea(CommonArea commonArea) {
        this.commonArea = commonArea;
    }

    public Company getCompany() {
        return company;
    }

    public CommonAreaReservations company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Charge getChargeId() {
        return chargeId;
    }

    public CommonAreaReservations chargeId(Charge charge) {
        this.chargeId = charge;
        return this;
    }

    public void setChargeId(Charge charge) {
        this.chargeId = charge;
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
        CommonAreaReservations commonAreaReservations = (CommonAreaReservations) o;
        if (commonAreaReservations.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commonAreaReservations.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommonAreaReservations{" +
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
