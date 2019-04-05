
package com.lighthouse.aditum.service;

    import com.lighthouse.aditum.domain.Company;
    import com.lighthouse.aditum.service.dto.AccountStatusDTO;
    import com.lighthouse.aditum.service.dto.HouseDTO;
    import com.lighthouse.aditum.service.dto.HouseYearCollectionDTO;
    import com.lighthouse.aditum.service.dto.MensualReportDTO;
    import com.lighthouse.aditum.service.mapper.CompanyMapper;
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

    private final Logger log = LoggerFactory.getLogger(CollectionTableDocumentService.class);
    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final SpringTemplateEngine templateEngine;
    private final MailService mailService;


    public AccountStatusDocumentService(SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties,CompanyService companyService, CompanyMapper companyMapper,MailService mailService){
        this.companyMapper = companyMapper;
        this.companyService = companyService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
    }


    public File obtainFileToPrint(AccountStatusDTO accountStatusDTO, HouseDTO houseDTO,String initialTime, String finalTime) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(houseDTO.getCompanyId()));
        String fileName = "Estado de cuenta - filial "+ houseDTO.getHousenumber()+".pdf";
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(HOUSE,houseDTO.getHousenumber());
            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(INITIALTIME,initialTime);
            contextTemplate.setVariable(FINALTIME,finalTime);
            contextTemplate.setVariable(ACCOUNTSTATUS,accountStatusDTO);


            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);

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
    public void sendEmail(AccountStatusDTO accountStatusDTO, HouseDTO houseDTO,String initialTime, String finalTime) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(houseDTO.getCompanyId()));
        Context context = new Context();
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(HOUSE,houseDTO.getHousenumber());
        context.setVariable(COMPANY,company);
        String content = templateEngine.process("accountStatusEmail", context);
        String subject = "Estado de cuenta - filial " + houseDTO.getHousenumber();
        String fileName = "Estado de cuenta - filial "+houseDTO.getHousenumber()+".pdf";

        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(HOUSE,houseDTO.getHousenumber());
            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(INITIALTIME,initialTime);
            contextTemplate.setVariable(FINALTIME,finalTime);
            contextTemplate.setVariable(ACCOUNTSTATUS,accountStatusDTO);


            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);

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
                    (accountStatusDTO.getEmailTo().get(i).getEmail(), subject, content, true, file,emailsToSend-1,i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

