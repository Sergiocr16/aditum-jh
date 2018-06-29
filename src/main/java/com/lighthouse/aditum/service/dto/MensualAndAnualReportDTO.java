package com.lighthouse.aditum.service.dto;

import java.util.List;

public class MensualAndAnualReportDTO {
    private MensualAndAnualIngressReportDTO mensualIngressReport;
    private MensualAndAnualEgressReportDTO mensualEgressReport;
    private List<MensualAndAnualAccountDTO> mensualAndAnualAccount;
    private int totalInitialBalance;

    public MensualAndAnualIngressReportDTO getMensualIngressReport() {
        return mensualIngressReport;
    }

    public void setMensualIngressReport(MensualAndAnualIngressReportDTO mensualIngressReport) {
        this.mensualIngressReport = mensualIngressReport;
    }

    public MensualAndAnualEgressReportDTO getMensualEgressReport() {
        return mensualEgressReport;
    }

    public void setMensualEgressReport(MensualAndAnualEgressReportDTO mensualEgressReport) {
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
}
