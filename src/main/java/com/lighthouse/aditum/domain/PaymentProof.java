package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A PaymentProof.
 */
@Entity
@Table(name = "payment_proof")
public class PaymentProof implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "status")
    private Integer status;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotNull
    @Column(name = "register_date", nullable = false)
    private ZonedDateTime registerDate;

    @Column(name = "bank")
    private String bank;

    @Column(name = "reference")
    private String reference;

    @ManyToOne
    private House house;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public PaymentProof imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public PaymentProof status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public PaymentProof description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public PaymentProof subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ZonedDateTime getRegisterDate() {
        return registerDate;
    }

    public PaymentProof registerDate(ZonedDateTime registerDate) {
        this.registerDate = registerDate;
        return this;
    }

    public void setRegisterDate(ZonedDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public String getBank() {
        return bank;
    }

    public PaymentProof bank(String bank) {
        this.bank = bank;
        return this;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getReference() {
        return reference;
    }

    public PaymentProof reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public House getHouse() {
        return house;
    }

    public PaymentProof house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Company getCompany() {
        return company;
    }

    public PaymentProof company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Payment getPayment() {
        return payment;
    }

    public PaymentProof payment(Payment payment) {
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
        PaymentProof paymentProof = (PaymentProof) o;
        if (paymentProof.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentProof.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentProof{" +
            "id=" + getId() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", subject='" + getSubject() + "'" +
            ", registerDate='" + getRegisterDate() + "'" +
            ", bank='" + getBank() + "'" +
            ", reference='" + getReference() + "'" +
            "}";
    }
}
