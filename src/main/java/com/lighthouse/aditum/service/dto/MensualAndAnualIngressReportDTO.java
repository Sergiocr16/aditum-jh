package com.lighthouse.aditum.service.dto;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MensualAndAnualIngressReportDTO implements Serializable {

    private List<SumChargeDTO> maintenanceIngress;
    private List<SumChargeDTO> extraOrdinaryIngress;
    private List<SumChargeDTO> commonAreasIngress;
    private List<SumChargeDTO> otherIngress;

    private int maintenanceIngressTotal;
    private int extraordinaryIngressTotal;
    private int commonAreasIngressTotal;
    private int otherIngressTotal;

    private double maintenanceIngressPercentage = 0;
    private double extraordinaryIngressPercentage = 0;
    private double commonAreasIngressPercentage = 0;
    private double otherIngressPercentage = 0;

    private int allIngressCategoriesTotal;

    public List<SumChargeDTO> getSumChargeIngress(Page<ChargeDTO> ingress) {
        List<SumChargeDTO> finalList = new ArrayList<>();
        for (int i =0;ingress.getContent().size()>i;i++) {
            ChargeDTO item = ingress.getContent().get(i);
            int total =  ingress.getContent().stream().filter(o -> o.getConcept().toUpperCase().equals(item.getConcept().toUpperCase())).mapToInt(o -> Integer.parseInt(o.getAmmount())).sum();
            SumChargeDTO object = new SumChargeDTO(ingress.getContent().get(i).getConcept(), total);
            if(finalList.stream().filter(o -> o.getConcept().toUpperCase().equals(item.getConcept().toUpperCase())).count()==0){
                finalList.add(object);
            }

        }
       return finalList;
    }


    public List<SumChargeDTO> getMaintenanceIngress() {
        return maintenanceIngress;
    }

    public void setMaintenanceIngress(List<SumChargeDTO> maintenanceIngress) {
        this.maintenanceIngress = maintenanceIngress;
    }

    public List<SumChargeDTO> getExtraOrdinaryIngress() {
        return extraOrdinaryIngress;
    }

    public void setExtraOrdinaryIngress(List<SumChargeDTO> extraOrdinaryIngress) {
        this.extraOrdinaryIngress = extraOrdinaryIngress;
    }

    public List<SumChargeDTO> getCommonAreasIngress() {
        return commonAreasIngress;
    }

    public void setCommonAreasIngress(List<SumChargeDTO> commonAreasIngress) {
        this.commonAreasIngress = commonAreasIngress;
    }

    public List<SumChargeDTO> getOtherIngress() {
        return otherIngress;
    }

    public void setOtherIngress(List<SumChargeDTO> otherIngress) {
        this.otherIngress = otherIngress;
    }


    public void setIngressCategoryTotal(List<SumChargeDTO> list, int type) {
        int total = 0;
        for(int i=0;i<list.size();i++){
            total = total + list.get(i).getTotal();
        }
        switch (type){
            case 1:
                this.setMaintenanceIngressTotal(total);
                break;
            case 2:
                this.setExtraordinaryIngressTotal(total);
                break;
            case 3:
                this.setCommonAreasIngressTotal(total);
                break;
            case 4:
                this.setOtherIngressTotal(total);
                break;

        }

    }

    public int getMaintenanceIngressTotal() {
        return maintenanceIngressTotal;
    }

    public void setMaintenanceIngressTotal(int maintenanceIngressTotal) {
        this.maintenanceIngressTotal = maintenanceIngressTotal;
    }

    public int getExtraordinaryIngressTotal() {
        return extraordinaryIngressTotal;
    }

    public void setExtraordinaryIngressTotal(int extraordinaryIngressTotal) {
        this.extraordinaryIngressTotal = extraordinaryIngressTotal;
    }

    public int getCommonAreasIngressTotal() {
        return commonAreasIngressTotal;
    }

    public void setCommonAreasIngressTotal(int commonAreasIngressTotal) {
        this.commonAreasIngressTotal = commonAreasIngressTotal;
    }

    public int getOtherIngressTotal() {
        return otherIngressTotal;
    }

    public void setOtherIngressTotal(int otherIngressTotal) {
        this.otherIngressTotal = otherIngressTotal;
    }

    public double getMaintenanceIngressPercentage() {
        return maintenanceIngressPercentage;
    }

    public void setMaintenanceIngressPercentage(double percentage) {
        this.maintenanceIngressPercentage = this.maintenanceIngressPercentage + percentage;
    }

    public double getExtraordinaryIngressPercentage() {
        return extraordinaryIngressPercentage;
    }

    public void setExtraordinaryIngressPercentage(double percentage) {
        this.extraordinaryIngressPercentage = this.extraordinaryIngressPercentage + percentage;
    }

    public double getCommonAreasIngressPercentage() {
        return commonAreasIngressPercentage;
    }

    public void setCommonAreasIngressPercentage(double percentage) {
        this.commonAreasIngressPercentage = this.commonAreasIngressPercentage + percentage;
    }

    public double getOtherIngressPercentage() {
        return otherIngressPercentage;
    }

    public void setOtherIngressPercentage(double percentage) {
        this.otherIngressPercentage = this.otherIngressPercentage + percentage;
    }

    public int getAllIngressCategoriesTotal() {
        return allIngressCategoriesTotal;
    }

    public void setAllIngressCategoriesTotal() {
        int totalIngress = this.getMaintenanceIngressTotal() + this.getExtraordinaryIngressTotal() + this.getCommonAreasIngressTotal() + this.getOtherIngressTotal();
        this.allIngressCategoriesTotal = totalIngress;
    }

    public void setPercetagePerCategory() {
       for(int i=0;i<this.getMaintenanceIngress().size();i++){
           double percentage = (this.getMaintenanceIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
           this.getMaintenanceIngress().get(i).setPercentage(percentage);
           this.setMaintenanceIngressPercentage(percentage);
       }
        for(int i=0;i<this.getExtraOrdinaryIngress().size();i++){
            double percentage = (this.getExtraOrdinaryIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getExtraOrdinaryIngress().get(i).setPercentage(percentage);
            this.setExtraordinaryIngressPercentage(percentage);
        }
        for(int i=0;i<this.getCommonAreasIngress().size();i++){
            double percentage = (this.getCommonAreasIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getCommonAreasIngress().get(i).setPercentage(percentage);
            this.setCommonAreasIngressPercentage(percentage);
        }
        for(int i=0;i<this.getOtherIngress().size();i++){
            double percentage = (this.getOtherIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getOtherIngress().get(i).setPercentage(percentage);
            this.setOtherIngressPercentage(percentage);
        }

    }
}
