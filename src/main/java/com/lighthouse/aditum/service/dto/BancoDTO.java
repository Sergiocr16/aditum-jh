package com.lighthouse.aditum.service.dto;


import com.lighthouse.aditum.service.util.RandomUtil;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the Banco entity.
 */
public class BancoDTO implements Serializable {

    private Long id;

    private String beneficiario;

    private String temporalName;

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


    private double capitalInicialTemporal;

    private String capitalInicialFormatted;

    public double getCapitalInicialTemporal() {
        return capitalInicialTemporal;
    }

    public void setCapitalInicialTemporal(double capitalInicialTemporal) {
        this.capitalInicialTemporal = capitalInicialTemporal;
    }

    public String getCapitalInicialFormatted() {
        return capitalInicialFormatted;
    }

    public void setCapitalInicialFormatted(String capitalInicialFormatted) {
        this.capitalInicialFormatted = capitalInicialFormatted;
    }

    private String saldo;

    private Integer deleted;

    private String currency;

    private ZonedDateTime echangeRateDate;

    private String saleEchangeRate;

    private String purcharseEchangeRate;

    private ZonedDateTime exchangeRateDate;

    private String purcharseExchangeRate;

    private String saleExchangeRate;

    private Long companyId;

    private double totalBalance;

    private double totalIngress;

    private double totalEgress;

    private String totalBalanceFormatted;

    private String totalIngressFormatted;

    private String totalEgressFormatted;

    private String balanceColor;

    private List<BancoMovementDTO> movimientos = new ArrayList<>();

    public List<BancoMovementDTO> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<BancoMovementDTO> movimientos) {
        this.movimientos = movimientos;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String currency,double totalBalance) {
        this.totalBalance = totalBalance;
        this.setTotalBalanceFormatted(RandomUtil.formatMoney(currency,totalBalance));
    }

    public double getTotalIngress() {
        return totalIngress;
    }

    public void setTotalIngress(String currency,double totalIngress) {
        this.totalIngress = totalIngress;
    }

    public double getTotalEgress() {
        return totalEgress;
    }

    public void setTotalEgress(String currency,double totalEgress) {
        this.totalEgress = totalEgress;
    }

    public String getTotalBalanceFormatted() {
        return totalBalanceFormatted;
    }

    public void setTotalBalanceFormatted(String totalBalanceFormatted) {
        this.totalBalanceFormatted = totalBalanceFormatted;
    }

    public String getTotalIngressFormatted() {
        return totalIngressFormatted;
    }

    public void setTotalIngressFormatted(String totalIngressFormatted) {
        this.totalIngressFormatted = totalIngressFormatted;
    }

    public String getTotalEgressFormatted() {
        return totalEgressFormatted;
    }

    public void setTotalEgressFormatted(String totalEgressFormatted) {
        this.totalEgressFormatted = totalEgressFormatted;
    }

    public String getBalanceColor() {
        return balanceColor;
    }

    public void setBalanceColor(String balanceColor) {
        this.balanceColor = balanceColor;
    }

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ZonedDateTime getEchangeRateDate() {
        return echangeRateDate;
    }

    public void setEchangeRateDate(ZonedDateTime echangeRateDate) {
        this.echangeRateDate = echangeRateDate;
    }

    public String getSaleEchangeRate() {
        return saleEchangeRate;
    }

    public void setSaleEchangeRate(String saleEchangeRate) {
        this.saleEchangeRate = saleEchangeRate;
    }

    public String getPurcharseEchangeRate() {
        return purcharseEchangeRate;
    }

    public void setPurcharseEchangeRate(String purcharseEchangeRate) {
        this.purcharseEchangeRate = purcharseEchangeRate;
    }

    public ZonedDateTime getExchangeRateDate() {
        return exchangeRateDate;
    }

    public void setExchangeRateDate(ZonedDateTime exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    public String getPurcharseExchangeRate() {
        return purcharseExchangeRate;
    }

    public void setPurcharseExchangeRate(String purcharseExchangeRate) {
        this.purcharseExchangeRate = purcharseExchangeRate;
    }

    public String getSaleExchangeRate() {
        return saleExchangeRate;
    }

    public void setSaleExchangeRate(String saleExchangeRate) {
        this.saleExchangeRate = saleExchangeRate;
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
            ", mostrarFactura=" + getMostrarFactura() +
            ", fechaCapitalInicial='" + getFechaCapitalInicial() + "'" +
            ", saldo='" + getSaldo() + "'" +
            ", deleted=" + getDeleted() +
            ", currency='" + getCurrency() + "'" +
            ", echangeRateDate='" + getEchangeRateDate() + "'" +
            ", saleEchangeRate='" + getSaleEchangeRate() + "'" +
            ", purcharseEchangeRate='" + getPurcharseEchangeRate() + "'" +
            ", exchangeRateDate='" + getExchangeRateDate() + "'" +
            ", purcharseExchangeRate='" + getPurcharseExchangeRate() + "'" +
            ", saleExchangeRate='" + getSaleExchangeRate() + "'" +
            "}";
    }

    public String getTemporalName() {
        return temporalName;
    }

    public void setTemporalName(String temporalName) {
        this.temporalName = temporalName;
    }
}
