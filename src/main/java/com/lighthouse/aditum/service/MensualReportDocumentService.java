package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.service.dto.HouseYearCollectionDTO;
import com.lighthouse.aditum.service.dto.MensualReportDTO;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lowagie.text.DocumentException;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class MensualReportDocumentService {
    private Locale locale = new Locale("es", "CR");
    private static final String COMPANY = "company";
    private static final String BASE_URL = "baseUrl";
    private static final String MENSUALDTO = "mensualDTO";
    private static final String INITIALTIME = "initialTime";
    private static final String FINALTIME = "finalTime";
    private static final String CURRENT_DATE = "currentDate";

    private static final String SHOW_MAINTENANCE_INGRESS = "showMaintenanceIngress";
    private static final String SHOW_EXTRAORDINARY_INGRESS = "showExtraordinaryIngress";
    private static final String SHOW_COMMONAREA_INGRESS = "showCommonAreaIngress";
    private static final String SHOW_OTHER_INGRESS = "showOtherIngress";
    private static final String SHOWFIELDS = "showFields";
    private static final String INGRESSBUDGETDIFERENCE = "ingressBudgetDiference";

    private static final String INGRESSBUDGETDIFERENCEFORMATTED = "ingressBudgetDiferenceFormatted";

    private static final String ALLEGRESSPERCENTAGEQUANTITY = "allEgressPercentageQuantity";

    private static final String TOTALEGRESSBUDGET = "totalEgressBudget";
    private static final String EGRESSBUDGETDIFERENCE = "egressBudgetDiference";

    private static final String TOTALEGRESSBUDGETFORMATTED = "totalEgressBudgetFormatted";
    private static final String EGRESSBUDGETDIFERENCEFORMATTED = "egressBudgetDiferenceFormatted";

    private static final String SUPERHABITPERCENTAGE = "superHabitPercentage";



    private static final String SUPERHABIT = "superHabit";
    private static final String SALDONETO = "saldoNeto";

    private static final String SUPERHABITFORMATTED = "superHabitFormatted";
    private static final String SALDONETOFORMATTED = "saldoNetoFormatted";

    private final Logger log = LoggerFactory.getLogger(CollectionTableDocumentService.class);
    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final SpringTemplateEngine templateEngine;

    public MensualReportDocumentService(SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties,CompanyService companyService, CompanyMapper companyMapper){
        this.companyMapper = companyMapper;
        this.companyService = companyService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
    }
    private String formatMoney(double ammount){
        DecimalFormat format = new DecimalFormat("₡#,##0.00;₡-#,##0.00");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        String total = format.format(ammount);
        return total;
    }

    public File obtainFileToPrint(Long companyId, String initialTime, String finalTime, MensualReportDTO mensualReportDTO,int withPresupuesto) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(companyId));
        String fileName = "Estado de resultados "+".pdf";
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(MENSUALDTO,mensualReportDTO);
            contextTemplate.setVariable(INITIALTIME,initialTime);
            contextTemplate.setVariable(FINALTIME,finalTime);
            if(withPresupuesto==1){
                contextTemplate.setVariable(SHOWFIELDS,true);
            }else{
                contextTemplate.setVariable(SHOWFIELDS,false);
            }

            if(mensualReportDTO.getMensualIngressReport().getMaintenanceIngress().size()==0 && mensualReportDTO.getMensualIngressReport().getMaintenanceBudget()==0){
                contextTemplate.setVariable(SHOW_MAINTENANCE_INGRESS,false);
            }else{
                contextTemplate.setVariable(SHOW_MAINTENANCE_INGRESS,true);
            }


            if(mensualReportDTO.getMensualIngressReport().getExtraOrdinaryIngress().size()==0 && mensualReportDTO.getMensualIngressReport().getExtraordinaryBudget()==0){
                contextTemplate.setVariable(SHOW_EXTRAORDINARY_INGRESS,false);
            }else{
                contextTemplate.setVariable(SHOW_EXTRAORDINARY_INGRESS,true);
            }


            if(mensualReportDTO.getMensualIngressReport().getCommonAreasIngress().size()==0 && mensualReportDTO.getMensualIngressReport().getCommonAreasBudget()==0){
                contextTemplate.setVariable(SHOW_COMMONAREA_INGRESS,false);
            }else{
                contextTemplate.setVariable(SHOW_COMMONAREA_INGRESS,true);
            }

            if(mensualReportDTO.getMensualIngressReport().getOtherIngress().size()==0 && mensualReportDTO.getMensualIngressReport().getOtherBudget()==0){
                contextTemplate.setVariable(SHOW_OTHER_INGRESS,false);
            }else{
                contextTemplate.setVariable(SHOW_OTHER_INGRESS,true);
            }
            double allEgressPercentageQuantity = mensualReportDTO.getMensualEgressReport().getFixedCostsPercentage() + mensualReportDTO.getMensualEgressReport().getVariableCostsPercentage() + mensualReportDTO.getMensualEgressReport().getOtherCostsPercentage();
            double totalEgressBudget = mensualReportDTO.getMensualEgressReport().getFixedCostsBudgetTotal() + mensualReportDTO.getMensualEgressReport().getVariableCostsBudgetTotal() + mensualReportDTO.getMensualEgressReport().getOtherCostsBudgetTotal();
            double ingressBudgetDiference = mensualReportDTO.getMensualIngressReport().getAllIngressCategoriesTotal() - mensualReportDTO.getMensualIngressReport().getTotalBudget();
            double egressBudgetDiference = mensualReportDTO.getMensualEgressReport().getAllEgressCategoriesTotal() - totalEgressBudget;

            contextTemplate.setVariable(INGRESSBUDGETDIFERENCE,ingressBudgetDiference);
            contextTemplate.setVariable(INGRESSBUDGETDIFERENCEFORMATTED,this.formatMoney(ingressBudgetDiference));

            contextTemplate.setVariable(ALLEGRESSPERCENTAGEQUANTITY,allEgressPercentageQuantity);

            contextTemplate.setVariable(TOTALEGRESSBUDGET,totalEgressBudget);
            contextTemplate.setVariable(TOTALEGRESSBUDGETFORMATTED,this.formatMoney(totalEgressBudget));

            contextTemplate.setVariable(EGRESSBUDGETDIFERENCE,egressBudgetDiference);
            contextTemplate.setVariable(EGRESSBUDGETDIFERENCEFORMATTED,this.formatMoney(egressBudgetDiference));

            contextTemplate.setVariable(SUPERHABITPERCENTAGE,100 - allEgressPercentageQuantity);

            double superHabit = (egressBudgetDiference * -1) - (ingressBudgetDiference * -1);

            contextTemplate.setVariable(SUPERHABIT,superHabit);

            contextTemplate.setVariable(SUPERHABITFORMATTED,this.formatMoney(superHabit));
            double saldoNeto = mensualReportDTO.getTotalInitialBalance() + mensualReportDTO.getMensualIngressReport().getAllIngressCategoriesTotal() - mensualReportDTO.getMensualEgressReport().getAllEgressCategoriesTotal();
            contextTemplate.setVariable(SALDONETO,saldoNeto);
            contextTemplate.setVariable(SALDONETOFORMATTED,this.formatMoney(saldoNeto));

            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);
            String contentTemplate = templateEngine.process("mensualReportTemplate", contextTemplate);
            OutputStream outputStream = new FileOutputStream(fileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(contentTemplate);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileName);


            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}

