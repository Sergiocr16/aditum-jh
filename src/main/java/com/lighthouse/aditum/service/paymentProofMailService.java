package com.lighthouse.aditum.service;

import com.lighthouse.aditum.service.dto.PaymentProofDTO;
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

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service

public class paymentProofMailService {
    private final JHipsterProperties jHipsterProperties;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;

    private final MailService mailService;

    private final CompanyService companyService;

    private static final String PAYMENTPROOF = "paymentProof";

    private static final String PAYMENT_DATE = "paymentDate";


    private static final String COMPANY = "company";

    private static final String BASE_URL = "baseUrl";

    private final AdminInfoService adminInfoService;

    private final Logger log = LoggerFactory.getLogger(ComplaintMailService.class);


    public paymentProofMailService(AdminInfoService adminInfoService, CompanyService companyService, MailService mailService, JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyService = companyService;
        this.adminInfoService = adminInfoService;
    }


    private String defineContent(PaymentProofDTO paymentProofDTO) {

        Locale locale = new Locale("es", "CR");
        Context context = new Context(locale);
        context.setVariable(PAYMENTPROOF, paymentProofDTO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        context.setVariable(PAYMENT_DATE, paymentProofDTO.getRegisterDate().format(formatter));
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(COMPANY, this.companyService.findOne(paymentProofDTO.getCompanyId()));


        String emailContent = "";
        if(this.companyService.findOne(paymentProofDTO.getCompanyId()).getEmailConfiguration().getAdminCompanyName().equals("ADITUM")){
            emailContent =  templateEngine.process("paymentProofMail", context);
        }else{
            emailContent = templateEngine.process("paymentProofMailNoAditum", context);
        }

        return emailContent;
    }

    @Async
    public void sendEmail(PaymentProofDTO paymentProofDTO) {
        String subject = "Comprobante de pago - Filial " + paymentProofDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(paymentProofDTO.getCompanyId()).getName();
        String content = defineContent(paymentProofDTO);
        this.adminInfoService.findAllByCompany(null, paymentProofDTO.getCompanyId()).getContent().forEach(adminInfoDTO -> {
            this.mailService.sendEmail(adminInfoDTO.getCompanyId(), adminInfoDTO.getEmail(), subject, content, false, true);
        });
    }


}


