package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A InvitationSchedule.
 */
@Entity
@Table(name = "invitation_schedule")
public class InvitationSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lunes")
    private String lunes;

    @Column(name = "martes")
    private String martes;

    @Column(name = "miercoles")
    private String miercoles;

    @Column(name = "jueves")
    private String jueves;

    @Column(name = "viernes")
    private String viernes;

    @Column(name = "sabado")
    private String sabado;

    @Column(name = "domingo")
    private String domingo;

    @Column(name = "visitant_invitation_id")
    private Long visitantInvitationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLunes() {
        return lunes;
    }

    public InvitationSchedule lunes(String lunes) {
        this.lunes = lunes;
        return this;
    }

    public void setLunes(String lunes) {
        this.lunes = lunes;
    }

    public String getMartes() {
        return martes;
    }

    public InvitationSchedule martes(String martes) {
        this.martes = martes;
        return this;
    }

    public void setMartes(String martes) {
        this.martes = martes;
    }

    public String getMiercoles() {
        return miercoles;
    }

    public InvitationSchedule miercoles(String miercoles) {
        this.miercoles = miercoles;
        return this;
    }

    public void setMiercoles(String miercoles) {
        this.miercoles = miercoles;
    }

    public String getJueves() {
        return jueves;
    }

    public InvitationSchedule jueves(String jueves) {
        this.jueves = jueves;
        return this;
    }

    public void setJueves(String jueves) {
        this.jueves = jueves;
    }

    public String getViernes() {
        return viernes;
    }

    public InvitationSchedule viernes(String viernes) {
        this.viernes = viernes;
        return this;
    }

    public void setViernes(String viernes) {
        this.viernes = viernes;
    }

    public String getSabado() {
        return sabado;
    }

    public InvitationSchedule sabado(String sabado) {
        this.sabado = sabado;
        return this;
    }

    public void setSabado(String sabado) {
        this.sabado = sabado;
    }

    public String getDomingo() {
        return domingo;
    }

    public InvitationSchedule domingo(String domingo) {
        this.domingo = domingo;
        return this;
    }

    public void setDomingo(String domingo) {
        this.domingo = domingo;
    }

    public Long getVisitantInvitationId() {
        return visitantInvitationId;
    }

    public InvitationSchedule visitantInvitationId(Long visitantInvitationId) {
        this.visitantInvitationId = visitantInvitationId;
        return this;
    }

    public void setVisitantInvitationId(Long visitantInvitationId) {
        this.visitantInvitationId = visitantInvitationId;
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
        InvitationSchedule invitationSchedule = (InvitationSchedule) o;
        if (invitationSchedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invitationSchedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvitationSchedule{" +
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
