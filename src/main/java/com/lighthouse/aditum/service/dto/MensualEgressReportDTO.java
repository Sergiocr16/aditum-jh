package com.lighthouse.aditum.service.dto;

import org.springframework.data.domain.Page;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;

public class MensualEgressReportDTO {
    private Locale locale = new Locale("es", "CR");
    private List<SumCategoryEgressDTO> fixedCosts = new ArrayList<>();
    private List<SumCategoryEgressDTO> variableCosts = new ArrayList<>();
    private List<SumCategoryEgressDTO> otherCosts = new ArrayList<>();
    private List<EgressDTO> egressList;

    private double allEgressCategoriesTotal = 0;
    private double fixedCostsTotal = 0;
    private double variableCostsTotal = 0;
    private double otherCostsTotal = 0;

    private String allEgressCategoriesTotalFormatted = "0";
    private String fixedCostsTotalFormatted = "0";
    private String variableCostsTotalFormatted = "0";
    private String otherCostsTotalFormatted = "0";

    private double fixedCostsPercentage = 0.0;
    private double variableCostsPercentage = 0.0;
    private double otherCostsPercentage = 0.0;

    private double fixedCostsBudgetTotal = 0;
    private double variableCostsBudgetTotal = 0;
    private double otherCostsBudgetTotal = 0;
    private double totalBudget = 0;

    private String fixedCostsBudgetTotalFormatted = "0";
    private String variableCostsBudgetTotalFormatted = "0";
    private String otherCostsBudgetTotalFormatted = "0";

    private double fixedCostsBudgetDiference = 0;
    private double variableCostsBudgetDiference = 0;
    private double otherCostsBudgetDiference = 0;

    private String fixedCostsBudgetDiferenceFormatted = "0";
    private String variableCostsBudgetDiferenceFormatted = "0";
    private String otherCostsBudgetDiferenceFormatted = "0";

    public MensualEgressReportDTO() {

    }


