package com.lighthouse.aditum.service.dto;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class MensualAndAnualEgressReportDTO {
    private List<SumCategoryEgressDTO> fixedCosts = new ArrayList<>();
    private List<SumCategoryEgressDTO> variableCosts = new ArrayList<>();
    private List<SumCategoryEgressDTO> otherCosts = new ArrayList<>();
    private List<EgressDTO> egressList;
    private int fixedCostsTotal;
    private int variableCostsTotal;
    private int otherCostsTotal;
    private double fixedCostsPercentage;
    private double variableCostsPercentage;
    private double otherCostsPercentage;

    public void setCategoriesNames(Page<EgressDTO> egress,Page<EgressCategoryDTO> egressCategories){
        for (int i =0;egress.getContent().size()>i;i++) {
            for (int j =0;egressCategories.getContent().size()>j;j++){
                Long egressCategory = Long.parseLong(egress.getContent().get(i).getCategory());
                Long categoryId = egressCategories.getContent().get(j).getId();
                if(Long.parseLong(egress.getContent().get(i).getCategory())==egressCategories.getContent().get(j).getId()){
                    egress.getContent().get(i).setCategoryName(egressCategories.getContent().get(j).getCategory());
                    egress.getContent().get(i).setGroupName(egressCategories.getContent().get(j).getGroup());
                }
            }
            Page<EgressDTO> egress3 = egress;
            SumCategoryEgressDTO object = new SumCategoryEgressDTO(egress.getContent().get(i).getCategoryName());
            EgressDTO egressPrueba = egress.getContent().get(i);
            switch (egress.getContent().get(i).getGroupName()){
                case "Gastos fijos":
                    if(fixedCosts.stream().filter(o -> o.getCategory().toUpperCase().equals(object.getCategory().toUpperCase())).count()==0) {
                        fixedCosts.add(object);
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

    public List<SumCategoryEgressDTO> getSumCategoryEgress( List<SumCategoryEgressDTO> sumCategoryEgress,String group,int totalIngress){
        List<SumCategoryEgressDTO> finalList = new ArrayList<>();
        for (int i =0;sumCategoryEgress.size()>i;i++) {
            SumCategoryEgressDTO item = sumCategoryEgress.get(i);
                this.setSumEgressListPerSumCategoryEgressDTO(item,group,totalIngress);
        }
        return sumCategoryEgress;
    }


public void setSumEgressListPerSumCategoryEgressDTO (SumCategoryEgressDTO sumCategory,String group,int totalIngress){
    List<SumEgressDTO> sumEgreesList = new ArrayList<>();
    for (int i = 0; i < egressList.size(); i++) {
            EgressDTO egrees = egressList.get(i);
            if(egrees.getCategoryName().equals(sumCategory.getCategory()) && egrees.getGroupName().equals(group)){
                int totalByCategory =  egressList.stream().filter(o -> o.getCategoryName().equals(sumCategory.getCategory())&&egrees.getGroupName().equals(group)).mapToInt(o -> Integer.parseInt(o.getTotal())).sum();
                SumEgressDTO sumEgressDTO = new SumEgressDTO(egrees.getConcept());
                sumCategory.setTotal(totalByCategory);
                double percentageByCategory = (totalByCategory * 100.0f) / totalIngress;
                sumCategory.setPercentage(percentageByCategory);
                 if(sumEgreesList.stream().filter(o -> o.getConcept().toUpperCase().equals(egrees.getConcept().toUpperCase())).count()==0) {
                     int totalbyEgress =  egressList.stream().filter(o -> o.getConcept().toUpperCase().equals(egrees.getConcept().toUpperCase())).mapToInt(o -> Integer.parseInt(o.getTotal())).sum();
                     sumEgressDTO.setTotal(totalbyEgress);
                     double percentagebyEgress = (totalbyEgress * 100.0f) / totalIngress;
                     sumEgressDTO.setPercentage(percentagebyEgress);
                     sumEgreesList.add(sumEgressDTO);

                 }
            }

        }
        sumCategory.setEgressList(sumEgreesList);
    }

    public void setGroupsTotal(List<SumCategoryEgressDTO> list,int type,int totalIngress){
        int total = 0;
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

    public int getFixedCostsTotal() {
        return fixedCostsTotal;
    }

    public void setFixedCostsTotal(int fixedCostsTotal) {
        this.fixedCostsTotal = fixedCostsTotal;
    }

    public int getVariableCostsTotal() {
        return variableCostsTotal;
    }

    public void setVariableCostsTotal(int variableCostsTotal) {
        this.variableCostsTotal = variableCostsTotal;
    }

    public int getOtherCostsTotal() {
        return otherCostsTotal;
    }

    public void setOtherCostsTotal(int otherCostsTotal) {
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
}
