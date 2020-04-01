package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.domain.Charge;
import com.lighthouse.aditum.domain.Egress;
import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.service.dto.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@Transactional
public class AnualReportService {
    private final MensualReportService mensualReportService;
    private final EgressCategoryService egressCategoryService;
    private final CompanyConfigurationService companyConfigurationService;

    public AnualReportService(CompanyConfigurationService companyConfigurationService, MensualReportService mensualReportService, EgressCategoryService egressCategoryService) {
        this.companyConfigurationService = companyConfigurationService;
        this.mensualReportService = mensualReportService;
        this.egressCategoryService = egressCategoryService;
    }

    public void getReportByMonth(AnualReportDTO anualReportDTO, String actual_month, Long companyId, int withPresupuesto) {
        Page<EgressCategoryDTO> egressCategories = egressCategoryService.findAll(companyId);
        List<MensualIngressReportDTO> ingressByMonth = new ArrayList<>();
        List<MensualEgressReportDTO> egressByMonth = new ArrayList<>();
        List<String> initialBalanceByMonth = new ArrayList<>();
        ZonedDateTime zd_actualMonth = ZonedDateTime.parse(actual_month);
        int finalMonth = zd_actualMonth.getMonthValue();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();

        for (int i = 1; i <= finalMonth; i++) {
            ZonedDateTime initialDate = zd_actualMonth.withMonth(i).withDayOfMonth(1).withMinute(0).withHour(0);
            ZonedDateTime initialDate2 = initialDate.withHour(23).withMinute(59);
            ZonedDateTime finalDate = initialDate.with(TemporalAdjusters.lastDayOfMonth());
            MensualIngressReportDTO mensualIngressReportDTO = mensualReportService.getMensualAndAnualIngressReportDTO(initialDate, finalDate, companyId, withPresupuesto);
            MensualEgressReportDTO mensualEgressReportDTO = mensualReportService.getMensualAndAnualEgressReportDTO(initialDate, finalDate, companyId, mensualIngressReportDTO, withPresupuesto);

            if (withPresupuesto == 2) {
                mensualReportService.getEgressBudgets(mensualEgressReportDTO, companyId, initialDate, finalDate, egressCategories);
                mensualEgressReportDTO.setTotalBudgetPerGroup(currency);

                mensualReportService.getIngressBudgets(mensualIngressReportDTO, companyId, initialDate, finalDate);
                mensualIngressReportDTO.setAllIngressCategoriesBudgetTotal(currency);
            }
            ingressByMonth.add(mensualIngressReportDTO);
            egressByMonth.add(mensualEgressReportDTO);
            List<MensualAndAnualAccountDTO> mensualAndAnualAccount = mensualReportService.getAccountBalance(initialDate, initialDate2, companyId);
            initialBalanceByMonth.add(this.setTotalInitialBalance(mensualAndAnualAccount) + "");

        }

        anualReportDTO.setInitialBalanceByMonth(initialBalanceByMonth);
        anualReportDTO.setAnualIngressByMonth(ingressByMonth);
        anualReportDTO.setAnualEgressByMonth(egressByMonth);
        this.setEgressCategoriesWithOutTotal(anualReportDTO, companyId);
        this.setfixedEgressCategoriesByMonth(anualReportDTO);

        anualReportDTO.setAllEgressAcumulado();
        this.setFlujoByMonth(anualReportDTO);
        this.setAllIngressAcumulado(anualReportDTO);
        if (withPresupuesto == 2) {
            this.getAllEgressBudgetAndDiference(anualReportDTO);
            this.setIngressBudgets(anualReportDTO);
        }
        this.setRealBalanceByMonth(anualReportDTO);

    }

