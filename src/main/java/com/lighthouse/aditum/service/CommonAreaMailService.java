package com.lighthouse.aditum.service;

import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service

public class CommonAreaMailService {
    private final JHipsterProperties jHipsterProperties;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final MailService mailService;

    private final CompanyService companyService;

    private final AdminInfoService adminInfoService;

    private final CommonAreaService commonAreaService;

    private static final String RESERVATION = "reservation";

    private static final String COMMONAREA = "commonArea";

    private static final String BASE_URL = "baseUrl";

    private static final String RESIDENT = "resident";

    private static final String HOUSE = "house";

    private static final String DATE = "date";

    private static final String CHARGEDATE = "chargeDate";

    private static final String SCHEDULE = "schedule";

    private static final String TYPE = "type";


    private final Logger log = LoggerFactory.getLogger(ComplaintMailService.class);


    public CommonAreaMailService(CompanyService companyService, MailService mailService, JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine,CommonAreaService commonAreaService, AdminInfoService adminInfoService) {
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyService = companyService;
        this.adminInfoService = adminInfoService;
        this.commonAreaService = commonAreaService;
    }
    private String formatSchedule(String initialTime, String finalTime) {
        String timeInitial = "";
        String timeFinal  = "";
        if(Integer.parseInt(initialTime)==0){
            timeInitial = "12:00AM";
        }else if(Integer.parseInt(initialTime)<12){
            timeInitial = initialTime+":00AM";
        }else if(Integer.parseInt(initialTime)>12){
            timeInitial = Integer.parseInt(initialTime)-12 + ":00PM";

        }else if(Integer.parseInt(initialTime)==12){
            timeInitial = "12:00PM";
        }
        if(Integer.parseInt(finalTime)==0){
            timeFinal = "12:00AM";
        }else if(Integer.parseInt(finalTime)<12){
            timeFinal = finalTime+":00AM";
        }else if(Integer.parseInt(finalTime)>12){
            timeFinal = Integer.parseInt(finalTime)-12 + ":00PM";
        }else if(Integer.parseInt(finalTime)==12){
            timeFinal = "12:00PM";
        }
   return timeInitial + " - " + timeFinal;
    }

    private String defineContentAdmin(CommonAreaReservationsDTO commonAreaReservationsDTO) {

        Locale locale = new Locale("es", "CR");
        Context context = new Context(locale);
        context.setVariable(RESERVATION, commonAreaReservationsDTO);
        context.setVariable(COMMONAREA, commonAreaReservationsDTO.getCommonArea());
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(HOUSE, commonAreaReservationsDTO.getHouse().getHousenumber());
        context.setVariable(TYPE, "Administrador");
        context.setVariable(SCHEDULE, this.formatSchedule(commonAreaReservationsDTO.getInitialTime(),commonAreaReservationsDTO.getFinalTime()));
        context.setVariable(RESIDENT, commonAreaReservationsDTO.getResident().getName() + " " + commonAreaReservationsDTO.getResident().getLastname() + " " + commonAreaReservationsDTO.getResident().getSecondlastname());
        context.setVariable(DATE,  DateTimeFormatter.ofPattern("EEEE  d MMMM yyyy").format(commonAreaReservationsDTO.getInitalDate()));
//        context.setVariable(CHARGEDATE,  DateTimeFormatter.ofPattern("EEEE  d MMMM yyyy").format(commonAreaReservationsDTO.getCharge().getDate()));
        return templateEngine.process("pendingReservationEmail", context);

    }
    private String defineContentResident(CommonAreaReservationsDTO commonAreaReservationsDTO) {

        Locale locale = new Locale("es", "CR");
        Context context = new Context(locale);
        context.setVariable(RESERVATION, commonAreaReservationsDTO);
        context.setVariable(COMMONAREA, commonAreaReservationsDTO.getCommonArea());
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(TYPE, "Residente");
        context.setVariable(HOUSE, commonAreaReservationsDTO.getHouse().getHousenumber());
        context.setVariable(SCHEDULE, this.formatSchedule(commonAreaReservationsDTO.getInitialTime(),commonAreaReservationsDTO.getFinalTime()));
        context.setVariable(RESIDENT, commonAreaReservationsDTO.getResident().getName() + " " + commonAreaReservationsDTO.getResident().getLastname() + " " + commonAreaReservationsDTO.getResident().getSecondlastname());
        context.setVariable(DATE,  DateTimeFormatter.ofPattern("EEEE  d MMMM yyyy").format(commonAreaReservationsDTO.getInitalDate()));
//        context.setVariable(CHARGEDATE,  DateTimeFormatter.ofPattern("EEEE  d MMMM yyyy").format(commonAreaReservationsDTO.getCharge().getDate()));
        return templateEngine.process("pendingReservationEmail", context);

    }
    @Async
    public void sendNewCommonAreaReservationEmailToAdmin(CommonAreaReservationsDTO commonAreaReservationsDTO) {
        commonAreaReservationsDTO.setUserType(1);
        String subject = "Solicitud de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        commonAreaReservationsDTO.setEmailTitle("Tiene una nueva solicitud de reservación de la filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " en el condominio " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName()+".");

