package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A Emergency.
 */
@Entity
@Table(name = "emergency")
public class Emergency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "is_attended", nullable = false)
    private Integer isAttended;

    @Column(name = "observation")
    private String observation;

    @Column(name = "file_url")
    private String file_url;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "reported_date")
    private ZonedDateTime reportedDate;

    @ManyToOne
    private Company company;

    @ManyToOne
    private House house;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsAttended() {
        return isAttended;
    }

    public Emergency isAttended(Integer isAttended) {
        this.isAttended = isAttended;
        return this;
    }

    public void setIsAttended(Integer isAttended) {
        this.isAttended = isAttended;
    }

    public String getObservation() {
        return observation;
    }

    public Emergency observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getFile_url() {
        return file_url;
    }

    public Emergency file_url(String file_url) {
        this.file_url = file_url;
        return this;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getTipo() {
        return tipo;
    }

    public Emergency tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTime getReportedDate() {
        return reportedDate;
    }

    public Emergency reportedDate(ZonedDateTime reportedDate) {
        this.reportedDate = reportedDate;
        return this;
    }

    public void setReportedDate(ZonedDateTime reportedDate) {
        this.reportedDate = reportedDate;
    }

    public Company getCompany() {
        return company;
    }

    public Emergency company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public House getHouse() {
        return house;
    }

    public Emergency house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
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
        Emergency emergency = (Emergency) o;
        if (emergency.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emergency.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Emergency{" +
            "id=" + getId() +
            ", isAttended=" + getIsAttended() +
            ", observation='" + getObservation() + "'" +
            ", file_url='" + getFile_url() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", reportedDate='" + getReportedDate() + "'" +
            "}";
    }
}