    public AnualReportDashboardDTO getReportByMonthDashboard(Long companyId, int year) throws JSONException {
        Page<EgressCategoryDTO> egressCategories = egressCategoryService.findAll(companyId);
        List<MensualIngressReportDTO> ingressByMonth = new ArrayList<>();
        List<MensualEgressReportDTO> egressByMonth = new ArrayList<>();
        ZonedDateTime zd_actualMonth = ZonedDateTime.now();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        int finalMonth = zd_actualMonth.getMonthValue();
        if (zd_actualMonth.getYear() != year) {
            finalMonth = 12;
        }
        List<DashboardReportIncomeEgressBudgetDTO> meses = new ArrayList<>();
        for (int i = 1; i <= finalMonth; i++) {
            DashboardReportIncomeEgressBudgetDTO month = new DashboardReportIncomeEgressBudgetDTO();
            ZonedDateTime initialDate = zd_actualMonth.withYear(year).withMonth(i).withDayOfMonth(1).withMinute(0).withHour(0);
            ZonedDateTime initialDate2 = initialDate.withHour(23).withMinute(59);
            ZonedDateTime finalDate = initialDate.with(TemporalAdjusters.lastDayOfMonth());
            ZonedDateTime initDate = initialDate;
            ZonedDateTime fiDate = finalDate;
            MensualIngressReportDTO mensualIngressReportDTO = mensualReportService.getMensualAndAnualIngressReportDTO(initDate, fiDate, companyId, 2);
            MensualEgressReportDTO mensualEgressReportDTO = mensualReportService.getMensualAndAnualEgressReportDTO(initDate, fiDate, companyId, mensualIngressReportDTO, 2);
            mensualReportService.getEgressBudgets(mensualEgressReportDTO, companyId, initDate, fiDate, egressCategories);
            mensualEgressReportDTO.setTotalBudgetPerGroup(currency);
            mensualReportService.getIngressBudgets(mensualIngressReportDTO, companyId, initDate, fiDate);
            mensualIngressReportDTO.setAllIngressCategoriesBudgetTotal(currency);
            ingressByMonth.add(mensualIngressReportDTO);
            egressByMonth.add(mensualEgressReportDTO);
            month.setbudgetIncomeTotal(currency, mensualIngressReportDTO.getTotalBudget());
            month.setBudgetEgressTotal(currency, mensualEgressReportDTO.getTotalBudget());
            month.setEgressTotal(currency, mensualEgressReportDTO.getAllEgressCategoriesTotal());
            month.setIncomeTotal(currency,mensualIngressReportDTO.getAllIngressCategoriesTotal());
            month.setInitialBalance(currency,this.setTotalInitialBalance(mensualReportService.getAccountBalance(initDate, initialDate2, companyId)));
            month.setRealBalance(currency,month.getInitialBalance() + (month.getIncomeTotal() - month.getEgressTotal()));
            month.setMonthValue(i);
            month.setMonth(ZonedDateTime.now().withMonth(i).getMonth().toString());
            meses.add(month);
        }
        AnualReportDashboardDTO anualReport = new AnualReportDashboardDTO();
        anualReport.setIncomeEgressBudgetList(meses);
        return anualReport;
    }

    private void getAllEgressBudgetAndDiference(AnualReportDTO anualReportDTO) {
        anualReportDTO.setAllEgressBudgetByMonth(new ArrayList<>());
        anualReportDTO.setAllEgressDiferenceByMonth(new ArrayList<>());
        for (int i = 0; i < anualReportDTO.getFixedCostsBudgetTotal().size(); i++) {
            double totalBudget = anualReportDTO.getAnualEgressByMonth().get(i).getFixedCostsBudgetTotal() + anualReportDTO.getAnualEgressByMonth().get(i).getVariableCostsBudgetTotal() + anualReportDTO.getAnualEgressByMonth().get(i).getOtherCostsBudgetTotal();
            double totalDiference = anualReportDTO.getAnualEgressByMonth().get(i).getFixedCostsBudgetDiference() + anualReportDTO.getAnualEgressByMonth().get(i).getVariableCostsBudgetDiference() + anualReportDTO.getAnualEgressByMonth().get(i).getOtherCostsBudgetDiference();
            anualReportDTO.getAllEgressBudgetByMonth().add(totalBudget);
            anualReportDTO.getAllEgressDiferenceByMonth().add(totalDiference);
            anualReportDTO.setAllEgressBudgetAcumulado(totalBudget);
            anualReportDTO.setAllEgressDiferenceAcumulado(totalDiference);
        }
    }

    private void setRealBalanceByMonth(AnualReportDTO anualReportDTO) {
        anualReportDTO.setRealBalanceByMonth(new ArrayList<>());
        for (int i = 0; i < anualReportDTO.getFlujoByMonth().size(); i++) {
            anualReportDTO.getRealBalanceByMonth().add(Double.parseDouble(anualReportDTO.getInitialBalanceByMonth().get(i)) + anualReportDTO.getFlujoByMonth().get(i) + "");

        }
    }


