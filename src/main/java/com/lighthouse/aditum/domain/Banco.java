package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A Banco.
 */
@Entity
@Table(name = "banco")
public class Banco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "beneficiario")
    private String beneficiario;

    @Column(name = "cedula")
    private String cedula;

    @NotNull
    @Column(name = "cuenta_corriente", nullable = false)
    private String cuentaCorriente;

    @NotNull
    @Column(name = "cuenta_cliente", nullable = false)
    private String cuentaCliente;

    @Column(name = "moneda")
    private String moneda;

    @Column(name = "cuenta_contable")
    private String cuentaContable;

    @Column(name = "capital_inicial")
    private String capitalInicial;

    @Column(name = "mostrar_factura")
    private Integer mostrarFactura;

    @Column(name = "fecha_capital_inicial")
    private ZonedDateTime fechaCapitalInicial;

    @Column(name = "saldo")
    private String saldo;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "currency")
    private String currency;

    @ManyToOne(optional = false)
    @NotNull
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public Banco beneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
        return this;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getCedula() {
        return cedula;
    }

    public Banco cedula(String cedula) {
        this.cedula = cedula;
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCuentaCorriente() {
        return cuentaCorriente;
    }

    public Banco cuentaCorriente(String cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
        return this;
    }

    public void setCuentaCorriente(String cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    public String getCuentaCliente() {
        return cuentaCliente;
    }

    public Banco cuentaCliente(String cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
        return this;
    }

    public void setCuentaCliente(String cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
    }

    public String getMoneda() {
        return moneda;
    }

    public Banco moneda(String moneda) {
        this.moneda = moneda;
        return this;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public Banco cuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
        return this;
    }

    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public String getCapitalInicial() {
        return capitalInicial;
    }

    public Banco capitalInicial(String capitalInicial) {
        this.capitalInicial = capitalInicial;
        return this;
    }

    public void setCapitalInicial(String capitalInicial) {
        this.capitalInicial = capitalInicial;
    }

    public Integer getMostrarFactura() {
        return mostrarFactura;
    }

    public Banco mostrarFactura(Integer mostrarFactura) {
        this.mostrarFactura = mostrarFactura;
        return this;
    }

    public void setMostrarFactura(Integer mostrarFactura) {
        this.mostrarFactura = mostrarFactura;
    }

    public ZonedDateTime getFechaCapitalInicial() {
        return fechaCapitalInicial;
    }

    public Banco fechaCapitalInicial(ZonedDateTime fechaCapitalInicial) {
        this.fechaCapitalInicial = fechaCapitalInicial;
        return this;
    }

    public void setFechaCapitalInicial(ZonedDateTime fechaCapitalInicial) {
        this.fechaCapitalInicial = fechaCapitalInicial;
    }

    public String getSaldo() {
        return saldo;
    }

    public Banco saldo(String saldo) {
        this.saldo = saldo;
        return this;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Banco deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCurrency() {
        return currency;
    }

    public Banco currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Company getCompany() {
        return company;
    }

    public Banco company(Company company) {
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
        Banco banco = (Banco) o;
        if (banco.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), banco.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Banco{" +
            "id=" + getId() +
            ", beneficiario='" + getBeneficiario() + "'" +
            ", cedula='" + getCedula() + "'" +
            ", cuentaCorriente='" + getCuentaCorriente() + "'" +
            ", cuentaCliente='" + getCuentaCliente() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", cuentaContable='" + getCuentaContable() + "'" +
            ", capitalInicial='" + getCapitalInicial() + "'" +
            ", mostrarFactura=" + getMostrarFactura() +
            ", fechaCapitalInicial='" + getFechaCapitalInicial() + "'" +
            ", saldo='" + getSaldo() + "'" +
            ", deleted=" + getDeleted() +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
