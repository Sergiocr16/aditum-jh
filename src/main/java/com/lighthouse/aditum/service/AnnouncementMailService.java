

package com.lighthouse.aditum.service;
//import org.ocpsoft.prettytime.*;
    import com.lighthouse.aditum.domain.Announcement;
    import com.lighthouse.aditum.service.dto.AnnouncementDTO;
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

public class AnnouncementMailService {
    private final JHipsterProperties jHipsterProperties;
    private final MessageSource messageSource;
    private final ResidentService residentService;
    private final SpringTemplateEngine templateEngine;

    private final MailService mailService;

    private final CompanyService companyService;

    private static final String ANNOUNCEMENT = "announcement";

    private static final String BASE_URL = "baseUrl";


    private static final String ANSWER_SIZE = "answerSize";

    private static final String COMPLAINT_COMMENTS = "announcementComments";


    private final Logger log = LoggerFactory.getLogger(ComplaintMailService.class);


    public AnnouncementMailService(ResidentService residentService, CompanyService companyService, MailService mailService, JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyService = companyService;
        this.residentService = residentService;
    }


    private String defineContent(AnnouncementDTO announcementDTO) {

        Locale locale = new Locale("es", "CR");
        Context context = new Context(locale);
        context.setVariable(ANNOUNCEMENT, announcementDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String complaintDate = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a").format(announcementDTO.getPublishingDate());

        return templateEngine.process("announcementEmail", context);
    }

    @Async
    public void sendEmail(AnnouncementDTO announcementDTO) {
        String subject = announcementDTO.getTitle() + " - " + this.companyService.findOne(announcementDTO.getCompanyId()).getName();
        String content = defineContent(announcementDTO);
        this.residentService.findPrincipalContactByCompanyId(null, announcementDTO.getCompanyId()).getContent().forEach(residentDTO -> {
            if(residentDTO.getEmail()!=null && residentDTO.getEnabled()==1) {
                this.mailService.sendEmail(residentDTO.getEmail(), subject, content, false, true);
            }
        });
    }


    }


