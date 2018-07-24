package com.lighthouse.aditum.service.dto;

import java.util.List;

public class AccountStatusDTO {

    private int saldoInicial;
    private int saldo;
    private int totalCharge;
    private int totalRecharge;
    private int totalAbono;
    private int totalTotal;
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

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(int totalCharge) {
        this.totalCharge = this.totalCharge + totalCharge;
    }

    public int getTotalRecharge() {
        return totalRecharge;
    }

    public void setTotalRecharge(int totalRecharge) {
        this.totalRecharge = this.totalRecharge + totalRecharge;
    }

    public int getTotalTotal() {
        return totalTotal;
    }

    public void setTotalTotal(int totalTotal) {
        this.totalTotal =  this.totalTotal + totalTotal;
    }

    public int getTotalAbono() {
        return totalAbono;
    }

    public void setTotalAbono(int totalAbono) {
        this.totalAbono =  this.totalAbono + totalAbono;
    }
}
