package com.lighthouse.aditum.service;

import org.ocpsoft.prettytime.*;
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

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service

public class ComplaintMailService {
    private final JHipsterProperties jHipsterProperties;
    private final AdminInfoService adminInfoService;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;

    private final MailService mailService;

    private final CompanyService companyService;

    private static final String COMPLAINT = "complaint";

    private static final String BASE_URL = "baseUrl";

    private static final String RESIDENT = "resident";

    private static final String ANSWER_SIZE = "answerSize";

    private static final String COMPLAINT_COMMENTS = "complaintComments";


    private final Logger log = LoggerFactory.getLogger(ComplaintMailService.class);


    public ComplaintMailService(CompanyService companyService, AdminInfoService adminInfoService, MailService mailService, JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.jHipsterProperties = jHipsterProperties;
        this.adminInfoService = adminInfoService;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyService = companyService;
    }


    private String defineContent(ComplaintDTO complaintDTO) {

        Locale locale = new Locale("es", "CR");
        Context context = new Context(locale);
        context.setVariable(COMPLAINT, complaintDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(RESIDENT, complaintDTO.getResident().getName() + " " + complaintDTO.getResident().getLastname() + " " + complaintDTO.getResident().getSecondlastname());
        String complaintDate = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a").format(complaintDTO.getCreationDate());
        context.setVariable(ANSWER_SIZE, complaintDTO.getComplaintComments().getContent().size());

        List<ComplaintCommentDTO> complaintCommentDTOList = complaintDTO.getComplaintComments().getContent();

        complaintCommentDTOList.forEach(complaintCommentDTO -> {
            PrettyTime p = new PrettyTime(new Locale("es"));
            complaintCommentDTO.setFormattedCreationDate(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a").format(complaintCommentDTO.getCreationDate().minusHours(6)));
        });

        context.setVariable(COMPLAINT_COMMENTS, complaintCommentDTOList);
        return templateEngine.process("complaintEmail", context);
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
    public void sendNewComplaintEmail(ComplaintDTO complaintDTO) {

        String subject = "Ticket # " + complaintDTO.getId() + " ("+defineStatus(complaintDTO.getStatus())+"), Queja o sugerencia " + this.companyService.findOne(complaintDTO.getCompanyId()).getName();
        String content = defineContent(complaintDTO);
        this.adminInfoService.findAllByCompany(null, complaintDTO.getCompanyId()).getContent().forEach(adminInfoDTO -> {
            this.mailService.sendEmail(adminInfoDTO.getEmail(), subject, content, false, true);
        });
    }

    @Async
    public void sendComplaintEmailChangeStatus(ComplaintDTO complaintDTO) {
        String subject = "Ticket # " + complaintDTO.getId() + " ("+defineStatus(complaintDTO.getStatus())+"), Queja o sugerencia " + this.companyService.findOne(complaintDTO.getCompanyId()).getName();
        String content = defineContent(complaintDTO);
        this.mailService.sendEmail(complaintDTO.getResident().getEmail(), subject, content, false, true);
        this.adminInfoService.findAllByCompany(null, complaintDTO.getCompanyId()).getContent().forEach(adminInfoDTO -> {
            this.mailService.sendEmail(adminInfoDTO.getEmail(), subject, content, false, true);
        });
    }
}

