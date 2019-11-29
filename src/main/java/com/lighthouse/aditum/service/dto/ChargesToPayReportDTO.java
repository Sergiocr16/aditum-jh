package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.util.RandomUtil;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ChargesToPayReportDTO implements Serializable {

    private Locale locale = new Locale("es", "CR");

    private List<DueHouseDTO> dueHouses;

    private int totalDueHouses = 0;

    private String totalDueFormatted = "0.00";

    private double totalDue = 0;

    public ChargesToPayReportDTO() {

    }


    public List<DueHouseDTO> getDueHouses() {
        return dueHouses;
    }

    public void setDueHouses(List<DueHouseDTO> dueHouses) {
        this.dueHouses = dueHouses;
    }

    public int getTotalDueHouses() {
        return totalDueHouses;
    }

    public void setTotalDueHouses(int totalDueHouses) {
        this.totalDueHouses = totalDueHouses;
    }

    public String getTotalDueFormatted() {
        return totalDueFormatted;
    }

    public void setTotalDueFormatted(String totalDueFormatted) {
        this.totalDueFormatted = totalDueFormatted;
    }

    public double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(double totalDue) {
        this.setTotalDueFormatted(RandomUtil.formatMoney(totalDue));
        this.totalDue = totalDue;
    }
}
