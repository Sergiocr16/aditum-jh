

package com.lighthouse.aditum.service;
import com.lighthouse.aditum.service.dto.SoporteDTO;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.util.Locale;

@Service

public class SoporteMailService {
    private final JHipsterProperties jHipsterProperties;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;

    private final MailService mailService;

    private final CompanyService companyService;

    private static final String SUPPORTMESAGGE = "supportMessage";

    private static final String COMPANY = "company";

    private static final String BASE_URL = "baseUrl";


    private final Logger log = LoggerFactory.getLogger(ComplaintMailService.class);


    public SoporteMailService(CompanyService companyService, MailService mailService, JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyService = companyService;
    }


    private String defineContent(SoporteDTO soporteDTO) {

        Locale locale = new Locale("es", "CR");
        Context context = new Context(locale);
        context.setVariable(SUPPORTMESAGGE, soporteDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(COMPANY, this.companyService.findOne(soporteDTO.getCompanyId()));

        return templateEngine.process("soporteMail", context);
    }

    @Async
    public void sendEmail(SoporteDTO soporteDTO) {
        String subject = "Nuevo mensaje de soporte - " + soporteDTO.getSubject();
        String content = defineContent(soporteDTO);
        this.mailService.sendEmail("diegobari06@gmail.com", subject, content, false, true);
        this.mailService.sendEmail("sergiojcr16@gmail.com", subject, content, false, true);
    }


}


