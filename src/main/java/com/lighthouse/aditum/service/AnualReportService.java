package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.domain.Charge;
import com.lighthouse.aditum.domain.Egress;
import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class AnualReportService {
    private final MensualReportService mensualReportService;
    private final EgressCategoryService egressCategoryService;

    public AnualReportService(MensualReportService mensualReportService,EgressCategoryService egressCategoryService) {

        this.mensualReportService = mensualReportService;
        this.egressCategoryService = egressCategoryService;
    }

    public void getReportByMonth(AnualReportDTO anualReportDTO,String actual_month,Long companyId,int withPresupuesto){
        List<MensualIngressReportDTO> ingressByMonth = new ArrayList<>();
        List<MensualEgressReportDTO> egressByMonth = new ArrayList<>();
        ZonedDateTime zd_actualMonth = ZonedDateTime.parse(actual_month);
        int finalMonth = zd_actualMonth.getMonthValue();
        for (int i = 1; i <= finalMonth; i++) {
            ZonedDateTime initialDate = zd_actualMonth.withMonth(i).withDayOfMonth(1);
            ZonedDateTime finalDate = initialDate.with(TemporalAdjusters.lastDayOfMonth());
            MensualIngressReportDTO mensualIngressReportDTO = mensualReportService.getMensualAndAnualIngressReportDTO(initialDate+"",finalDate+"",companyId,withPresupuesto);
            MensualEgressReportDTO mensualEgressReportDTO = mensualReportService.getMensualAndAnualEgressReportDTO(initialDate+"",finalDate+"",companyId,mensualIngressReportDTO,withPresupuesto);
            ingressByMonth.add(mensualIngressReportDTO);
            egressByMonth.add(mensualEgressReportDTO);

        }
        anualReportDTO.setAnualIngressByMonth(ingressByMonth);
        anualReportDTO.setAnualEgressByMonth(egressByMonth);

        this.setEgressCategoriesWithOutTotal(anualReportDTO,companyId);
        this.setfixedEgressCategoriesByMonth(anualReportDTO);
        anualReportDTO.setAllEgressAcumulado();
        this.setFlujoByMonth(anualReportDTO);
        this.setAllIngressAcumulado(anualReportDTO);

    }
    private void setfixedEgressCategoriesByMonth(AnualReportDTO anualReportDTO){
        List<EgressCategoryByMonthDTO> fixedCostsEgressList = new ArrayList<>();
        for (int i = 0; i <anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().size(); i++) {
            int acumulado = 0;
            EgressCategoryByMonthDTO object = new EgressCategoryByMonthDTO(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getCategory());
            object.setTotalByMonth(new ArrayList<>());
            object.getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getTotal()+"");
            acumulado = acumulado + anualReportDTO.getAnualEgressByMonth().get(0).getFixedCosts().get(i).getTotal();
            object.setAcumulado(acumulado);
            fixedCostsEgressList.add(object);
        }
        for (int i = 1; i <anualReportDTO.getAnualEgressByMonth().size() ; i++) {
            for (int j = 0; j < anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().size(); j++) {
                fixedCostsEgressList.get(j).getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().get(j).getTotal()+"");
                fixedCostsEgressList.get(j).setAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCosts().get(j).getTotal());

            }
            anualReportDTO.setFixedCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getFixedCostsTotal());
        }
        anualReportDTO.setFixedCostEgress(fixedCostsEgressList);
        setvariableEgressCategoriesByMonth(anualReportDTO);
    }

    private void setvariableEgressCategoriesByMonth(AnualReportDTO anualReportDTO){
        List<EgressCategoryByMonthDTO> variableCostsEgressList = new ArrayList<>();
        for (int i = 0; i <anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().size(); i++) {
            int acumulado = 0;
            EgressCategoryByMonthDTO object = new EgressCategoryByMonthDTO(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getCategory());
            object.setTotalByMonth(new ArrayList<>());
            object.getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getTotal()+"");
            acumulado = acumulado + anualReportDTO.getAnualEgressByMonth().get(0).getVariableCosts().get(i).getTotal();
            object.setAcumulado(acumulado);
            variableCostsEgressList.add(object);
        }
        for (int i = 1; i <anualReportDTO.getAnualEgressByMonth().size() ; i++) {
            for (int j = 0; j < anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().size(); j++) {
                variableCostsEgressList.get(j).getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().get(j).getTotal()+"");
                variableCostsEgressList.get(j).setAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCosts().get(j).getTotal());

            }
            anualReportDTO.setVariableCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getVariableCostsTotal());
        }
        anualReportDTO.setVariableCostEgress(variableCostsEgressList);
        setOtherEgressCategoriesByMonth(anualReportDTO);
    }

    private void setOtherEgressCategoriesByMonth(AnualReportDTO anualReportDTO){
        List<EgressCategoryByMonthDTO> otherCostsEgressList = new ArrayList<>();
        for (int i = 0; i <anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().size(); i++) {
            int acumulado = 0;
            EgressCategoryByMonthDTO object = new EgressCategoryByMonthDTO(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getCategory());
            object.setTotalByMonth(new ArrayList<>());
            object.getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getTotal()+"");
            acumulado = acumulado + anualReportDTO.getAnualEgressByMonth().get(0).getOtherCosts().get(i).getTotal();
            object.setAcumulado(acumulado);
            otherCostsEgressList.add(object);
        }
        for (int i = 1; i <anualReportDTO.getAnualEgressByMonth().size() ; i++) {
            for (int j = 0; j < anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().size(); j++) {
                otherCostsEgressList.get(j).getTotalByMonth().add(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().get(j).getTotal()+"");
                otherCostsEgressList.get(j).setAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCosts().get(j).getTotal());
            }
            anualReportDTO.setOtherCostsAcumulado(anualReportDTO.getAnualEgressByMonth().get(i).getOtherCostsTotal());
        }
        anualReportDTO.setOtherCostEgress(otherCostsEgressList);

    }

    private void setEgressCategoriesWithOutTotal(AnualReportDTO anualReportDTO,Long companyId){
        Page<EgressCategoryDTO> categories = egressCategoryService.findAll(companyId);
        for (int i = 0; i < categories.getContent().size(); i++) {
            for (int j = 0; j < anualReportDTO.getAnualEgressByMonth().size(); j++) {
                String categoryName = categories.getContent().get(i).getCategory();
                switch (categories.getContent().get(i).getGroup()){
                    case "Gastos fijos":
                        if (anualReportDTO.getAnualEgressByMonth().get(j).getFixedCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(categoryName.toUpperCase())).count() == 0) {
                            SumCategoryEgressDTO item = new SumCategoryEgressDTO(categories.getContent().get(i).getCategory(), 0);
                            anualReportDTO.getAnualEgressByMonth().get(j).getFixedCosts().add(item);
                        }
                        break;
                    case "Gastos variables":
                        if (anualReportDTO.getAnualEgressByMonth().get(j).getVariableCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(categoryName.toUpperCase())).count() == 0) {
                            SumCategoryEgressDTO item = new SumCategoryEgressDTO(categories.getContent().get(i).getCategory(), 0);
                            anualReportDTO.getAnualEgressByMonth().get(j).getVariableCosts().add(item);
                        }
                        break;
                    case "Otros gastos":
                        if (anualReportDTO.getAnualEgressByMonth().get(j).getOtherCosts().stream().filter(o -> o.getCategory().toUpperCase().equals(categoryName.toUpperCase())).count() == 0) {
                            SumCategoryEgressDTO item = new SumCategoryEgressDTO(categories.getContent().get(i).getCategory(), 0);
                            anualReportDTO.getAnualEgressByMonth().get(j).getOtherCosts().add(item);
                        }
                        break;


                }

            }
        }


    }
    public void setFlujoByMonth(AnualReportDTO anualReportDTO){
        anualReportDTO.setFlujoByMonth(new ArrayList<>());
        for (int i = 0; i <anualReportDTO.getAnualEgressByMonth().size() ; i++) {
          int flujo = anualReportDTO.getAnualIngressByMonth().get(i).getAllIngressCategoriesTotal() - anualReportDTO.getAnualEgressByMonth().get(i).getAllEgressCategoriesTotal();
          anualReportDTO.getFlujoByMonth().add(flujo+"");
        }


    }
    public void setAllIngressAcumulado(AnualReportDTO anualReportDTO){
        int total = 0;
        for (int i = 0; i <anualReportDTO.getAnualIngressByMonth().size() ; i++) {
            total = total + anualReportDTO.getAnualIngressByMonth().get(i).getAllIngressCategoriesTotal();
            anualReportDTO.setMaintenanceIngressAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getMaintenanceIngressTotal());
            anualReportDTO.setExtraordinaryIngressAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getExtraordinaryIngressTotal());
            anualReportDTO.setCommonAreasAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getCommonAreasIngressTotal());
            anualReportDTO.setOtherIngressAcumulado(anualReportDTO.getAnualIngressByMonth().get(i).getOtherIngressTotal());
        }
        anualReportDTO.setAllIngressAcumulado(total);

    }
}
