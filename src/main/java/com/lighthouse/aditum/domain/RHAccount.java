package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RHAccount.
 */
@Entity
@Table(name = "rhaccount")
public class RHAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotNull
    @Column(name = "secondlastname", nullable = false)
    private String secondlastname;

    @NotNull
    @Column(name = "enterprisename", nullable = false)
    private String enterprisename;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @NotNull
    @JoinTable(name = "rhaccount_company",
               joinColumns = @JoinColumn(name="rhaccounts_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="companies_id", referencedColumnName="id"))
    private Set<Company> companies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RHAccount name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public RHAccount lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public RHAccount secondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
        return this;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getEnterprisename() {
        return enterprisename;
    }

    public RHAccount enterprisename(String enterprisename) {
        this.enterprisename = enterprisename;
        return this;
    }

    public void setEnterprisename(String enterprisename) {
        this.enterprisename = enterprisename;
    }

    public String getEmail() {
        return email;
    }

    public RHAccount email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public RHAccount user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public RHAccount companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public RHAccount addCompany(Company company) {
        this.companies.add(company);
        company.getRHAccounts().add(this);
        return this;
    }

    public RHAccount removeCompany(Company company) {
        this.companies.remove(company);
        company.getRHAccounts().remove(this);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RHAccount rHAccount = (RHAccount) o;
        if (rHAccount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rHAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RHAccount{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastname='" + lastname + "'" +
            ", secondlastname='" + secondlastname + "'" +
            ", enterprisename='" + enterprisename + "'" +
            ", email='" + email + "'" +
            '}';
    }
}
