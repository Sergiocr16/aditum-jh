package com.lighthouse.aditum.service.dto;

import java.util.List;

public class AccountStatusDTO {

    private int saldoInicial;
    private int saldo;
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
}
