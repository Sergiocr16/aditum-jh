package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.HouseService;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sergio on 19/07/2018.
 */
public class IncomeReportDTO implements Serializable {

    private String totalMaintenance;

    private String totalExtraordinary;

    private String totalCommonArea;

    private String total;

    private List<PaymentDTO> payments;

    private String filter;

    private HouseService houseService;

    public IncomeReportDTO(HouseService houseService){
        this.houseService = houseService;
    }

    public String getTotalMaintenance() {
        return totalMaintenance;
    }

    public void setTotalMaintenance(String totalMaintenance) {
        this.totalMaintenance = totalMaintenance;
    }

    public String getTotalExtraordinary() {
        return totalExtraordinary;
    }

    public void setTotalExtraordinary(String totalExtraordinary) {
        this.totalExtraordinary = totalExtraordinary;
    }

    public String getTotalCommonArea() {
        return totalCommonArea;
    }

    public void setTotalCommonArea(String totalCommonArea) {
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


}
