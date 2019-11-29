package com.lighthouse.aditum.service.dto;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectDeserializer;

public class AnualReportDashboardDTO implements Serializable{

    private  List<DashboardReportIncomeEgressBudgetDTO> incomeEgressBudgetList;

    public List<DashboardReportIncomeEgressBudgetDTO> getIncomeEgressBudgetList() {
        return incomeEgressBudgetList;
    }

    public void setIncomeEgressBudgetList(List<DashboardReportIncomeEgressBudgetDTO> incomeEgressBudgetList) {
        this.incomeEgressBudgetList = incomeEgressBudgetList;
    }
}