    public void setCategoriesNames(Page<EgressDTO> egress, Page<EgressCategoryDTO> egressCategories) {
        for (int i = 0; egress.getContent().size() > i; i++) {
            for (int j = 0; egressCategories.getContent().size() > j; j++) {
                if (Long.parseLong(egress.getContent().get(i).getCategory()) == egressCategories.getContent().get(j).getId()) {
                    egress.getContent().get(i).setCategoryName(egressCategories.getContent().get(j).getCategory());
                    egress.getContent().get(i).setGroupName(egressCategories.getContent().get(j).getGroup());
                    egress.getContent().get(i).setCategoryId(egressCategories.getContent().get(j).getId());
                }
            }
            SumCategoryEgressDTO object = new SumCategoryEgressDTO(egress.getContent().get(i).getCategoryId(), egress.getContent().get(i).getCategoryName());

            switch (egress.getContent().get(i).getGroupName()) {
                case "Gastos fijos":
                    if (getFixedCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(object.getCategory().toUpperCase())).count() == 0) {
                        getFixedCosts().add(object);
                    }
                    break;
                case "Gastos variables":
                    if (getVariableCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(object.getCategory().toUpperCase())).count() == 0) {
                        getVariableCosts().add(object);
                    }
                    break;
                case "Otros gastos":
                    if (getOtherCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(object.getCategory().toUpperCase())).count() == 0) {
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

    public List<SumCategoryEgressDTO> getSumCategoryEgress(String currency, List<SumCategoryEgressDTO> sumCategoryEgress, String group, double totalIngress) {
        List<SumCategoryEgressDTO> finalList = new ArrayList<>();
        for (int i = 0; sumCategoryEgress.size() > i; i++) {
            SumCategoryEgressDTO item = sumCategoryEgress.get(i);
            this.setSumEgressListPerSumCategoryEgressDTO(currency,item, group, totalIngress);
        }
        return sumCategoryEgress;
    }



    public void setSumEgressListPerSumCategoryEgressDTO(String currency, SumCategoryEgressDTO sumCategory, String group, double totalIngress) {
        List<SumEgressDTO> sumEgreesList = new ArrayList<>();
        for (int i = 0; i < egressList.size(); i++) {
            EgressDTO egrees = egressList.get(i);
            if (egrees.getCategoryName().equals(sumCategory.getCategory()) && egrees.getGroupName().equals(group)) {
                double totalByCategory = egressList.stream().filter(o -> o.getCategoryName().equals(sumCategory.getCategory()) && egrees.getGroupName().equals(group)).mapToDouble(o -> Double.parseDouble(currency.equals(o.getCurrency())?o.getTotal():o.getAmmountDoubleMoney())).sum();
                SumEgressDTO sumEgressDTO = new SumEgressDTO(egrees.getConcept());
                sumCategory.setTotal(currency,totalByCategory);
                double percentageByCategory = (totalByCategory * 100.0f) / totalIngress;
                sumCategory.setPercentage(percentageByCategory);
                if (sumEgreesList.stream().filter(o -> o.getConcept().toUpperCase().equals(egrees.getConcept().toUpperCase())).count() == 0) {
                    double totalbyEgress = egressList.stream().filter(o -> o.getConcept().toUpperCase().equals(egrees.getConcept().toUpperCase())).mapToDouble(o -> Double.parseDouble(currency.equals(o.getCurrency())?o.getTotal():o.getAmmountDoubleMoney())).sum();
                    sumEgressDTO.setTotal(currency,totalbyEgress);
                    double percentagebyEgress = (totalbyEgress * 100.0f) / totalIngress;
                    sumEgressDTO.setPercentage(percentagebyEgress);
                    sumEgreesList.add(sumEgressDTO);

                }
            }

        }
        sumCategory.setEgressList(sumEgreesList);
    }

    public void setTotalBudgetPerGroup(String currency) {
        for (int i = 0; i < fixedCosts.size(); i++) {
            this.setFixedCostsBudgetTotal(currency,this.getFixedCostsBudgetTotal() + fixedCosts.get(i).getBudget());
        }
        this.setFixedCostsBudgetDiference(currency,this.getFixedCostsBudgetTotal() - this.getFixedCostsTotal());

        for (int i = 0; i < variableCosts.size(); i++) {
            this.setVariableCostsBudgetTotal(currency,this.getVariableCostsBudgetTotal() + variableCosts.get(i).getBudget());

        }
        this.setVariableCostsBudgetDiference(currency,this.getVariableCostsBudgetTotal() - this.getVariableCostsTotal());

        for (int i = 0; i < otherCosts.size(); i++) {
            this.setOtherCostsBudgetTotal(currency,this.getOtherCostsBudgetTotal() + otherCosts.get(i).getBudget());

        }
        this.setOtherCostsBudgetDiference(currency,this.getOtherCostsBudgetTotal() - this.getOtherCostsTotal());
        this.setTotalBudget();
    }

    public void setGroupsTotal(String currency,List<SumCategoryEgressDTO> list, int type, double totalIngress) {
        double total = 0;
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i).getTotal();
        }
        double percentage = (total * 100.0f) / totalIngress;
        switch (type) {
            case 1:
                this.setFixedCostsPercentage(percentage);
                this.setFixedCostsTotal(currency,total);
                break;
            case 2:
                this.setVariableCostsPercentage(percentage);
                this.setVariableCostsTotal(currency,total);
                break;
            case 3:
                this.setOtherCostsPercentage(percentage);
                this.setOtherCostsTotal(currency,total);
                break;
        }

    }

    public void setAllEgressTotal(String currency) {
        this.allEgressCategoriesTotal = this.getFixedCostsTotal() + this.getOtherCostsTotal() + this.getVariableCostsTotal();
        this.setAllEgressCategoriesTotalFormatted(formatMoney(currency, this.allEgressCategoriesTotal));
    }

    public double getFixedCostsTotal() {
        return fixedCostsTotal;
    }

    public void setFixedCostsTotal(String currency,double fixedCostsTotal) {
        this.fixedCostsTotal = fixedCostsTotal;
        this.setFixedCostsTotalFormatted(formatMoney(currency,fixedCostsTotal));
    }

    public double getVariableCostsTotal() {
        return variableCostsTotal;
    }

    public void setVariableCostsTotal(String currency,double variableCostsTotal) {
        this.variableCostsTotal = variableCostsTotal;
        this.setVariableCostsTotalFormatted(formatMoney(currency,variableCostsTotal));
    }

    public double getOtherCostsTotal() {
        return otherCostsTotal;
    }

    public void setOtherCostsTotal(String currency,double otherCostsTotal) {
        this.otherCostsTotal = otherCostsTotal;
        this.setOtherCostsTotalFormatted(formatMoney(currency,otherCostsTotal));
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

    public void setFixedCostsBudgetDiference(String currency,double fixedCostsBudgetDiference) {
        this.fixedCostsBudgetDiference = fixedCostsBudgetDiference;
        this.setFixedCostsBudgetDiferenceFormatted(formatMoney(currency,fixedCostsBudgetDiference));
    }

    public double getVariableCostsBudgetDiference() {
        return variableCostsBudgetDiference;
    }

    public void setVariableCostsBudgetDiference(String currency, double variableCostsBudgetDiference) {
        this.variableCostsBudgetDiference = variableCostsBudgetDiference;
        this.setVariableCostsBudgetDiferenceFormatted(formatMoney(currency,variableCostsBudgetDiference));
    }

    public double getOtherCostsBudgetDiference() {
        return otherCostsBudgetDiference;
    }

    public void setOtherCostsBudgetDiference(String currency,double otherCostsBudgetDiference) {
        this.otherCostsBudgetDiference = otherCostsBudgetDiference;
        this.setOtherCostsBudgetDiferenceFormatted(formatMoney(currency,otherCostsBudgetDiference));
    }

    public double getFixedCostsBudgetTotal() {
        return fixedCostsBudgetTotal;
    }

    public void setFixedCostsBudgetTotal(String currency,double fixedCostsBudgetTotal) {
        this.fixedCostsBudgetTotal = fixedCostsBudgetTotal;
        this.setFixedCostsBudgetTotalFormatted(formatMoney(currency,fixedCostsBudgetTotal));
    }

    public double getVariableCostsBudgetTotal() {
        return variableCostsBudgetTotal;
    }

    public void setVariableCostsBudgetTotal(String currency, double variableCostsBudgetTotal) {
        this.variableCostsBudgetTotal = variableCostsBudgetTotal;
        this.setVariableCostsBudgetTotalFormatted(formatMoney(currency, variableCostsBudgetTotal));
    }

    public double getOtherCostsBudgetTotal() {
        return otherCostsBudgetTotal;
    }

    public void setOtherCostsBudgetTotal(String currency, double otherCostsBudgetTotal) {
        this.otherCostsBudgetTotal = otherCostsBudgetTotal;
        this.setOtherCostsBudgetTotalFormatted(formatMoney(currency, otherCostsBudgetTotal));
    }

    public double getAllEgressCategoriesTotal() {
        return allEgressCategoriesTotal;
    }

    public void setAllEgressCategoriesTotal(double allEgressCategoriesTotal) {
        this.allEgressCategoriesTotal = allEgressCategoriesTotal;
//        this.setAllEgressCategoriesTotalFormatted(formatMoney(allEgressCategoriesTotal));
    }

    public String getAllEgressCategoriesTotalFormatted() {
        return allEgressCategoriesTotalFormatted;
    }

    public void setAllEgressCategoriesTotalFormatted(String allEgressCategoriesTotalFormatted) {
        this.allEgressCategoriesTotalFormatted = allEgressCategoriesTotalFormatted;
    }

    public String getFixedCostsTotalFormatted() {
        return fixedCostsTotalFormatted;
    }

    public void setFixedCostsTotalFormatted(String fixedCostsTotalFormatted) {
        this.fixedCostsTotalFormatted = fixedCostsTotalFormatted;
    }

    public String getVariableCostsTotalFormatted() {
        return variableCostsTotalFormatted;
    }

    public void setVariableCostsTotalFormatted(String variableCostsTotalFormatted) {
        this.variableCostsTotalFormatted = variableCostsTotalFormatted;
    }

    public String getOtherCostsTotalFormatted() {
        return otherCostsTotalFormatted;
    }

    public void setOtherCostsTotalFormatted(String otherCostsTotalFormatted) {
        this.otherCostsTotalFormatted = otherCostsTotalFormatted;
    }

    public String getFixedCostsBudgetTotalFormatted() {
        return fixedCostsBudgetTotalFormatted;
    }

    public void setFixedCostsBudgetTotalFormatted(String fixedCostsBudgetTotalFormatted) {
        this.fixedCostsBudgetTotalFormatted = fixedCostsBudgetTotalFormatted;
    }

    public String getVariableCostsBudgetTotalFormatted() {
        return variableCostsBudgetTotalFormatted;
    }

    public void setVariableCostsBudgetTotalFormatted(String variableCostsBudgetTotalFormatted) {
        this.variableCostsBudgetTotalFormatted = variableCostsBudgetTotalFormatted;
    }

    public String getOtherCostsBudgetTotalFormatted() {
        return otherCostsBudgetTotalFormatted;
    }

    public void setOtherCostsBudgetTotalFormatted(String otherCostsBudgetTotalFormatted) {
        this.otherCostsBudgetTotalFormatted = otherCostsBudgetTotalFormatted;
    }

    public String getFixedCostsBudgetDiferenceFormatted() {
        return fixedCostsBudgetDiferenceFormatted;
    }

    public void setFixedCostsBudgetDiferenceFormatted(String fixedCostsBudgetDiferenceFormatted) {
        this.fixedCostsBudgetDiferenceFormatted = fixedCostsBudgetDiferenceFormatted;
    }

    public String getVariableCostsBudgetDiferenceFormatted() {
        return variableCostsBudgetDiferenceFormatted;
    }

    public void setVariableCostsBudgetDiferenceFormatted(String variableCostsBudgetDiferenceFormatted) {
        this.variableCostsBudgetDiferenceFormatted = variableCostsBudgetDiferenceFormatted;
    }

    public String getOtherCostsBudgetDiferenceFormatted() {
        return otherCostsBudgetDiferenceFormatted;
    }

    public void setOtherCostsBudgetDiferenceFormatted(String otherCostsBudgetDiferenceFormatted) {
        this.otherCostsBudgetDiferenceFormatted = otherCostsBudgetDiferenceFormatted;
    }

    public double getTotalBudget() {
        return this.getFixedCostsBudgetTotal() + this.getOtherCostsBudgetTotal() + this.getVariableCostsBudgetTotal();
    }

    public void setTotalBudget() {
        this.totalBudget = this.getFixedCostsBudgetTotal() + this.getOtherCostsBudgetTotal() + this.getVariableCostsBudgetTotal();
    }
}
