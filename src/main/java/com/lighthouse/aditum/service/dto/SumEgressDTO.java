package com.lighthouse.aditum.service.dto;

public class SumEgressDTO {
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

    public SumEgressDTO(String concept) {

        this.setConcept(concept);

    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
