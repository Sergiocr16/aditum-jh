package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A ActivosFijos.
 */
@Entity
@Table(name = "activos_fijos")
public class ActivosFijos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "depreciacion")
    private Double depreciacion;

    @Column(name = "deleted")
    private Integer deleted;

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public ActivosFijos nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getValor() {
        return valor;
    }

    public ActivosFijos valor(Double valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getDepreciacion() {
        return depreciacion;
    }

    public ActivosFijos depreciacion(Double depreciacion) {
        this.depreciacion = depreciacion;
        return this;
    }

    public void setDepreciacion(Double depreciacion) {
        this.depreciacion = depreciacion;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public ActivosFijos deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Company getCompany() {
        return company;
    }

    public ActivosFijos company(Company company) {
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
        ActivosFijos activosFijos = (ActivosFijos) o;
        if (activosFijos.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activosFijos.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivosFijos{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", valor=" + getValor() +
            ", depreciacion=" + getDepreciacion() +
            ", deleted=" + getDeleted() +
            "}";
    }
}
