package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
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

    private ZonedDateTime fechanacimiento;

    private Integer annosexperiencia;

    private Long companyId;

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
    public Boolean getEnable() {
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
    public ZonedDateTime getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(ZonedDateTime fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }
    public Integer getAnnosexperiencia() {
        return annosexperiencia;
    }

    public void setAnnosexperiencia(Integer annosexperiencia) {
        this.annosexperiencia = annosexperiencia;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

        if ( ! Objects.equals(id, officerDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OfficerDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastname='" + lastname + "'" +
            ", secondlastname='" + secondlastname + "'" +
            ", image='" + image + "'" +
            ", identificationnumber='" + identificationnumber + "'" +
            ", inservice='" + inservice + "'" +
            ", enable='" + enable + "'" +
            ", image_url='" + image_url + "'" +
            ", fechanacimiento='" + fechanacimiento + "'" +
            ", annosexperiencia='" + annosexperiencia + "'" +
            '}';
    }
}
