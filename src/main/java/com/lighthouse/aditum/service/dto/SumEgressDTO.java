package com.lighthouse.aditum.service.dto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;

public class SumEgressDTO {
    private Locale locale = new Locale("es", "CR");
    private String concept;
    private double total = 0;
    private String totalFormatted = "0.00";
    private double percentage;

    public SumEgressDTO() {

    }
    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(String currency,double total) {
        this.total = total;
        this.setTotalFormatted(formatMoney(currency,total));
    }

    public SumEgressDTO(String concept) {

        this.setConcept(concept);

    }


    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }
}
