package com.lighthouse.aditum.service.dto;


import java.io.Serializable;

/**
 * A DTO for the AccessDoor entity.
 */
public class NotificationRequestDTO implements Serializable {
    private String body;
    private String title;
    private String priority;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
