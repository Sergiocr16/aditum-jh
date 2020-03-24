package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.service.dto.BancoDTO;
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
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@Transactional
public class BancoDocumentService {

    private final Logger log = LoggerFactory.getLogger(BancoDocumentService.class);
    private Locale locale = new Locale("es", "CR");
    private static final String COMPANY = "company";
    private static final String BASE_URL = "baseUrl";
    private static final String CURRENT_DATE = "currentDate";
    private static final String BANCO_DTO = "bancoDTO";
    private static final String INITIALTIME = "initialTime";
    private static final String FINALTIME = "finalTime";
    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final SpringTemplateEngine templateEngine;
    private final MailService mailService;
    private static final String LOGO = "logo";
    private static final String LOGO_ADMIN = "logoAdmin";

    public BancoDocumentService(SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties, CompanyService companyService, CompanyMapper companyMapper, MailService mailService) {
        this.companyMapper = companyMapper;
        this.companyService = companyService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
    }


    public File obtainFileToPrint(BancoDTO bancoDTO, String initialTime, String finalTime, Long companyId) {
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "ES"));
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(companyId));
        String fileName = "Estado de cuenta banco " + company.getName() + ".pdf";
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());

            contextTemplate.setVariable(COMPANY, company);
            contextTemplate.setVariable(BANCO_DTO, bancoDTO);
            contextTemplate.setVariable(INITIALTIME, initialTime);
            contextTemplate.setVariable(FINALTIME, finalTime);

            contextTemplate.setVariable(LOGO,company.getLogoUrl());
            contextTemplate.setVariable(LOGO_ADMIN,company.getAdminLogoUrl());
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE, timeNowFormatted);

            String contentTemplate = templateEngine.process("bancoReportTemplate", contextTemplate);
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
