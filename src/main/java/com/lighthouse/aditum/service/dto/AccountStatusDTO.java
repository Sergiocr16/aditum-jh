package com.lighthouse.aditum.service.dto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AccountStatusDTO {
    private Locale locale = new Locale("es", "CR");
    private double saldoInicial;
    private double saldo;
    private double totalCharge;
    private double totalRecharge;
    private double totalAbono;
    private double totalTotal;
    private List<ResidentDTO> emailTo;
    private String saldoInicialFormatted;
    private String saldoFormatted;
    private String totalChargeFormatted;
    private String totalRechargeFormatted;
    private String totalAbonoFormatted;
    private String totalTotalFormatted;

    private List<AccountStatusItemDTO> listaAccountStatusItems;

    private String formatMoney(double ammount){
        DecimalFormat format = new DecimalFormat("₡#,##0.00;₡-#,##0.00");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        String total = format.format(ammount);
        return total;
    }


    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public List<AccountStatusItemDTO> getListaAccountStatusItems() {
        return listaAccountStatusItems;
    }

    public void setListaAccountStatusItems(List<AccountStatusItemDTO> listaAccountStatusItems) {
        this.listaAccountStatusItems = listaAccountStatusItems;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
        this.setSaldoFormatted(formatMoney(this.saldo));
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = this.totalCharge + totalCharge;
        this.setTotalChargeFormatted(formatMoney(this.totalCharge));
    }

    public double getTotalRecharge() {
        return totalRecharge;
    }

    public void setTotalRecharge(double totalRecharge) {
        this.totalRecharge = this.totalRecharge + totalRecharge;
        this.setTotalRechargeFormatted(formatMoney(this.totalRecharge));
    }

    public double getTotalTotal() {
        return totalTotal;
    }

    public void setTotalTotal(double totalTotal) {
        this.totalTotal =  this.totalTotal + totalTotal;
        this.setTotalTotalFormatted(formatMoney(this.totalTotal));
    }

    public double getTotalAbono() {
        return totalAbono;
    }

    public void setTotalAbono(double totalAbono) {
        this.totalAbono =  this.totalAbono + totalAbono;
        this.setTotalAbonoFormatted(formatMoney(this.totalAbono));
    }

    public String getSaldoInicialFormatted() {
        return saldoInicialFormatted;
    }

    public void setSaldoInicialFormatted(String saldoInicialFormatted) {
        this.saldoInicialFormatted = saldoInicialFormatted;
    }

    public String getSaldoFormatted() {
        return saldoFormatted;
    }

    public void setSaldoFormatted(String saldoFormatted) {
        this.saldoFormatted = saldoFormatted;
    }

    public String getTotalChargeFormatted() {
        return totalChargeFormatted;
    }

    public void setTotalChargeFormatted(String totalChargeFormatted) {
        this.totalChargeFormatted = totalChargeFormatted;
    }

    public String getTotalRechargeFormatted() {
        return totalRechargeFormatted;
    }

    public void setTotalRechargeFormatted(String totalRechargeFormatted) {
        this.totalRechargeFormatted = totalRechargeFormatted;
    }

    public String getTotalAbonoFormatted() {
        return totalAbonoFormatted;
    }

    public void setTotalAbonoFormatted(String totalAbonoFormatted) {
        this.totalAbonoFormatted = totalAbonoFormatted;
    }

    public String getTotalTotalFormatted() {
        return totalTotalFormatted;
    }

    public void setTotalTotalFormatted(String totalTotalFormatted) {
        this.totalTotalFormatted = totalTotalFormatted;
    }

    public List<ResidentDTO> getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(List<ResidentDTO> emailTo) {
        this.emailTo = emailTo;
    }
}
