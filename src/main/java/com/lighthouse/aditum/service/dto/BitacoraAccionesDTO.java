package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the BitacoraAcciones entity.
 */
public class BitacoraAccionesDTO implements Serializable {

    private Long id;

    @NotNull
    private String concept;

    private Integer type;

    private Long idReference;

    private Long idResponsable;


    private AdminInfoDTO responsable;

    @NotNull
    private ZonedDateTime ejecutionDate;

    private String category;

    private String urlState;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getIdReference() {
        return idReference;
    }

    public void setIdReference(Long idReference) {
        this.idReference = idReference;
    }

    public Long getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Long idResponsable) {
        this.idResponsable = idResponsable;
    }

    public ZonedDateTime getEjecutionDate() {
        return ejecutionDate;
    }

    public void setEjecutionDate(ZonedDateTime ejecutionDate) {
        this.ejecutionDate = ejecutionDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrlState() {
        return urlState;
    }

    public void setUrlState(String urlState) {
        this.urlState = urlState;
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

        BitacoraAccionesDTO bitacoraAccionesDTO = (BitacoraAccionesDTO) o;
        if(bitacoraAccionesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bitacoraAccionesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BitacoraAccionesDTO{" +
            "id=" + getId() +
            ", concept='" + getConcept() + "'" +
            ", type=" + getType() +
            ", idReference=" + getIdReference() +
            ", idResponsable=" + getIdResponsable() +
            ", ejecutionDate='" + getEjecutionDate() + "'" +
            ", category='" + getCategory() + "'" +
            ", urlState='" + getUrlState() + "'" +
            "}";
    }

    public AdminInfoDTO getResponsable() {
        return responsable;
    }

    public void setResponsable(AdminInfoDTO responsable) {
        this.responsable = responsable;
    }
}
