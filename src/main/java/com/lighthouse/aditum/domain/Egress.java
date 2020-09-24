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

    @Column(name = "exchange_rate")
    private String exchangeRate;

    @Column(name = "iva_double_money")
    private String ivaDoubleMoney;

    @Column(name = "ammount_double_money")
    private String ammountDoubleMoney;

    @Column(name = "double_money")
    private int doubleMoney;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @Column(name = "expiration_date")
    private ZonedDateTime expirationDate;

    @Column(name = "state")
    private Integer state;

    @Column(name = "bill_number")
    private String billNumber;

    @Column(name = "deleted")
    private Integer deleted;

    @ManyToOne(optional = false)
    @NotNull
    private Company company;


    @Column(name = "subtotal")
    private String subtotal;

    @Column(name = "iva")
    private String iva;

    @Column(name = "has_comission")
    private Integer hasComission;

    @Column(name = "comission")
    private String comission;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "url_file")
    private String urlFile;

    @Column(name = "jhi_type")
    private Integer type;

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

    public Egress date(ZonedDateTime date) {
        this.date = date;
        return this;
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

    public Integer getState() {
        return state;
    }

    public Egress state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public Egress billNumber(String billNumber) {
        this.billNumber = billNumber;
        return this;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Egress deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public Egress subtotal(String subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIva() {
        return iva;
    }

    public Egress iva(String iva) {
        this.iva = iva;
        return this;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public Integer getHasComission() {
        return hasComission;
    }

    public Egress hasComission(Integer hasComission) {
        this.hasComission = hasComission;
        return this;
    }

    public void setHasComission(Integer hasComission) {
        this.hasComission = hasComission;
    }

    public String getComission() {
        return comission;
    }

    public Egress comission(String comission) {
        this.comission = comission;
        return this;
    }

    public void setComission(String comission) {
        this.comission = comission;
    }

    public String getFileName() {
        return fileName;
    }

    public Egress fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public Egress urlFile(String urlFile) {
        this.urlFile = urlFile;
        return this;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public Integer getType() {
        return type;
    }

    public Egress type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
}
