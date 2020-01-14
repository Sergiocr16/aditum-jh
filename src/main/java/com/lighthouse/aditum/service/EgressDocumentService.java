package com.lighthouse.aditum.service;

    import com.lighthouse.aditum.domain.Company;
    import com.lighthouse.aditum.service.dto.AccountStatusDTO;
    import com.lighthouse.aditum.service.dto.EgressDTO;
    import com.lighthouse.aditum.service.dto.EgressReportDTO;
    import com.lighthouse.aditum.service.dto.HouseDTO;
    import com.lighthouse.aditum.service.mapper.CompanyMapper;
    import com.lowagie.text.DocumentException;
    import io.github.jhipster.config.JHipsterProperties;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.data.domain.Page;
    import org.springframework.scheduling.annotation.Async;
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

    import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;

@Service
@Transactional
public class EgressDocumentService {
    private Locale locale = new Locale("es", "CR");
    private static final String COMPANY = "company";
    private static final String BASE_URL = "baseUrl";
    private static final String CURRENT_DATE = "currentDate";
    private static final String EGRESS_REPORT_DTO = "egressReportDTO";
    private static final String INITIALTIME = "initialTime";
    private static final String FINALTIME = "finalTime";
    private static final String EGRESS_REPORT = "egress_report";
    private static final String TOTAL = "total";
    private static final String CURRENCY = "currency";
    private static final String TOTAL_EGRESS_TO_PAY = "total_egress_to_pay";
    private final Logger log = LoggerFactory.getLogger(CollectionTableDocumentService.class);
    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final SpringTemplateEngine templateEngine;
    private final MailService mailService;
    private final CompanyConfigurationService companyConfigurationService;


    public EgressDocumentService(CompanyConfigurationService companyConfigurationService,SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties,CompanyService companyService, CompanyMapper companyMapper,MailService mailService){
        this.companyMapper = companyMapper;
        this.companyService = companyService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyConfigurationService = companyConfigurationService;
    }


    public File obtainFileToPrint(EgressReportDTO egressReportDTO,String initialTime, String finalTime,Long companyId) {
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es","ES"));
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(companyId));

        String currency = companyConfigurationService.getByCompanyId(null,companyId).getContent().get(0).getCurrency();


        String fileName = "Reporte de egresos " + company.getName() + ".pdf";
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());

            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(EGRESS_REPORT_DTO,egressReportDTO);
            contextTemplate.setVariable(INITIALTIME,initialTime);
            contextTemplate.setVariable(FINALTIME,finalTime);
            contextTemplate.setVariable(TOTAL, formatMoney(currency,egressReportDTO.getTotal()));
            contextTemplate.setVariable(CURRENCY,currency);

            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted =  spanish.format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);

            String contentTemplate = templateEngine.process("egressReportTemplate", contextTemplate);
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
    public File obtainFileToPrintEgressToPay(Page<EgressDTO> egressReportDTO, String finalTime, Long companyId,String totalEgressToPay) {
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es","ES"));
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(companyId));
        String fileName = "Reporte de cuentas por pagar " + company.getName() + ".pdf";
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());

            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(EGRESS_REPORT,egressReportDTO);
            contextTemplate.setVariable(FINALTIME,finalTime);
            contextTemplate.setVariable(TOTAL_EGRESS_TO_PAY,totalEgressToPay);

            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = spanish.format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);

            String contentTemplate = templateEngine.process("egressToPayReportTemplate", contextTemplate);
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

