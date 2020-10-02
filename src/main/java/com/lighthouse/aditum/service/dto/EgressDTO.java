package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.persistence.Column;
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

    private String category;

    private ZonedDateTime expirationDate;

    private Integer state;

    private String billNumber;

    private Long companyId;

    private String paymentMethod;

    private String concept;

    private String total;

    private String reference;

    private String comments;

    private String proveedor;

    private ZonedDateTime paymentDate;

    private int deleted;

    private String subtotal;

    private String iva;

    private Integer hasComission;

    private String comission;

    private String fileName;

    private String urlFile;

    private Integer type;

    private boolean paymentDateSelected = false;

    private String accountName;

    private String categoryName;

    private String groupName;

    private Long categoryId;

    private String stateFormatted;

    private String totalFormatted;

    private String dateFormatted;

    private String paymentDateFormatted;

    private String expirationDateFormatted;

    private String exchangeRate;

    private String ivaDoubleMoney;

    private String ammountDoubleMoney;

    private String subtotalDoubleMoney;

    private int doubleMoney;

    private String currency;

    public String getSubtotalDoubleMoney() {
        return subtotalDoubleMoney;
    }

    public void setSubtotalDoubleMoney(String subtotalDoubleMoney) {
        this.subtotalDoubleMoney = subtotalDoubleMoney;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getIvaDoubleMoney() {
        return ivaDoubleMoney;
    }

    public void setIvaDoubleMoney(String ivaDoubleMoney) {
        this.ivaDoubleMoney = ivaDoubleMoney;
    }

    public String getAmmountDoubleMoney() {
        return ammountDoubleMoney;
    }

    public void setAmmountDoubleMoney(String ammountDoubleMoney) {
        this.ammountDoubleMoney = ammountDoubleMoney;
    }

    public int getDoubleMoney() {
        return doubleMoney;
    }

    public void setDoubleMoney(int doubleMoney) {
        this.doubleMoney = doubleMoney;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public Integer getHasComission() {
        return hasComission;
    }

    public void setHasComission(Integer hasComission) {
        this.hasComission = hasComission;
    }

    public String getComission() {
        return comission;
    }

    public void setComission(String comission) {
        this.comission = comission;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        if(egressDTO.getId() == null || getId() == null) {
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
            ", state=" + getState() +
            ", billNumber='" + getBillNumber() + "'" +
            ", deleted=" + getDeleted() +
            ", subtotal='" + getSubtotal() + "'" +
            ", iva='" + getIva() + "'" +
            ", hasComission=" + getHasComission() +
            ", comission='" + getComission() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", urlFile='" + getUrlFile() + "'" +
            ", type=" + getType() +
            "}";
    }

    public boolean isPaymentDateSelected() {
        return paymentDateSelected;
    }

    public void setPaymentDateSelected(boolean paymentDateSelected) {
        this.paymentDateSelected = paymentDateSelected;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public String getStateFormatted() {
        return stateFormatted;
    }

    public void setStateFormatted(String stateFormatted) {
        this.stateFormatted = stateFormatted;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
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
}
