package com.lighthouse.aditum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;

    @NotNull
    @Column(name = "jhi_transaction", nullable = false)
    private String transaction;

    @NotNull
    @Column(name = "jhi_account", nullable = false)
    private String account;

    @Column(name = "document_reference", nullable = false)
    private String documentReference;

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    @NotNull
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "comments")
    private String comments;

    @Column(name = "ammount")
    private String ammount;

    @Column(name = "ammount_left_dollar")
    private String ammountLeftDollar;

    @Column(name = "exchange_rate")
    private String exchangeRate;

    @Column(name = "ammount_dollar")
    private String ammountDollar;

    @Column(name = "double_money")
    private int doubleMoney;

    @Column(name = "concept")
    private String concept;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "ammount_left")
    private String ammountLeft;

    @ManyToOne
    private House house;

    @OneToMany(mappedBy = "payment")
    @JsonIgnore
    private Set<PaymentProof> paymentProofs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Payment date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public String getAmmountLeftDollar() {
        return ammountLeftDollar;
    }

    public void setAmmountLeftDollar(String ammountLeftDollar) {
        this.ammountLeftDollar = ammountLeftDollar;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getAmmountDollar() {
        return ammountDollar;
    }

    public void setAmmountDollar(String ammountDollar) {
        this.ammountDollar = ammountDollar;
    }

    public int getDoubleMoney() {
        return doubleMoney;
    }

    public void setDoubleMoney(int doubleMoney) {
        this.doubleMoney = doubleMoney;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public Payment receiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
        return this;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getTransaction() {
        return transaction;
    }

    public Payment transaction(String transaction) {
        this.transaction = transaction;
        return this;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getAccount() {
        return account;
    }

    public Payment account(String account) {
        this.account = account;
        return this;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Payment paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getComments() {
        return comments;
    }

    public Payment comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAmmount() {
        return ammount;
    }

    public Payment ammount(String ammount) {
        this.ammount = ammount;
        return this;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getConcept() {
        return concept;
    }

    public Payment concept(String concept) {
        this.concept = concept;
        return this;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Payment companyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getAmmountLeft() {
        return ammountLeft;
    }

    public Payment ammountLeft(String ammountLeft) {
        this.ammountLeft = ammountLeft;
        return this;
    }

    public void setAmmountLeft(String ammountLeft) {
        this.ammountLeft = ammountLeft;
    }

    public House getHouse() {
        return house;
    }

    public Payment house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Set<PaymentProof> getPaymentProofs() {
        return paymentProofs;
    }

    public Payment paymentProofs(Set<PaymentProof> paymentProofs) {
        this.paymentProofs = paymentProofs;
        return this;
    }

    public Payment addPaymentProof(PaymentProof paymentProof) {
        this.paymentProofs.add(paymentProof);
        paymentProof.setPayment(this);
        return this;
    }

    public Payment removePaymentProof(PaymentProof paymentProof) {
        this.paymentProofs.remove(paymentProof);
        paymentProof.setPayment(null);
        return this;
    }

    public void setPaymentProofs(Set<PaymentProof> paymentProofs) {
        this.paymentProofs = paymentProofs;
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
        Payment payment = (Payment) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", receiptNumber='" + getReceiptNumber() + "'" +
            ", transaction='" + getTransaction() + "'" +
            ", account='" + getAccount() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", comments='" + getComments() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", concept='" + getConcept() + "'" +
            ", companyId=" + getCompanyId() +
            ", ammountLeft='" + getAmmountLeft() + "'" +
            "}";
    }


}
