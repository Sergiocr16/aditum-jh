package com.lighthouse.aditum.service.dto;

public class MensualAndAnualReportDTO {
    private MensualAndAnualIngressReportDTO mensualIngressReport;
    private MensualAndAnualEgressReportDTO mensualEgressReport;


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
}
