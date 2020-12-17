package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A PaymentCharge.
 */
@Entity
@Table(name = "payment_charge")
public class PaymentCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "concept")
    private String concept;

    @Column(name = "consecutive")
    private String consecutive;

    @Column(name = "original_charge")
    private Long originalCharge;

    @Column(name = "ammount")
    private String ammount;

    @Column(name = "left_to_pay")
    private String leftToPay;

    @Column(name = "abonado")
    private String abonado;

    @Column(name = "old_style")
    private Integer oldStyle;

    @ManyToOne
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public PaymentCharge type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public PaymentCharge date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getConcept() {
        return concept;
    }

    public PaymentCharge concept(String concept) {
        this.concept = concept;
        return this;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getConsecutive() {
        return consecutive;
    }

    public PaymentCharge consecutive(String consecutive) {
        this.consecutive = consecutive;
        return this;
    }

    public void setConsecutive(String consecutive) {
        this.consecutive = consecutive;
    }

    public Long getOriginalCharge() {
        return originalCharge;
    }

    public PaymentCharge originalCharge(Long originalCharge) {
        this.originalCharge = originalCharge;
        return this;
    }

    public void setOriginalCharge(Long originalCharge) {
        this.originalCharge = originalCharge;
    }

    public String getAmmount() {
        return ammount;
    }

    public PaymentCharge ammount(String ammount) {
        this.ammount = ammount;
        return this;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getLeftToPay() {
        return leftToPay;
    }

    public PaymentCharge leftToPay(String leftToPay) {
        this.leftToPay = leftToPay;
        return this;
    }

    public void setLeftToPay(String leftToPay) {
        this.leftToPay = leftToPay;
    }

    public String getAbonado() {
        return abonado;
    }

    public PaymentCharge abonado(String abonado) {
        this.abonado = abonado;
        return this;
    }

    public void setAbonado(String abonado) {
        this.abonado = abonado;
    }

    public Integer getOldStyle() {
        return oldStyle;
    }

    public PaymentCharge oldStyle(Integer oldStyle) {
        this.oldStyle = oldStyle;
        return this;
    }

    public void setOldStyle(Integer oldStyle) {
        this.oldStyle = oldStyle;
    }

    public Payment getPayment() {
        return payment;
    }

    public PaymentCharge payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
        PaymentCharge paymentCharge = (PaymentCharge) o;
        if (paymentCharge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentCharge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentCharge{" +
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