        String content = this.defineContentAdmin(commonAreaReservationsDTO);
        this.adminInfoService.findAllByCompany(null, commonAreaReservationsDTO.getCompanyId()).getContent().forEach(adminInfoDTO -> {
            this.mailService.sendEmail(adminInfoDTO.getEmail(), subject, content, false, true);
        });
    }
    @Async
    public void sendNewCommonAreaReservationEmailToResident(CommonAreaReservationsDTO commonAreaReservationsDTO) {
        commonAreaReservationsDTO.setUserType(2);
        String subject = "Solicitud de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        commonAreaReservationsDTO.setEmailTitle("Se ha creado una nueva solicitud de reservación del área común en el condominio "  + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName()+".");

        String content = this.defineContentResident(commonAreaReservationsDTO);
        if(commonAreaReservationsDTO.getResident().getEmail()!=null){
            this.mailService.sendEmail(commonAreaReservationsDTO.getResident().getEmail(), subject, content, false, true);
        }
    }
    @Async
    public void sendAcceptedCommonAreaReservationEmail(CommonAreaReservationsDTO commonAreaReservationsDTO) {
        String subject = "Aprobación de solicitud de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        commonAreaReservationsDTO.setEmailTitle("Se ha aceptado la solicitud de reservación del área común en el condominio "  + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName()+".");
        commonAreaReservationsDTO.setUserType(2);
        String content = this.defineContentResident(commonAreaReservationsDTO);
        if(commonAreaReservationsDTO.getResident().getEmail()!=null){
            this.mailService.sendEmail(commonAreaReservationsDTO.getResident().getEmail(), subject, content, false, true);
        }
    }

    @Async
    public void sendCanceledCommonAreaReservationEmail(CommonAreaReservationsDTO commonAreaReservationsDTO) {

        String subject = "Aprobación de solicitud de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        commonAreaReservationsDTO.setEmailTitle("Se ha rechazado la solicitud de reservación del área común en el condominio "  + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName()+".");
        commonAreaReservationsDTO.setUserType(2);
        String content = this.defineContentResident(commonAreaReservationsDTO);
        if(commonAreaReservationsDTO.getResident().getEmail()!=null){
            this.mailService.sendEmail(commonAreaReservationsDTO.getResident().getEmail(), subject, content, false, true);
        }
    }
    @Async
    public void sendUpdateCommonAreaReservationEmail(CommonAreaReservationsDTO commonAreaReservationsDTO) {

        String subject = "Actualización de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        commonAreaReservationsDTO.setUserType(2);
        commonAreaReservationsDTO.setEmailTitle("El administrador ha actualizado la información de la reservación del área común en el condominio "  + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName()+".");
        String content = this.defineContentResident(commonAreaReservationsDTO);
        if(commonAreaReservationsDTO.getResident().getEmail()!=null){
            this.mailService.sendEmail(commonAreaReservationsDTO.getResident().getEmail(), subject, content, false, true);
        }

    }

    @Async
    public void sendComplaintEmailChangeStatus(CommonAreaReservationsDTO commonAreaReservationsDTO) {
//        String subject = "Ticket # " + complaintDTO.getId() + " ("+defineStatus(complaintDTO.getStatus())+"), Queja o sugerencia " + this.companyService.findOne(complaintDTO.getCompanyId()).getName();
//        String content = defineContent(complaintDTO);
//        this.mailService.sendEmail(complaintDTO.getResident().getEmail(), subject, content, false, true);
//        this.adminInfoService.findAllByCompany(null, complaintDTO.getCompanyId()).getContent().forEach(adminInfoDTO -> {
//            this.mailService.sendEmail(adminInfoDTO.getEmail(), subject, content, false, true);
//        });
    }
}

