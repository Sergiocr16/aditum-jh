package com.lighthouse.aditum.service;

import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.util.RandomUtil;
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
import java.net.URISyntaxException;
import java.text.NumberFormat;
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

    private final PaymentProofService paymentProofService;

    private final CompanyConfigurationService companyConfigurationService;

    private final PushNotificationService pNotification;

    private static final String RESERVATION = "reservation";

    private static final String COMMONAREA = "commonArea";

    private static final String BASE_URL = "baseUrl";

    private static final String RESIDENT = "resident";

    private static final String HOUSE = "house";

    private static final String DATE = "date";

    private static final String COMPANY = "company";

    private static final String CHARGEDATE = "chargeDate";

    private static final String SCHEDULE = "schedule";

    private static final String TYPE = "type";

    private static final String USERTYPE = "userType";


    private static final String DEVOLUTIONAMOUNT = "devolutionAmount";

    private static final String RESERVATIONCHARGE = "reservationCharge";

    private static final String PAYMENT_PROOF = "paymentProof";


    private final Logger log = LoggerFactory.getLogger(ComplaintMailService.class);


    public CommonAreaMailService(PushNotificationService pNotification,CompanyConfigurationService companyConfigurationService, CompanyService companyService, MailService mailService, JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine, PaymentProofService paymentProofService, AdminInfoService adminInfoService) {
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.mailService = mailService;
        this.companyService = companyService;
        this.adminInfoService = adminInfoService;
        this.paymentProofService = paymentProofService;
        this.companyConfigurationService = companyConfigurationService;
        this.pNotification = pNotification;
    }

    private String formatSchedule(String initialTime, String finalTime) {
        String timeInitial = "";
        String timeFinal = "";
        if (Integer.parseInt(initialTime) == 0) {
            timeInitial = "12:00AM";
        } else if (Integer.parseInt(initialTime) < 12) {
            timeInitial = initialTime + ":00AM";
        } else if (Integer.parseInt(initialTime) > 12) {
            timeInitial = Integer.parseInt(initialTime) - 12 + ":00PM";

        } else if (Integer.parseInt(initialTime) == 12) {
            timeInitial = "12:00PM";
        }
        if (Integer.parseInt(finalTime) == 0) {
            timeFinal = "12:00AM";
        } else if (Integer.parseInt(finalTime) < 12) {
            timeFinal = finalTime + ":00AM";
        } else if (Integer.parseInt(finalTime) > 12) {
            timeFinal = Integer.parseInt(finalTime) - 12 + ":00PM";
        } else if (Integer.parseInt(finalTime) == 12) {
            timeFinal = "12:00PM";
        }
        return timeInitial + " - " + timeFinal;
    }

    private String defineContentAdmin(CommonAreaReservationsDTO commonAreaReservationsDTO) {

        Locale locale = new Locale("es", "CR");
        String currency = companyConfigurationService.getByCompanyId(null, commonAreaReservationsDTO.getCompanyId()).getContent().get(0).getCurrency();
        CompanyDTO companyDTO = companyService.findOne(commonAreaReservationsDTO.getCompanyId());
        Context context = new Context(locale);
        context.setVariable(RESERVATION, commonAreaReservationsDTO);
        context.setVariable(COMMONAREA, commonAreaReservationsDTO.getCommonArea());
        context.setVariable(COMPANY, companyDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(HOUSE, commonAreaReservationsDTO.getHouse().getHousenumber());
        context.setVariable(TYPE, "Administrador");
        context.setVariable(USERTYPE, 1);
        if(commonAreaReservationsDTO.getPaymentProof()!=null){
            context.setVariable(PAYMENT_PROOF,this.paymentProofService.findOne(Long.parseLong(commonAreaReservationsDTO.getPaymentProof())).getImageUrl());
        }
        if (commonAreaReservationsDTO.getReservationCharge() != null) {
            context.setVariable(RESERVATIONCHARGE, RandomUtil.formatMoney(currency,Double.parseDouble(commonAreaReservationsDTO.getReservationCharge() + "")));
            context.setVariable(DEVOLUTIONAMOUNT, RandomUtil.formatMoney(currency,Double.parseDouble(commonAreaReservationsDTO.getDevolutionAmmount() + "")));
        }
        context.setVariable(SCHEDULE, this.formatSchedule(commonAreaReservationsDTO.getInitialTime(), commonAreaReservationsDTO.getFinalTime()));
        context.setVariable(RESIDENT, commonAreaReservationsDTO.getResident().getName() + " " + commonAreaReservationsDTO.getResident().getLastname());
        context.setVariable(DATE, DateTimeFormatter.ofPattern("EEEE  d MMMM yyyy").format(commonAreaReservationsDTO.getInitalDate()));
        if (commonAreaReservationsDTO.getCharge() != null) {
            context.setVariable(CHARGEDATE, DateTimeFormatter.ofPattern("EEEE  d MMMM yyyy").format(commonAreaReservationsDTO.getCharge().getDate()));
        }

        String emailContent = "";
        if(companyDTO.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")){
            emailContent = templateEngine.process("pendingReservationEmail", context);
        }else{
            emailContent = templateEngine.process("pendingReservationEmailNoAditum", context);
        }

        return emailContent;

    }

    private String defineContentResident(CommonAreaReservationsDTO commonAreaReservationsDTO) {
        Locale locale = new Locale("es", "CR");
        String currency = companyConfigurationService.getByCompanyId(null, commonAreaReservationsDTO.getCompanyId()).getContent().get(0).getCurrency();
        CompanyDTO companyDTO = companyService.findOne(commonAreaReservationsDTO.getCompanyId());
        Context context = new Context(locale);
        context.setVariable(RESERVATION, commonAreaReservationsDTO);
        context.setVariable(COMMONAREA, commonAreaReservationsDTO.getCommonArea());
        context.setVariable(COMPANY, companyDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(TYPE, "Residente");
        context.setVariable(USERTYPE, 2);
        if(commonAreaReservationsDTO.getPaymentProof()!=null){
            context.setVariable(PAYMENT_PROOF,this.paymentProofService.findOne(Long.parseLong(commonAreaReservationsDTO.getPaymentProof())).getImageUrl());
        }
        if (commonAreaReservationsDTO.getReservationCharge() != null) {
            context.setVariable(RESERVATIONCHARGE, RandomUtil.formatMoney(currency,Double.parseDouble(commonAreaReservationsDTO.getReservationCharge() + "")));
            context.setVariable(DEVOLUTIONAMOUNT, RandomUtil.formatMoney(currency,Double.parseDouble(commonAreaReservationsDTO.getDevolutionAmmount() + "")));
        }

        context.setVariable(HOUSE, commonAreaReservationsDTO.getHouse().getHousenumber());
        context.setVariable(SCHEDULE, this.formatSchedule(commonAreaReservationsDTO.getInitialTime(), commonAreaReservationsDTO.getFinalTime()));
        context.setVariable(RESIDENT, commonAreaReservationsDTO.getResident().getName() + " " + commonAreaReservationsDTO.getResident().getLastname());
        context.setVariable(DATE, DateTimeFormatter.ofPattern("EEEE  d MMMM yyyy").format(commonAreaReservationsDTO.getInitalDate()));
        if (commonAreaReservationsDTO.getCharge() != null) {
            context.setVariable(CHARGEDATE, DateTimeFormatter.ofPattern("EEEE  d MMMM yyyy").format(commonAreaReservationsDTO.getCharge().getDate()));
        }

        String emailContent = "";
        if(companyDTO.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")){
            emailContent = templateEngine.process("pendingReservationEmail", context);
        }else{
            emailContent = templateEngine.process("pendingReservationEmailNoAditum", context);
        }

        return emailContent;

    }

    @Async
    public void sendNewCommonAreaReservationEmailToAdmin(CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {
        String subject = "Solicitud de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        String title = "Tiene una nueva solicitud de reservación de la filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " en el condominio " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName() + ".";
        commonAreaReservationsDTO.setEmailTitle(title);

       this.pNotification.sendNotificationAllAdminsByCompanyId(commonAreaReservationsDTO.getCompanyId(),this.pNotification.createPushNotification(subject,title));
        String content = this.defineContentAdmin(commonAreaReservationsDTO);
        this.adminInfoService.findAllByCompany(null, commonAreaReservationsDTO.getCompanyId()).getContent().forEach(adminInfoDTO -> {
            this.mailService.sendEmail(commonAreaReservationsDTO.getCompanyId(),adminInfoDTO.getEmail(), subject, content, false, true);
        });
    }

    @Async
    public void sendNewCommonAreaReservationEmailToResident(CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {
        String subject = "Solicitud de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        String title = "Se ha creado una nueva solicitud de reservación del área común en " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName() + ".";
        commonAreaReservationsDTO.setEmailTitle(title);
        this.pNotification.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),this.pNotification.createPushNotification(subject,title));
        String content = this.defineContentResident(commonAreaReservationsDTO);
        if (commonAreaReservationsDTO.getResident().getEmail() != null) {
            this.mailService.sendEmail(commonAreaReservationsDTO.getCompanyId(),commonAreaReservationsDTO.getResident().getEmail(), subject, content, false, true);
        }
    }

    @Async
    public void sendAcceptedCommonAreaReservationEmail(CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {
        String subject = "Aprobación de solicitud de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        String title = "Se ha aceptado la solicitud de reservación del área común en " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName() + ".";
        commonAreaReservationsDTO.setEmailTitle(title);
        this.pNotification.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),this.pNotification.createPushNotification(subject,title));
        this.pNotification.sendNotificationAllAdminsByCompanyId(commonAreaReservationsDTO.getCompanyId(),this.pNotification.createPushNotification(subject,title));
        String content = this.defineContentResident(commonAreaReservationsDTO);
        if (commonAreaReservationsDTO.getChargeEmail() != null) {
            this.mailService.sendEmail(commonAreaReservationsDTO.getCompanyId(),commonAreaReservationsDTO.getChargeEmail(), subject, content, false, true);
        }
    }

    @Async
    public void sendCanceledCommonAreaReservationEmail(CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {

      String subject = "Rechazo de solicitud de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
      String title = "Se ha rechazado la solicitud de reservación del área común en " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName() + ".";
        commonAreaReservationsDTO.setEmailTitle(title);
        this.pNotification.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),this.pNotification.createPushNotification(subject,title));
        this.pNotification.sendNotificationAllAdminsByCompanyId(commonAreaReservationsDTO.getCompanyId(),this.pNotification.createPushNotification(subject,title));
        String content = this.defineContentResident(commonAreaReservationsDTO);
        if (commonAreaReservationsDTO.getResident().getEmail() != null) {
            this.mailService.sendEmail(commonAreaReservationsDTO.getCompanyId(),commonAreaReservationsDTO.getResident().getEmail(), subject, content, false, true);
        }
    }

    @Async
    public void sendCanceledCommonAreaReservationAprobedEmail(CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {

        String subject = "Cancelación de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
       String title = "Se ha cancelado la reservación del área común en " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName() + ".";
        commonAreaReservationsDTO.setEmailTitle(title);
        this.pNotification.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),this.pNotification.createPushNotification(subject,title));
        this.pNotification.sendNotificationAllAdminsByCompanyId(commonAreaReservationsDTO.getCompanyId(),this.pNotification.createPushNotification(subject,title));
        String content = this.defineContentResident(commonAreaReservationsDTO);
        if (commonAreaReservationsDTO.getResident().getEmail() != null) {
            this.mailService.sendEmail(commonAreaReservationsDTO.getCompanyId(),commonAreaReservationsDTO.getResident().getEmail(), subject, content, false, true);
        }
    }

    @Async
    public void sendUpdateCommonAreaReservationEmail(CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {
        String subject = "Actualización de reservación de " + commonAreaReservationsDTO.getCommonArea().getName() + " - Filial " + commonAreaReservationsDTO.getHouse().getHousenumber() + " - " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName();
        String title = "El administrador ha actualizado la información de la reservación del área común en " + this.companyService.findOne(commonAreaReservationsDTO.getCompanyId()).getName() + ".";
        commonAreaReservationsDTO.setEmailTitle(title);
        String content = this.defineContentResident(commonAreaReservationsDTO);
        if (commonAreaReservationsDTO.getResident().getEmail() != null) {
            this.pNotification.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),this.pNotification.createPushNotification(subject,title));
            this.pNotification.sendNotificationAllAdminsByCompanyId(commonAreaReservationsDTO.getCompanyId(),this.pNotification.createPushNotification(subject,title));
            this.mailService.sendEmail(commonAreaReservationsDTO.getCompanyId(),commonAreaReservationsDTO.getResident().getEmail(), subject, content, false, true);
        }

    }

}


