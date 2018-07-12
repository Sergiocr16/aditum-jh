package com.lighthouse.aditum.service.dto;

public class MensualAndAnualAccountDTO {
    private String name;
    private String balance;
    private int inicialBalance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInicialBalance() {
        return inicialBalance;
    }

    public void setInicialBalance(int inicialBalance) {
        this.inicialBalance = inicialBalance;
    }
}
