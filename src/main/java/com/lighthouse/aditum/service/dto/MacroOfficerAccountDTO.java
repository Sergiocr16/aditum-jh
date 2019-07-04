package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the MacroOfficerAccount entity.
 */
public class MacroOfficerAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean enabled;

    @NotNull
    private String login;

    private Long userId;

    private String userLogin;

    private Long macroCondominiumId;

    private String macroCondominiumName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public Long getMacroCondominiumId() {
        return macroCondominiumId;
    }

    public void setMacroCondominiumId(Long macroCondominiumId) {
        this.macroCondominiumId = macroCondominiumId;
    }

    public String getMacroCondominiumName() {
        return macroCondominiumName;
    }

    public void setMacroCondominiumName(String macroCondominiumName) {
        this.macroCondominiumName = macroCondominiumName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MacroOfficerAccountDTO macroOfficerAccountDTO = (MacroOfficerAccountDTO) o;
        if(macroOfficerAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), macroOfficerAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MacroOfficerAccountDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", login='" + getLogin() + "'" +
            "}";
    }
}
