package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Presupuesto entity.
 */
public class PresupuestoDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;

    private ZonedDateTime modificationDate;

    @NotNull
    private Integer anno;

    private Integer deleted;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ZonedDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(ZonedDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
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

        PresupuestoDTO presupuestoDTO = (PresupuestoDTO) o;
        if(presupuestoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), presupuestoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PresupuestoDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", anno='" + getAnno() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
