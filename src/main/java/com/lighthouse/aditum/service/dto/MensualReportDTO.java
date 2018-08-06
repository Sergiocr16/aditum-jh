package com.lighthouse.aditum.service.dto;

import java.util.List;

public class MensualReportDTO {
    private MensualIngressReportDTO mensualIngressReport;
    private MensualEgressReportDTO mensualEgressReport;
    private List<MensualAndAnualAccountDTO> mensualAndAnualAccount;
    private int totalInitialBalance;
    private int flujo;

    public MensualIngressReportDTO getMensualIngressReport() {
        return mensualIngressReport;
    }

    public void setMensualIngressReport(MensualIngressReportDTO mensualIngressReport) {
        this.mensualIngressReport = mensualIngressReport;
    }

    public MensualEgressReportDTO getMensualEgressReport() {
        return mensualEgressReport;
    }

    public void setMensualEgressReport(MensualEgressReportDTO mensualEgressReport) {
        this.mensualEgressReport = mensualEgressReport;
    }

    public List<MensualAndAnualAccountDTO> getMensualAndAnualAccount() {
        return mensualAndAnualAccount;
    }

    public void setMensualAndAnualAccount(List<MensualAndAnualAccountDTO> mensualAndAnualAccount) {
        this.mensualAndAnualAccount = mensualAndAnualAccount;
    }

    public int getTotalInitialBalance() {
        return totalInitialBalance;
    }

    public void setTotalInitialBalance(List<MensualAndAnualAccountDTO> mensualAndAnualAccount) {
        int totalBalance =0;
        for (int i = 0; i <mensualAndAnualAccount.size() ; i++) {
            totalBalance = totalBalance +mensualAndAnualAccount.get(i).getInicialBalance();
        }
        this.totalInitialBalance = totalBalance;
    }

    public int getFlujo() {
        return flujo;
    }

    public void setFlujo(int flujo) {
        this.flujo = flujo;
    }
}
