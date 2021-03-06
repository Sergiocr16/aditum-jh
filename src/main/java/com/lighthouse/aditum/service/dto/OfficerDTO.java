package com.lighthouse.aditum.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Officer entity.
 */
public class OfficerDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    @NotNull
    private String secondlastname;

    @Lob
    private byte[] image;
    private String imageContentType;

    @NotNull
    private String identificationnumber;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer inservice;

    private Boolean enable;

    private String image_url;

    private Integer annosexperiencia;

    private LocalDate fechanacimiento;

    private String phonenumber;

    private String direction;

    private Integer deleted;

    private Long companyId;

    private CompanyDTO company;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public Integer getInservice() {
        return inservice;
    }

    public void setInservice(Integer inservice) {
        this.inservice = inservice;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getAnnosexperiencia() {
        return annosexperiencia;
    }

    public void setAnnosexperiencia(Integer annosexperiencia) {
        this.annosexperiencia = annosexperiencia;
    }

    public LocalDate getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(LocalDate fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OfficerDTO officerDTO = (OfficerDTO) o;
        if(officerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), officerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfficerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", secondlastname='" + getSecondlastname() + "'" +
            ", image='" + getImage() + "'" +
            ", identificationnumber='" + getIdentificationnumber() + "'" +
            ", inservice='" + getInservice() + "'" +
            ", enable='" + isEnable() + "'" +
            ", image_url='" + getImage_url() + "'" +
            ", annosexperiencia='" + getAnnosexperiencia() + "'" +
            ", fechanacimiento='" + getFechanacimiento() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", direction='" + getDirection() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
