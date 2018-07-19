package com.lighthouse.aditum.service.dto;

import java.util.List;

public class AnualReportDTO {
    private List<MensualIngressReportDTO> anualIngressByMonth;
    private List<MensualEgressReportDTO> anualEgressByMonth;
    private List<EgressCategoryByMonthDTO> fixedCostEgress;
    private List<EgressCategoryByMonthDTO> variableCostEgress;
    private List<EgressCategoryByMonthDTO> otherCostEgress;
    private List<String> flujoByMonth;
    private List<String> initialBalanceByMonth;
    private List<String> realBalanceByMonth;

    private int fixedCostsAcumulado = 0;
    private List<Double> fixedCostsBudgetTotal;
    private List<Double> fixedCostsBudgetDiference;
    private double fixedCostsBudgetAcumulado = 0;
    private double fixedCostsBudgetDiferenceAcumulado = 0;

    private int variableCostsAcumulado = 0;
    private List<Double> variableCostsBudgetTotal;
    private List<Double> variableCostsBudgetDiference;
    private double variableCostsBudgetAcumulado = 0;
    private double variableCostsBudgetDiferenceAcumulado = 0;

    private int otherCostsAcumulado = 0;
    private List<Double> otherCostsBudgetTotal;
    private List<Double> otherCostsBudgetDiference;
    private double otherCostsBudgetAcumulado = 0;
    private double otherCostsBudgetDiferenceAcumulado = 0;

    private int maintenanceIngressAcumulado = 0;
    private int maintenanceBudgetAcumulado = 0;
    private int maintenanceBudgetDiferenceAcumulado = 0;

    private int extraordinaryIngressAcumulado = 0;
    private int extraordinaryIngressBudgetAcumulado = 0;
    private int extraordinaryIngressBudgetDiferenceAcumulado = 0;

    private int commonAreasAcumulado = 0;
    private int commonAreasBudgetAcumulado = 0;
    private int commonAreasBudgetDiferenceAcumulado = 0;

    private int otherIngressAcumulado = 0;
    private int otherIngressBudgetAcumulado = 0;
    private int otherIngressBudgetDiferenceAcumulado = 0;

    private int totalIngressBudget = 0;
    private int totalIngressBudgetDiference = 0;

    private int allEgressAcumulado = 0;
    private int allIngressAcumulado = 0;

    private List<Double> allEgressBudgetByMonth;
    private List<Double> allEgressDiferenceByMonth;
    private double allEgressBudgetAcumulado = 0;
    private double allEgressDiferenceAcumulado = 0;

    public List<MensualIngressReportDTO> getAnualIngressByMonth() {
        return anualIngressByMonth;
    }

    public void setAnualIngressByMonth(List<MensualIngressReportDTO> anualIngressByMonth) {
        this.anualIngressByMonth = anualIngressByMonth;
    }

    public List<MensualEgressReportDTO> getAnualEgressByMonth() {
        return anualEgressByMonth;
    }

    public void setAnualEgressByMonth(List<MensualEgressReportDTO> anualEgressByMonth) {
        this.anualEgressByMonth = anualEgressByMonth;
    }

    public List<EgressCategoryByMonthDTO> getFixedCostEgress() {
        return fixedCostEgress;
    }

    public void setFixedCostEgress(List<EgressCategoryByMonthDTO> fixedCostEgress) {
        this.fixedCostEgress = fixedCostEgress;
    }

    public List<EgressCategoryByMonthDTO> getVariableCostEgress() {
        return variableCostEgress;
    }

    public void setVariableCostEgress(List<EgressCategoryByMonthDTO> variableCostEgress) {
        this.variableCostEgress = variableCostEgress;
    }

    public List<EgressCategoryByMonthDTO> getOtherCostEgress() {
        return otherCostEgress;
    }

    public void setOtherCostEgress(List<EgressCategoryByMonthDTO> otherCostEgress) {
        this.otherCostEgress = otherCostEgress;
    }

    public int getFixedCostsAcumulado() {
        return fixedCostsAcumulado;
    }

