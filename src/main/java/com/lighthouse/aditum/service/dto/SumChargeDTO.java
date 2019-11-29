package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;

/**
 * A DTO for the Charge entity.
 */
public class SumChargeDTO implements Serializable {
    private Locale locale = new Locale("es", "CR");
    @NotNull
    private String concept;
    private double total;
    private String totalFormatted;
    private double percentage;

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
        this.setTotalFormatted(formatMoney(total));
    }

    public SumChargeDTO(String concept,double total ) {

        this.setConcept(concept);
        this.setTotal(total);

    }
    public SumChargeDTO() {

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
