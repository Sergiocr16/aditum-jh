package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A Charge.
 */
@Entity
@Table(name = "charge")
public class Charge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private Integer type;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "concept", nullable = false)
    private String concept;

    @NotNull
    @Column(name = "ammount", nullable = false)
    private String ammount;

    @NotNull
    @Column(name = "state", nullable = false)
    private Integer state;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @Column(name = "subcharge")
    private String subcharge;

    @Column(name = "payed_subcharge")
    private Boolean payedSubcharge;

    @ManyToOne(optional = false)
    @NotNull
    private House house;

    @ManyToOne
    private Payment payment;

    @ManyToOne
    private Company company;

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

    public Charge type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Charge date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getConcept() {
        return concept;
    }

    public Charge concept(String concept) {
        this.concept = concept;
        return this;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getAmmount() {
        return ammount;
    }

    public Charge ammount(String ammount) {
        this.ammount = ammount;
        return this;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public Integer getState() {
        return state;
    }

    public Charge state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Charge deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public Charge paymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getSubcharge() {
        return subcharge;
    }

    public Charge subcharge(String subcharge) {
        this.subcharge = subcharge;
        return this;
    }

    public void setSubcharge(String subcharge) {
        this.subcharge = subcharge;
    }

    public Boolean isPayedSubcharge() {
        return payedSubcharge;
    }

    public Charge payedSubcharge(Boolean payedSubcharge) {
        this.payedSubcharge = payedSubcharge;
        return this;
    }

    public void setPayedSubcharge(Boolean payedSubcharge) {
        this.payedSubcharge = payedSubcharge;
    }

    public House getHouse() {
        return house;
    }

    public Charge house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Payment getPayment() {
        return payment;
    }

    public Charge payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Company getCompany() {
        return company;
    }

    public Charge company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        Charge charge = (Charge) o;
        if (charge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), charge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Charge{" +
            "id=" + getId() +
            ", type=" + getType() +
            ", date='" + getDate() + "'" +
            ", concept='" + getConcept() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", state=" + getState() +
            ", deleted=" + getDeleted() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", subcharge='" + getSubcharge() + "'" +
            ", payedSubcharge='" + isPayedSubcharge() + "'" +
            "}";
    }
}
