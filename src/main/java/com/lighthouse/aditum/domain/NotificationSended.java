package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A NotificationSended.
 */
@Entity
@Table(name = "notification_sended")
public class NotificationSended implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "sended_to")
    private String sendedTo;

    @Column(name = "url")
    private String url;

    @Column(name = "state")
    private String state;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public NotificationSended title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public NotificationSended description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSendedTo() {
        return sendedTo;
    }

    public NotificationSended sendedTo(String sendedTo) {
        this.sendedTo = sendedTo;
        return this;
    }

    public void setSendedTo(String sendedTo) {
        this.sendedTo = sendedTo;
    }

    public String getUrl() {
        return url;
    }

    public NotificationSended url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public NotificationSended state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public NotificationSended date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Company getCompany() {
        return company;
    }

    public NotificationSended company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationSended notificationSended = (NotificationSended) o;
        if (notificationSended.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationSended.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationSended{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", sendedTo='" + getSendedTo() + "'" +
            ", url='" + getUrl() + "'" +
            ", state='" + getState() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
