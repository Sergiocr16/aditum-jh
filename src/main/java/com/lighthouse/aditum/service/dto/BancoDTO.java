package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Banco entity.
 */
public class BancoDTO implements Serializable {

    private Long id;

    private String beneficiario;

    private String cedula;

    @NotNull
    private String cuentaCorriente;

    @NotNull
    private String cuentaCliente;

    private String moneda;

    private String cuentaContable;

    private String capitalInicial;

    private Integer mostrarFactura;

    private ZonedDateTime fechaCapitalInicial;

    private String saldo;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCuentaCorriente() {
        return cuentaCorriente;
    }

    public void setCuentaCorriente(String cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    public String getCuentaCliente() {
        return cuentaCliente;
    }

    public void setCuentaCliente(String cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public String getCapitalInicial() {
        return capitalInicial;
    }

    public void setCapitalInicial(String capitalInicial) {
        this.capitalInicial = capitalInicial;
    }

    public Integer getMostrarFactura() {
        return mostrarFactura;
    }

    public void setMostrarFactura(Integer mostrarFactura) {
        this.mostrarFactura = mostrarFactura;
    }

    public ZonedDateTime getFechaCapitalInicial() {
        return fechaCapitalInicial;
    }

    public void setFechaCapitalInicial(ZonedDateTime fechaCapitalInicial) {
        this.fechaCapitalInicial = fechaCapitalInicial;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
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

        BancoDTO bancoDTO = (BancoDTO) o;
        if(bancoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bancoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BancoDTO{" +
            "id=" + getId() +
            ", beneficiario='" + getBeneficiario() + "'" +
            ", cedula='" + getCedula() + "'" +
            ", cuentaCorriente='" + getCuentaCorriente() + "'" +
            ", cuentaCliente='" + getCuentaCliente() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", cuentaContable='" + getCuentaContable() + "'" +
            ", capitalInicial='" + getCapitalInicial() + "'" +
            ", mostrarFactura='" + getMostrarFactura() + "'" +
            ", fechaCapitalInicial='" + getFechaCapitalInicial() + "'" +
            ", saldo='" + getSaldo() + "'" +
            "}";
    }
}
