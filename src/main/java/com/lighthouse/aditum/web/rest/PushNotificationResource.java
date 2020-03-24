package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.service.PushNotificationService;
import com.lighthouse.aditum.service.dto.NotificationRequestDTO;
import com.lighthouse.aditum.service.dto.PushNotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

/**
 * REST controller for managing AccessDoor.
 */
@RestController
@RequestMapping("/api")
public class PushNotificationResource {

    private final Logger log = LoggerFactory.getLogger(PushNotificationResource.class);

    private static final String ENTITY_NAME = "pushNotification";

    private final PushNotificationService pushNotificationService;

    public PushNotificationResource(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping(
        value = "/notification", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createPerson(@RequestBody PushNotificationDTO notification) throws URISyntaxException {
        return pushNotificationService.sendNotification(notification);
    }
}
