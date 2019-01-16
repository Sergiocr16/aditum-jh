package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.PresupuestoService;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MensualIngressReportDTO implements Serializable {
    private Locale locale = new Locale("es", "CR");
    private List<SumChargeDTO> maintenanceIngress;
    private List<SumChargeDTO> extraOrdinaryIngress;
    private List<SumChargeDTO> commonAreasIngress;
    private List<SumChargeDTO> otherIngress;

    private double maintenanceIngressTotal = 0;
    private double extraordinaryIngressTotal = 0;
    private double commonAreasIngressTotal = 0;
    private double otherIngressTotal = 0;

    private String maintenanceIngressTotalFormatted = "0";
    private String extraordinaryIngressTotalFormatted = "0";
    private String commonAreasIngressTotalFormatted = "0";
    private String otherIngressTotalFormatted  = "0";


    private double maintenanceIngressPercentage = 0;
    private double extraordinaryIngressPercentage = 0;
    private double commonAreasIngressPercentage = 0;
    private double otherIngressPercentage = 0;

    private double maintenanceBudget = 0;
    private double extraordinaryBudget = 0;
    private double commonAreasBudget = 0;
    private double otherBudget = 0;

    private double totalBudget = 0;

    private String maintenanceBudgetFormatted = "0";
    private String extraordinaryBudgetFormatted = "0";
    private String commonAreasBudgetFormatted = "0";
    private String otherBudgetFormatted = "0";

    private String totalBudgetFormatted = "0";

    private double maintenanceBudgetDiference = 0;
    private double extraordinaryBudgetDiference = 0;
    private double commonAreasBudgetDiference = 0;
    private double otherBudgetDiference = 0;
    private double totalBudgetDiference = 0;

    private double allIngressCategoriesTotal = 0;

    private String maintenanceBudgetDiferenceFormatted = "0";
    private String extraordinaryBudgetDiferenceFormatted = "0";
    private String commonAreasBudgetDiferenceFormatted = "0";
    private String otherBudgetDiferenceFormatted = "0";
    private String totalBudgetDiferenceFormatted = "0";

    private String allIngressCategoriesTotalFormatted = "0";

     public MensualIngressReportDTO(){

     }

    public List<SumChargeDTO> getSumChargeIngress(List<ChargeDTO> ingress) {
        List<SumChargeDTO> finalList = new ArrayList<>();

        for (int i =0;ingress.size()>i;i++) {
            ChargeDTO item = ingress.get(i);
            double total =  ingress.stream().filter(o -> o.getConcept().toUpperCase().equals(item.getConcept().toUpperCase())).mapToDouble(o -> Double.parseDouble(o.getAmmount())).sum();
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

    public double getMaintenanceIngressTotal() {
        return maintenanceIngressTotal;
    }

    public void setMaintenanceIngressTotal(double maintenanceIngressTotal) {
        this.maintenanceIngressTotal = maintenanceIngressTotal;
        this.setMaintenanceIngressTotalFormatted(formatMoney(maintenanceIngressTotal));
    }

    public double getExtraordinaryIngressTotal() {
        return extraordinaryIngressTotal;
    }

    public void setExtraordinaryIngressTotal(double extraordinaryIngressTotal) {
        this.extraordinaryIngressTotal = extraordinaryIngressTotal;
        this.setExtraordinaryIngressTotalFormatted(formatMoney(extraordinaryIngressTotal));
    }

    public double getCommonAreasIngressTotal() {
        return commonAreasIngressTotal;
    }

    public void setCommonAreasIngressTotal(double commonAreasIngressTotal) {
        this.commonAreasIngressTotal = commonAreasIngressTotal;
        this.setCommonAreasIngressTotalFormatted(formatMoney(commonAreasIngressTotal));
    }

    public double getOtherIngressTotal() {
        return otherIngressTotal;
    }

    public void setOtherIngressTotal(double otherIngressTotal) {
         this.otherIngressTotal = otherIngressTotal;
        this.setOtherIngressTotalFormatted(formatMoney(otherIngressTotal));
    }

    private String formatMoney(double ammount){
        DecimalFormat format = new DecimalFormat("₡#,##0.00;₡-#,##0.00");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        String total = format.format(ammount);
        return total;
    }


    public double getAllIngressCategoriesTotal() {
        return allIngressCategoriesTotal;
    }

    public void setAllIngressCategoriesTotal() {
        double totalIngress = this.getMaintenanceIngressTotal() + this.getExtraordinaryIngressTotal() + this.getCommonAreasIngressTotal() + this.getOtherIngressTotal();
        this.allIngressCategoriesTotal = totalIngress;
        this.setAllIngressCategoriesTotalFormatted(formatMoney(totalIngress));
    }

    public void setIngressCategoryTotal(List<SumChargeDTO> list, int type) {
        double total = 0;
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

    public double getMaintenanceBudget() {
        return maintenanceBudget;
    }

    public void setMaintenanceBudget(double maintenanceBudget) {
         this.maintenanceBudget = maintenanceBudget;
         this.setMaintenanceBudgetFormatted(formatMoney(maintenanceBudget));
    }

    public double getExtraordinaryBudget() {
        return extraordinaryBudget;
    }

    public void setExtraordinaryBudget(double extraordinaryBudget) {
         this.extraordinaryBudget = extraordinaryBudget;
        this.setExtraordinaryBudgetFormatted(formatMoney(extraordinaryBudget));
    }

    public double getCommonAreasBudget() {
        return commonAreasBudget;
    }

    public void setCommonAreasBudget(double commonAreasBudget) {
         this.commonAreasBudget = commonAreasBudget;
         this.setCommonAreasBudgetFormatted(formatMoney(commonAreasBudget));
    }

    public double getOtherBudget() {
        return otherBudget;
    }

    public void setOtherBudget(double otherBudget) {
         this.otherBudget = otherBudget;
         this.setOtherBudgetFormatted(formatMoney(otherBudget));
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

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setAllIngressCategoriesBudgetTotal() {
        this.totalBudget = this.getOtherBudget() + this.getMaintenanceBudget() + this.getCommonAreasBudget() + this.getExtraordinaryBudget();
        this.setTotalBudgetFormatted(formatMoney(this.totalBudget));
        this.setCategoriesBudgetDiference();
    }
    public void setCategoriesBudgetDiference() {
        this.setMaintenanceBudgetDiference( this.getMaintenanceIngressTotal() - this.getMaintenanceBudget());
        this.setExtraordinaryBudgetDiference(this.getExtraordinaryIngressTotal() - this.getExtraordinaryBudget());
        this.setCommonAreasBudgetDiference(this.getCommonAreasIngressTotal() - this.getCommonAreasBudget());
        this.setOtherBudgetDiference(this.getOtherIngressTotal() - this.getOtherBudget());
        this.setTotalBudgetDiference( this.getAllIngressCategoriesTotal() - this.getTotalBudget());

        this.setMaintenanceBudgetDiferenceFormatted(formatMoney(this.getMaintenanceBudgetDiference()));
        this.setExtraordinaryBudgetDiferenceFormatted(formatMoney(this.getExtraordinaryBudgetDiference()));
        this.setCommonAreasBudgetDiferenceFormatted(formatMoney(this.getCommonAreasBudgetDiference()));
        this.setOtherBudgetDiferenceFormatted(formatMoney(this.getOtherBudgetDiference()));
        this.setTotalBudgetDiferenceFormatted(formatMoney(this.getTotalBudgetDiference()));




    }

    public double getMaintenanceBudgetDiference() {
        return maintenanceBudgetDiference;
    }

    public void setMaintenanceBudgetDiference(double maintenanceBudgetDiference) {
        this.maintenanceBudgetDiference = maintenanceBudgetDiference;
    }

    public double getExtraordinaryBudgetDiference() {
        return extraordinaryBudgetDiference;
    }

    public void setExtraordinaryBudgetDiference(double extraordinaryBudgetDiference) {
        this.extraordinaryBudgetDiference = extraordinaryBudgetDiference;
    }

    public double getCommonAreasBudgetDiference() {
        return commonAreasBudgetDiference;
    }

    public void setCommonAreasBudgetDiference(double commonAreasBudgetDiference) {
        this.commonAreasBudgetDiference = commonAreasBudgetDiference;
    }

    public double getOtherBudgetDiference() {
        return otherBudgetDiference;
    }

    public void setOtherBudgetDiference(double otherBudgetDiference) {
        this.otherBudgetDiference = otherBudgetDiference;
    }

    public double getTotalBudgetDiference() {
        return totalBudgetDiference;
    }

    public void setTotalBudgetDiference(double totalBudgetDiference) {
        this.totalBudgetDiference = totalBudgetDiference;
    }

    public String getMaintenanceIngressTotalFormatted() {
        return maintenanceIngressTotalFormatted;
    }

    public void setMaintenanceIngressTotalFormatted(String maintenanceIngressTotalFormatted) {
        this.maintenanceIngressTotalFormatted = maintenanceIngressTotalFormatted;
    }

    public String getExtraordinaryIngressTotalFormatted() {
        return extraordinaryIngressTotalFormatted;
    }

    public void setExtraordinaryIngressTotalFormatted(String extraordinaryIngressTotalFormatted) {
        this.extraordinaryIngressTotalFormatted = extraordinaryIngressTotalFormatted;
    }

    public String getCommonAreasIngressTotalFormatted() {
        return commonAreasIngressTotalFormatted;
    }

    public void setCommonAreasIngressTotalFormatted(String commonAreasIngressTotalFormatted) {
        this.commonAreasIngressTotalFormatted = commonAreasIngressTotalFormatted;
    }

    public String getOtherIngressTotalFormatted() {
        return otherIngressTotalFormatted;
    }

    public void setOtherIngressTotalFormatted(String otherIngressTotalFormatted) {
        this.otherIngressTotalFormatted = otherIngressTotalFormatted;
    }

    public String getMaintenanceBudgetFormatted() {
        return maintenanceBudgetFormatted;
    }

    public void setMaintenanceBudgetFormatted(String maintenanceBudgetFormatted) {
        this.maintenanceBudgetFormatted = maintenanceBudgetFormatted;
    }

    public String getExtraordinaryBudgetFormatted() {
        return extraordinaryBudgetFormatted;
    }

    public void setExtraordinaryBudgetFormatted(String extraordinaryBudgetFormatted) {
        this.extraordinaryBudgetFormatted = extraordinaryBudgetFormatted;
    }

    public String getCommonAreasBudgetFormatted() {
        return commonAreasBudgetFormatted;
    }

    public void setCommonAreasBudgetFormatted(String commonAreasBudgetFormatted) {
        this.commonAreasBudgetFormatted = commonAreasBudgetFormatted;
    }

    public String getOtherBudgetFormatted() {
        return otherBudgetFormatted;
    }

    public void setOtherBudgetFormatted(String otherBudgetFormatted) {
        this.otherBudgetFormatted = otherBudgetFormatted;
    }

    public String getTotalBudgetFormatted() {
        return totalBudgetFormatted;
    }

    public void setTotalBudgetFormatted(String totalBudgetFormatted) {
        this.totalBudgetFormatted = totalBudgetFormatted;
    }

    public String getMaintenanceBudgetDiferenceFormatted() {
        return maintenanceBudgetDiferenceFormatted;
    }

    public void setMaintenanceBudgetDiferenceFormatted(String maintenanceBudgetDiferenceFormatted) {
        this.maintenanceBudgetDiferenceFormatted = maintenanceBudgetDiferenceFormatted;
    }

    public String getExtraordinaryBudgetDiferenceFormatted() {
        return extraordinaryBudgetDiferenceFormatted;
    }

    public void setExtraordinaryBudgetDiferenceFormatted(String extraordinaryBudgetDiferenceFormatted) {
        this.extraordinaryBudgetDiferenceFormatted = extraordinaryBudgetDiferenceFormatted;
    }

    public String getCommonAreasBudgetDiferenceFormatted() {
        return commonAreasBudgetDiferenceFormatted;
    }

    public void setCommonAreasBudgetDiferenceFormatted(String commonAreasBudgetDiferenceFormatted) {
        this.commonAreasBudgetDiferenceFormatted = commonAreasBudgetDiferenceFormatted;
    }

    public String getOtherBudgetDiferenceFormatted() {
        return otherBudgetDiferenceFormatted;
    }

    public void setOtherBudgetDiferenceFormatted(String otherBudgetDiferenceFormatted) {
        this.otherBudgetDiferenceFormatted = otherBudgetDiferenceFormatted;
    }

    public String getTotalBudgetDiferenceFormatted() {
        return totalBudgetDiferenceFormatted;
    }

    public void setTotalBudgetDiferenceFormatted(String totalBudgetDiferenceFormatted) {
        this.totalBudgetDiferenceFormatted = totalBudgetDiferenceFormatted;
    }

    public String getAllIngressCategoriesTotalFormatted() {
        return allIngressCategoriesTotalFormatted;
    }

    public void setAllIngressCategoriesTotalFormatted(String allIngressCategoriesTotalFormatted) {
        this.allIngressCategoriesTotalFormatted = allIngressCategoriesTotalFormatted;
    }
}
