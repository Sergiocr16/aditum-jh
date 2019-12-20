package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.util.RandomUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardReportIncomeEgressBudgetDTO {

    private String month;

    private int monthValue;

    private double incomeTotal;

    private double egressTotal;

    private double budgetIncomeTotal;

    private double budgetEgressTotal;

    private double initialBalance;

    private double realBalance;

    private String initialBalanceformated;

    private String realBalanceformated;

    private String incomeTotalformated;

    private String egressTotalformated;

    private String budgetIncomeTotalformated;

    private String budgetEgressTotalformated;


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

    public void setIncomeTotal(String currency, double incomeTotal) {
        this.incomeTotalformated = RandomUtil.formatMoney(currency, incomeTotal);
        this.incomeTotal = incomeTotal;
    }

    public double getEgressTotal() {
        return egressTotal;
    }

    public void setEgressTotal(String currency, double egressTotal) {
        this.egressTotalformated = RandomUtil.formatMoney(currency, egressTotal);
        this.egressTotal = egressTotal;
    }

    public double getbudgetIncomeTotal() {

        return budgetIncomeTotal;
    }

    public void setbudgetIncomeTotal(String currency, double budgetIncomeTotal) {
        this.budgetIncomeTotalformated = RandomUtil.formatMoney(currency, budgetIncomeTotal);
        this.budgetIncomeTotal = budgetIncomeTotal;
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

    public String getbudgetIncomeTotalformated() {
        return budgetIncomeTotalformated;
    }

    public void setbudgetIncomeTotalformated(String budgetIncomeTotalformated) {
        this.budgetIncomeTotalformated = budgetIncomeTotalformated;
    }


    public double getBudgetEgressTotal() {
        return budgetEgressTotal;
    }

    public void setBudgetEgressTotal(String currency, double budgetEgressTotal) {
        setBudgetEgressTotalformated(RandomUtil.formatMoney(currency, budgetEgressTotal));
        this.budgetEgressTotal = budgetEgressTotal;
    }

    public String getBudgetEgressTotalformated() {
        return budgetEgressTotalformated;
    }

    public void setBudgetEgressTotalformated(String budgetEgressTotalformated) {
        this.budgetEgressTotalformated = budgetEgressTotalformated;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(String currency, double initialBalance) {
        this.setInitialBalanceformated(RandomUtil.formatMoney(currency, initialBalance));
        this.initialBalance = initialBalance;
    }

    public double getRealBalance() {
        return realBalance;
    }

    public void setRealBalance(String currency, double realBalance) {
        this.setRealBalanceformated(RandomUtil.formatMoney(currency, realBalance));
        this.realBalance = realBalance;
    }

    public String getInitialBalanceformated() {
        return initialBalanceformated;
    }

    public void setInitialBalanceformated(String initialBalanceformated) {
        this.initialBalanceformated = initialBalanceformated;
    }

    public String getRealBalanceformated() {
        return realBalanceformated;
    }

    public void setRealBalanceformated(String realBalanceformated) {
        this.realBalanceformated = realBalanceformated;
    }
}