    private void setIngressBudgets(AnualReportDTO anualReportDTO) {

        for (int i = 0; i < anualReportDTO.getFlujoByMonth().size(); i++) {
            anualReportDTO.setMaintenanceBudgetAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getMaintenanceBudget());
            anualReportDTO.setExtraordinaryIngressBudgetAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getExtraordinaryBudget());
            anualReportDTO.setCommonAreasBudgetAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getCommonAreasBudget());
            anualReportDTO.setOtherIngressBudgetAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getOtherBudget());

            anualReportDTO.setMaintenanceBudgetDiferenceAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getMaintenanceBudgetDiference());
            anualReportDTO.setExtraordinaryIngressBudgetDiferenceAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getExtraordinaryBudgetDiference());
            anualReportDTO.setCommonAreasBudgetDiferenceAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getCommonAreasBudgetDiference());
            anualReportDTO.setOtherIngressBudgetDiferenceAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getOtherBudgetDiference());

            anualReportDTO.setTotalIngressBudget(anualReportDTO.getAnualIngressByMonth().get(i).getTotalBudget());
            anualReportDTO.setTotalIngressBudgetDiference(anualReportDTO.getAnualIngressByMonth().get(i).getTotalBudgetDiference());

        }
    }

    private double setTotalInitialBalance(List<MensualAndAnualAccountDTO> mensualAndAnualAccount) {
        double totalBalance = 0;
        for (int i = 0; i < mensualAndAnualAccount.size(); i++) {
            totalBalance = totalBalance + mensualAndAnualAccount.get(i).getInicialBalance();
        }
        return totalBalance;
    }

    private void setfixedEgressCategoriesByMonth(AnualReportDTO anualReportDTO) {

        for (int i = 0; i < anualReportDTO.getAnualEgressByMonth().size(); i++) {


        }

        List<EgressCategoryByMonthDTO> fixedCostsEgressList = new ArrayList<>();
        anualReportDTO.setFixedCostsBudgetTotal(new ArrayList<>());
        anualReportDTO.setFixedCostsBudgetDiference(new ArrayList<>());
        for (int i = 0; i < anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().size(); i++) {
            Collections.sort(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts(), Comparator.comparing(SumCategoryEgressDTO::getCategory));
            EgressCategoryByMonthDTO object = new EgressCategoryByMonthDTO(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getCategory());
            object.setTotalByMonth(new ArrayList<>());
            object.setBudgetByMonth(new ArrayList<>());
            object.setDiferenceByMonth(new ArrayList<>());

            object.getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getTotal() + "");
            object.setAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getTotal());

            object.getBudgetByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getBudget() + "");

            object.getDiferenceByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getBudgetDiference() + "");

            object.setBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getBudget());
            object.setDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getBudgetDiference());


            fixedCostsEgressList.add(object);

        }
        anualReportDTO.setFixedCostsBudgetDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCostsBudgetDiference());
        anualReportDTO.setFixedCostsBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCostsBudgetTotal());
        anualReportDTO.getFixedCostsBudgetTotal().add(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCostsBudgetTotal());
        anualReportDTO.getFixedCostsBudgetDiference().add(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCostsBudgetDiference());
        anualReportDTO.setFixedCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCostsTotal());
        for (int i = 1; i < anualReportDTO.getAnualEgressByMonth().size(); i++) {
            for (int j = 0; j < anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().size(); j++) {
                Collections.sort(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts(), Comparator.comparing(SumCategoryEgressDTO::getCategory));
                fixedCostsEgressList.get(j).getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().get(j).getTotal() + "");
                fixedCostsEgressList.get(j).getBudgetByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().get(j).getBudget() + "");

                fixedCostsEgressList.get(j).getDiferenceByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().get(j).getBudgetDiference() + "");
                fixedCostsEgressList.get(j).setAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().get(j).getTotal());
                fixedCostsEgressList.get(j).setBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().get(j).getBudget());
                fixedCostsEgressList.get(j).setDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().get(j).getBudgetDiference());

            }
            anualReportDTO.setFixedCostsBudgetDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCostsBudgetDiference());
            anualReportDTO.setFixedCostsBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCostsBudgetTotal());
            anualReportDTO.getFixedCostsBudgetTotal().add(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCostsBudgetTotal());
            anualReportDTO.getFixedCostsBudgetDiference().add(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCostsBudgetDiference());

            anualReportDTO.setFixedCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCostsTotal());
        }
        anualReportDTO.setFixedCostEgress(fixedCostsEgressList);

        setvariableEgressCategoriesByMonth(anualReportDTO);
    }

    private void setvariableEgressCategoriesByMonth(AnualReportDTO anualReportDTO) {
        List<EgressCategoryByMonthDTO> variableCostsEgressList = new ArrayList<>();

        anualReportDTO.setFvariableCostsBudgetTotal(new ArrayList<>());
        anualReportDTO.setVariableCostsBudgetDiference(new ArrayList<>());
        for (int i = 0; i < anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().size(); i++) {
            Collections.sort(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts(), Comparator.comparing(SumCategoryEgressDTO::getCategory));
            EgressCategoryByMonthDTO object = new EgressCategoryByMonthDTO(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getCategory());

            object.setTotalByMonth(new ArrayList<>());

            object.setBudgetByMonth(new ArrayList<>());
            object.setDiferenceByMonth(new ArrayList<>());

            object.getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getTotal() + "");
            object.setAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getTotal());

            object.getBudgetByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getBudget() + "");
            object.getDiferenceByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getBudgetDiference() + "");

            object.setBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getBudget());
            object.setDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getBudgetDiference());


            variableCostsEgressList.add(object);
        }

        anualReportDTO.setVariableCostsBudgetDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCostsBudgetDiference());
        anualReportDTO.setVariableCostsBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCostsBudgetTotal());
        anualReportDTO.getvariableCostsBudgetTotal().add(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCostsBudgetTotal());
        anualReportDTO.getVariableCostsBudgetDiference().add(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCostsBudgetDiference());
        anualReportDTO.setVariableCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCostsTotal());
        for (int i = 1; i < anualReportDTO.getAnualEgressByMonth().size(); i++) {
            for (int j = 0; j < anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().size(); j++) {
                Collections.sort(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts(), Comparator.comparing(SumCategoryEgressDTO::getCategory));
                variableCostsEgressList.get(j).getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().get(j).getTotal() + "");
                variableCostsEgressList.get(j).getBudgetByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().get(j).getBudget() + "");
                variableCostsEgressList.get(j).getDiferenceByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().get(j).getBudgetDiference() + "");
                variableCostsEgressList.get(j).setAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().get(j).getTotal());
                variableCostsEgressList.get(j).setBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().get(j).getBudget());
                variableCostsEgressList.get(j).setDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().get(j).getBudgetDiference());

            }
            anualReportDTO.setVariableCostsBudgetDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCostsBudgetDiference());
            anualReportDTO.setVariableCostsBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCostsBudgetTotal());
            anualReportDTO.getvariableCostsBudgetTotal().add(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCostsBudgetTotal());
            anualReportDTO.getVariableCostsBudgetDiference().add(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCostsBudgetDiference());

            anualReportDTO.setVariableCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCostsTotal());
        }
        anualReportDTO.setVariableCostEgress(variableCostsEgressList);
        setOtherEgressCategoriesByMonth(anualReportDTO);
    }

    private void setOtherEgressCategoriesByMonth(AnualReportDTO anualReportDTO) {
        List<EgressCategoryByMonthDTO> otherCostsEgressList = new ArrayList<>();
        anualReportDTO.setOtherCostsBudgetTotal(new ArrayList<>());
        anualReportDTO.setOtherCostsBudgetDiference(new ArrayList<>());
        for (int i = 0; i < anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().size(); i++) {
            Collections.sort(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts(), Comparator.comparing(SumCategoryEgressDTO::getCategory));
            EgressCategoryByMonthDTO object = new EgressCategoryByMonthDTO(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getCategory());
            object.setTotalByMonth(new ArrayList<>());
            object.setBudgetByMonth(new ArrayList<>());
            object.setDiferenceByMonth(new ArrayList<>());

            object.getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getTotal() + "");

            object.setAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getTotal());

            object.getBudgetByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getBudget() + "");
            object.getDiferenceByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getBudgetDiference() + "");

            object.setBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getBudget());
            object.setDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getBudgetDiference());


            object.setAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getTotal());
            otherCostsEgressList.add(object);
        }
        anualReportDTO.setOtherCostsBudgetDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCostsBudgetDiference());
        anualReportDTO.setOtherCostsBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCostsBudgetTotal());
        anualReportDTO.getOtherCostsBudgetTotal().add(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCostsBudgetTotal());
        anualReportDTO.getOtherCostsBudgetDiference().add(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCostsBudgetDiference());
        anualReportDTO.setOtherCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCostsTotal());

        for (int i = 1; i < anualReportDTO.getAnualEgressByMonth().size(); i++) {
            for (int j = 0; j < anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().size(); j++) {
                Collections.sort(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts(), Comparator.comparing(SumCategoryEgressDTO::getCategory));
                otherCostsEgressList.get(j).getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().get(j).getTotal() + "");
                otherCostsEgressList.get(j).setAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().get(j).getTotal());
                otherCostsEgressList.get(j).getBudgetByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().get(j).getBudget() + "");
                otherCostsEgressList.get(j).getDiferenceByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().get(j).getBudgetDiference() + "");
                otherCostsEgressList.get(j).setBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().get(j).getBudget());
                otherCostsEgressList.get(j).setDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().get(j).getBudgetDiference());

            }
            anualReportDTO.setOtherCostsBudgetDiferenceAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCostsBudgetDiference());
            anualReportDTO.setOtherCostsBudgetAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCostsBudgetTotal());
            anualReportDTO.getOtherCostsBudgetTotal().add(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCostsBudgetTotal());
            anualReportDTO.getOtherCostsBudgetDiference().add(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCostsBudgetDiference());


            anualReportDTO.setOtherCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCostsTotal());
        }
        anualReportDTO.setOtherCostEgress(otherCostsEgressList);

    }

    private void setEgressCategoriesWithOutTotal(AnualReportDTO anualReportDTO, Long companyId) {
        Page<EgressCategoryDTO> categories = egressCategoryService.findAll(companyId);
        String currency = companyConfigurationService.getByCompanyId(null,companyId).getContent().get(0).getCurrency();

        for (int i = 0; i < categories.getContent().size(); i++) {
            for (int j = 0; j < anualReportDTO.getAnualEgressByMonth().size(); j++) {
                String categoryName = categories.getContent().get(i).getCategory();
                switch (categories.getContent().get(i).getGroup()) {
                    case "Gastos fijos":
                        if (anualReportDTO.getAnualEgressByMonth().get(j).getFixedCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(categoryName.toUpperCase())).count() == 0) {
                            SumCategoryEgressDTO item = new SumCategoryEgressDTO(currency,categories.getContent().get(i).getCategory(), 0);
                            anualReportDTO.getAnualEgressByMonth().get(j).getFixedCosts().add(item);
                        }
                        break;
                    case "Gastos variables":
                        if (anualReportDTO.getAnualEgressByMonth().get(j).getVariableCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(categoryName.toUpperCase())).count() == 0) {
                            SumCategoryEgressDTO item = new SumCategoryEgressDTO(currency,categories.getContent().get(i).getCategory(), 0);
                            anualReportDTO.getAnualEgressByMonth().get(j).getVariableCosts().add(item);
                        }
                        break;
                    case "Otros gastos":
                        if (anualReportDTO.getAnualEgressByMonth().get(j).getOtherCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(categoryName.toUpperCase())).count() == 0) {
                            SumCategoryEgressDTO item = new SumCategoryEgressDTO(currency,categories.getContent().get(i).getCategory(), 0);
                            anualReportDTO.getAnualEgressByMonth().get(j).getOtherCosts().add(item);
                        }
                        break;


                }

            }
        }


    }

    public void setFlujoByMonth(AnualReportDTO anualReportDTO) {
        anualReportDTO.setFlujoByMonth(new ArrayList<>());
        for (int i = 0; i < anualReportDTO.getAnualEgressByMonth().size(); i++) {
            double flujo = anualReportDTO.getAnualIngressByMonth().get(i).getAllIngressCategoriesTotal() - anualReportDTO.getAnualEgressByMonth().get(i).getAllEgressCategoriesTotal();
            anualReportDTO.getFlujoByMonth().add(flujo);
        }


    }

    public void setAllIngressAcumulado(AnualReportDTO anualReportDTO) {
        double total = 0;
        for (int i = 0; i < anualReportDTO.getAnualIngressByMonth().size(); i++) {
            total = total + anualReportDTO.getAnualIngressByMonth().get(i).getAllIngressCategoriesTotal();
            anualReportDTO.setMaintenanceIngressAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getMaintenanceIngressTotal());
            anualReportDTO.setMultasIngressAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getMultaIngressTotal());
            anualReportDTO.setExtraordinaryIngressAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getExtraordinaryIngressTotal());
            anualReportDTO.setCommonAreasAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getCommonAreasIngressTotal());
            anualReportDTO.setOtherIngressAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getOtherIngressTotal());
            anualReportDTO.setWaterChargeIngressAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getWaterChargeIngressTotal());
        }
        anualReportDTO.setAllIngressAcumulado(total);

    }
}
