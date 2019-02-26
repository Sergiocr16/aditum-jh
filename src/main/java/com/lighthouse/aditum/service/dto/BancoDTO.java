package com.lighthouse.aditum.service.dto;


import java.text.NumberFormat;
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

    private String cedula;

    @NotNull
    private String cuentaCorriente;

    @NotNull
    private String cuentaCliente;

    private String moneda;

    private String cuentaContable;

    private String capitalInicial;

    private String capitalInicialFormatted;

    private Integer mostrarFactura;

    private ZonedDateTime fechaCapitalInicial;

    private String saldo;

    private Integer deleted;

    private List<BancoMovementDTO> movimientos = new ArrayList<>();

    private Long companyId;

    private Double totalBalance;

    private Double totalIngress;

    private Double totalEgress;

    private String totalBalanceFormatted;

    private String totalIngressFormatted;

    private String totalEgressFormatted;

    private String balanceColor;

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
            ", deleted='" + getDeleted() + "'" +
            "}";
    }

    public List<BancoMovementDTO> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<BancoMovementDTO> movimientos) {
        this.movimientos = movimientos;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
        this.setTotalBalanceFormatted(formatMoney(totalBalance));
    }

    public String getBalanceColor() {
        return balanceColor;
    }

    public void setBalanceColor(String balanceColor) {
        this.balanceColor = balanceColor;
    }

    public Double getTotalIngress() {
        return totalIngress;
    }

    public void setTotalIngress(Double totalIngress) {
        this.totalIngress = totalIngress;
    }

    public Double getTotalEgress() {
        return totalEgress;
    }

    public void setTotalEgress(Double totalEgress) {
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

    public String getCapitalInicialFormatted() {
        return capitalInicialFormatted;
    }

    public void setCapitalInicialFormatted(String capitalInicialFormatted) {
        this.capitalInicialFormatted = capitalInicialFormatted;
    }

    private String formatMoney(double ammount) {
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        if(ammount==0){
            return currencyFormatter.format(ammount).substring(1);
        }else {
            String t = "";
            if(ammount<0){
                t = currencyFormatter.format(ammount).substring(0,2);
            }else{
                t = currencyFormatter.format(ammount).substring(1);
            }
            return t.substring(0, t.length() - 3);
        }
    }
}
