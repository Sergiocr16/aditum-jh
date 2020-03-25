package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the EmailConfiguration entity.
 */
public class EmailConfigurationDTO implements Serializable {

    private Long id;

    private String email;

    private String password;

    private Boolean customEmail;

    private String emailCompany;

    private String adminCompanyName;

    private Long companyId;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isCustomEmail() {
        return customEmail;
    }

    public void setCustomEmail(Boolean customEmail) {
        this.customEmail = customEmail;
    }

    public String getEmailCompany() {
        return emailCompany;
    }

    public void setEmailCompany(String emailCompany) {
        this.emailCompany = emailCompany;
    }

    public String getAdminCompanyName() {
        return adminCompanyName;
    }

    public void setAdminCompanyName(String adminCompanyName) {
        this.adminCompanyName = adminCompanyName;
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

        EmailConfigurationDTO emailConfigurationDTO = (EmailConfigurationDTO) o;
        if(emailConfigurationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailConfigurationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailConfigurationDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", customEmail='" + isCustomEmail() + "'" +
            ", emailCompany='" + getEmailCompany() + "'" +
            ", adminCompanyName='" + getAdminCompanyName() + "'" +
            "}";
    }
}
