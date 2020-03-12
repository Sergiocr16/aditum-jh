package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);
    private static final String USER = "user";
    private static final String HOUSE = "house";
    private static final String INITIALTIME = "initialTime";
    private static final String FINALTIME = "finalTime";
    private static final String BASE_URL = "baseUrl";
    private static final String COMPANY = "company";
    private static final String IS_ADMIN = "isAdmin";


    private final JHipsterProperties jHipsterProperties;
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final HouseService houseService;
    private final HouseMapper houseMapper;
    private final ChargeService chargeService;
    private final ResidentService residentService;
    private final AdminInfoService adminInfoService;

    public MailService(ResidentService residentService, AdminInfoService adminInfoService, ChargeService chargeService, HouseService houseService, HouseMapper houseMapper, CompanyMapper companyMapper, CompanyService companyService, JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.companyService = companyService;
        this.companyMapper = companyMapper;
        this.houseMapper = houseMapper;
        this.houseService = houseService;
        this.chargeService = chargeService;
        this.residentService = residentService;
        this.adminInfoService = adminInfoService;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailWithAtachment(String to, String subject, String content, boolean isHtml, File file, int emailsToSend, int currentEmailNumber) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", true, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            message.addAttachment(file.getName(), file);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
            if (currentEmailNumber == emailsToSend) {
                file.delete();
            }
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(User user) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> actualSendCreationEmail(user), 10, TimeUnit.SECONDS);
    }

    @Async
    public void actualSendCreationEmail(User user) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        AdminInfoDTO adminInfo = null;
        ResidentDTO resident = null;
        CompanyDTO company = null;
        HouseDTO house = null;
        String authorityName = user.getAuthorities().iterator().next().getName();
        Boolean isAdmin = false;
        String subject = null;
        if (authorityName.equals("ROLE_MANAGER")) {
            isAdmin = true;
            context.setVariable(IS_ADMIN, true);
            subject = user.getFirstName() + ", Bienvenido a ADITUM";
            context.setVariable(COMPANY, company);
        }

        if (authorityName.equals("ROLE_USER")) {
            resident = this.residentService.findOneByUserId(user.getId());
            company = this.companyService.findOne(resident.getCompanyId());
            context.setVariable(IS_ADMIN, false);
            isAdmin = false;
            context.setVariable(COMPANY, company);
            subject = user.getFirstName() + ", Bienvenido a ADITUM - " + company.getName();

        }
        if (authorityName.equals("ROLE_OWNER")) {
            resident = this.residentService.findOneByUserId(user.getId());
            isAdmin = true;
            context.setVariable(IS_ADMIN, false);
            company = this.companyService.findOne(resident.getCompanyId());
            context.setVariable(COMPANY, company);
            subject = user.getFirstName() + ", Bienvenido a ADITUM - " + company.getName();

        }

        String content = templateEngine.process("creationEmail", context);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendAbsenceEmail(House house, User user, String companyName) {
        log.debug("Sending creation e-mail to '{}'", house.getId());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(HOUSE, house);
        context.setVariable(INITIALTIME, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(house.getDesocupationinitialtime()));
        context.setVariable(FINALTIME, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(house.getDesocupationfinaltime()));
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("absenceEmail", context);
        String subject = "Reporte de ausencia en filial en condominio " + companyName;
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }


}
