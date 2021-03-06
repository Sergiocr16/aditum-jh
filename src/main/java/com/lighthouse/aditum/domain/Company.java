package com.lighthouse.aditum.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public Long getId() {
        return id;
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


    public Set<AdminInfo> getAdminInfos() {
        return adminInfos;
    }

    public void setAdminInfos(Set<AdminInfo> adminInfos) {
        this.adminInfos = adminInfos;
    }


    public Set<RHAccount> getRHAccounts() {
        return rhAccounts;
    }

    public void setRHAccounts(Set<RHAccount> rhAccounts) {
        this.rhAccounts = rhAccounts;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        if (company.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", active='" + active + "'" +
            '}';
    }

    public Collection<MacroCondominium> getMacroCondominiums() {
        return null;
    }
}
