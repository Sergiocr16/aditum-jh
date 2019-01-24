package com.lighthouse.aditum.service.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Sergio on 13/07/2018.
 */
public class MensualCollectionDTO implements Serializable {

    private Locale locale = new Locale("es", "CR");

    private double mensualBalance;

    private String mensualBalanceToShow;

    private String month;

    private String style;

    private String debtFormatted;

    public String getDebtFormatted() {
        return debtFormatted;
    }

    public void setDebtFormatted(String debtFormatted) {
        this.debtFormatted = debtFormatted;
    }

    private double debt;

    private double payedAmmount;

    private String payedAmmountFormatted;


    //  0 = libre 1 = morosa
    private int state;

    private String formatMoney(double ammount){
        DecimalFormat format = new DecimalFormat("₡#,##0;₡-#,##0");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        String total = format.format(ammount);
        return total;
    }
    public double getMensualBalance() {
        return mensualBalance;
    }

    public void setMensualBalance(double mensualBalance) {
        this.mensualBalance = mensualBalance;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getMensualBalanceToShow() {
        return mensualBalanceToShow;
    }

    public void setMensualBalanceToShow(String mensualBalanceToShow) {
        this.mensualBalanceToShow = mensualBalanceToShow;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
        this.debtFormatted = formatMoney(debt);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getPayedAmmount() {
        return payedAmmount;
    }

    public void setPayedAmmount(double payedAmmount) {
        this.payedAmmountFormatted = formatMoney(payedAmmount);
        this.payedAmmount = payedAmmount;
    }

    public String getPayedAmmountFormatted() {
        return payedAmmountFormatted;
    }

    public void setPayedAmmountFormatted(String payedAmmountFormatted) {
        this.payedAmmountFormatted = payedAmmountFormatted;
    }
}
