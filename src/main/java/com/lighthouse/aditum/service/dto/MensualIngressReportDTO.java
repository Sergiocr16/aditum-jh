package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.PresupuestoService;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;

public class MensualIngressReportDTO implements Serializable {
    private Locale locale = new Locale("es", "CR");
    private List<SumChargeDTO> maintenanceIngress;
    private List<SumChargeDTO> extraOrdinaryIngress;
    private List<SumChargeDTO> commonAreasIngress;
    private List<SumChargeDTO> otherIngress;
    private List<SumChargeDTO> multaIngress;
    private List<SumChargeDTO> waterChargeIngress;

    private double maintenanceIngressTotal = 0;
    private double extraordinaryIngressTotal = 0;
    private double commonAreasIngressTotal = 0;
    private double otherIngressTotal = 0;
    private double multaIngressTotal = 0;
    private double waterChargeIngressTotal = 0;

    private String maintenanceIngressTotalFormatted = "0";
    private String extraordinaryIngressTotalFormatted = "0";
    private String commonAreasIngressTotalFormatted = "0";
    private String otherIngressTotalFormatted = "0";
    private String multaIngressTotalFormatted = "0";
    private String waterChargeIngressTotalFormatted = "0";


    private double maintenanceIngressPercentage = 0;
    private double extraordinaryIngressPercentage = 0;
    private double commonAreasIngressPercentage = 0;
    private double otherIngressPercentage = 0;
    private double multaIngressPercentage = 0;
    private double waterChargeIngressPercentage = 0;

    private double maintenanceBudget = 0;
    private double extraordinaryBudget = 0;
    private double commonAreasBudget = 0;
    private double otherBudget = 0;
    private double multaBudget = 0;
    private double waterChargeBudget = 0;

    private double totalBudget = 0;

    private String maintenanceBudgetFormatted = "0";
    private String extraordinaryBudgetFormatted = "0";
    private String commonAreasBudgetFormatted = "0";
    private String otherBudgetFormatted = "0";
    private String multaBudgetFormatted = "0";
    private String waterChargeBudgetFormatted = "0";

    private String totalBudgetFormatted = "0";

    private double maintenanceBudgetDiference = 0;
    private double extraordinaryBudgetDiference = 0;
    private double commonAreasBudgetDiference = 0;
    private double otherBudgetDiference = 0;
    private double multaBudgetDiference = 0;
    private double waterChargeBudgetDiference = 0;

    private double totalBudgetDiference = 0;


    private double allIngressCategoriesTotal = 0;

    private String maintenanceBudgetDiferenceFormatted = "0";
    private String extraordinaryBudgetDiferenceFormatted = "0";
    private String commonAreasBudgetDiferenceFormatted = "0";
    private String otherBudgetDiferenceFormatted = "0";
    private String multaBudgetDiferenceFormatted = "0";
    private String waterChargeBudgetDiferenceFormatted = "0";

    private String totalBudgetDiferenceFormatted = "0";

    private String allIngressCategoriesTotalFormatted = "0";

    public MensualIngressReportDTO() {

    }

