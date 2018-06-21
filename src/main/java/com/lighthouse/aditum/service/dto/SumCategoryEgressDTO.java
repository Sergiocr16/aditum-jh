package com.lighthouse.aditum.service.dto;

import java.util.List;

public class SumCategoryEgressDTO {
    private String category;
    private List<SumEgressDTO> egressList;
    private int total;
    private double percentage;
    private boolean showDetail;

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
    public SumCategoryEgressDTO(String category) {
        this.category = category;
        this.showDetail = false;
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
}
