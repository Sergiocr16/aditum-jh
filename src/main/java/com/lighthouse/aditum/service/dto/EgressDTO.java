package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Egress entity.
 */
public class EgressDTO implements Serializable {

    private Long id;

    private ZonedDateTime date;

    private String folio;

    private String account;

    private String accountName;

    private String category;

    private String paymentMethod;

    private String concept;

    private String total;

    private String reference;

    private String comments;

    private String proveedor;

    private ZonedDateTime paymentDate;

    private boolean paymentDateSelected = false;

    private ZonedDateTime expirationDate;

    private Integer state;

    private String billNumber;

    private Long companyId;

    private String categoryName;

    private String groupName;

    private Long categoryId;

    private String stateFormatted;

    private String totalFormatted;

    private String dateFormatted;

    private String paymentDateFormatted;

    private String expirationDateFormatted;

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

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EgressDTO egressDTO = (EgressDTO) o;
        if (egressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), egressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EgressDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", folio='" + getFolio() + "'" +
            ", account='" + getAccount() + "'" +
            ", category='" + getCategory() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", concept='" + getConcept() + "'" +
            ", total='" + getTotal() + "'" +
            ", reference='" + getReference() + "'" +
            ", comments='" + getComments() + "'" +
            ", proveedor='" + getProveedor() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", state='" + getState() + "'" +
            ", billNumber='" + getBillNumber() + "'" +
            "}";
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isPaymentDateSelected() {
        return paymentDateSelected;
    }

    public void setPaymentDateSelected(boolean paymentDateSelected) {
        this.paymentDateSelected = paymentDateSelected;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }

    public String getPaymentDateFormatted() {
        return paymentDateFormatted;
    }

    public void setPaymentDateFormatted(String paymentDateFormatted) {
        this.paymentDateFormatted = paymentDateFormatted;
    }

    public String getExpirationDateFormatted() {
        return expirationDateFormatted;
    }

    public void setExpirationDateFormatted(String expirationDateFormatted) {
        this.expirationDateFormatted = expirationDateFormatted;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public String getStateFormatted() {
        return stateFormatted;
    }

    public void setStateFormatted(String stateFormatted) {
        this.stateFormatted = stateFormatted;
    }
}
