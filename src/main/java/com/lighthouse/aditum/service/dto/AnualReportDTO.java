package com.lighthouse.aditum.service.dto;

import java.util.ArrayList;
import java.util.List;

public class AnualReportDTO {
    private List<MensualIngressReportDTO> anualIngressByMonth;
    private List<MensualEgressReportDTO> anualEgressByMonth;
    private List<EgressCategoryByMonthDTO> fixedCostEgress;
    private List<EgressCategoryByMonthDTO> variableCostEgress;
    private List<EgressCategoryByMonthDTO> otherCostEgress;
    private List<Double> flujoByMonth;
    private List<String> initialBalanceByMonth;
    private List<String> realBalanceByMonth;

    private double fixedCostsAcumulado = 0;
    private List<Double> fixedCostsBudgetTotal = new ArrayList<>();
    private List<Double> fixedCostsBudgetDiference = new ArrayList<>();
    private double fixedCostsBudgetAcumulado = 0;
    private double fixedCostsBudgetDiferenceAcumulado = 0;

    private double variableCostsAcumulado = 0;
    private List<Double> variableCostsBudgetTotal = new ArrayList<>();
    private List<Double> variableCostsBudgetDiference = new ArrayList<>();
    private double variableCostsBudgetAcumulado = 0;
    private double variableCostsBudgetDiferenceAcumulado = 0;

    private double otherCostsAcumulado = 0;

    private List<Double> otherCostsBudgetTotal = new ArrayList<>();
    private List<Double> otherCostsBudgetDiference = new ArrayList<>();
    private double otherCostsBudgetAcumulado = 0;
    private double otherCostsBudgetDiferenceAcumulado = 0;

    private double maintenanceIngressAcumulado = 0;
    private double maintenanceBudgetAcumulado = 0;
    private double maintenanceBudgetDiferenceAcumulado = 0;

    private double extraordinaryIngressAcumulado = 0;
    private double extraordinaryIngressBudgetAcumulado = 0;
    private double extraordinaryIngressBudgetDiferenceAcumulado = 0;

    private double commonAreasAcumulado = 0;
    private double commonAreasBudgetAcumulado = 0;
    private double commonAreasBudgetDiferenceAcumulado = 0;

    private double otherIngressAcumulado = 0;
    private double otherIngressBudgetAcumulado = 0;
    private double otherIngressBudgetDiferenceAcumulado = 0;

    private double totalIngressBudget = 0;
    private double totalIngressBudgetDiference = 0;

    private double allEgressAcumulado = 0;
    private double allIngressAcumulado = 0;

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

    public double getFixedCostsAcumulado() {
        return fixedCostsAcumulado;
    }

    public void setFixedCostsAcumulado(double fixedCostsAcumulado) {
        this.fixedCostsAcumulado =  this.fixedCostsAcumulado + fixedCostsAcumulado;
    }

    public double getVariableCostsAcumulado() {
        return variableCostsAcumulado;
    }

    public void setVariableCostsAcumulado(double variableCostsAcumulado) {
        this.variableCostsAcumulado = this.variableCostsAcumulado + variableCostsAcumulado;
    }

    public double getOtherCostsAcumulado() {
        return otherCostsAcumulado;
    }

    public void setOtherCostsAcumulado(double otherCostsAcumulado) {
        this.otherCostsAcumulado = this.otherCostsAcumulado + otherCostsAcumulado;
    }

    public double getAllEgressAcumulado() {
        return allEgressAcumulado;
    }

    public void setAllEgressAcumulado() {
        this.allEgressAcumulado = this.getFixedCostsAcumulado() + this.getVariableCostsAcumulado() + this.getOtherCostsAcumulado();
    }

    public List<Double> getFlujoByMonth() {
        return flujoByMonth;
    }

    public void setFlujoByMonth(List<Double> flujoByMonth) {
        this.flujoByMonth = flujoByMonth;
    }

    public double getAllIngressAcumulado() {
        return allIngressAcumulado;
    }

    public void setAllIngressAcumulado(double allIngressAcumulado) {
        this.allIngressAcumulado = allIngressAcumulado;
    }

    public double getMaintenanceIngressAcumulado() {
        return maintenanceIngressAcumulado;
    }

    public void setMaintenanceIngressAcumulado(double maintenanceIngressAcumulado) {
        this.maintenanceIngressAcumulado = this.maintenanceIngressAcumulado + maintenanceIngressAcumulado;
    }

    public double getExtraordinaryIngressAcumulado() {
        return extraordinaryIngressAcumulado;
    }

