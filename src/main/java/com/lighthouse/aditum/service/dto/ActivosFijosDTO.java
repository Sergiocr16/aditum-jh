package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ActivosFijos entity.
 */
public class ActivosFijosDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private Double valor;

    private Double depreciacion;

    private Integer deleted;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getDepreciacion() {
        return depreciacion;
    }

    public void setDepreciacion(Double depreciacion) {
        this.depreciacion = depreciacion;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivosFijosDTO activosFijosDTO = (ActivosFijosDTO) o;
        if(activosFijosDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activosFijosDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivosFijosDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", valor=" + getValor() +
            ", depreciacion=" + getDepreciacion() +
            ", deleted=" + getDeleted() +
            "}";
    }
}
