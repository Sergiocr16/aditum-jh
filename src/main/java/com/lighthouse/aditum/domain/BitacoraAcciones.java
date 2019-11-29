package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A BitacoraAcciones.
 */
@Entity
@Table(name = "bitacora_acciones")
public class BitacoraAcciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "concept", nullable = false)
    private String concept;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "id_reference")
    private Long idReference;

    @Column(name = "id_responsable")
    private Long idResponsable;

    @NotNull
    @Column(name = "ejecution_date", nullable = false)
    private ZonedDateTime ejecutionDate;

    @Column(name = "category")
    private String category;

    @Column(name = "url_state")
    private String urlState;

    @Column(name = "house_id")
    private Long houseId;

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcept() {
        return concept;
    }

    public BitacoraAcciones concept(String concept) {
        this.concept = concept;
        return this;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public Integer getType() {
        return type;
    }

    public BitacoraAcciones type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getIdReference() {
        return idReference;
    }

    public BitacoraAcciones idReference(Long idReference) {
        this.idReference = idReference;
        return this;
    }

    public void setIdReference(Long idReference) {
        this.idReference = idReference;
    }

    public Long getIdResponsable() {
        return idResponsable;
    }

    public BitacoraAcciones idResponsable(Long idResponsable) {
        this.idResponsable = idResponsable;
        return this;
    }

    public void setIdResponsable(Long idResponsable) {
        this.idResponsable = idResponsable;
    }

    public ZonedDateTime getEjecutionDate() {
        return ejecutionDate;
    }

    public BitacoraAcciones ejecutionDate(ZonedDateTime ejecutionDate) {
        this.ejecutionDate = ejecutionDate;
        return this;
    }

    public void setEjecutionDate(ZonedDateTime ejecutionDate) {
        this.ejecutionDate = ejecutionDate;
    }

    public String getCategory() {
        return category;
    }

    public BitacoraAcciones category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrlState() {
        return urlState;
    }

    public BitacoraAcciones urlState(String urlState) {
        this.urlState = urlState;
        return this;
    }

    public void setUrlState(String urlState) {
        this.urlState = urlState;
    }

    public Long getHouseId() {
        return houseId;
    }

    public BitacoraAcciones houseId(Long houseId) {
        this.houseId = houseId;
        return this;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Company getCompany() {
        return company;
    }

    public BitacoraAcciones company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        BitacoraAcciones bitacoraAcciones = (BitacoraAcciones) o;
        if (bitacoraAcciones.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bitacoraAcciones.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BitacoraAcciones{" +
            "id=" + getId() +
            ", concept='" + getConcept() + "'" +
            ", type=" + getType() +
            ", idReference=" + getIdReference() +
            ", idResponsable=" + getIdResponsable() +
            ", ejecutionDate='" + getEjecutionDate() + "'" +
            ", category='" + getCategory() + "'" +
            ", urlState='" + getUrlState() + "'" +
            ", houseId=" + getHouseId() +
            "}";
    }
}
