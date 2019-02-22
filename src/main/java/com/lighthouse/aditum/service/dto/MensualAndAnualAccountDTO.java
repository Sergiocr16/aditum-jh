package com.lighthouse.aditum.service.dto;

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

    public void setInicialBalance(double inicialBalance) {
        this.inicialBalance = inicialBalance;
        this.setInicialBalanceFormatted(formatMoney(Double.parseDouble(inicialBalance+"")));
    }

    public MensualAndAnualAccountDTO(){

    }
    private String formatMoney(double ammount){
        DecimalFormat format = new DecimalFormat("₡#,##0.00;₡-#,##0.00");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        String total = format.format(ammount);
        return total;
    }

    public String getInicialBalanceFormatted() {
        return inicialBalanceFormatted;
    }

    public void setInicialBalanceFormatted(String inicialBalanceFormatted) {
        this.inicialBalanceFormatted = inicialBalanceFormatted;
    }
}
