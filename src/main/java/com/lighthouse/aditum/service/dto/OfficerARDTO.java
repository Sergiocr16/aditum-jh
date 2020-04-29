package com.lighthouse.aditum.service.dto;


import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the OfficerAR entity.
 */
public class OfficerARDTO implements Serializable {

    private Long id;

    private String name;

    private String lastName;

    private String secondLastName;

    private String identificationNumber;

    private String inService;

    private String imageUrl;

    private String annosExperiencia;

    private LocalDate birthDate;

    private String phoneNumber;

    private String direction;

    private String plateNumber;

    private String aditionalNotes;

    private Boolean enabled;

    private Integer deleted;

    private String email;

    private Long companyId;

    private Long userId;

    private String userLogin;

    private Set<HouseDTO> houses = new HashSet<>();

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getInService() {
        return inService;
    }

    public void setInService(String inService) {
        this.inService = inService;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAnnosExperiencia() {
        return annosExperiencia;
    }

    public void setAnnosExperiencia(String annosExperiencia) {
        this.annosExperiencia = annosExperiencia;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getAditionalNotes() {
        return aditionalNotes;
    }

    public void setAditionalNotes(String aditionalNotes) {
        this.aditionalNotes = aditionalNotes;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    public Set<HouseDTO> getHouses() {
        return houses;
    }

    public void setHouses(Set<HouseDTO> houses) {
        this.houses = houses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OfficerARDTO officerARDTO = (OfficerARDTO) o;
        if(officerARDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), officerARDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfficerARDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", secondLastName='" + getSecondLastName() + "'" +
            ", identificationNumber='" + getIdentificationNumber() + "'" +
            ", inService='" + getInService() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", annosExperiencia='" + getAnnosExperiencia() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", direction='" + getDirection() + "'" +
            ", plateNumber='" + getPlateNumber() + "'" +
            ", aditionalNotes='" + getAditionalNotes() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", deleted=" + getDeleted() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
