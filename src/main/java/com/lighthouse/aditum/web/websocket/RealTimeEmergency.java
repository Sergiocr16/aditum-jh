package com.lighthouse.aditum.web.websocket;

/**
 * Created by Sergio on 04/04/2017.
 */

import com.lighthouse.aditum.service.CompanyService;
import com.lighthouse.aditum.service.EmergencyService;
import com.lighthouse.aditum.service.NoteService;
import com.lighthouse.aditum.service.PushNotificationService;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.dto.EmergencyDTO;
import com.lighthouse.aditum.service.dto.NoteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;


import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import java.nio.channels.AsynchronousChannel;
import java.security.Principal;
import java.time.ZonedDateTime;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by sergio on 3/14/2017.
 */
@Controller
public class RealTimeEmergency {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private EmergencyService emergencyService;
    private PushNotificationService pNotification;
    private CompanyService companyService;

    public RealTimeEmergency(CompanyService companyService, SimpMessageSendingOperations messagingTemplate,
                             EmergencyService emergencyService, PushNotificationService pNotification) {
        this.messagingTemplate = messagingTemplate;
        this.emergencyService = emergencyService;
        this.pNotification = pNotification;
        this.companyService = companyService;
    }

    @SubscribeMapping("/topic/reportEmergency/{idCompany}")
    @SendTo("/topic/emergency/{idCompany}")
    public EmergencyDTO reportEmergency(EmergencyDTO emergencyDTO) throws URISyntaxException {
        String companyName = this.companyService.findOne(emergencyDTO.getCompanyId()).getName();
        this.pNotification.sendNotificationAllAdminsByCompanyId(emergencyDTO.getCompanyId(),
            this.pNotification.createPushNotification("EMERGENCIA REPORTADA - " + emergencyDTO.getHouseNumber(),
                "Se ha reportado una emergencia del tipo " + emergencyDTO.getTipo() + " en el condominio " + companyName + "."));
        this.pNotification.sendNotificationsToAllLivingInHouse(emergencyDTO.getHouseId(),
            this.pNotification.createPushNotification("EMERGENCIA REPORTADA - " + emergencyDTO.getHouseNumber(),
                "Alguien  reportó una emergencia del tipo " + emergencyDTO.getTipo() + " en tu filial del condominio " + companyName + "."));
        return this.emergencyService.save(emergencyDTO);
    }

    @SubscribeMapping("/topic/attendEmergency/{code}")
    @SendTo("/topic/emergencyAttended/{code}")
    public EmergencyDTO attendEmergency(EmergencyDTO emergencyDTO) throws URISyntaxException {
        this.pNotification.sendNotificationsToAllLivingInHouse(emergencyDTO.getHouseId(),
            this.pNotification.createPushNotification("Los oficiales van en camino - " + emergencyDTO.getHouseNumber(),
                "Se reportó una emergencia en tu filial del tipo " + emergencyDTO.getTipo() + ", los oficiales la están atendiendo en este momento.")
        );
        this.pNotification.sendNotificationAllAdminsByCompanyId(emergencyDTO.getCompanyId(),
            this.pNotification.createPushNotification("Los oficiales han visto la emergencia de la filial " + emergencyDTO.getHouseNumber(),
                "Se reportó una emergencia en la filial " + emergencyDTO.getHouseNumber() + " del tipo " + emergencyDTO.getTipo() + " y los oficiales la están atendiendo en este momento.")
        );
        return emergencyDTO;
    }

}
