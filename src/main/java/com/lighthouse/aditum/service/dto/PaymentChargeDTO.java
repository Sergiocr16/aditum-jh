package com.lighthouse.aditum.service.dto;


import com.lighthouse.aditum.service.util.RandomUtil;

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

    private String abonadoFormatted;

    private String paymentAmmount;

    private String pendienteFormatted;

    private String ammountFormatted;

    private String leftToPayFormatted;

    private String category;

    private String waterConsumption;

    public String getWaterConsumption() {

        return waterConsumption;
    }

    public void setWaterConsumption(String waterConsumption) {
        this.waterConsumption = waterConsumption;
    }

    public String getPaymentAmmount() {
        return paymentAmmount;
    }

    public void setPaymentAmmount(String paymentAmmount) {
        this.paymentAmmount = paymentAmmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAbonadoFormatted() {
        return abonadoFormatted;
    }

    public void setAbonadoFormatted(String currency,String abonadoFormatted) {
        this.abonadoFormatted = RandomUtil.formatMoneyString(currency,abonadoFormatted);
    }


    public String getAmmountFormatted() {
        return ammountFormatted;
    }

    public void setAmmountFormatted(String currency,String ammountFormatted) {
        this.ammountFormatted = RandomUtil.formatMoneyString(currency, ammountFormatted);
    }

    public String getLeftToPayFormatted() {
        return leftToPayFormatted;
    }

    public void setLeftToPayFormatted(String currency,String leftToPayFormatted) {
        this.leftToPayFormatted = RandomUtil.formatMoneyString(currency, leftToPayFormatted);
    }

    public PaymentChargeDTO(Long id, Integer type, ZonedDateTime date, String concept, String ammount, Long originalCharge, String consecutive, String abonado, String pendiente, Integer oldStyle, Long paymentId) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.concept = concept;
        this.ammount = ammount;
        this.originalCharge = originalCharge;
        this.consecutive = consecutive;
        this.abonado = abonado;
        this.leftToPay = pendiente;
        this.oldStyle = oldStyle;
        this.paymentId = paymentId;
    }
    public PaymentChargeDTO(){

    }
    public PaymentChargeDTO(Long id, Integer type, ZonedDateTime date, String concept, String consecutive, Long originalCharge, String ammount, String leftToPay, String abonado, Integer oldStyle, Long paymentId, String abonadoFormatted, String pendienteFormatted, String ammountFormatted) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.concept = concept;
        this.consecutive = consecutive;
        this.originalCharge = originalCharge;
        this.ammount = ammount;
        this.leftToPay = leftToPay;
        this.abonado = abonado;
        this.oldStyle = oldStyle;
        this.paymentId = paymentId;
        this.abonadoFormatted = abonadoFormatted;
        this.pendienteFormatted = pendienteFormatted;
        this.ammountFormatted = ammountFormatted;
    }

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