    public void setFixedCostsAcumulado(int fixedCostsAcumulado) {
        this.fixedCostsAcumulado =  this.fixedCostsAcumulado + fixedCostsAcumulado;
    }

    public int getVariableCostsAcumulado() {
        return variableCostsAcumulado;
    }

    public void setVariableCostsAcumulado(int variableCostsAcumulado) {
        this.variableCostsAcumulado = this.variableCostsAcumulado + variableCostsAcumulado;
    }

    public int getOtherCostsAcumulado() {
        return otherCostsAcumulado;
    }

    public void setOtherCostsAcumulado(int otherCostsAcumulado) {
        this.otherCostsAcumulado = this.otherCostsAcumulado + otherCostsAcumulado;
    }

    public int getAllEgressAcumulado() {
        return allEgressAcumulado;
    }

    public void setAllEgressAcumulado() {
        this.allEgressAcumulado = this.getFixedCostsAcumulado() + this.getVariableCostsAcumulado() + this.getOtherCostsAcumulado();
    }

    public List<String> getFlujoByMonth() {
        return flujoByMonth;
    }

    public void setFlujoByMonth(List<String> flujoByMonth) {
        this.flujoByMonth = flujoByMonth;
    }

    public int getAllIngressAcumulado() {
        return allIngressAcumulado;
    }

    public void setAllIngressAcumulado(int allIngressAcumulado) {
        this.allIngressAcumulado = allIngressAcumulado;
    }

    public int getMaintenanceIngressAcumulado() {
        return maintenanceIngressAcumulado;
    }

    public void setMaintenanceIngressAcumulado(int maintenanceIngressAcumulado) {
        this.maintenanceIngressAcumulado = this.maintenanceIngressAcumulado + maintenanceIngressAcumulado;
    }

    public int getExtraordinaryIngressAcumulado() {
        return extraordinaryIngressAcumulado;
    }

    public void setExtraordinaryIngressAcumulado(int extraordinaryIngressAcumulado) {
        this.extraordinaryIngressAcumulado = this.extraordinaryIngressAcumulado + extraordinaryIngressAcumulado;
    }

    public int getCommonAreasAcumulado() {
        return commonAreasAcumulado;
    }

    public void setCommonAreasAcumulado(int commonAreasAcumulado) {
        this.commonAreasAcumulado = this.commonAreasAcumulado + commonAreasAcumulado;
    }

    public int getOtherIngressAcumulado() {
        return otherIngressAcumulado;
    }

    public void setOtherIngressAcumulado(int otherIngressAcumulado) {
        this.otherIngressAcumulado =  this.otherIngressAcumulado + otherIngressAcumulado;
    }

    public List<String> getInitialBalanceByMonth() {
        return initialBalanceByMonth;
    }

    public void setInitialBalanceByMonth(List<String> initialBalanceByMonth) {
        this.initialBalanceByMonth = initialBalanceByMonth;
    }

    public List<String> getRealBalanceByMonth() {
        return realBalanceByMonth;
    }

    public void setRealBalanceByMonth(List<String> realBalanceByMonth) {
        this.realBalanceByMonth = realBalanceByMonth;
    }

    public int getMaintenanceBudgetAcumulado() {
        return maintenanceBudgetAcumulado;
    }

    public void setMaintenanceBudgetAcumulado(int maintenanceBudgetAcumulado) {
        this.maintenanceBudgetAcumulado = this.maintenanceBudgetAcumulado + maintenanceBudgetAcumulado;
    }

    public int getExtraordinaryIngressBudgetAcumulado() {
        return extraordinaryIngressBudgetAcumulado;
    }

    public void setExtraordinaryIngressBudgetAcumulado(int extraordinaryIngressBudgetAcumulado) {
        this.extraordinaryIngressBudgetAcumulado =   this.extraordinaryIngressBudgetAcumulado + extraordinaryIngressBudgetAcumulado;
    }

    public int getCommonAreasBudgetAcumulado() {
        return commonAreasBudgetAcumulado;
    }

