package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Officer.
 */
@Entity
@Table(name = "officer")
public class Officer implements Serializable {

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

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Column(name = "identificationnumber", nullable = false)
    private String identificationnumber;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "inservice", nullable = false)
    private Integer inservice;

    @Column(name = "jhi_enable")
    private Boolean enable;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "annosexperiencia")
    private Integer annosexperiencia;

    @Column(name = "fechanacimiento")
    private LocalDate fechanacimiento;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Column(name = "direction")
    private String direction;

    @Column(name = "deleted")
    private Integer deleted;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Officer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public Officer lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public Officer secondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
        return this;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public byte[] getImage() {
        return image;
    }

    public Officer image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Officer imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public Officer identificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
        return this;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public Integer getInservice() {
        return inservice;
    }

    public Officer inservice(Integer inservice) {
        this.inservice = inservice;
        return this;
    }

    public void setInservice(Integer inservice) {
        this.inservice = inservice;
    }

    public Boolean isEnable() {
        return enable;
    }

    public Officer enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getImage_url() {
        return image_url;
    }

    public Officer image_url(String image_url) {
        this.image_url = image_url;
        return this;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getAnnosexperiencia() {
        return annosexperiencia;
    }

    public Officer annosexperiencia(Integer annosexperiencia) {
        this.annosexperiencia = annosexperiencia;
        return this;
    }

    public void setAnnosexperiencia(Integer annosexperiencia) {
        this.annosexperiencia = annosexperiencia;
    }

    public LocalDate getFechanacimiento() {
        return fechanacimiento;
    }

    public Officer fechanacimiento(LocalDate fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
        return this;
    }

    public void setFechanacimiento(LocalDate fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public Officer phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getDirection() {
        return direction;
    }

    public Officer direction(String direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Officer deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Company getCompany() {
        return company;
    }

    public Officer company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
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
        Officer officer = (Officer) o;
        if (officer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), officer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Officer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", secondlastname='" + getSecondlastname() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + imageContentType + "'" +
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
