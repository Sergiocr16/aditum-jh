package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the NotificationSended entity.
 */
public class NotificationSendedDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private String sendedTo;

    private String url;

    private String sendToResident;

    private String toAll;

    private String state;

    private Long companyId;

    private ZonedDateTime date;

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getSendToResident() {
        return sendToResident;
    }

    public void setSendToResident(String sendToResident) {
        this.sendToResident = sendToResident;
    }

    public String getToAll() {
        return toAll;
    }

    public void setToAll(String toAll) {
        this.toAll = toAll;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSendedTo() {
        return sendedTo;
    }

    public void setSendedTo(String sendedTo) {
        this.sendedTo = sendedTo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationSendedDTO notificationSendedDTO = (NotificationSendedDTO) o;
        if(notificationSendedDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationSendedDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationSendedDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", sendedTo='" + getSendedTo() + "'" +
            ", url='" + getUrl() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
