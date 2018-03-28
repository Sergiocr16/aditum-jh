package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Egress.
 */
@Entity
@Table(name = "egress")
public class Egress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @Column(name = "folio")
    private String folio;

    @NotNull
    @Column(name = "jhi_account", nullable = false)
    private String account;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @NotNull
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @NotNull
    @Column(name = "concept", nullable = false)
    private String concept;

    @Column(name = "total")
    private String total;

    @Column(name = "reference")
    private String reference;

    @Column(name = "comments")
    private String comments;

    @Column(name = "proveedor")
    private String proveedor;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @Column(name = "expiration_date")
    private ZonedDateTime expirationDate;

    @ManyToOne(optional = false)
    @NotNull
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Egress date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getFolio() {
        return folio;
    }

    public Egress folio(String folio) {
        this.folio = folio;
        return this;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getAccount() {
        return account;
    }

    public Egress account(String account) {
        this.account = account;
        return this;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCategory() {
        return category;
    }

    public Egress category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Egress paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getConcept() {
        return concept;
    }

    public Egress concept(String concept) {
        this.concept = concept;
        return this;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getTotal() {
        return total;
    }

    public Egress total(String total) {
        this.total = total;
        return this;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReference() {
        return reference;
    }

    public Egress reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getComments() {
        return comments;
    }

    public Egress comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getProveedor() {
        return proveedor;
    }

    public Egress proveedor(String proveedor) {
        this.proveedor = proveedor;
        return this;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public Egress paymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public Egress expirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Company getCompany() {
        return company;
    }

    public Egress company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Egress egress = (Egress) o;
        if (egress.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), egress.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Egress{" +
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
