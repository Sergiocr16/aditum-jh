package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.util.RandomUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MensualAndAnualAccountDTO {
    private Locale locale = new Locale("es", "CR");
    private String name;
    private String balance;
    private double inicialBalance;
    private String inicialBalanceFormatted;

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

    public double getInicialBalance() {
        return inicialBalance;
    }

    public void setInicialBalance(String currency, double inicialBalance) {
        this.inicialBalance = inicialBalance;
        this.setInicialBalanceFormatted(RandomUtil.formatMoney(currency,Double.parseDouble(inicialBalance+"")));
    }

    public MensualAndAnualAccountDTO(){

    }


    public String getInicialBalanceFormatted() {
        return inicialBalanceFormatted;
    }

    public void setInicialBalanceFormatted(String inicialBalanceFormatted) {
        this.inicialBalanceFormatted = inicialBalanceFormatted;
    }
}