    public void setExtraordinaryIngressAcumulado(double extraordinaryIngressAcumulado) {
        this.extraordinaryIngressAcumulado = this.extraordinaryIngressAcumulado + extraordinaryIngressAcumulado;
    }

    public double getCommonAreasAcumulado() {
        return commonAreasAcumulado;
    }

    public void setCommonAreasAcumulado(double commonAreasAcumulado) {
        this.commonAreasAcumulado = this.commonAreasAcumulado + commonAreasAcumulado;
    }

    public double getOtherIngressAcumulado() {
        return otherIngressAcumulado;
    }

    public void setOtherIngressAcumulado(double otherIngressAcumulado) {
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

    public double getMaintenanceBudgetAcumulado() {
        return maintenanceBudgetAcumulado;
    }

    public void setMaintenanceBudgetAcumulado(double maintenanceBudgetAcumulado) {
        this.maintenanceBudgetAcumulado = this.maintenanceBudgetAcumulado + maintenanceBudgetAcumulado;
    }

    public double getExtraordinaryIngressBudgetAcumulado() {
        return extraordinaryIngressBudgetAcumulado;
    }

    public void setExtraordinaryIngressBudgetAcumulado(double extraordinaryIngressBudgetAcumulado) {
        this.extraordinaryIngressBudgetAcumulado =   this.extraordinaryIngressBudgetAcumulado + extraordinaryIngressBudgetAcumulado;
    }

    public double getCommonAreasBudgetAcumulado() {
        return commonAreasBudgetAcumulado;
    }

    public void setCommonAreasBudgetAcumulado(double commonAreasBudgetAcumulado) {
        this.commonAreasBudgetAcumulado =  this.commonAreasBudgetAcumulado + commonAreasBudgetAcumulado;
    }

    public double getOtherIngressBudgetAcumulado() {
        return otherIngressBudgetAcumulado;
    }

    public void setOtherIngressBudgetAcumulado(double otherIngressBudgetAcumulado) {
        this.otherIngressBudgetAcumulado =  this.otherIngressBudgetAcumulado + otherIngressBudgetAcumulado;
    }

    public double getMaintenanceBudgetDiferenceAcumulado() {
        return maintenanceBudgetDiferenceAcumulado;
    }

    public void setMaintenanceBudgetDiferenceAcumulado(double maintenanceBudgetDiferenceAcumulado) {
        this.maintenanceBudgetDiferenceAcumulado = this.maintenanceBudgetDiferenceAcumulado + maintenanceBudgetDiferenceAcumulado;
    }

    public double getExtraordinaryIngressBudgetDiferenceAcumulado() {
        return extraordinaryIngressBudgetDiferenceAcumulado;
    }

    public void setExtraordinaryIngressBudgetDiferenceAcumulado(double extraordinaryIngressBudgetDiferenceAcumulado) {
        this.extraordinaryIngressBudgetDiferenceAcumulado = this.extraordinaryIngressBudgetDiferenceAcumulado + extraordinaryIngressBudgetDiferenceAcumulado;
    }

    public double getCommonAreasBudgetDiferenceAcumulado() {
        return commonAreasBudgetDiferenceAcumulado;
    }

    public void setCommonAreasBudgetDiferenceAcumulado(double commonAreasBudgetDiferenceAcumulado) {
        this.commonAreasBudgetDiferenceAcumulado = this.commonAreasBudgetDiferenceAcumulado +commonAreasBudgetDiferenceAcumulado;
    }

    public double getOtherIngressBudgetDiferenceAcumulado() {
        return otherIngressBudgetDiferenceAcumulado;
    }

    public void setOtherIngressBudgetDiferenceAcumulado(double otherIngressBudgetDiferenceAcumulado) {
        this.otherIngressBudgetDiferenceAcumulado = this.otherIngressBudgetDiferenceAcumulado + otherIngressBudgetDiferenceAcumulado;
    }

    public double getTotalIngressBudget() {
        return totalIngressBudget;
    }

    public void setTotalIngressBudget(double totalIngressBudget) {
        this.totalIngressBudget =  this.totalIngressBudget + totalIngressBudget;
    }

    public double getTotalIngressBudgetDiference() {
        return totalIngressBudgetDiference;
    }

    public void setTotalIngressBudgetDiference(double totalIngressBudgetDiference) {
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

    public List<Double> getOtherCostsBudgetTotal() {
        return otherCostsBudgetTotal;
    }

    public void setOtherCostsBudgetTotal(List<Double> otherCostsBudgetTotal) {
        this.otherCostsBudgetTotal = otherCostsBudgetTotal;
    }
}
