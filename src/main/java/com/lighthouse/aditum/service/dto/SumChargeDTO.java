package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Charge entity.
 */
public class SumChargeDTO implements Serializable {

    @NotNull
    private String concept;
    private double total;
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
    }

    public SumChargeDTO(String concept,double total ) {

        this.concept = concept;
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
}
