package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
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
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "identificationnumber", nullable = false)
    private String identificationnumber;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "inservice", nullable = false)
    private Integer inservice;

    @Column(name = "enable")
    private Boolean enable;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

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

    public String getEmail() {
        return email;
    }

    public Officer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public User getUser() {
        return user;
    }

    public Officer user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (officer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, officer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Officer{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastname='" + lastname + "'" +
            ", secondlastname='" + secondlastname + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", email='" + email + "'" +
            ", identificationnumber='" + identificationnumber + "'" +
            ", inservice='" + inservice + "'" +
            ", enable='" + enable + "'" +
            '}';
    }
}
