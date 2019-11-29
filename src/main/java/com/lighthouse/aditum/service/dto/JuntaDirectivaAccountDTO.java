package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the JuntaDirectivaAccount entity.
 */
public class JuntaDirectivaAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String email;

    private Long companyId;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = (JuntaDirectivaAccountDTO) o;
        if(juntaDirectivaAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), juntaDirectivaAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JuntaDirectivaAccountDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
