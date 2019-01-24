package com.lighthouse.aditum.service.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DefaultersMonthDTO implements Serializable {


    private int totalHousesOnTime;

    private int totalHousesDefaulter;

    private double debt;

    private double total;

    private String debtFormat;

    private String totalFormated;

    private Locale locale = new Locale("es", "CR");

    private String formatMoney(double ammount) {
        DecimalFormat format = new DecimalFormat("₡#,##0;₡-#,##0");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        String total = format.format(ammount);
        return total;
    }

    public int getTotalHousesOnTime() {
        return totalHousesOnTime;
    }

    public void setTotalHousesOnTime(int totalHousesOnTime) {
        this.totalHousesOnTime = totalHousesOnTime;
    }

    public int getTotalHousesDefaulter() {
        return totalHousesDefaulter;
    }

    public void setTotalHousesDefaulter(int totalHousesDefaulter) {
        this.totalHousesDefaulter = totalHousesDefaulter;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debtFormat = formatMoney(debt);
        this.debt = debt;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.totalFormated = formatMoney(total);
        this.total = total;
    }

    public String getDebtFormat() {
        return debtFormat;
    }

    public void setDebtFormat(String debtFormat) {
        this.debtFormat = debtFormat;
    }

    public String getTotalFormated() {
        return totalFormated;
    }

    public void setTotalFormated(String totalFormated) {
        this.totalFormated = totalFormated;
    }
}
