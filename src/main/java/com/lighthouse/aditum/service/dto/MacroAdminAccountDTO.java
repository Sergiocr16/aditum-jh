package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the MacroAdminAccount entity.
 */
public class MacroAdminAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    @NotNull
    private String secondlastname;

    @NotNull
    private String identificationnumber;

    @NotNull
    private String email;

    @NotNull
    private Boolean enabled;

    private String imageUrl;

    private Long macroCondominiumId;

    private String macroCondominiumName;

    private Long userId;

    private String userLogin;

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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

        MacroAdminAccountDTO macroAdminAccountDTO = (MacroAdminAccountDTO) o;
        if(macroAdminAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), macroAdminAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MacroAdminAccountDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", secondlastname='" + getSecondlastname() + "'" +
            ", identificationnumber='" + getIdentificationnumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }
}
