package com.lighthouse.aditum.service.dto;

import java.util.List;

public class EgressCategoryByMonthDTO {
    private String name;
    private List<String> totalByMonth;
    private List<String> budgetByMonth;
    private List<String> diferenceByMonth;
    private double acumulado = 0;
    private double budgetAcumulado = 0;
    private double diferenceAcumulado = 0;
    private boolean showDetail = false;

    public EgressCategoryByMonthDTO(String name){

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTotalByMonth() {
        return totalByMonth;
    }

    public void setTotalByMonth(List<String> totalByMonth) {
        this.totalByMonth = totalByMonth;
    }

    public double getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(double acumulado) {
        this.acumulado = this.acumulado + acumulado;
    }

    public List<String> getBudgetByMonth() {
        return budgetByMonth;
    }

    public void setBudgetByMonth(List<String> budgetByMonth) {
        this.budgetByMonth = budgetByMonth;
    }

    public List<String> getDiferenceByMonth() {
        return diferenceByMonth;
    }

    public void setDiferenceByMonth(List<String> diferenceByMonth) {
        this.diferenceByMonth = diferenceByMonth;
    }

    public double getBudgetAcumulado() {
        return budgetAcumulado;
    }

    public void setBudgetAcumulado(double budgetAcumulado) {
        this.budgetAcumulado =   this.budgetAcumulado + budgetAcumulado;
    }

    public double getDiferenceAcumulado() {
        return diferenceAcumulado;
    }

    public void setDiferenceAcumulado(double diferenceAcumulado) {
        this.diferenceAcumulado =   this.diferenceAcumulado + diferenceAcumulado;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }
}
