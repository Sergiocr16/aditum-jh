package com.lighthouse.aditum.service.dto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardReportIncomeEgressBudgetDTO {

    private  String month;

    private int monthValue;

    private double incomeTotal;

    private double egressTotal;

    private double budgetTotal;

    private String incomeTotalformated;

    private String egressTotalformated;

    private String budgetTotalformated;


    private String formatMoney(double ammount){
        DecimalFormat format = new DecimalFormat("₡#,##0;₡-#,##0");
        String total = format.format(ammount);
        return total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(int monthValue) {
        this.monthValue = monthValue;
    }

    public double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(double incomeTotal) {
        this.incomeTotalformated = formatMoney(incomeTotal);
        this.incomeTotal = incomeTotal;
    }

    public double getEgressTotal() {
        return egressTotal;
    }

    public void setEgressTotal(double egressTotal) {
        this.egressTotalformated = formatMoney(egressTotal);
        this.egressTotal = egressTotal;
    }

    public double getBudgetTotal() {

        return budgetTotal;
    }

    public void setBudgetTotal(double budgetTotal) {
        this.budgetTotalformated = formatMoney(budgetTotal);

        this.budgetTotal = budgetTotal;
    }

    public String getIncomeTotalformated() {
        return incomeTotalformated;
    }

    public void setIncomeTotalformated(String incomeTotalformated) {
        this.incomeTotalformated = incomeTotalformated;
    }

    public String getEgressTotalformated() {
        return egressTotalformated;
    }

    public void setEgressTotalformated(String egressTotalformated) {
        this.egressTotalformated = egressTotalformated;
    }

    public String getBudgetTotalformated() {
        return budgetTotalformated;
    }

    public void setBudgetTotalformated(String budgetTotalformated) {
        this.budgetTotalformated = budgetTotalformated;
    }


}
