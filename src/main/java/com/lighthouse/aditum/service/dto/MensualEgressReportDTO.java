package com.lighthouse.aditum.service.dto;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class MensualEgressReportDTO {
    private List<SumCategoryEgressDTO> fixedCosts = new ArrayList<>();
    private List<SumCategoryEgressDTO> variableCosts = new ArrayList<>();
    private List<SumCategoryEgressDTO> otherCosts = new ArrayList<>();
    private List<EgressDTO> egressList;
    private double allEgressCategoriesTotal = 0;
    private double fixedCostsTotal = 0;
    private double variableCostsTotal = 0;
    private double otherCostsTotal = 0;
    private double fixedCostsPercentage = 0.0;
    private double variableCostsPercentage = 0.0;
    private double otherCostsPercentage = 0.0;

    private double fixedCostsBudgetTotal =0;
    private double variableCostsBudgetTotal =0;
    private double otherCostsBudgetTotal =0;

    private double fixedCostsBudgetDiference = 0;
    private double variableCostsBudgetDiference  = 0;
    private double otherCostsBudgetDiference  = 0;


    public void setCategoriesNames(Page<EgressDTO> egress,Page<EgressCategoryDTO> egressCategories){
        for (int i =0;egress.getContent().size()>i;i++) {
            for (int j =0;egressCategories.getContent().size()>j;j++){
                if(Long.parseLong(egress.getContent().get(i).getCategory())==egressCategories.getContent().get(j).getId()){
                    egress.getContent().get(i).setCategoryName(egressCategories.getContent().get(j).getCategory());
                    egress.getContent().get(i).setGroupName(egressCategories.getContent().get(j).getGroup());
                    egress.getContent().get(i).setCategoryId(egressCategories.getContent().get(j).getId());
                }
            }
            SumCategoryEgressDTO object = new SumCategoryEgressDTO(egress.getContent().get(i).getCategoryId(), egress.getContent().get(i).getCategoryName());

            switch (egress.getContent().get(i).getGroupName()){
                case "Gastos fijos":
                    if(getFixedCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(object.getCategory().toUpperCase())).count()==0) {
                        getFixedCosts().add(object);
                    }
                    break;
                case "Gastos variables":
                    if(getVariableCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(object.getCategory().toUpperCase())).count()==0) {
                        getVariableCosts().add(object);
                    }
                    break;
                case "Otros gastos":
                    if(getOtherCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(object.getCategory().toUpperCase())).count()==0) {
                        getOtherCosts().add(object);
                    }
                    break;

            }
        }
        egressList = egress.getContent();
    }

    public List<SumCategoryEgressDTO> getFixedCosts() {
        return fixedCosts;
    }

    public void setFixedCosts(List<SumCategoryEgressDTO> fixedCosts) {
        this.fixedCosts = fixedCosts;
    }

    public List<SumCategoryEgressDTO> getSumCategoryEgress( List<SumCategoryEgressDTO> sumCategoryEgress,String group,double totalIngress){
        List<SumCategoryEgressDTO> finalList = new ArrayList<>();
        for (int i =0;sumCategoryEgress.size()>i;i++) {
            SumCategoryEgressDTO item = sumCategoryEgress.get(i);
                this.setSumEgressListPerSumCategoryEgressDTO(item,group,totalIngress);
        }
        return sumCategoryEgress;
    }


