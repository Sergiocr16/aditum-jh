package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A OfficerAR.
 */
@Entity
@Table(name = "officerar")
public class OfficerAR implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "second_last_name")
    private String secondLastName;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "in_service")
    private String inService;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "annos_experiencia")
    private String annosExperiencia;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "direction")
    private String direction;

    @Column(name = "plate_number")
    private String plateNumber;

    @Column(name = "aditional_notes")
    private String aditionalNotes;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "email")
    private String email;

    @ManyToOne
    private Company company;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "officerar_house",
               joinColumns = @JoinColumn(name="officerars_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="houses_id", referencedColumnName="id"))
    private Set<House> houses = new HashSet<>();

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

    public OfficerAR name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public OfficerAR lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public OfficerAR secondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
        return this;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public OfficerAR identificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
        return this;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getInService() {
        return inService;
    }

    public OfficerAR inService(String inService) {
        this.inService = inService;
        return this;
    }

    public void setInService(String inService) {
        this.inService = inService;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public OfficerAR imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAnnosExperiencia() {
        return annosExperiencia;
    }

    public OfficerAR annosExperiencia(String annosExperiencia) {
        this.annosExperiencia = annosExperiencia;
        return this;
    }

    public void setAnnosExperiencia(String annosExperiencia) {
        this.annosExperiencia = annosExperiencia;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public OfficerAR birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public OfficerAR phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDirection() {
        return direction;
    }

    public OfficerAR direction(String direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public OfficerAR plateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        return this;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getAditionalNotes() {
        return aditionalNotes;
    }

    public OfficerAR aditionalNotes(String aditionalNotes) {
        this.aditionalNotes = aditionalNotes;
        return this;
    }

    public void setAditionalNotes(String aditionalNotes) {
        this.aditionalNotes = aditionalNotes;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public OfficerAR enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public OfficerAR deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getEmail() {
        return email;
    }

    public OfficerAR email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Company getCompany() {
        return company;
    }

    public OfficerAR company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public OfficerAR user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<House> getHouses() {
        return houses;
    }

    public OfficerAR houses(Set<House> houses) {
        this.houses = houses;
        return this;
    }

    public OfficerAR addHouse(House house) {
        this.houses.add(house);
        return this;
    }

    public OfficerAR removeHouse(House house) {
        this.houses.remove(house);
        return this;
    }

    public void setHouses(Set<House> houses) {
        this.houses = houses;
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
        OfficerAR officerAR = (OfficerAR) o;
        if (officerAR.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), officerAR.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfficerAR{" +
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
