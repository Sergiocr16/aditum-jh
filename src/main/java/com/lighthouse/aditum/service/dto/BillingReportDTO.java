package com.lighthouse.aditum.service.dto;

import java.util.List;

public class BillingReportDTO {

    private List<ChargeDTO> charges;

    private Double totalMaintenance;

    private Double totalExtraordinary;

    private Double totalCommonArea;

    private Double totalWaterCharge;

    private Double totalMulta;

    private String totalMaintenanceFormatted;

    private String totalExtraordinaryFormatted;

    private String totalCommonAreaFormatted;

    private String totalWaterChargeFormatted;

    private String totalMultaFormatted;

    private Double total;

    private String totalFormatted;

    public List<ChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<ChargeDTO> charges) {
        this.charges = charges;
    }

    public Double getTotalMaintenance() {
        return totalMaintenance;
    }

    public void setTotalMaintenance(Double totalMaintenance) {
        this.totalMaintenance = totalMaintenance;
    }

    public Double getTotalExtraordinary() {
        return totalExtraordinary;
    }

    public void setTotalExtraordinary(Double totalExtraordinary) {
        this.totalExtraordinary = totalExtraordinary;
    }

    public Double getTotalCommonArea() {
        return totalCommonArea;
    }

    public void setTotalCommonArea(Double totalCommonArea) {
        this.totalCommonArea = totalCommonArea;
    }

    public Double getTotalWaterCharge() {
        return totalWaterCharge;
    }

    public void setTotalWaterCharge(Double totalWaterCharge) {
        this.totalWaterCharge = totalWaterCharge;
    }

    public Double getTotalMulta() {
        return totalMulta;
    }

    public void setTotalMulta(Double totalMulta) {
        this.totalMulta = totalMulta;
    }

    public String getTotalMaintenanceFormatted() {
        return totalMaintenanceFormatted;
    }

    public void setTotalMaintenanceFormatted(String totalMaintenanceFormatted) {
        this.totalMaintenanceFormatted = totalMaintenanceFormatted;
    }

    public String getTotalExtraordinaryFormatted() {
        return totalExtraordinaryFormatted;
    }

    public void setTotalExtraordinaryFormatted(String totalExtraordinaryFormatted) {
        this.totalExtraordinaryFormatted = totalExtraordinaryFormatted;
    }

    public String getTotalCommonAreaFormatted() {
        return totalCommonAreaFormatted;
    }

    public void setTotalCommonAreaFormatted(String totalCommonAreaFormatted) {
        this.totalCommonAreaFormatted = totalCommonAreaFormatted;
    }

    public String getTotalWaterChargeFormatted() {
        return totalWaterChargeFormatted;
    }

    public void setTotalWaterChargeFormatted(String totalWaterChargeFormatted) {
        this.totalWaterChargeFormatted = totalWaterChargeFormatted;
    }

    public String getTotalMultaFormatted() {
        return totalMultaFormatted;
    }

    public void setTotalMultaFormatted(String totalMultaFormatted) {
        this.totalMultaFormatted = totalMultaFormatted;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }
}
