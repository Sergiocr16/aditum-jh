package com.lighthouse.aditum.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lighthouse.aditum.service.dto.PushNotificationDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class PushNotificationService {

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
}
