package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import com.sendgrid.*;
import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.core.env.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.apache.commons.codec.binary.Base64;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Collection;
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
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;
    private final CompanyService companyService;
    private final ResidentService residentService;
    private final EmailConfigurationService emailConfigurationService;
    private final CompanyConfigurationService companyConfigurationService;
    private final Environment env;


    public MailService(CompanyConfigurationService companyConfigurationService, Environment env, EmailConfigurationService emailConfigurationService, ResidentService residentService, AdminInfoService adminInfoService, ChargeService chargeService, HouseService houseService, HouseMapper houseMapper, CompanyMapper companyMapper, CompanyService companyService, JHipsterProperties jHipsterProperties, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.companyService = companyService;
        this.residentService = residentService;
        this.emailConfigurationService = emailConfigurationService;
        this.companyConfigurationService = companyConfigurationService;
        this.env = env;
    }

    private Email defineFromEmail(Long companyId) {
        Email from = new Email("aditum_app@aditumapp.com");
        EmailConfigurationDTO emailConfiguration = null;
        String fromName = "";
        if (companyId != null) {
            emailConfiguration = this.emailConfigurationService.findOneByCompanyId(companyId);
            fromName = this.companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getEmailFromName();
            from.setName(fromName);
        }
        if (emailConfiguration != null) {
            if (emailConfiguration.isCustomEmail()) {
                from = new Email(emailConfiguration.getEmail());
                from.setName(fromName);
            }
        }
        return from;
    }

    @Async
    public void sendEmail(Long companyId, String to1, String subject, String content1, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart, isHtml, to1, subject, content1);
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            Email from = defineFromEmail(companyId);
            Email to = new Email(to1);
            Content content = new Content("text/html", content1);
            Mail mail = new Mail(from, subject, to, content);
            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
            } catch (IOException ex) {
                try {
                    throw ex;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//        EmailConfigurationDTO emailConfiguration = this.emailConfigurationService.findOneByCompanyId(companyId);
//        MimeMessage mimeMessage = null;
//        JavaMailSenderImpl mailSender = null;
//        Properties properties = new Properties();
//        if (emailConfiguration != null) {
//            if (emailConfiguration.isCustomEmail()) {
//                mailSender = new JavaMailSenderImpl();
//                switch (emailConfiguration.getEmailCompany()) {
//                    case "1":
//                        mailSender.setHost("smtp.gmail.com");
//                        properties.put("mail.smtp.host", "smtp.gmail.com");
//                        break;
//                    case "2":
//                        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
//                        mailSender.setHost("smtp-mail.outlook.com");
//                        break;
//                    case "3":
//                        properties.put("mail.smtp.host", "smtp.mail.yahoo.com");
//                        mailSender.setHost("smtp.mail.yahoo.com");
//                        break;
//                }
//                mailSender.setProtocol("smtp");
//                mailSender.setUsername(emailConfiguration.getEmail());
//                mailSender.setPassword(emailConfiguration.getPassword());
//                properties.put("mail.smtp.user", emailConfiguration.getEmail());
//                properties.put("mail.smtp.password", emailConfiguration.getPassword());
//                properties.put("mail.smtp.port", "587");
//                properties.put("mail.smtp.auth", "true");
//                properties.put("mail.smtp.starttls.enable", "true");
//                mailSender.setJavaMailProperties(properties);
//                mimeMessage = mailSender.createMimeMessage();
//            } else {
//                mimeMessage = javaMailSender.createMimeMessage();
//            }
//        } else {
//            mimeMessage = javaMailSender.createMimeMessage();
//        }
//
//        try {
//            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(content, isHtml);
//            if (emailConfiguration != null) {
//                if (emailConfiguration.isCustomEmail()) {
//                    message.setFrom(properties.getProperty("mail.smtp.user"));
//                    mailSender.send(mimeMessage);
//                } else {
//                    message.setFrom(jHipsterProperties.getMail().getFrom());
//                    javaMailSender.send(mimeMessage);
//                }
//            } else {
//                message.setFrom(jHipsterProperties.getMail().getFrom());
//                javaMailSender.send(mimeMessage);
//            }
//
//            log.debug("Sent e-mail to User '{}'", to);
//        } catch (Exception e) {
//            log.warn("E-mail could not be sent to user '{}'", to, e);
//        }
        }
    }

    @Async
    public void sendEmailWithSeveralAtachment(Long companyId, String to1, String subject, String content1, boolean isHtml, ArrayList<File> files) throws IOException {
        Email from = defineFromEmail(companyId);
        Email to = new Email(to1);
        Content content = new Content("text/html", content1);
        Mail mail = new Mail(from, subject, to, content);
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            byte[] filedata = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
            Base64 x = new Base64();
            String imageDataString = x.encodeAsString(filedata);
            Attachments attachments = new Attachments();
            attachments.setContent(imageDataString);
            attachments.setType("application/pdf");//"application/pdf"
            attachments.setFilename(file.getName());
            attachments.setDisposition("attachment");
            attachments.setContentId("Banner");
            mail.addAttachments(attachments);
        }
        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            try {
                throw ex;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(40000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (File file : files) {
                    try {
                        FileDeleteStrategy.FORCE.delete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Async
    public void sendEmailWithAtachment(Long companyId, String to1, String subject, String content1, boolean isHtml, File file) throws IOException {
        Email from = defineFromEmail(companyId);
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            Email to = new Email(to1);
            Content content = new Content("text/html", content1);
            Mail mail = new Mail(from, subject, to, content);
            byte[] filedata = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
            Base64 x = new Base64();
            String imageDataString = x.encodeAsString(filedata);
            Attachments attachments = new Attachments();
            attachments.setContent(imageDataString);
            attachments.setType("application/pdf");//"application/pdf"
            attachments.setFilename(file.getName());
            attachments.setDisposition("attachment");
            attachments.setContentId("Banner");
            mail.addAttachments(attachments);
            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
            } catch (IOException ex) {
                try {
                    throw ex;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            new Thread() {
                @Override
                public void run() {
                    try {
                        this.sleep(40000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    file.delete();
                }
            }.start();
        }
    }


    @Async
    public void sendEmailWithAtachment(Long companyId, String to1, String subject, String content1, boolean isHtml, File file, int emailsToSend, int currentEmailNumber) throws IOException {
        Email from = defineFromEmail(companyId);
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            Email to = new Email(to1);
            Content content = new Content("text/html", content1);
            Mail mail = new Mail(from, subject, to, content);
            byte[] filedata = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
            Base64 x = new Base64();
            String imageDataString = x.encodeAsString(filedata);
            Attachments attachments = new Attachments();
            attachments.setContent(imageDataString);
            attachments.setType("application/pdf");//"application/pdf"
            attachments.setFilename(file.getName());
            attachments.setDisposition("attachment");
            attachments.setContentId("Banner");
            mail.addAttachments(attachments);
            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
                if (currentEmailNumber == emailsToSend) {
                    file.delete();
                }
            } catch (IOException ex) {
                try {
                    throw ex;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
//        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(User user) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            try {
                actualSendCreationEmail(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 10, TimeUnit.SECONDS);
    }

    private String defineBaseUrl(String app) {
        Collection<String> activeProfiles = Arrays.asList(this.env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)) {
            return jHipsterProperties.getMail().getBaseUrl();
        } else {
            if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION))
                if (app.equals("ADITUM")) {
                    return "https://app.aditumcr.com";
                } else {
                    return "https://app.convivecr.com";
                }
        }
        return jHipsterProperties.getMail().getBaseUrl();
    }

    @Async
    public void actualSendCreationEmail(User user) throws IOException {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
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
            context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        }

        if (authorityName.equals("ROLE_USER")) {
            resident = this.residentService.findOneByUserId(user.getId());
            company = this.companyService.findOne(resident.getCompanyId());
            context.setVariable(IS_ADMIN, false);
            isAdmin = false;
            context.setVariable(COMPANY, company);
            if (company.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
                context.setVariable(BASE_URL, this.defineBaseUrl("ADITUM"));
            } else {
                context.setVariable(BASE_URL, this.defineBaseUrl("CONVIVE"));
            }
            subject = user.getFirstName() + ", Bienvenido a ADITUM - " + company.getName();
        }
        if (authorityName.equals("ROLE_OWNER")) {
            resident = this.residentService.findOneByUserId(user.getId());
            isAdmin = true;
            context.setVariable(IS_ADMIN, false);
            company = this.companyService.findOne(resident.getCompanyId());
            if (company.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
                context.setVariable(BASE_URL, this.defineBaseUrl("ADITUM"));
            } else {
                context.setVariable(BASE_URL, this.defineBaseUrl("CONVIVE"));
            }
            context.setVariable(COMPANY, company);
            subject = user.getFirstName() + ", Bienvenido a ADITUM - " + company.getName();
        }
        String content = "";
        if (authorityName.equals("ROLE_MANAGER")) {
            content = templateEngine.process("creationEmailNoAditum", context);
            sendEmail(null, user.getEmail(), subject, content, false, true);
        } else {
            if (company.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
                context.setVariable(BASE_URL, this.defineBaseUrl("ADITUM"));
                content = templateEngine.process("creationEmail", context);
            } else {
                context.setVariable(BASE_URL, this.defineBaseUrl("CONVIVE"));
                subject = user.getFirstName() + ", Bienvenido a CONVIVE";
                content = templateEngine.process("creationEmailNoAditum", context);
            }
            sendEmail(company.getId(), user.getEmail(), subject, content, false, true);
        }
    }

    @Async
    public void sendAbsenceEmail(House house, User user, String companyName) throws IOException {
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
        sendEmail(house.getCompany().getId(), user.getEmail(), subject, content, false, true);
    }

    @Transactional
    @Async
    public void sendPasswordResetMail(User user) throws IOException {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        CompanyDTO company = null;
        ResidentDTO resident = null;
        resident = this.residentService.findOneByUserIdResetEmail(user.getId());
        String subject = "Reinicio de contraseña de ADITUM";
        if (resident != null) {
            company = this.companyService.findOne(resident.getCompanyId());
            if (company.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
                context.setVariable(BASE_URL, this.defineBaseUrl("ADITUM"));
                subject = "Reinicio de contraseña de ADITUM";
            } else {
                context.setVariable(BASE_URL, this.defineBaseUrl("CONVIVE"));
                subject = "Reinicio de contraseña de ConviveCR";
            }
        } else {
            context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        }
        String content = templateEngine.process("passwordResetEmail", context);
        sendEmail(company != null ? company.getId() : null, user.getEmail(), subject, content, false, true);
    }


}
