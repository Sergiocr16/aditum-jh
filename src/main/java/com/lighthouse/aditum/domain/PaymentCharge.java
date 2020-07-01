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

    @Column(name = "ammount")
    private String ammount;

    @Column(name = "original_charge")
    private Long originalCharge;

    @Column(name = "consecutive")
    private Integer consecutive;

    @Column(name = "abonado")
    private String abonado;

    @Column(name = "pendiente")
    private String pendiente;

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

    public Integer getConsecutive() {
        return consecutive;
    }

    public PaymentCharge consecutive(Integer consecutive) {
        this.consecutive = consecutive;
        return this;
    }

    public void setConsecutive(Integer consecutive) {
        this.consecutive = consecutive;
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

    public String getPendiente() {
        return pendiente;
    }

    public PaymentCharge pendiente(String pendiente) {
        this.pendiente = pendiente;
        return this;
    }

    public void setPendiente(String pendiente) {
        this.pendiente = pendiente;
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
            ", ammount='" + getAmmount() + "'" +
            ", originalCharge=" + getOriginalCharge() +
            ", consecutive=" + getConsecutive() +
            ", abonado='" + getAbonado() + "'" +
            ", pendiente='" + getPendiente() + "'" +
            "}";
    }
}