    public void setCommonAreasBudgetAcumulado(int commonAreasBudgetAcumulado) {
        this.commonAreasBudgetAcumulado =  this.commonAreasBudgetAcumulado + commonAreasBudgetAcumulado;
    }

    public int getOtherIngressBudgetAcumulado() {
        return otherIngressBudgetAcumulado;
    }

    public void setOtherIngressBudgetAcumulado(int otherIngressBudgetAcumulado) {
        this.otherIngressBudgetAcumulado =  this.otherIngressBudgetAcumulado + otherIngressBudgetAcumulado;
    }

    public int getMaintenanceBudgetDiferenceAcumulado() {
        return maintenanceBudgetDiferenceAcumulado;
    }

    public void setMaintenanceBudgetDiferenceAcumulado(int maintenanceBudgetDiferenceAcumulado) {
        this.maintenanceBudgetDiferenceAcumulado = this.maintenanceBudgetDiferenceAcumulado + maintenanceBudgetDiferenceAcumulado;
    }

    public int getExtraordinaryIngressBudgetDiferenceAcumulado() {
        return extraordinaryIngressBudgetDiferenceAcumulado;
    }

    public void setExtraordinaryIngressBudgetDiferenceAcumulado(int extraordinaryIngressBudgetDiferenceAcumulado) {
        this.extraordinaryIngressBudgetDiferenceAcumulado = this.extraordinaryIngressBudgetDiferenceAcumulado + extraordinaryIngressBudgetDiferenceAcumulado;
    }

    public int getCommonAreasBudgetDiferenceAcumulado() {
        return commonAreasBudgetDiferenceAcumulado;
    }

    public void setCommonAreasBudgetDiferenceAcumulado(int commonAreasBudgetDiferenceAcumulado) {
        this.commonAreasBudgetDiferenceAcumulado = this.commonAreasBudgetDiferenceAcumulado +commonAreasBudgetDiferenceAcumulado;
    }

    public int getOtherIngressBudgetDiferenceAcumulado() {
        return otherIngressBudgetDiferenceAcumulado;
    }

    public void setOtherIngressBudgetDiferenceAcumulado(int otherIngressBudgetDiferenceAcumulado) {
        this.otherIngressBudgetDiferenceAcumulado = this.otherIngressBudgetDiferenceAcumulado + otherIngressBudgetDiferenceAcumulado;
    }

    public int getTotalIngressBudget() {
        return totalIngressBudget;
    }

    public void setTotalIngressBudget(int totalIngressBudget) {
        this.totalIngressBudget =  this.totalIngressBudget + totalIngressBudget;
    }

    public int getTotalIngressBudgetDiference() {
        return totalIngressBudgetDiference;
    }

    public void setTotalIngressBudgetDiference(int totalIngressBudgetDiference) {
        this.totalIngressBudgetDiference = this.totalIngressBudgetDiference + totalIngressBudgetDiference;
    }

    public List<Double> getFixedCostsBudgetTotal() {
        return fixedCostsBudgetTotal;
    }

    public void setFixedCostsBudgetTotal(List<Double> fixedCostsBudgetTotal) {
        this.fixedCostsBudgetTotal = fixedCostsBudgetTotal;
    }

    public List<Double> getvariableCostsBudgetTotal() {
        return variableCostsBudgetTotal;
    }

    public void setFvariableCostsBudgetTotal(List<Double> variableCostsBudgetTotal) {
        this.variableCostsBudgetTotal = variableCostsBudgetTotal;
    }

    public List<Double> getOtherCostsBudgetTotal() {
        return otherCostsBudgetTotal;
    }

    public void setOtherCostsBudgetTotal(List<Double> otherCostsBudgetTotal) {
        this.otherCostsBudgetTotal = otherCostsBudgetTotal;
    }

    public List<Double> getFixedCostsBudgetDiference() {
        return fixedCostsBudgetDiference;
    }

    public void setFixedCostsBudgetDiference(List<Double> fixedCostsBudgetDiference) {
        this.fixedCostsBudgetDiference = fixedCostsBudgetDiference;
    }

    public List<Double> getVariableCostsBudgetDiference() {
        return variableCostsBudgetDiference;
    }

