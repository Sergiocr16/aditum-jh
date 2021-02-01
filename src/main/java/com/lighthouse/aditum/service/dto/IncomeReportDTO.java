package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.HouseService;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sergio on 19/07/2018.
 */
public class IncomeReportDTO implements Serializable {

    private Double totalMaintenance;

    private Double totalExtraordinary;

    private Double totalCommonArea;

    private Double totalOtherIngress;

    private Double totalWaterCharge;

    private Double totalMulta;

    private Double totalAdelanto;

    private String totalMaintenanceFormatted;

    private String totalExtraordinaryFormatted;

    private String totalCommonAreaFormatted;

    private String totalOtherIngressFormatted;

    private String totalWaterChargeFormatted;

    private String totalMultaFormatted;

    private String totalAdelantoFormatted;

    private String total;

    private String totalFormatted;

    private List<PaymentDTO> payments;

    private String filter;

    private HouseService houseService;

    public IncomeReportDTO(HouseService houseService){
        this.houseService = houseService;
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

    public List<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }


    public void defineFilter(String houseId,String paymentMethod,String category, String account){
        String filterString = "";
        if(!houseId.equals("empty")){
            filterString += "Filial "+this.houseService.findOne(Long.valueOf(houseId)).getHousenumber();
        }
        if(!account.equals("empty")){
            if(filterString.contains("Filial")){
                filterString+=" / ";
            }
            filterString += "Cuenta "+ account;
        }
        if(!paymentMethod.equals("empty")){
            if(filterString.contains("Cuenta") || filterString.contains("Filial")){
                filterString+=" / ";
            }
            filterString += "Método de pago "+ paymentMethod;
        }
        if(!category.equals("empty")){
            if(filterString.contains("Método") || filterString.contains("Cuenta") || filterString.contains("Filial")){
                filterString+=" / ";
            }
            filterString += "Categoría "+ category;
        }
        if(filterString!=""){
            this.setFilter("Filtrado por: "+filterString);
        }else{
            this.setFilter("");
        }
      }


    public Double getTotalOtherIngress() {
        return totalOtherIngress;
    }

    public void setTotalOtherIngress(Double totalOtherIngress) {
        this.totalOtherIngress = totalOtherIngress;
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

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
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

    public String getTotalOtherIngressFormatted() {
        return totalOtherIngressFormatted;
    }

    public void setTotalOtherIngressFormatted(String totalOtherIngressFormatted) {
        this.totalOtherIngressFormatted = totalOtherIngressFormatted;
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

    public Double getTotalAdelanto() {
        return totalAdelanto;
    }

    public void setTotalAdelanto(Double totalAdelanto) {
        this.totalAdelanto = totalAdelanto;
    }

    public String getTotalAdelantoFormatted() {
        return totalAdelantoFormatted;
    }

    public void setTotalAdelantoFormatted(String totalAdelantoFormatted) {
        this.totalAdelantoFormatted = totalAdelantoFormatted;
    }
}
