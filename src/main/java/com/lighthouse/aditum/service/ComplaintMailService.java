package com.lighthouse.aditum.service;
//import org.ocpsoft.prettytime.*;

import com.lighthouse.aditum.domain.AdminInfo;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.dto.ComplaintCommentDTO;
import com.lighthouse.aditum.service.dto.ComplaintDTO;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service

public class ComplaintMailService {
    private final JHipsterProperties jHipsterProperties;
    private final AdminInfoService adminInfoService;
    private final MessageSource messageSource;
    private final ResidentService residentService;
    private final SpringTemplateEngine templateEngine;

    private final MailService mailService;

    private final CompanyService companyService;

    private final PushNotificationService pNotification;


    private static final String COMPLAINT = "complaint";

    private static final String BASE_URL = "baseUrl";

    private static final String RESIDENT = "resident";

    private static final String ADMIN = "admin";

    private static final String ADMIN_EMAIL = "adminEmail";

    private static final String ADMIN_NUMBER = "adminNumber";

    private static final String COMPANY = "company";

    private static final String ANSWER_SIZE = "answerSize";

    private static final String COMPLAINT_COMMENTS = "complaintComments";


    private final Logger log = LoggerFactory.getLogger(ComplaintMailService.class);


    public ComplaintMailService(PushNotificationService pNotification, ResidentService residentService, CompanyService companyService, AdminInfoService adminInfoService, MailService mailService, JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.jHipsterProperties = jHipsterProperties;
        this.adminInfoService = adminInfoService;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyService = companyService;
        this.residentService = residentService;
        this.pNotification = pNotification;
    }