public void setSumEgressListPerSumCategoryEgressDTO (SumCategoryEgressDTO sumCategory,String group,double totalIngress){
    List<SumEgressDTO> sumEgreesList = new ArrayList<>();
    for (int i = 0; i < egressList.size(); i++) {
            EgressDTO egrees = egressList.get(i);
            if(egrees.getCategoryName().equals(sumCategory.getCategory()) && egrees.getGroupName().equals(group)){
                double totalByCategory =  egressList.stream().filter(o -> o.getCategoryName().equals(sumCategory.getCategory())&&egrees.getGroupName().equals(group)).mapToDouble(o -> Double.parseDouble(o.getTotal())).sum();
                SumEgressDTO sumEgressDTO = new SumEgressDTO(egrees.getConcept());
                sumCategory.setTotal(totalByCategory);
                double percentageByCategory = (totalByCategory * 100.0f) / totalIngress;
                sumCategory.setPercentage(percentageByCategory);
                 if(sumEgreesList.stream().filter(o -> o.getConcept().toUpperCase().equals(egrees.getConcept().toUpperCase())).count()==0) {
                     double totalbyEgress =  egressList.stream().filter(o -> o.getConcept().toUpperCase().equals(egrees.getConcept().toUpperCase())).mapToDouble(o -> Double.parseDouble(o.getTotal())).sum();
                     sumEgressDTO.setTotal(totalbyEgress);
                     double percentagebyEgress = (totalbyEgress * 100.0f) / totalIngress;
                     sumEgressDTO.setPercentage(percentagebyEgress);
                     sumEgreesList.add(sumEgressDTO);

                 }
            }

        }
        sumCategory.setEgressList(sumEgreesList);
    }
    public void setTotalBudgetPerGroup(){
        for (int i = 0; i < fixedCosts.size(); i++) {
            this.setFixedCostsBudgetTotal(this.getFixedCostsBudgetTotal() + fixedCosts.get(i).getBudget());
        }
        this.setFixedCostsBudgetDiference(this.getFixedCostsBudgetTotal() - this.getFixedCostsTotal());

        for (int i = 0; i < variableCosts.size(); i++) {
            this.setVariableCostsBudgetTotal(this.getVariableCostsBudgetTotal() + variableCosts.get(i).getBudget());

        }
        this.setVariableCostsBudgetDiference(this.getVariableCostsBudgetTotal() - this.getVariableCostsTotal());

        for (int i = 0; i < otherCosts.size(); i++) {
            this.setOtherCostsBudgetTotal(this.getOtherCostsBudgetTotal() + otherCosts.get(i).getBudget());

        }
        this.setOtherCostsBudgetDiference(this.getOtherCostsBudgetTotal() - this.getOtherCostsTotal());

    }

    public void setGroupsTotal(List<SumCategoryEgressDTO> list,int type,double totalIngress){
        double total = 0;
        for (int i = 0; i <list.size() ; i++) {
            total = total + list.get(i).getTotal();
        }
        double percentage = (total * 100.0f) / totalIngress;
        switch (type){
            case 1:
                this.setFixedCostsPercentage(percentage);
                this.setFixedCostsTotal(total);
                break;
            case 2:
                this.setVariableCostsPercentage(percentage);
                this.setVariableCostsTotal(total);
                break;
            case 3:
                this.setOtherCostsPercentage(percentage);
                this.setOtherCostsTotal(total);
                break;
        }

    }

    public void setAllEgressTotal(){
        this.allEgressCategoriesTotal = this.getFixedCostsTotal() + this.getOtherCostsTotal() + this.getVariableCostsTotal();

    }

    public double getFixedCostsTotal() {
        return fixedCostsTotal;
    }

    public void setFixedCostsTotal(double fixedCostsTotal) {
        this.fixedCostsTotal = fixedCostsTotal;
    }

    public double getVariableCostsTotal() {
        return variableCostsTotal;
    }

    public void setVariableCostsTotal(double variableCostsTotal) {
        this.variableCostsTotal = variableCostsTotal;
    }

    public double getOtherCostsTotal() {
        return otherCostsTotal;
    }

    public void setOtherCostsTotal(double otherCostsTotal) {
        this.otherCostsTotal = otherCostsTotal;
    }

    public List<SumCategoryEgressDTO> getVariableCosts() {
        return variableCosts;
    }

    public void setVariableCosts(List<SumCategoryEgressDTO> variableCosts) {
        this.variableCosts = variableCosts;
    }

    public List<SumCategoryEgressDTO> getOtherCosts() {
        return otherCosts;
    }

    public void setOtherCosts(List<SumCategoryEgressDTO> otherCosts) {
        this.otherCosts = otherCosts;
    }


    public double getFixedCostsPercentage() {
        return fixedCostsPercentage;
    }

    public void setFixedCostsPercentage(double fixedCostsPercentage) {
        this.fixedCostsPercentage = fixedCostsPercentage;
    }

    public double getVariableCostsPercentage() {
        return variableCostsPercentage;
    }

    public void setVariableCostsPercentage(double variableCostsPercentage) {
        this.variableCostsPercentage = variableCostsPercentage;
    }

    public double getOtherCostsPercentage() {
        return otherCostsPercentage;
    }

    public void setOtherCostsPercentage(double otherCostsPercentage) {
        this.otherCostsPercentage = otherCostsPercentage;
    }

    public double getFixedCostsBudgetDiference() {
        return fixedCostsBudgetDiference;
    }

    public void setFixedCostsBudgetDiference(double fixedCostsBudgetDiference) {
        this.fixedCostsBudgetDiference = fixedCostsBudgetDiference;
    }

    public double getVariableCostsBudgetDiference() {
        return variableCostsBudgetDiference;
    }

    public void setVariableCostsBudgetDiference(double variableCostsBudgetDiference) {
        this.variableCostsBudgetDiference = variableCostsBudgetDiference;
    }

    public double getOtherCostsBudgetDiference() {
        return otherCostsBudgetDiference;
    }

    public void setOtherCostsBudgetDiference(double otherCostsBudgetDiference) {
        this.otherCostsBudgetDiference = otherCostsBudgetDiference;
    }

    public double getFixedCostsBudgetTotal() {
        return fixedCostsBudgetTotal;
    }

    public void setFixedCostsBudgetTotal(double fixedCostsBudgetTotal) {
        this.fixedCostsBudgetTotal = fixedCostsBudgetTotal;
    }

    public double getVariableCostsBudgetTotal() {
        return variableCostsBudgetTotal;
    }

    public void setVariableCostsBudgetTotal(double variableCostsBudgetTotal) {
        this.variableCostsBudgetTotal = variableCostsBudgetTotal;
    }

    public double getOtherCostsBudgetTotal() {
        return otherCostsBudgetTotal;
    }

    public void setOtherCostsBudgetTotal(double otherCostsBudgetTotal) {
        this.otherCostsBudgetTotal = otherCostsBudgetTotal;
    }

    public double getAllEgressCategoriesTotal() {
        return allEgressCategoriesTotal;
    }

    public void setAllEgressCategoriesTotal(double allEgressCategoriesTotal) {
        this.allEgressCategoriesTotal = allEgressCategoriesTotal;
    }
}
