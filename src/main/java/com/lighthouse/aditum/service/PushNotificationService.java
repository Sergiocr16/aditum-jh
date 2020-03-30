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

    private final AdminInfoService adminInfoService;

    private final UserService userService;

    private final TokenNotificationsService tokenNotificationsService;

    public PushNotificationService(AdminInfoService adminInfoService, ResidentService residentService, UserService userService, TokenNotificationsService tokenNotificationsService) {
        this.residentService = residentService;
        this.userService = userService;
        this.tokenNotificationsService = tokenNotificationsService;
        this.adminInfoService = adminInfoService;
    }

    public NotificationRequestDTO createPushNotification(String title,String body){
        NotificationRequestDTO notificationRequest = new NotificationRequestDTO();
        notificationRequest.setBody(body);
        notificationRequest.setTitle(title);
        notificationRequest.setPriority("high");
        return  notificationRequest;
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
            List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
            for (String token : tokenNotifications) {
                PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                pushNotificationDTO.setTo(token);
                pushNotificationDTO.setNotification(notificationRequestDTO);
                this.sendNotification(pushNotificationDTO);
            }
        }
    }

    @Async
    public void sendNotificationToAdmin(Long adminId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        AdminInfoDTO adminInfoDTO = this.adminInfoService.findOne(adminId);
        UserDTO user = this.userService.findOneByUserId(adminInfoDTO.getUserId());
        List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
        for (String token : tokenNotifications) {
            PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
            pushNotificationDTO.setTo(token);
            pushNotificationDTO.setNotification(notificationRequestDTO);
            this.sendNotification(pushNotificationDTO);
        }
    }

    @Async
    public void sendNotificationAllAdminsByCompanyId(Long companyId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        List<AdminInfoDTO> adminInfos = this.adminInfoService.findAllByCompany(null, companyId).getContent();
        for (AdminInfoDTO admin : adminInfos) {
            UserDTO user = this.userService.findOneByUserId(admin.getUserId());
            List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
            for (String token : tokenNotifications) {
                PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                pushNotificationDTO.setTo(token);
                pushNotificationDTO.setNotification(notificationRequestDTO);
                this.sendNotification(pushNotificationDTO);
            }
        }
    }

    @Async
    public void sendNotificationsToOwnersByCompany(Long companyId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        List<ResidentDTO> residents = this.residentService.findOwnersToSendEmailByCompanyId(companyId);
        for (ResidentDTO resident : residents) {
            if (resident.getIsOwner() == 1) {
                UserDTO user = this.userService.findOneByUserId(resident.getUserId());
                List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
                for (String token : tokenNotifications) {
                    PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                    pushNotificationDTO.setTo(token);
                    pushNotificationDTO.setNotification(notificationRequestDTO);
                    this.sendNotification(pushNotificationDTO);
                }
            }
        }
    }

    @Async
    public void sendNotificationsToOwnersByHouse(Long houseId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        List<ResidentDTO> residents = this.residentService.findOwnerToSendNotificationByHouseId(houseId);
        for (ResidentDTO resident : residents) {
            if (resident.getIsOwner() == 1) {
                UserDTO user = this.userService.findOneByUserId(resident.getUserId());
                List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
                for (String token : tokenNotifications) {
                    PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                    pushNotificationDTO.setTo(token);
                    pushNotificationDTO.setNotification(notificationRequestDTO);
                    this.sendNotification(pushNotificationDTO);
                }
            }
        }
    }

    @Async
    public void sendNotificationsToAllResidentsByCompany(Long companyId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        List<ResidentDTO> residents = this.residentService.findAllToSendEmailByCompanyId(companyId);
        for (ResidentDTO resident : residents) {
            if (resident.getIsOwner() == 1) {
                UserDTO user = this.userService.findOneByUserId(resident.getUserId());
                List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
                for (String token : tokenNotifications) {
                    PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                    pushNotificationDTO.setTo(token);
                    pushNotificationDTO.setNotification(notificationRequestDTO);
                    this.sendNotification(pushNotificationDTO);
                }
            }
        }
    }

    @Async
    public void sendNotificationsToAllTenantByCompany(Long companyId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        List<ResidentDTO> residents = this.residentService.findtenantToSendEmailByCompanyId(companyId);
        for (ResidentDTO resident : residents) {
            if (resident.getIsOwner() == 1) {
                UserDTO user = this.userService.findOneByUserId(resident.getUserId());
                List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
                for (String token : tokenNotifications) {
                    PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                    pushNotificationDTO.setTo(token);
                    pushNotificationDTO.setNotification(notificationRequestDTO);
                    this.sendNotification(pushNotificationDTO);
                }
            }
        }
    }

    @Async
    public void sendNotificationsToAllLivingInHouse(Long houseId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        List<ResidentDTO> residents = this.residentService.findAllResidentsLivingToSendNotificationByHouseId(houseId);
        for (ResidentDTO resident : residents) {
            if (resident.getIsOwner() == 1) {
                UserDTO user = this.userService.findOneByUserId(resident.getUserId());
                List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
                for (String token : tokenNotifications) {
                    PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                    pushNotificationDTO.setTo(token);
                    pushNotificationDTO.setNotification(notificationRequestDTO);
                    this.sendNotification(pushNotificationDTO);
                }
            }
        }
    }

    @Async
    public void sendNotificationsToAdmin(Long companyId, NotificationRequestDTO notificationRequestDTO) throws URISyntaxException {
        List<ResidentDTO> residents = this.residentService.findtenantToSendEmailByCompanyId(companyId);
        for (ResidentDTO resident : residents) {
            if (resident.getIsOwner() == 1) {
                UserDTO user = this.userService.findOneByUserId(resident.getUserId());
                List<String> tokenNotifications = this.tokenNotificationsService.findAllByUserId(user.getId());
                for (String token : tokenNotifications) {
                    PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();
                    pushNotificationDTO.setTo(token);
                    pushNotificationDTO.setNotification(notificationRequestDTO);
                    this.sendNotification(pushNotificationDTO);
                }
            }
        }
    }

}
