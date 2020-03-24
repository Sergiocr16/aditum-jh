package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A TokenNotifications.
 */
@Entity
@Table(name = "token_notifications")
public class TokenNotifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public TokenNotifications token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public TokenNotifications user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        TokenNotifications tokenNotifications = (TokenNotifications) o;
        if (tokenNotifications.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tokenNotifications.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TokenNotifications{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            "}";
    }
}
