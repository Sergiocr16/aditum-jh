package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.util.RandomUtil;

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

    public void setDebt(String currency, double debt) {
        this.debtFormat = RandomUtil.formatMoney(currency,debt);
        this.debt = debt;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(String currency, double total) {
        this.totalFormated = RandomUtil.formatMoney(currency,total);
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