    private String defineContent(ComplaintDTO complaintDTO) {
        Locale locale = new Locale("es", "CR");
        Context context = new Context(locale);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(RESIDENT, complaintDTO.getResident().getName() + " " + complaintDTO.getResident().getLastname() + " " + complaintDTO.getResident().getSecondlastname());
        String complaintDate = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a").format(complaintDTO.getCreationDate());
        context.setVariable(ANSWER_SIZE, complaintDTO.getComplaintComments().getContent().size());

        CompanyDTO company = this.companyService.findOne(complaintDTO.getCompanyId());
        context.setVariable(ADMIN_EMAIL, company.getEmail());
        context.setVariable(COMPANY, company);
        context.setVariable(ADMIN_NUMBER, company.getPhoneNumber());
        complaintDTO.getComplaintComments().forEach(complaintCommentDTO -> {
            if (complaintCommentDTO.getAdminInfoId() != null) {
                AdminInfoDTO adminInfo = this.adminInfoService.findOne(complaintCommentDTO.getAdminInfoId());
                adminInfo.setName(adminInfo.getName() + " " + adminInfo.getLastname() + " " + adminInfo.getSecondlastname());
                complaintCommentDTO.setAdmin(adminInfo);
            }
        });

        context.setVariable(COMPLAINT, complaintDTO);

        List<ComplaintCommentDTO> complaintCommentDTOList = complaintDTO.getComplaintComments().getContent();

        complaintCommentDTOList.forEach(complaintCommentDTO -> {
//            PrettyTime p = new PrettyTime(new Locale("es"));
            complaintCommentDTO.setFormattedCreationDate(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a").format(complaintCommentDTO.getCreationDate().minusHours(6)));
        });

        context.setVariable(COMPLAINT_COMMENTS, complaintCommentDTOList);


        String emailContent = "";
        if (company.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
            if(complaintDTO.getComplaintCategory()==3){
                emailContent = templateEngine.process("releaseEmail", context);
            }else{
                emailContent = templateEngine.process("complaintEmail", context);
            }
        } else {
            if(complaintDTO.getComplaintCategory()==3){
                emailContent = templateEngine.process("releaseEmail", context);
            }else{
                emailContent = templateEngine.process("complaintEmailNoAditum", context);
            }
        }
        return emailContent;
    }

    private String defineStatus(int complaintStatus) {
        String status = "";
        switch (complaintStatus) {
            case 1:
                status = "Pendiente";
                break;
            case 2:
                status = "Activo";
                break;
            case 3:
                status = "Resuelto";
                break;
        }
        return status;
    }

    @Async
    public void sendNewComplaintEmail(ComplaintDTO complaintDTO) throws URISyntaxException {
        CompanyDTO company = this.companyService.findOne(complaintDTO.getCompanyId());
        String subject = "";
        String subjectNoti = "";
        String bodyNotiAdmin = "";
        String bodyNotiResident = "";
        if (complaintDTO.getComplaintCategory() == 3) {
            subject = complaintDTO.getSubject() + " - " +complaintDTO.getHouseNumber()+" - "+ company.getName();
            subjectNoti = complaintDTO.getSubject() + " - " + company.getName();
            bodyNotiAdmin = "Se ha enviado un comunicado individual a la filial "+complaintDTO.getHouseNumber();
            bodyNotiResident = "Se ha enviado un comunicado individual a tu filial, ingresa para ver los detalles.";
        } else if (complaintDTO.getComplaintCategory() == 2) {
            subject = "Ticket # " + complaintDTO.getId() + " (" + defineStatus(complaintDTO.getStatus()) + "), Queja " + company.getName();
            subjectNoti = "Ticket # " + complaintDTO.getId() + " (" + defineStatus(complaintDTO.getStatus()) + "), Queja";
            bodyNotiAdmin = "Se ha creado una nueva queja en la filial " + complaintDTO.getHouseNumber() + " del condominio " + company.getName() + ", ingrese para ver más detalles.";
            bodyNotiResident = "Se ha creado una nueva queja en la filial" + complaintDTO.getHouseNumber() + " del condominio " + company.getName() + ".";
        } else {
            subject = "Ticket # " + complaintDTO.getId() + " (" + defineStatus(complaintDTO.getStatus()) + "), Sugerencia " + company.getName();
            subjectNoti = "Ticket # " + complaintDTO.getId() + " (" + defineStatus(complaintDTO.getStatus()) + "), Sugerencia";
            bodyNotiAdmin = "Se ha creado una nueva queja sugerencia en la filial " + complaintDTO.getHouseNumber() + " del condominio " + company.getName() + ", ingrese para ver más detalles.";
            bodyNotiResident = "Se ha creado una nueva sugerencia en la filial" + complaintDTO.getHouseNumber() + " del condominio " + company.getName() + ".";
        }
        String content = defineContent(complaintDTO);
        this.pNotification.sendNotificationAllAdminsByCompanyId(complaintDTO.getCompanyId(), this.pNotification.createPushNotification(subjectNoti, bodyNotiAdmin));
        this.pNotification.sendNotificationToResident(complaintDTO.getResidentId(), this.pNotification.createPushNotification(subjectNoti, bodyNotiResident));
        this.mailService.sendEmail(complaintDTO.getCompanyId(), this.residentService.findOne(complaintDTO.getResidentId()).getEmail(), subject, content, false, true);
        String finalSubject = subject;
        this.adminInfoService.findAllByCompany(null, complaintDTO.getCompanyId()).getContent().forEach(adminInfoDTO -> {
            this.mailService.sendEmail(complaintDTO.getCompanyId(), adminInfoDTO.getEmail(), finalSubject, content, false, true);
        });
    }

    @Async
    public void sendComplaintEmailChangeStatus(ComplaintDTO complaintDTO) throws URISyntaxException {
        CompanyDTO company = this.companyService.findOne(complaintDTO.getCompanyId());
        String content = defineContent(complaintDTO);
        String subject = "";
        String subjectNoti = "";
        String bodyNotiAdmin = "";
        String bodyNotiResident = "";
        if (complaintDTO.getComplaintCategory() == 3) {
            subject = "Respuesta comunicado/" +complaintDTO.getSubject() + " - " +complaintDTO.getHouseNumber()+" - "+ company.getName();
            subjectNoti = "Respuesta comunicado/"+ complaintDTO.getSubject() + " - " + company.getName();
            bodyNotiAdmin = "Se ha enviado una respuesta del comunicado individual";
            bodyNotiResident = "Se ha enviado una respuesta del comunicado individual";
        } else if (complaintDTO.getComplaintCategory() == 2) {
            subject = "Ticket # " + complaintDTO.getId() + " (" + defineStatus(complaintDTO.getStatus()) + "), Queja " + company.getName();
            subjectNoti= "Respuesta Ticket # " + complaintDTO.getId() + " (" + defineStatus(complaintDTO.getStatus()) + "), Queja";
            bodyNotiAdmin = "Se ha enviado una respuesta del Ticket #" + complaintDTO.getId() + " de la filial " + complaintDTO.getHouseNumber() + " en el condominio " + company.getName() + ", ingrese para ver más detalles.";
            bodyNotiResident = "Se ha enviado una respuesta del Ticket #" + complaintDTO.getId() + " de su filial " + complaintDTO.getHouseNumber() + " en el condominio " + company.getName() + ", ingrese para ver más detalles.";
        } else {
            subjectNoti= "Respuesta Ticket # " + complaintDTO.getId() + " (" + defineStatus(complaintDTO.getStatus()) + "), Sugerencia";
            subject = "Ticket # " + complaintDTO.getId() + " (" + defineStatus(complaintDTO.getStatus()) + "), Sugerencia " + company.getName();
            bodyNotiAdmin = "Se ha enviado una respuesta del Ticket #" + complaintDTO.getId() + " de la filial " + complaintDTO.getHouseNumber() + " en el condominio " + company.getName() + ", ingrese para ver más detalles.";
            bodyNotiResident = "Se ha enviado una respuesta del Ticket #" + complaintDTO.getId() + " de su filial " + complaintDTO.getHouseNumber() + " en el condominio " + company.getName() + ", ingrese para ver más detalles.";
        }
        this.pNotification.sendNotificationAllAdminsByCompanyId(complaintDTO.getCompanyId(), this.pNotification.createPushNotification(subjectNoti, bodyNotiAdmin));
        this.pNotification.sendNotificationToResident(complaintDTO.getResidentId(), this.pNotification.createPushNotification(subjectNoti, bodyNotiResident));
        this.mailService.sendEmail(complaintDTO.getCompanyId(), complaintDTO.getResident().getEmail(), subject, content, false, true);
        String finalSubject = subject;
        this.adminInfoService.findAllByCompany(null, complaintDTO.getCompanyId()).getContent().forEach(adminInfoDTO -> {
            this.mailService.sendEmail(complaintDTO.getCompanyId(), adminInfoDTO.getEmail(), finalSubject, content, false, true);
        });
    }
}

