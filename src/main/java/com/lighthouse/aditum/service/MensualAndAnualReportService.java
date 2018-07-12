package com.lighthouse.aditum.service;

import com.lighthouse.aditum.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MensualAndAnualReportService {

    private final ChargeService chargeService;
    private final EgressService egressService;
    private final EgressCategoryService egressCategoryService;
    public MensualAndAnualReportService(ChargeService chargeService,EgressService egressService, EgressCategoryService egressCategoryService) {
        this.chargeService = chargeService;
        this.egressService = egressService;
        this.egressCategoryService = egressCategoryService;
    }

    public MensualAndAnualIngressReportDTO getMensualAndAnualIngressReportDTO(String initialTime, String finalTime, long companyId){
        Page<ChargeDTO> maintenanceIngress = chargeService.findPaidChargesBetweenDates(initialTime,finalTime,1,companyId);
        Page<ChargeDTO> extraOrdinaryIngress = chargeService.findPaidChargesBetweenDates(initialTime,finalTime,2,companyId);
        String a = "a";
        Page<ChargeDTO> commonAreasIngress = chargeService.findPaidChargesBetweenDates(initialTime,finalTime,3,companyId);
        Page<ChargeDTO> otherIngress = chargeService.findPaidChargesBetweenDates(initialTime,finalTime,4,companyId);

        MensualAndAnualIngressReportDTO mensualAndAnualIngressReportDTO = new MensualAndAnualIngressReportDTO();

        mensualAndAnualIngressReportDTO.setMaintenanceIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(maintenanceIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(mensualAndAnualIngressReportDTO.getMaintenanceIngress(),1);

        mensualAndAnualIngressReportDTO.setExtraOrdinaryIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(extraOrdinaryIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(mensualAndAnualIngressReportDTO.getExtraOrdinaryIngress(),2);

        mensualAndAnualIngressReportDTO.setCommonAreasIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(commonAreasIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(mensualAndAnualIngressReportDTO.getCommonAreasIngress(),3);

        mensualAndAnualIngressReportDTO.setOtherIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(otherIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(mensualAndAnualIngressReportDTO.getOtherIngress(),4);

        mensualAndAnualIngressReportDTO.setAllIngressCategoriesTotal();
        mensualAndAnualIngressReportDTO.setPercetagePerCategory();

        return mensualAndAnualIngressReportDTO;
    }



    public MensualAndAnualEgressReportDTO getMensualAndAnualEgressReportDTO( String initialTime, String finalTime, long companyId,MensualAndAnualIngressReportDTO mensualAndAnualIngressReportDTO){
        Page<EgressCategoryDTO> egressCategories = egressCategoryService.findAll(companyId);
        Page<EgressDTO> allEgressList = egressService.findByDatesBetweenAndCompany(initialTime,finalTime,companyId);

        MensualAndAnualEgressReportDTO mensualAndAnualEgressReportDTO = new MensualAndAnualEgressReportDTO();
        mensualAndAnualEgressReportDTO.setCategoriesNames(allEgressList,egressCategories);

        mensualAndAnualEgressReportDTO.setFixedCosts(mensualAndAnualEgressReportDTO.getSumCategoryEgress(mensualAndAnualEgressReportDTO.getFixedCosts(),"Gastos fijos",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualAndAnualEgressReportDTO.setGroupsTotal(mensualAndAnualEgressReportDTO.getFixedCosts(),1,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualAndAnualEgressReportDTO.setVariableCosts(mensualAndAnualEgressReportDTO.getSumCategoryEgress(mensualAndAnualEgressReportDTO.getVariableCosts(),"Gastos variables",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualAndAnualEgressReportDTO.setGroupsTotal(mensualAndAnualEgressReportDTO.getVariableCosts(),2,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualAndAnualEgressReportDTO.setOtherCosts(mensualAndAnualEgressReportDTO.getSumCategoryEgress(mensualAndAnualEgressReportDTO.getOtherCosts(),"Otros gastos",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualAndAnualEgressReportDTO.setGroupsTotal(mensualAndAnualEgressReportDTO.getOtherCosts(),3,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        return mensualAndAnualEgressReportDTO;
    }

}
