package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A EmailConfiguration.
 */
@Entity
@Table(name = "email_configuration")
public class EmailConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "jhi_password")
    private String password;

    @Column(name = "custom_email")
    private Boolean customEmail;

    @Column(name = "email_company")
    private String emailCompany;

    @Column(name = "admin_company_name")
    private String adminCompanyName;

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public EmailConfiguration email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public EmailConfiguration password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isCustomEmail() {
        return customEmail;
    }

    public EmailConfiguration customEmail(Boolean customEmail) {
        this.customEmail = customEmail;
        return this;
    }

    public void setCustomEmail(Boolean customEmail) {
        this.customEmail = customEmail;
    }

    public String getEmailCompany() {
        return emailCompany;
    }

    public EmailConfiguration emailCompany(String emailCompany) {
        this.emailCompany = emailCompany;
        return this;
    }

    public void setEmailCompany(String emailCompany) {
        this.emailCompany = emailCompany;
    }

    public String getAdminCompanyName() {
        return adminCompanyName;
    }

    public EmailConfiguration adminCompanyName(String adminCompanyName) {
        this.adminCompanyName = adminCompanyName;
        return this;
    }

    public void setAdminCompanyName(String adminCompanyName) {
        this.adminCompanyName = adminCompanyName;
    }

    public Company getCompany() {
        return company;
    }

    public EmailConfiguration company(Company company) {
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
        EmailConfiguration emailConfiguration = (EmailConfiguration) o;
        if (emailConfiguration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailConfiguration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailConfiguration{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", customEmail='" + isCustomEmail() + "'" +
            ", emailCompany='" + getEmailCompany() + "'" +
            ", adminCompanyName='" + getAdminCompanyName() + "'" +
            "}";
    }
}
