package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CommonAreaSchedule.
 */
@Entity
@Table(name = "common_area_schedule")
public class CommonAreaSchedule implements Serializable {

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

    @Column(name = "common_area_id")
    private Long commonAreaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLunes() {
        return lunes;
    }

    public CommonAreaSchedule lunes(String lunes) {
        this.lunes = lunes;
        return this;
    }

    public void setLunes(String lunes) {
        this.lunes = lunes;
    }

    public String getMartes() {
        return martes;
    }

    public CommonAreaSchedule martes(String martes) {
        this.martes = martes;
        return this;
    }

    public void setMartes(String martes) {
        this.martes = martes;
    }

    public String getMiercoles() {
        return miercoles;
    }

    public CommonAreaSchedule miercoles(String miercoles) {
        this.miercoles = miercoles;
        return this;
    }

    public void setMiercoles(String miercoles) {
        this.miercoles = miercoles;
    }

    public String getJueves() {
        return jueves;
    }

    public CommonAreaSchedule jueves(String jueves) {
        this.jueves = jueves;
        return this;
    }

    public void setJueves(String jueves) {
        this.jueves = jueves;
    }

    public String getViernes() {
        return viernes;
    }

    public CommonAreaSchedule viernes(String viernes) {
        this.viernes = viernes;
        return this;
    }

    public void setViernes(String viernes) {
        this.viernes = viernes;
    }

    public String getSabado() {
        return sabado;
    }

    public CommonAreaSchedule sabado(String sabado) {
        this.sabado = sabado;
        return this;
    }

    public void setSabado(String sabado) {
        this.sabado = sabado;
    }

    public String getDomingo() {
        return domingo;
    }

    public CommonAreaSchedule domingo(String domingo) {
        this.domingo = domingo;
        return this;
    }

    public void setDomingo(String domingo) {
        this.domingo = domingo;
    }

    public Long getCommonAreaId() {
        return commonAreaId;
    }

    public CommonAreaSchedule commonAreaId(Long commonAreaId) {
        this.commonAreaId = commonAreaId;
        return this;
    }

    public void setCommonAreaId(Long commonAreaId) {
        this.commonAreaId = commonAreaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonAreaSchedule commonAreaSchedule = (CommonAreaSchedule) o;
        if (commonAreaSchedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commonAreaSchedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommonAreaSchedule{" +
            "id=" + getId() +
            ", lunes='" + getLunes() + "'" +
            ", martes='" + getMartes() + "'" +
            ", miercoles='" + getMiercoles() + "'" +
            ", jueves='" + getJueves() + "'" +
            ", viernes='" + getViernes() + "'" +
            ", sabado='" + getSabado() + "'" +
            ", domingo='" + getDomingo() + "'" +
            ", commonAreaId='" + getCommonAreaId() + "'" +
            "}";
    }
}
