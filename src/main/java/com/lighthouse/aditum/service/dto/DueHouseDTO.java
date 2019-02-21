package com.lighthouse.aditum.service.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DueHouseDTO implements Serializable {

    private HouseDTO house;
    private Locale locale = new Locale("es", "CR");

    private ResidentDTO responsable;

    private List<ChargeDTO> dues;

    private double totalDue = 0;

    private String totalDueFormatted;

    public DueHouseDTO(){

    }

    private String formatMoney(double ammount) {
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        if(ammount==0){
            return currencyFormatter.format(ammount).substring(1);
        }else {
            String t = currencyFormatter.format(ammount).substring(1);
            return t.substring(0, t.length() - 3).replace(",", ".");
        }
    }

    public HouseDTO getHouseDTO() {
        return house;
    }

    public void setHouseDTO(HouseDTO houseDTO) {
        this.house = houseDTO;
    }

    public ResidentDTO getResponsable() {
        return responsable;
    }

    public void setResponsable(ResidentDTO responsable) {
        this.responsable = responsable;
    }

    public List<ChargeDTO> getDues() {
        return dues;
    }

    public void setDues(List<ChargeDTO> dues) {
        this.dues = dues;
    }

    public double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(double totalDue) {
        this.setTotalDueFormatted(formatMoney(totalDue));
        this.totalDue = totalDue;
    }

    public String getTotalDueFormatted() {
        return totalDueFormatted;
    }

    public void setTotalDueFormatted(String totalDueFormatted) {
        this.totalDueFormatted = totalDueFormatted;
    }
}
