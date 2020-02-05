package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Announcement entity.
 */
public class AnnouncementDTO implements Serializable {

    private Long id;

    private int commentsQuantity;

    private String title;

    private String imageBannerUrl;

    @NotNull
    private ZonedDateTime publishingDate;

    private String description;

    @NotNull
    private Integer status;

    @NotNull
    private Integer useBanner;

    private Integer deleted;

    private Long companyId;

    private int sendEmail;

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

    public ZonedDateTime getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(ZonedDateTime publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
    public String getImageBannerUrl() {
        return imageBannerUrl;
    }

    public void setImageBannerUrl(String imageBannerUrl) {
        this.imageBannerUrl = imageBannerUrl;
    }

    public Integer getUseBanner() {
        return useBanner;
    }

    public void setUseBanner(Integer useBanner) {
        this.useBanner = useBanner;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnouncementDTO announcementDTO = (AnnouncementDTO) o;
        if(announcementDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), announcementDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnnouncementDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", publishingDate='" + getPublishingDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }

    public int getCommentsQuantity() {
        return commentsQuantity;
    }

    public void setCommentsQuantity(int commentsQuantity) {
        this.commentsQuantity = commentsQuantity;
    }

    public int getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(int sendEmail) {
        this.sendEmail = sendEmail;
    }
}
