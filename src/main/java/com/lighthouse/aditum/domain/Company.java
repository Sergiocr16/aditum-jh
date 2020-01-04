package com.lighthouse.aditum.domain;


import org.hibernate.annotations.BatchSize;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "active", nullable = false)
    private Integer active;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "direction")
    private String direction;

    @Column(name = "legal_name")
    private String legalName;

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    @Column(name = "legal_identification")
    private String legalIdentification;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "support_email")
    private String supportEmail;

    @Column(name = "support_number")
    private String supportNumber;

    @Column(name = "email")
    private String email;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "rhaccount_company",
        joinColumns = {@JoinColumn(name = "companies_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "rhaccounts_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Set<RHAccount> rhAccounts = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "admin_info_company",
        joinColumns = @JoinColumn(name="companies_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="admin_infos_id", referencedColumnName="id"))
    private Set<AdminInfo> adminInfos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public Set<RHAccount> getRHAccounts() {
        return rhAccounts;
    }

    public void setRHAccounts(Set<RHAccount> rhAccounts) {
        this.rhAccounts = rhAccounts;
    }

    public Set<AdminInfo> getAdminInfos() {
        return adminInfos;
    }

    public void setAdminInfos(Set<AdminInfo> adminInfos) {
        this.adminInfos = adminInfos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getActive() {
        return active;
    }

    public Company active(Integer active) {
        this.active = active;
        return this;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Company logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDirection() {
        return direction;
    }

    public Company direction(String direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLegalIdentification() {
        return legalIdentification;
    }

    public Company legalIdentification(String legalIdentification) {
        this.legalIdentification = legalIdentification;
        return this;
    }

    public void setLegalIdentification(String legalIdentification) {
        this.legalIdentification = legalIdentification;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Company phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public Company supportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
        return this;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getSupportNumber() {
        return supportNumber;
    }

    public Company supportNumber(String supportNumber) {
        this.supportNumber = supportNumber;
        return this;
    }

    public void setSupportNumber(String supportNumber) {
        this.supportNumber = supportNumber;
    }

    public String getEmail() {
        return email;
    }

    public Company email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
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
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active=" + getActive() +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", direction='" + getDirection() + "'" +
            ", legalIdentification='" + getLegalIdentification() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", supportEmail='" + getSupportEmail() + "'" +
            ", supportNumber='" + getSupportNumber() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
    public Collection<MacroCondominium> getMacroCondominiums() {
        return null;
    }
}
