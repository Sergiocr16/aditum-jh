package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Resident.
 */
@Entity
@Table(name = "resident")
public class Resident implements Serializable {

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

    @NotNull
    @Column(name = "identificationnumber", nullable = false)
    private String identificationnumber;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "email")
    private String email;

    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "is_owner")
    private Integer isOwner;

    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "enabled")
    private Integer enabled;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "type")
    private Integer type;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private Company company;

    @ManyToOne
    private House house;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Resident name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public Resident lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public Resident secondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
        return this;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getIdentificationnumber() {
        return identificationnumber;
    }

    public Resident identificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
        return this;
    }

    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public Resident phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public byte[] getImage() {
        return image;
    }

    public Resident image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Resident imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getEmail() {
        return email;
    }

    public Resident email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsOwner() {
        return isOwner;
    }

    public Resident isOwner(Integer isOwner) {
        this.isOwner = isOwner;
        return this;
    }

    public void setIsOwner(Integer isOwner) {
        this.isOwner = isOwner;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public Resident enabled(Integer enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getImage_url() {
        return image_url;
    }

    public Resident image_url(String image_url) {
        this.image_url = image_url;
        return this;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getType() {
        return type;
    }

    public Resident type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public Resident user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public Resident company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public House getHouse() {
        return house;
    }

    public Resident house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resident resident = (Resident) o;
        if (resident.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, resident.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Resident{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastname='" + lastname + "'" +
            ", secondlastname='" + secondlastname + "'" +
            ", identificationnumber='" + identificationnumber + "'" +
            ", phonenumber='" + phonenumber + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", email='" + email + "'" +
            ", isOwner='" + isOwner + "'" +
            ", enabled='" + enabled + "'" +
            ", image_url='" + image_url + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
