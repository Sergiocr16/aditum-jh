package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.PresupuestoService;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MensualIngressReportDTO implements Serializable {

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

    private int maintenanceBudget;
    private int extraordinaryBudget;
    private int commonAreasBudget;
    private int otherBudget;
    private int totalBudget;

    private int maintenanceBudgetDiference;
    private int extraordinaryBudgetDiference;
    private int commonAreasBudgetDiference;
    private int otherBudgetDiference;
    private int totalBudgetDiference;

    private int allIngressCategoriesTotal;

    public List<SumChargeDTO> getSumChargeIngress(List<ChargeDTO> ingress) {
        List<SumChargeDTO> finalList = new ArrayList<>();

        for (int i =0;ingress.size()>i;i++) {
            ChargeDTO item = ingress.get(i);
            int total =  ingress.stream().filter(o -> o.getConcept().toUpperCase().equals(item.getConcept().toUpperCase())).mapToInt(o -> Integer.parseInt(o.getAmmount())).sum();
            SumChargeDTO object = new SumChargeDTO(ingress.get(i).getConcept(), total);
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




    public int getAllIngressCategoriesTotal() {
        return allIngressCategoriesTotal;
    }

    public void setAllIngressCategoriesTotal() {
        int totalIngress = this.getMaintenanceIngressTotal() + this.getExtraordinaryIngressTotal() + this.getCommonAreasIngressTotal() + this.getOtherIngressTotal();
        this.allIngressCategoriesTotal = totalIngress;
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

    public int getMaintenanceBudget() {
        return maintenanceBudget;
    }

    public void setMaintenanceBudget(int maintenanceBudget) {
        this.maintenanceBudget = maintenanceBudget;
    }

    public int getExtraordinaryBudget() {
        return extraordinaryBudget;
    }

    public void setExtraordinaryBudget(int extraordinaryBudget) {
        this.extraordinaryBudget = extraordinaryBudget;
    }

    public int getCommonAreasBudget() {
        return commonAreasBudget;
    }

    public void setCommonAreasBudget(int commonAreasBudget) {
        this.commonAreasBudget = commonAreasBudget;
    }

    public int getOtherBudget() {
        return otherBudget;
    }

    public void setOtherBudget(int otherBudget) {
        this.otherBudget = otherBudget;
    }

    public double getMaintenanceIngressPercentage() {
        return maintenanceIngressPercentage;
    }

    public void setMaintenanceIngressPercentage(double percentage) {
        this.maintenanceIngressPercentage += percentage;
    }

    public double getExtraordinaryIngressPercentage() {
        return extraordinaryIngressPercentage;
    }

    public void setExtraordinaryIngressPercentage(double percentage) {
        this.extraordinaryIngressPercentage += percentage;
    }

    public double getCommonAreasIngressPercentage() {
        return commonAreasIngressPercentage;
    }

    public void setCommonAreasIngressPercentage(double percentage) {
        this.commonAreasIngressPercentage += percentage;
    }

    public double getOtherIngressPercentage() {
        return otherIngressPercentage;
    }

    public void setOtherIngressPercentage(double percentage) {
        this.otherIngressPercentage += percentage;
    }

    public int getTotalBudget() {
        return totalBudget;
    }

    public void setAllIngressCategoriesBudgetTotal() {
        this.totalBudget = this.getOtherBudget() + this.getMaintenanceBudget() + this.getCommonAreasBudget() + this.getExtraordinaryBudget();
        this.setCategoriesBudgetDiference();
    }
    public void setCategoriesBudgetDiference() {
        this.setMaintenanceBudgetDiference(this.getMaintenanceBudget() - this.getMaintenanceIngressTotal());
        this.setExtraordinaryBudgetDiference(this.getExtraordinaryBudget() - this.getExtraordinaryIngressTotal());
        this.setCommonAreasBudgetDiference(this.getCommonAreasBudget() - this.getCommonAreasIngressTotal());
        this.setOtherBudgetDiference(this.getOtherBudget() - this.getOtherIngressTotal());
        this.setTotalBudgetDiference(this.getTotalBudget() - this.getAllIngressCategoriesTotal());
    }

    public int getMaintenanceBudgetDiference() {
        return maintenanceBudgetDiference;
    }

    public void setMaintenanceBudgetDiference(int maintenanceBudgetDiference) {
        this.maintenanceBudgetDiference = maintenanceBudgetDiference;
    }

    public int getExtraordinaryBudgetDiference() {
        return extraordinaryBudgetDiference;
    }

    public void setExtraordinaryBudgetDiference(int extraordinaryBudgetDiference) {
        this.extraordinaryBudgetDiference = extraordinaryBudgetDiference;
    }

    public int getCommonAreasBudgetDiference() {
        return commonAreasBudgetDiference;
    }

    public void setCommonAreasBudgetDiference(int commonAreasBudgetDiference) {
        this.commonAreasBudgetDiference = commonAreasBudgetDiference;
    }

    public int getOtherBudgetDiference() {
        return otherBudgetDiference;
    }

    public void setOtherBudgetDiference(int otherBudgetDiference) {
        this.otherBudgetDiference = otherBudgetDiference;
    }

    public int getTotalBudgetDiference() {
        return totalBudgetDiference;
    }

    public void setTotalBudgetDiference(int totalBudgetDiference) {
        this.totalBudgetDiference = totalBudgetDiference;
    }
}
