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

    @ManyToOne(optional = false)
    @NotNull
    private House house;

    @ManyToOne
    private Payment payment;

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
            ", type='" + getType() + "'" +
            ", date='" + getDate() + "'" +
            ", concept='" + getConcept() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", state='" + getState() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
