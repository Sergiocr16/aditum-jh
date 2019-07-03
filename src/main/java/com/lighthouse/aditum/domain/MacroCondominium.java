package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A MacroCondominium.
 */
@Entity
@Table(name = "macro_condominium")
public class MacroCondominium implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToMany
    @JoinTable(name = "macro_condominium_company",
               joinColumns = @JoinColumn(name="macro_condominiums_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="companies_id", referencedColumnName="id"))
    private Set<Company> companies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MacroCondominium name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public MacroCondominium enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public MacroCondominium companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public MacroCondominium addCompany(Company company) {
        this.companies.add(company);
        company.getMacroCondominiums().add(this);
        return this;
    }

    public MacroCondominium removeCompany(Company company) {
        this.companies.remove(company);
        company.getMacroCondominiums().remove(this);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
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
        MacroCondominium macroCondominium = (MacroCondominium) o;
        if (macroCondominium.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), macroCondominium.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MacroCondominium{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
