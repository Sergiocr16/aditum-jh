package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A MacroOfficerAccount.
 */
@Entity
@Table(name = "macro_officer_account")
public class MacroOfficerAccount implements Serializable {

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

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private MacroCondominium macroCondominium;

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

    public MacroOfficerAccount name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public MacroOfficerAccount enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getLogin() {
        return login;
    }

    public MacroOfficerAccount login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public User getUser() {
        return user;
    }

    public MacroOfficerAccount user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MacroCondominium getMacroCondominium() {
        return macroCondominium;
    }

    public MacroOfficerAccount macroCondominium(MacroCondominium macroCondominium) {
        this.macroCondominium = macroCondominium;
        return this;
    }

    public void setMacroCondominium(MacroCondominium macroCondominium) {
        this.macroCondominium = macroCondominium;
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
        MacroOfficerAccount macroOfficerAccount = (MacroOfficerAccount) o;
        if (macroOfficerAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), macroOfficerAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MacroOfficerAccount{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", login='" + getLogin() + "'" +
            "}";
    }
}
