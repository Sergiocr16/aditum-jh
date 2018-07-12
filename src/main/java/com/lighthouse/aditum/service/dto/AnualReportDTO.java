package com.lighthouse.aditum.service.dto;

import java.util.List;

public class AnualReportDTO {
    private List<MensualIngressReportDTO> anualIngressByMonth;
    private List<MensualEgressReportDTO> anualEgressByMonth;
    private List<EgressCategoryByMonthDTO> fixedCostEgress;
    private List<EgressCategoryByMonthDTO> variableCostEgress;
    private List<EgressCategoryByMonthDTO> otherCostEgress;
    private List<String> flujoByMonth;
    private int fixedCostsAcumulado = 0;
    private int variableCostsAcumulado = 0;
    private int otherCostsAcumulado = 0;
    private int maintenanceIngressAcumulado = 0;
    private int extraordinaryIngressAcumulado = 0;
    private int commonAreasAcumulado = 0;
    private int otherIngressAcumulado = 0;
    private int allEgressAcumulado = 0;
    private int allIngressAcumulado = 0;

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
}
