package com.lighthouse.aditum.service.dto;

import java.util.List;

public class SumCategoryEgressDTO {
    private Long id;
    private String category;
    private List<SumEgressDTO> egressList;
    private int total;
    private double percentage;
    private boolean showDetail;
    private int budget;
    private int budgetDiference;

    public String getCategory() {
        return category;
    }

    public void setCategory(String concept) {
        this.category = concept;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
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

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getBudgetDiference() {
        return budgetDiference;
    }

    public void setBudgetDiference(int budgetDiference) {
        this.budgetDiference = budgetDiference;
    }
}
