package com.lighthouse.aditum.web.websocket;

/**
 * Created by Sergio on 04/04/2017.
 */

import com.lighthouse.aditum.service.NoteService;
import com.lighthouse.aditum.service.dto.NoteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

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
public class RealTimeHomeService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private NoteService noteService;

    public RealTimeHomeService(SimpMessageSendingOperations messagingTemplate,
                               NoteService noteService) {
        this.messagingTemplate = messagingTemplate;
        this.noteService = noteService;
    }

    @SubscribeMapping("/topic/saveHomeService/{idCompany}")
    @SendTo("/topic/HomeService/{idCompany}")
    public NoteDTO saveHomeService(NoteDTO noteDTO){
          return this.noteService.save(noteDTO);
    }

}
