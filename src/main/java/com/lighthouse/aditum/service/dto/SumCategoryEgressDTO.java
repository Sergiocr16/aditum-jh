package com.lighthouse.aditum.service.dto;

import java.util.List;

public class SumCategoryEgressDTO {
    private Long id;
    private String category;
    private List<SumEgressDTO> egressList;
    private double total;
    private double percentage;
    private boolean showDetail;
    private double budget = 0;
    private double budgetDiference = 0;

    public String getCategory() {
        return category;
    }

    public void setCategory(String concept) {
        this.category = concept;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public SumCategoryEgressDTO(String category,int total) {

        this.category = category;
        this.setTotal(total);
        this.showDetail = false;

    }
    public SumCategoryEgressDTO(Long id,String category) {
        this.category = category;
        this.showDetail = false;
        this.id = id;
    }
    public SumCategoryEgressDTO(Long id,String category,int budget) {
        this.category = category;
        this.showDetail = false;
        this.id = id;
        this.budget = budget;
        this.budgetDiference = budget;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public List<SumEgressDTO> getEgressList() {
        return egressList;
    }

    public void setEgressList(List<SumEgressDTO> egressList) {
        this.egressList = egressList;
    }


    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getBudgetDiference() {
        return budgetDiference;
    }

    public void setBudgetDiference(double budgetDiference) {
        this.budgetDiference = budgetDiference;
    }
}
