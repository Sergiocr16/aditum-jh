package com.lighthouse.aditum.service.dto;

import java.util.List;

public class AccountStatusDTO {

    private int saldoInicial;
    private double saldo;
    private double totalCharge;
    private double totalRecharge;
    private int totalAbono;
    private double totalTotal;
    private List<AccountStatusItemDTO> listaAccountStatusItems;

    public int getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(int saldoInicial) {
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
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = this.totalCharge + totalCharge;
    }

    public double getTotalRecharge() {
        return totalRecharge;
    }

    public void setTotalRecharge(int totalRecharge) {
        this.totalRecharge = this.totalRecharge + totalRecharge;
    }

    public double getTotalTotal() {
        return totalTotal;
    }

    public void setTotalTotal(double totalTotal) {
        this.totalTotal =  this.totalTotal + totalTotal;
    }

    public int getTotalAbono() {
        return totalAbono;
    }

    public void setTotalAbono(int totalAbono) {
        this.totalAbono =  this.totalAbono + totalAbono;
    }
}
