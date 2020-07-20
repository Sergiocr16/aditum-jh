package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;
import static com.lighthouse.aditum.service.util.RandomUtil.formatMoneyString;
/**
 * A DTO for the PaymentCharge entity.
 */
public class PaymentChargeDTO implements Serializable {

    private Long id;

    private Integer type;

    private ZonedDateTime date;

    private String concept;

    private String ammount;

    private Long originalCharge;

    private Integer consecutive;

    private String abonado;

    private String pendiente;

    private Integer oldStyle;

    private Long paymentId;

    private String abonadoFormatted;

    private String pendienteFormatted;

    private String ammountFormatted;

    public String getAbonadoFormatted() {
        return abonadoFormatted;
    }

    public void setAbonadoFormatted(String abonadoFormatted) {
        this.abonadoFormatted = abonadoFormatted;
    }

    public String getPendienteFormatted() {
        return pendienteFormatted;
    }

    public void setPendienteFormatted(String pendienteFormatted) {
        this.pendienteFormatted = pendienteFormatted;
    }

    public String getAmmountFormatted() {
        return ammountFormatted;
    }

    public void setAmmountFormatted(String ammountFormatted) {
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

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public Long getOriginalCharge() {
        return originalCharge;
    }

    public void setOriginalCharge(Long originalCharge) {
        this.originalCharge = originalCharge;
    }

    public Integer getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(Integer consecutive) {
        this.consecutive = consecutive;
    }

    public String getAbonado() {
        return abonado;
    }

    public void setAbonado(String abonado) {
        this.abonado = abonado;
    }

    public String getPendiente() {
        return pendiente;
    }

    public void setPendiente(String pendiente) {
        this.pendiente = pendiente;
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

    public PaymentChargeDTO(){}

    public PaymentChargeDTO(Long id, Integer type, ZonedDateTime date, String concept, String ammount, Long originalCharge, Integer consecutive, String abonado, String pendiente, Integer oldStyle, Long paymentId) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.concept = concept;
        this.ammount = ammount;
        this.originalCharge = originalCharge;
        this.consecutive = consecutive;
        this.abonado = abonado;
        this.pendiente = pendiente;
        this.oldStyle = oldStyle;
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
        if (paymentChargeDTO.getId() == null || getId() == null) {
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
            ", ammount='" + getAmmount() + "'" +
            ", originalCharge=" + getOriginalCharge() +
            ", consecutive=" + getConsecutive() +
            ", abonado='" + getAbonado() + "'" +
            ", pendiente='" + getPendiente() + "'" +
            ", oldStyle=" + getOldStyle() +
            "}";
    }
}