    public List<SumChargeDTO> getSumChargeIngress(String currency, List<ChargeDTO> ingress) {
        List<SumChargeDTO> finalList = new ArrayList<>();

        for (int i = 0; ingress.size() > i; i++) {
            ChargeDTO item = ingress.get(i);
            double total = ingress.stream().filter(o -> o.getConcept().toUpperCase().equals(item.getConcept().toUpperCase())).mapToDouble(o -> o.getAbonado()).sum();
            SumChargeDTO object = new SumChargeDTO(currency, ingress.get(i).getConcept(), total);
            if (finalList.stream().filter(o -> o.getConcept().toUpperCase().equals(item.getConcept().toUpperCase())).count() == 0) {
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

    public void setMaintenanceIngressTotal(String currency, double maintenanceIngressTotal) {
        this.maintenanceIngressTotal = maintenanceIngressTotal;
        this.setMaintenanceIngressTotalFormatted(formatMoney(currency, maintenanceIngressTotal));
    }

    public double getExtraordinaryIngressTotal() {
        return extraordinaryIngressTotal;
    }

    public void setExtraordinaryIngressTotal(String currency, double extraordinaryIngressTotal) {
        this.extraordinaryIngressTotal = extraordinaryIngressTotal;
        this.setExtraordinaryIngressTotalFormatted(formatMoney(currency, extraordinaryIngressTotal));
    }

    public double getCommonAreasIngressTotal() {
        return commonAreasIngressTotal;
    }

    public void setCommonAreasIngressTotal(String currency, double commonAreasIngressTotal) {
        this.commonAreasIngressTotal = commonAreasIngressTotal;
        this.setCommonAreasIngressTotalFormatted(formatMoney(currency, commonAreasIngressTotal));
    }

    public double getOtherIngressTotal() {
        return otherIngressTotal;
    }

    public void setOtherIngressTotal(String currency, double otherIngressTotal) {
        this.otherIngressTotal = otherIngressTotal;
        this.setOtherIngressTotalFormatted(formatMoney(currency, otherIngressTotal));
    }


    public double getAllIngressCategoriesTotal() {
        return allIngressCategoriesTotal;
    }

    public void setAllIngressCategoriesTotal(String currency) {
        double totalIngress = this.getMaintenanceIngressTotal() + this.getExtraordinaryIngressTotal() + this.getCommonAreasIngressTotal() + this.getOtherIngressTotal() + this.getMultaIngressTotal() +this.getWaterChargeIngressTotal();
        this.allIngressCategoriesTotal = totalIngress;
        this.setAllIngressCategoriesTotalFormatted(formatMoney(currency, totalIngress));
    }

    public void setIngressCategoryTotal(String currency, List<SumChargeDTO> list, int type) {
        double total = 0;
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i).getTotal();
        }
        switch (type) {
            case 1:
                this.setMaintenanceIngressTotal(currency, total);
                break;
            case 2:
                this.setExtraordinaryIngressTotal(currency, total);
                break;
            case 3:
                this.setCommonAreasIngressTotal(currency, total);
                break;
            case 4:
                this.setOtherIngressTotal(currency, total);
                break;
            case 5:
                this.setMultaIngressTotal(currency, total);
                break;
            case 6:
                this.setWaterChargeIngressTotal(currency, total);
                break;

        }

    }

    public void setPercetagePerCategory() {
        for (int i = 0; i < this.getMaintenanceIngress().size(); i++) {
            double percentage = (this.getMaintenanceIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getMaintenanceIngress().get(i).setPercentage(percentage);
            this.setMaintenanceIngressPercentage(percentage);
        }
        for (int i = 0; i < this.getExtraOrdinaryIngress().size(); i++) {
            double percentage = (this.getExtraOrdinaryIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getExtraOrdinaryIngress().get(i).setPercentage(percentage);
            this.setExtraordinaryIngressPercentage(percentage);
        }
        for (int i = 0; i < this.getCommonAreasIngress().size(); i++) {
            double percentage = (this.getCommonAreasIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getCommonAreasIngress().get(i).setPercentage(percentage);
            this.setCommonAreasIngressPercentage(percentage);
        }
        for (int i = 0; i < this.getOtherIngress().size(); i++) {
            double percentage = (this.getOtherIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getOtherIngress().get(i).setPercentage(percentage);
            this.setOtherIngressPercentage(percentage);
        }
        for (int i = 0; i < this.getMultaIngress().size(); i++) {
            double percentage = (this.getMultaIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getMultaIngress().get(i).setPercentage(percentage);
            this.setMultaIngressPercentage(percentage);
        }
        for (int i = 0; i < this.getWaterChargeIngress().size(); i++) {
            double percentage = (this.getWaterChargeIngress().get(i).getTotal() * 100.0f) / this.getAllIngressCategoriesTotal();
            this.getWaterChargeIngress().get(i).setPercentage(percentage);
            this.setWaterChargeIngressPercentage(percentage);
        }

    }

    public double getMaintenanceBudget() {
        return maintenanceBudget;
    }

    public void setMaintenanceBudget(String currency, double maintenanceBudget) {
        this.maintenanceBudget = maintenanceBudget;
        this.setMaintenanceBudgetFormatted(formatMoney(currency, maintenanceBudget));
    }

    public double getExtraordinaryBudget() {
        return extraordinaryBudget;
    }

    public void setExtraordinaryBudget(String currency, double extraordinaryBudget) {
        this.extraordinaryBudget = extraordinaryBudget;
        this.setExtraordinaryBudgetFormatted(formatMoney(currency, extraordinaryBudget));
    }

    public double getCommonAreasBudget() {
        return commonAreasBudget;
    }

    public void setCommonAreasBudget(String currency, double commonAreasBudget) {
        this.commonAreasBudget = commonAreasBudget;
        this.setCommonAreasBudgetFormatted(formatMoney(currency, commonAreasBudget));
    }

    public double getOtherBudget() {
        return otherBudget;
    }

    public void setOtherBudget(String currency, double otherBudget) {
        this.otherBudget = otherBudget;
        this.setOtherBudgetFormatted(formatMoney(currency, otherBudget));
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

    public void setAllIngressCategoriesBudgetTotal(String currency) {
        this.totalBudget = this.getOtherBudget() + this.getMaintenanceBudget() + this.getCommonAreasBudget() + this.getExtraordinaryBudget();
        this.setTotalBudgetFormatted(formatMoney(currency, this.totalBudget));
        this.setCategoriesBudgetDiference(currency);
    }

    public void setCategoriesBudgetDiference(String currency) {
        this.setMaintenanceBudgetDiference(this.getMaintenanceIngressTotal() - this.getMaintenanceBudget());
        this.setMultaBudgetDiference(this.getMultaIngressTotal() - this.getMultaBudget());
        this.setExtraordinaryBudgetDiference(this.getExtraordinaryIngressTotal() - this.getExtraordinaryBudget());
        this.setCommonAreasBudgetDiference(this.getCommonAreasIngressTotal() - this.getCommonAreasBudget());
        this.setOtherBudgetDiference(this.getOtherIngressTotal() - this.getOtherBudget());
        this.setWaterChargeBudgetDiference(this.getWaterChargeIngressTotal() - this.getWaterChargeBudget());
        this.setTotalBudgetDiference(this.getAllIngressCategoriesTotal() - this.getTotalBudget());

        this.setMaintenanceBudgetDiferenceFormatted(formatMoney(currency, this.getMaintenanceBudgetDiference()));
        this.setMultaBudgetDiferenceFormatted(formatMoney(currency, this.getMultaBudgetDiference()));
        this.setExtraordinaryBudgetDiferenceFormatted(formatMoney(currency, this.getExtraordinaryBudgetDiference()));
        this.setCommonAreasBudgetDiferenceFormatted(formatMoney(currency, this.getCommonAreasBudgetDiference()));
        this.setOtherBudgetDiferenceFormatted(formatMoney(currency, this.getOtherBudgetDiference()));
        this.setWaterChargeBudgetDiferenceFormatted(formatMoney(currency, this.getWaterChargeBudgetDiference()));
        this.setTotalBudgetDiferenceFormatted(formatMoney(currency, this.getTotalBudgetDiference()));


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

    public List<SumChargeDTO> getMultaIngress() {
        return multaIngress;
    }

    public void setMultaIngress(List<SumChargeDTO> multaIngress) {
        this.multaIngress = multaIngress;
    }

    public List<SumChargeDTO> getWaterChargeIngress() {
        return waterChargeIngress;
    }

    public void setWaterChargeIngress(List<SumChargeDTO> waterChargeIngress) {
        this.waterChargeIngress = waterChargeIngress;
    }

    public double getMultaIngressTotal() {
        return multaIngressTotal;
    }

    public void setMultaIngressTotal(String currency, double multaIngressTotal) {
        this.multaIngressTotal = multaIngressTotal;
        this.setMultaIngressTotalFormatted(formatMoney(currency, multaIngressTotal));
    }

    public double getWaterChargeIngressTotal() {
        return waterChargeIngressTotal;
    }

    public void setWaterChargeIngressTotal(String currency, double waterChargeIngressTotal) {
        this.waterChargeIngressTotal = waterChargeIngressTotal;
        this.setWaterChargeIngressTotalFormatted(formatMoney(currency, waterChargeIngressTotal));
    }

    public String getMultaIngressTotalFormatted() {
        return multaIngressTotalFormatted;
    }

    public void setMultaIngressTotalFormatted(String multaIngressTotalFormatted) {
        this.multaIngressTotalFormatted = multaIngressTotalFormatted;
    }

    public String getWaterChargeIngressTotalFormatted() {
        return waterChargeIngressTotalFormatted;
    }

    public void setWaterChargeIngressTotalFormatted(String waterChargeIngressTotalFormatted) {
        this.waterChargeIngressTotalFormatted = waterChargeIngressTotalFormatted;
    }

    public double getMultaIngressPercentage() {
        return multaIngressPercentage;
    }

    public void setMultaIngressPercentage(double multaIngressPercentage) {
        this.multaIngressPercentage = multaIngressPercentage;
    }

    public double getWaterChargeIngressPercentage() {
        return waterChargeIngressPercentage;
    }

    public void setWaterChargeIngressPercentage(double waterChargeIngressPercentage) {
        this.waterChargeIngressPercentage = waterChargeIngressPercentage;
    }

    public double getMultaBudget() {
        return multaBudget;
    }

    public void setMultaBudget(String currency, double multaBudget) {
        this.multaBudget = multaBudget;
        this.setMultaBudgetFormatted(formatMoney(currency, multaBudget));
    }

    public double getWaterChargeBudget() {
        return waterChargeBudget;
    }

    public void setWaterChargeBudget(String currency, double waterChargeBudget) {
        this.waterChargeBudget = waterChargeBudget;
        this.setWaterChargeBudgetFormatted(formatMoney(currency, waterChargeBudget));
    }

    public String getMultaBudgetFormatted() {
        return multaBudgetFormatted;
    }

    public void setMultaBudgetFormatted(String multaBudgetFormatted) {
        this.multaBudgetFormatted = multaBudgetFormatted;
    }

    public String getWaterChargeBudgetFormatted() {
        return waterChargeBudgetFormatted;
    }

    public void setWaterChargeBudgetFormatted(String waterChargeBudgetFormatted) {
        this.waterChargeBudgetFormatted = waterChargeBudgetFormatted;
    }

    public double getMultaBudgetDiference() {
        return multaBudgetDiference;
    }

    public void setMultaBudgetDiference(double multaBudgetDiference) {
        this.multaBudgetDiference = multaBudgetDiference;
    }

    public double getWaterChargeBudgetDiference() {
        return waterChargeBudgetDiference;
    }

    public void setWaterChargeBudgetDiference(double waterChargeBudgetDiference) {
        this.waterChargeBudgetDiference = waterChargeBudgetDiference;
    }

    public String getMultaBudgetDiferenceFormatted() {
        return multaBudgetDiferenceFormatted;
    }

    public void setMultaBudgetDiferenceFormatted(String multaBudgetDiferenceFormatted) {
        this.multaBudgetDiferenceFormatted = multaBudgetDiferenceFormatted;
    }

    public String getWaterChargeBudgetDiferenceFormatted() {
        return waterChargeBudgetDiferenceFormatted;
    }

    public void setWaterChargeBudgetDiferenceFormatted(String waterChargeBudgetDiferenceFormatted) {
        this.waterChargeBudgetDiferenceFormatted = waterChargeBudgetDiferenceFormatted;
    }
}
