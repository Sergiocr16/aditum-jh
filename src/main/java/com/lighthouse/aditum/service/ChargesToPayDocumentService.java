package com.lighthouse.aditum.service;
    import com.lighthouse.aditum.domain.Company;
    import com.lighthouse.aditum.service.dto.ChargesToPayReportDTO;
    import com.lighthouse.aditum.service.mapper.CompanyMapper;
    import com.lighthouse.aditum.service.util.RandomUtil;
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
    import java.text.NumberFormat;
    import java.time.ZonedDateTime;
    import java.time.format.DateTimeFormatter;
    import java.time.format.FormatStyle;
    import java.util.Locale;

    import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;
    import static com.lighthouse.aditum.service.util.RandomUtil.formatMoneyString;

@Service
@Transactional
public class ChargesToPayDocumentService {
    private static final String COMPANY = "company";
    private static final String BASE_URL = "baseUrl";
    private static final String REPORT = "report";
    private static final String FILTERING = "filtering";
    private static final String FILTERTYPE = "filterType";
    private static final String CURRENT_DATE = "currentDate";
    private static final String FINALTIME = "finalTime";

    private final Logger log = LoggerFactory.getLogger(ChargesToPayDocumentService.class);
    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final ChargeService chargeService;
    private final CompanyMapper companyMapper;
    private final SpringTemplateEngine templateEngine;
    private final CompanyConfigurationService companyConfigurationService;

    public ChargesToPayDocumentService(CompanyConfigurationService companyConfigurationService,ChargeService chargeService,SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties,CompanyService companyService, CompanyMapper companyMapper){
        this.companyMapper = companyMapper;
        this.companyService = companyService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.chargeService = chargeService;
        this.companyConfigurationService = companyConfigurationService;
    }

    public File obtainFileToPrint(ZonedDateTime finalDate,int type, Long companyId) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(companyId));
        String fileName = "Reporte de cuotas por cobrar.pdf";
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY,company);
            ChargesToPayReportDTO chargesToPayReportDTO = this.chargeService.findChargesToPay(finalDate,type,companyId);
            Locale locale = new Locale("es", "CR");
            DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(locale);
            String currency = companyConfigurationService.getByCompanyId(null,companyId).getContent().get(0).getCurrency();

            chargesToPayReportDTO.getDueHouses().forEach(dueHouseDTO -> {
                dueHouseDTO.getDues().forEach(chargeDTO -> {
                    chargeDTO.setPaymentAmmount(formatMoney(currency,chargeDTO.getTotal()));
                    chargeDTO.setAmmount(formatMoneyString(currency,chargeDTO.getAmmount()));
                    chargeDTO.setSubcharge(formatMoneyString(currency,chargeDTO.getSubcharge()));
                    chargeDTO.setFormatedDate(pattern.ofPattern("dd MMMM yyyy").format(chargeDTO.getDate()));
                });
            });
            contextTemplate.setVariable(REPORT,chargesToPayReportDTO);
            contextTemplate.setVariable(FILTERTYPE,type);
            if (type != 5) {
                contextTemplate.setVariable(FILTERING,true);
            }else{
                contextTemplate.setVariable(FILTERING,false);
            }

            ZonedDateTime zd_finalTime = ZonedDateTime.parse(finalDate+"[America/Regina]");
            String finalTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_finalTime);
            contextTemplate.setVariable(FINALTIME,"Previas al "+finalTimeFormatted);
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);
            String contentTemplate = templateEngine.process("chargesToPayReportTemplate", contextTemplate);
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
