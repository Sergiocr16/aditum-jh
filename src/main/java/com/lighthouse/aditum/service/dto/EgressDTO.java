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

    @NotNull
    private ZonedDateTime date;

    private String folio;

    @NotNull
    private String account;

    @NotNull
    private String category;

    @NotNull
    private String paymentMethod;

    @NotNull
    private String concept;

    private String total;

    private String reference;

    private String comments;

    private String proveedor;

    private ZonedDateTime paymentDate;

    private ZonedDateTime expirationDate;

    private Long companyId;

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
            "}";
    }
}
