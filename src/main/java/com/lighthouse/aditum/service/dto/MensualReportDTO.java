package com.lighthouse.aditum.service.dto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MensualReportDTO {
    private Locale locale = new Locale("es", "CR");
    private MensualIngressReportDTO mensualIngressReport;
    private MensualEgressReportDTO mensualEgressReport;
    private List<MensualAndAnualAccountDTO> mensualAndAnualAccount;
    private double totalInitialBalance;
    private String totalInitialBalanceFormatted;
    private double flujo;
    private String flujoFormatted;

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

    public double getTotalInitialBalance() {
        return totalInitialBalance;
    }

    public void setTotalInitialBalance(List<MensualAndAnualAccountDTO> mensualAndAnualAccount) {
        double totalBalance =0;
        for (int i = 0; i <mensualAndAnualAccount.size() ; i++) {
            totalBalance = totalBalance +mensualAndAnualAccount.get(i).getInicialBalance();
        }
        this.totalInitialBalance = totalBalance;
        this.setTotalInitialBalanceFormatted(formatMoney(totalBalance));

    }
    private String formatMoney(double ammount){
        DecimalFormat format = new DecimalFormat("₡#,##0.00;₡-#,##0.00");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        String total = format.format(ammount);
        return total;
    }

    public double getFlujo() {
        return flujo;
    }

    public void setFlujo(double flujo) {
        this.flujo = flujo;
        this.setFlujoFormatted(formatMoney(flujo));
    }

    public String getTotalInitialBalanceFormatted() {
        return totalInitialBalanceFormatted;
    }

    public void setTotalInitialBalanceFormatted(String totalInitialBalanceFormatted) {
        this.totalInitialBalanceFormatted = totalInitialBalanceFormatted;
    }

    public String getFlujoFormatted() {
        return flujoFormatted;
    }

    public void setFlujoFormatted(String flujoFormatted) {
        this.flujoFormatted = flujoFormatted;
    }
}