    public void setVariableCostsBudgetDiference(List<Double> variableCostsBudgetDiference) {
        this.variableCostsBudgetDiference = variableCostsBudgetDiference;
    }

    public List<Double> getOtherCostsBudgetDiference() {
        return otherCostsBudgetDiference;
    }

    public void setOtherCostsBudgetDiference(List<Double> otherCostsBudgetDiference) {
        this.otherCostsBudgetDiference = otherCostsBudgetDiference;
    }

    public double getFixedCostsBudgetAcumulado() {
        return fixedCostsBudgetAcumulado;
    }

    public void setFixedCostsBudgetAcumulado(double fixedCostsBudgetAcumulado) {
        this.fixedCostsBudgetAcumulado =  this.fixedCostsBudgetAcumulado  + fixedCostsBudgetAcumulado;
    }

    public double getFixedCostsBudgetDiferenceAcumulado() {
        return fixedCostsBudgetDiferenceAcumulado;
    }

    public void setFixedCostsBudgetDiferenceAcumulado(double fixedCostsBudgetDiferenceAcumulado) {
        this.fixedCostsBudgetDiferenceAcumulado = this.fixedCostsBudgetDiferenceAcumulado + fixedCostsBudgetDiferenceAcumulado;
    }

    public double getVariableCostsBudgetAcumulado() {
        return variableCostsBudgetAcumulado;
    }

    public void setVariableCostsBudgetAcumulado(double variableCostsBudgetAcumulado) {
        this.variableCostsBudgetAcumulado = this.variableCostsBudgetAcumulado + variableCostsBudgetAcumulado;
    }

    public double getVariableCostsBudgetDiferenceAcumulado() {
        return variableCostsBudgetDiferenceAcumulado;
    }

    public void setVariableCostsBudgetDiferenceAcumulado(double variableCostsBudgetDiferenceAcumulado) {
        this.variableCostsBudgetDiferenceAcumulado =  this.variableCostsBudgetDiferenceAcumulado + variableCostsBudgetDiferenceAcumulado;
    }

    public double getOtherCostsBudgetAcumulado() {
        return otherCostsBudgetAcumulado;
    }

    public void setOtherCostsBudgetAcumulado(double otherCostsBudgetAcumulado) {
        this.otherCostsBudgetAcumulado =  this.otherCostsBudgetAcumulado + otherCostsBudgetAcumulado;
    }

    public double getOtherCostsBudgetDiferenceAcumulado() {
        return otherCostsBudgetDiferenceAcumulado;
    }

    public void setOtherCostsBudgetDiferenceAcumulado(double otherCostsBudgetDiferenceAcumulado) {
        this.otherCostsBudgetDiferenceAcumulado =  this.otherCostsBudgetDiferenceAcumulado + otherCostsBudgetDiferenceAcumulado;
    }

    public List<Double> getAllEgressBudgetByMonth() {
        return allEgressBudgetByMonth;
    }

    public void setAllEgressBudgetByMonth(List<Double> allEgressBudgetByMonth) {
        this.allEgressBudgetByMonth = allEgressBudgetByMonth;
    }

    public List<Double> getAllEgressDiferenceByMonth() {
        return allEgressDiferenceByMonth;
    }

    public void setAllEgressDiferenceByMonth(List<Double> allEgressDiferenceByMonth) {
        this.allEgressDiferenceByMonth = allEgressDiferenceByMonth;
    }

    public Double getAllEgressBudgetAcumulado() {
        return allEgressBudgetAcumulado;
    }

    public void setAllEgressBudgetAcumulado(Double allEgressBudgetAcumulado) {
        this.allEgressBudgetAcumulado = this.allEgressBudgetAcumulado + allEgressBudgetAcumulado;
    }

    public Double getAllEgressDiferenceAcumulado() {
        return allEgressDiferenceAcumulado;
    }

    public void setAllEgressDiferenceAcumulado(Double allEgressDiferenceAcumulado) {
        this.allEgressDiferenceAcumulado = this.allEgressDiferenceAcumulado + allEgressDiferenceAcumulado;
    }
}
