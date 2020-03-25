package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.TokenNotifications;
import com.lighthouse.aditum.service.dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@Service
@Transactional
public class PushNotificationService {

    private final ResidentService residentService;

    private final UserService userService;

    private final TokenNotificationsService tokenNotificationsService;

    public PushNotificationService(ResidentService residentService, UserService userService, TokenNotificationsService tokenNotificationsService) {
        this.residentService = residentService;
        this.userService = userService;
        this.tokenNotificationsService = tokenNotificationsService;
    }

    @Async
    public ResponseEntity<String> sendNotification(PushNotificationDTO notification) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "https://fcm.googleapis.com/fcm/send";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "key=AAAASm8iH8I:APA91bGPhQiVfb0l25d2YAiERY2y6m6i3EQ_BZyLWkDuNwz8ILfoVaM-RGwN0W6IXRqU11Zq1gEGpeOHqUC0PsAIoRtVP4kCgYKsY8iraErkKRgoV7d-Sy8u625N1078B7JftMhPmuS9");
        HttpEntity<PushNotificationDTO> request = new HttpEntity<PushNotificationDTO>(notification, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
        return result;
    }


    @Async
    public void sendNotificationToResident(Long residentId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        ResidentDTO resident = this.residentService.findOne(residentId);
        if (resident.getIsOwner() == 1) {
            UserDTO user = this.userService.findOneByUserId(resident.getUserId());
            List<TokenNotificationsDTO> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
            for (TokenNotificationsDTO token : tokenNotifications) {
                PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                pushNotificationDTO.setTo(token.getToken());
                pushNotificationDTO.setNotification(notificationRequestDTO);
                this.sendNotification(pushNotificationDTO);
            }
        }
    }

    @Async
    public void sendNotificationsToPrincipalContactsByCompany(Long companyId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        List<ResidentDTO> residents = this.residentService.findPrincipalContactByCompanyId(null, companyId).getContent();
        for (ResidentDTO resident : residents) {
            if (resident.getIsOwner() == 1) {
                UserDTO user = this.userService.findOneByUserId(resident.getUserId());
                List<TokenNotificationsDTO> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
                for (TokenNotificationsDTO token : tokenNotifications) {
                    PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                    pushNotificationDTO.setTo(token.getToken());
                    pushNotificationDTO.setNotification(notificationRequestDTO);
                    this.sendNotification(pushNotificationDTO);
                }
            }
        }
    }

    @Async
    public void sendNotificationsToPrincipalContactByHouse(Long houseId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        ResidentDTO resident = this.residentService.findPrincipalContactByHouse(houseId);
        if (resident.getIsOwner() == 1) {
            UserDTO user = this.userService.findOneByUserId(resident.getUserId());
            List<TokenNotificationsDTO> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
            for (TokenNotificationsDTO token : tokenNotifications) {
                PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                pushNotificationDTO.setTo(token.getToken());
                pushNotificationDTO.setNotification(notificationRequestDTO);
                this.sendNotification(pushNotificationDTO);
            }
        }
    }
}
