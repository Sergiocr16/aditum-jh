package com.lighthouse.aditum.service.dto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;

public class SumCategoryEgressDTO {
    private Locale locale = new Locale("es", "CR");
    private Long id;
    private String category;
    private List<SumEgressDTO> egressList;
    private double total = 0;
    private String totalFormatted = "0.00";
    private double percentage;
    private boolean showDetail;
    private double budget = 0;
    private double budgetDiference = 0;
    private String budgetFormatted = "0.00";
    private String budgetDiferenceFormatted = "0.00";

    public String getCategory() {
        return category;
    }

    public void setCategory(String concept) {
        this.category = concept;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(String currency,double total) {
        this.total = total;
        this.setTotalFormatted(formatMoney(currency,total));
    }

    public SumCategoryEgressDTO() {


    }
    public SumCategoryEgressDTO(String currency,String category,int total) {
        this.category = category;
        this.setTotal(currency,total);
        this.showDetail = false;
    }
    public SumCategoryEgressDTO(Long id,String category) {
        this.category = category;
        this.showDetail = false;
        this.id = id;
    }
    public SumCategoryEgressDTO(String currency, Long id,String category,int budget) {
        this.category = category;
        this.showDetail = false;
        this.id = id;
        this.setBudget(currency,budget);
        this.setBudgetDiference(currency,budget);
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

    public void setBudget(String currency, double budget) {
        this.budget = budget;
        this.setBudgetFormatted(formatMoney(currency,budget));
    }

    public double getBudgetDiference() {
        return budgetDiference;
    }

    public void setBudgetDiference(String currency, double budgetDiference) {
        this.budgetDiference = budgetDiference;
        this.setBudgetDiferenceFormatted(formatMoney(currency,budgetDiference));
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public String getBudgetFormatted() {
        return budgetFormatted;
    }

    public void setBudgetFormatted(String budgetFormatted) {
        this.budgetFormatted = budgetFormatted;
    }

    public String getBudgetDiferenceFormatted() {
        return budgetDiferenceFormatted;
    }

    public void setBudgetDiferenceFormatted(String budgetDiferenceFormatted) {
        this.budgetDiferenceFormatted = budgetDiferenceFormatted;
    }
}
