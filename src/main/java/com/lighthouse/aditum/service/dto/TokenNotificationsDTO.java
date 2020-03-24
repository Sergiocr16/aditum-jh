package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TokenNotifications entity.
 */
public class TokenNotificationsDTO implements Serializable {

    private Long id;

    private String token;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TokenNotificationsDTO tokenNotificationsDTO = (TokenNotificationsDTO) o;
        if(tokenNotificationsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tokenNotificationsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TokenNotificationsDTO{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            "}";
    }
}
