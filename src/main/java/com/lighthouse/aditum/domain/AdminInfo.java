package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AdminInfo.
 */
@Entity
@Table(name = "admin_info")
public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "secondlastname")
    private String secondlastname;

    @Column(name = "identificationnumber")
    private String identificationnumber;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "enabled")
    private Integer enabled;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "administrada_oficiales")
    private Integer administradaOficiales;

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

    public AdminInfo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public AdminInfo lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public AdminInfo secondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
        return this;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public AdminInfo identificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
        return this;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public String getEmail() {
        return email;
    }

    public AdminInfo email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public AdminInfo image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public AdminInfo imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public AdminInfo enabled(Integer enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getImage_url() {
        return image_url;
    }

    public AdminInfo image_url(String image_url) {
        this.image_url = image_url;
        return this;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getAdministradaOficiales() {
        return administradaOficiales;
    }

    public AdminInfo administradaOficiales(Integer administradaOficiales) {
        this.administradaOficiales = administradaOficiales;
        return this;
    }

    public void setAdministradaOficiales(Integer administradaOficiales) {
        this.administradaOficiales = administradaOficiales;
    }

    public User getUser() {
        return user;
    }

    public AdminInfo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public AdminInfo company(Company company) {
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
        AdminInfo adminInfo = (AdminInfo) o;
        if (adminInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, adminInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AdminInfo{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastname='" + lastname + "'" +
            ", secondlastname='" + secondlastname + "'" +
            ", identificationnumber='" + identificationnumber + "'" +
            ", email='" + email + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", enabled='" + enabled + "'" +
            ", image_url='" + image_url + "'" +
            ", administradaOficiales='" + administradaOficiales + "'" +
            '}';
    }
}
