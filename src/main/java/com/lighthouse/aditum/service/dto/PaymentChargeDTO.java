package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PaymentCharge entity.
 */
public class PaymentChargeDTO implements Serializable {

    private Long id;

    private Integer type;

    private ZonedDateTime date;

    private String concept;

    private String consecutive;

    private Long originalCharge;

    private String ammount;

    private String leftToPay;

    private String abonado;

    private Integer oldStyle;

    private Long paymentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(String consecutive) {
        this.consecutive = consecutive;
    }

    public Long getOriginalCharge() {
        return originalCharge;
    }

    public void setOriginalCharge(Long originalCharge) {
        this.originalCharge = originalCharge;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getLeftToPay() {
        return leftToPay;
    }

    public void setLeftToPay(String leftToPay) {
        this.leftToPay = leftToPay;
    }

    public String getAbonado() {
        return abonado;
    }

    public void setAbonado(String abonado) {
        this.abonado = abonado;
    }

    public Integer getOldStyle() {
        return oldStyle;
    }

    public void setOldStyle(Integer oldStyle) {
        this.oldStyle = oldStyle;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentChargeDTO paymentChargeDTO = (PaymentChargeDTO) o;
        if(paymentChargeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentChargeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentChargeDTO{" +
            "id=" + getId() +
            ", type=" + getType() +
            ", date='" + getDate() + "'" +
            ", concept='" + getConcept() + "'" +
            ", consecutive='" + getConsecutive() + "'" +
            ", originalCharge=" + getOriginalCharge() +
            ", ammount='" + getAmmount() + "'" +
            ", leftToPay='" + getLeftToPay() + "'" +
            ", abonado='" + getAbonado() + "'" +
            ", oldStyle=" + getOldStyle() +
            "}";
    }
}
