package com.lighthouse.aditum.service.dto;


import java.io.Serializable;

/**
 * A DTO for the AccessDoor entity.
 */
public class PushNotificationDTO implements Serializable {
    private String to;

    private NotificationRequestDTO notification;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationRequestDTO getNotification() {
        return notification;
    }

    public void setNotification(NotificationRequestDTO notification) {
        this.notification = notification;
    }
}
