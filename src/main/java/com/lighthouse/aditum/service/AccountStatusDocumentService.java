
package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.CompanyConfiguration;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.util.RandomUtil;
import com.lowagie.text.DocumentException;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class AccountStatusDocumentService {
    private Locale locale = new Locale("es", "CR");
    private static final String COMPANY = "company";
    private static final String BASE_URL = "baseUrl";
    private static final String HOUSE = "house";
    private static final String ACCOUNTSTATUS = "accountStatus";
    private static final String CURRENT_DATE = "currentDate";
    private static final String INITIALTIME = "initialTime";
    private static final String FINALTIME = "finalTime";
    private static final String CURRENCY = "currency";
    private static final String LOGO = "logo";
    private static final String LOGO_ADMIN = "logoAdmin";
    private static final String ADMINISTRATION_CONFIGURATION = "adminConfig";

    private final Logger log = LoggerFactory.getLogger(CollectionTableDocumentService.class);
    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final SpringTemplateEngine templateEngine;
    private final MailService mailService;
    private final CompanyConfigurationService companyConfigurationService;
    private final ResidentService residentService;
    private final ChargeService chargeService;
    private final PaymentDocumentService paymentDocumentService;



    public AccountStatusDocumentService(PaymentDocumentService paymentDocumentService,ChargeService chargeService, ResidentService residentService, CompanyConfigurationService companyConfigurationService, SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties, CompanyService companyService, CompanyMapper companyMapper, MailService mailService) {
        this.companyMapper = companyMapper;
        this.companyService = companyService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyConfigurationService = companyConfigurationService;
        this.residentService = residentService;
        this.chargeService = chargeService;
        this.paymentDocumentService = paymentDocumentService;
    }


    public File obtainFileToPrint(AccountStatusDTO accountStatusDTO, HouseDTO houseDTO, String initialTime, String finalTime) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(houseDTO.getCompanyId()));
//        Se consulta el currency
        String currency = companyConfigurationService.getByCompanyId(null, houseDTO.getCompanyId()).getContent().get(0).getCurrency();

        String fileName = "Estado de cuenta - filial " + houseDTO.getHousenumber() + ".pdf";
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(HOUSE, houseDTO.getHousenumber());
            contextTemplate.setVariable(COMPANY, company);
            contextTemplate.setVariable(INITIALTIME, initialTime);
            contextTemplate.setVariable(FINALTIME, finalTime);
            accountStatusDTO.formatMoneyReport(currency);
            contextTemplate.setVariable(ACCOUNTSTATUS, accountStatusDTO);
            contextTemplate.setVariable(CURRENCY, currency);
            contextTemplate.setVariable(LOGO, company.getLogoUrl());
            contextTemplate.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE, timeNowFormatted);
            String contentTemplate = templateEngine.process("accountStatusTemplate", contextTemplate);
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

    @Async
    public void sendEmail(AccountStatusDTO accountStatusDTO, HouseDTO houseDTO, String initialTime, String finalTime) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(houseDTO.getCompanyId()));
        Context context = new Context();
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(HOUSE, houseDTO.getHousenumber());
        context.setVariable(COMPANY, company);
        String content = templateEngine.process("accountStatusEmail", context);
        String subject = "Estado de cuenta - filial " + houseDTO.getHousenumber();
        String fileName = "Estado de cuenta - filial " + houseDTO.getHousenumber() + ".pdf";

        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(HOUSE, houseDTO.getHousenumber());
            contextTemplate.setVariable(COMPANY, company);
            contextTemplate.setVariable(INITIALTIME, initialTime);
            contextTemplate.setVariable(FINALTIME, finalTime);
            contextTemplate.setVariable(ACCOUNTSTATUS, accountStatusDTO);


            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE, timeNowFormatted);

            String contentTemplate = templateEngine.process("accountStatusTemplate", contextTemplate);
            OutputStream outputStream = new FileOutputStream(fileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(contentTemplate);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileName);
            int emailsToSend = accountStatusDTO.getEmailTo().size();
            for (int i = 0; i < accountStatusDTO.getEmailTo().size(); i++) {
                this.mailService.sendEmailWithAtachment
                    (company.getId(), accountStatusDTO.getEmailTo().get(i).getEmail(), subject, content, true, file, emailsToSend - 1, i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendAccountsStatusAcumulative(AccountStatusToSendDTO accountStatus, String emailTo, ZonedDateTime month, String currency, AdministrationConfigurationDTO adminConfig) throws IOException, DocumentException {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(accountStatus.getHouse().getCompanyId()));
        Context context = new Context();
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("MMMM", locale);
        DateTimeFormatter spanishYear = DateTimeFormatter.ofPattern("YYYY", locale);
        String monthToShow = spanish.format(month);
        String yearToShow = spanishYear.format(month);
        String subject = "Estado de cuenta "+monthToShow+" "+yearToShow+" - Filial " + accountStatus.getHouse().getHousenumber() + " - "+company.getName();
        context.setVariable(CURRENCY, currency);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(COMPANY, company);
        context.setVariable(ACCOUNTSTATUS, accountStatus);
        context.setVariable(ADMINISTRATION_CONFIGURATION, adminConfig);
        context.setVariable(CURRENT_DATE, monthToShow);
        String content = templateEngine.process("accountStatusRecopilation", context);
        String[] emailsToSend = emailTo.split(",");
        ZonedDateTime lastDay = month.with(TemporalAdjusters.lastDayOfMonth()).withMinute(59).withHour(23).withSecond(59);
        ZonedDateTime firstDay = month.with(TemporalAdjusters.firstDayOfMonth()).withMinute(0).withHour(0).withSecond(0);
        ArrayList<File> files = new ArrayList(); 
        if(accountStatus.isHasNegativeBalance()){
            ResidentDTO r = this.residentService.findPrincipalContactByHouse(accountStatus.getHouse().getId());
            for (int i = 0; i < accountStatus.getCharges().size(); i++) {
                ChargeDTO c = accountStatus.getCharges().get(i);
                if(c.getTemporalAmmount().equals("c")){
                    if(c.getDate().isAfter(firstDay) && c.getDate().isBefore(lastDay)){
                        File file = this.paymentDocumentService.getChargeBillFile(adminConfig,accountStatus.getHouse(),c,r);
                        if(file!=null){
                            files.add(file);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < emailsToSend.length; i++) {
            if (emailsToSend[i] != "") {
                ResidentDTO r = this.residentService.findOne(Long.parseLong(emailsToSend[i]));
                this.mailService.sendEmailWithSeveralAtachment(company.getId(), r.getEmail(), subject, content, false, files);
            }
        }
    }


}

