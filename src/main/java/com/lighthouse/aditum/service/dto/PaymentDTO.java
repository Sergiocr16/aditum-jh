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
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;

    private String stringDate;

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    @NotNull
    private String receiptNumber;

    @NotNull
    private String transaction;

    @NotNull
    private String account;

    @NotNull
    private String paymentMethod;

    private String comments;

    private String ammount;

    private String concept;

    private Integer companyId;

    private String ammountLeft;

    private String documentReference;

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    private Long houseId;

    private String houseNumber;

    private String categories;

    private List<PaymentChargeDTO> charges;

    private List<ResidentDTO> emailTo;

    private List<PaymentProofDTO> paymentProofs;

    public List<PaymentProofDTO> getPaymentProofs() {
        return paymentProofs;
    }

    public void setPaymentProofs(List<PaymentProofDTO> paymentProofs) {
        this.paymentProofs = paymentProofs;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public List<PaymentChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<PaymentChargeDTO> charges) {
        this.charges = charges;
    }

    public List<ResidentDTO> getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(List<ResidentDTO> emailTo) {
        this.emailTo = emailTo;
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

    public String getAmmountLeft() {
        return ammountLeft;
    }

    public void setAmmountLeft(String ammountLeft) {
        this.ammountLeft = ammountLeft;
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
            ", companyId=" + getCompanyId() +
            ", ammountLeft='" + getAmmountLeft() + "'" +
            "}";
    }
}
