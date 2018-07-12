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
    private int total;
    private double percentage;

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public SumChargeDTO(String concept,int total ) {

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
