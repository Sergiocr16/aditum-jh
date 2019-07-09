package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the InvitationSchedule entity.
 */
public class InvitationScheduleDTO implements Serializable {

    private Long id;

    private String lunes;

    private String martes;

    private String miercoles;

    private String jueves;

    private String viernes;

    private String sabado;

    private String domingo;

    private Long visitantInvitationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLunes() {
        return lunes;
    }

    public void setLunes(String lunes) {
        this.lunes = lunes;
    }

    public String getMartes() {
        return martes;
    }

    public void setMartes(String martes) {
        this.martes = martes;
    }

    public String getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(String miercoles) {
        this.miercoles = miercoles;
    }

    public String getJueves() {
        return jueves;
    }

    public void setJueves(String jueves) {
        this.jueves = jueves;
    }

    public String getViernes() {
        return viernes;
    }

    public void setViernes(String viernes) {
        this.viernes = viernes;
    }

    public String getSabado() {
        return sabado;
    }

    public void setSabado(String sabado) {
        this.sabado = sabado;
    }

    public String getDomingo() {
        return domingo;
    }

    public void setDomingo(String domingo) {
        this.domingo = domingo;
    }

    public Long getVisitantInvitationId() {
        return visitantInvitationId;
    }

    public void setVisitantInvitationId(Long visitantInvitationId) {
        this.visitantInvitationId = visitantInvitationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvitationScheduleDTO invitationScheduleDTO = (InvitationScheduleDTO) o;
        if(invitationScheduleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invitationScheduleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvitationScheduleDTO{" +
            "id=" + getId() +
            ", lunes='" + getLunes() + "'" +
            ", martes='" + getMartes() + "'" +
            ", miercoles='" + getMiercoles() + "'" +
            ", jueves='" + getJueves() + "'" +
            ", viernes='" + getViernes() + "'" +
            ", sabado='" + getSabado() + "'" +
            ", domingo='" + getDomingo() + "'" +
            ", visitantInvitationId=" + getVisitantInvitationId() +
            "}";
    }
}
