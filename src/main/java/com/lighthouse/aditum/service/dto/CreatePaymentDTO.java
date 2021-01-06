package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Payment entity.
 */
public class CreatePaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private String receiptNumber;

    private String transaction;

    private String account;

    private String paymentMethod;

    private String documentReference;

    private boolean cancellingFavorBalance;

    private String favorUsed;

    private String favorCategory;

    public String getFavorUsed() {
        return favorUsed;
    }

    public void setFavorUsed(String favorUsed) {
        this.favorUsed = favorUsed;
    }

    public String getFavorCategory() {
        return favorCategory;
    }

    public void setFavorCategory(String favorCategory) {
        this.favorCategory = favorCategory;
    }

    public boolean getCancellingFavorBalance() {
        return cancellingFavorBalance;
    }

    public void setCancellingFavorBalance(boolean cancellingFavorBalance) {
        this.cancellingFavorBalance = cancellingFavorBalance;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    private String comments;

    private String ammount;

    private String concept;

    private Integer companyId;

    private Long houseId;

    private String exchangeRate;

    private String ammountDollar;

    private String ammountLeftDollar;

    private String ammountLeft;

    private int doubleMoney;

    public String getAmmountLeftDollar() {
        return ammountLeftDollar;
    }

    public void setAmmountLeftDollar(String ammountLeftDollar) {
        this.ammountLeftDollar = ammountLeftDollar;
    }

    public String getAmmountLeft() {
        return ammountLeft;
    }

    public void setAmmountLeft(String ammountLeft) {
        this.ammountLeft = ammountLeft;
    }

    private List<ChargeDTO> charges;

    private List<ResidentDTO> emailTo;

    private List<PaymentProofDTO> paymentProofs;

    public List<PaymentProofDTO> getPaymentProofs() {
        return paymentProofs;
    }

    public void setPaymentProofs(List<PaymentProofDTO> paymentProofs) {
        this.paymentProofs = paymentProofs;
    }

    public int getDoubleMoney() {
        return doubleMoney;
    }

    public void setDoubleMoney(int doubleMoney) {
        this.doubleMoney = doubleMoney;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if(paymentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", receiptNumber='" + getReceiptNumber() + "'" +
            ", transaction='" + getTransaction() + "'" +
            ", account='" + getAccount() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", comments='" + getComments() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", concept='" + getConcept() + "'" +
            ", companyId='" + getCompanyId() + "'" +
            "}";
    }

    public List<ChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<ChargeDTO> charges) {
        this.charges = charges;
    }

    public List<ResidentDTO> getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(List<ResidentDTO> emailTo) {
        this.emailTo = emailTo;
    }
}
