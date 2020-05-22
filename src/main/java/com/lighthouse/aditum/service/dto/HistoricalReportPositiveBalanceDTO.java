package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.util.RandomUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class HistoricalReportPositiveBalanceDTO implements Serializable {

    private Locale locale = new Locale("es", "CR");

    private List<HouseHistoricalReportDefaulterDTO> dueHouses;

    private int totalDueHouses = 0;

    private String totalDueFormatted = "0.00";

    private double totalDue = 0;

    public HistoricalReportPositiveBalanceDTO() {
    }


    public List<HouseHistoricalReportDefaulterDTO> getDueHouses() {
        return dueHouses;
    }

    public void setDueHouses(List<HouseHistoricalReportDefaulterDTO> dueHouses) {
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

    public void setTotalDue(String currency,double totalDue) {
        this.setTotalDueFormatted(RandomUtil.formatMoney(currency,totalDue));
        this.totalDue = totalDue;
    }
}

