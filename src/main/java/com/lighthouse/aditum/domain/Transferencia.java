package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Transferencia.
 */
@Entity
@Table(name = "transferencia")
public class Transferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "concepto", nullable = false)
    private String concepto;

    @NotNull
    @Column(name = "cuenta_origen", nullable = false)
    private String cuentaOrigen;

    @NotNull
    @Column(name = "cuenta_destino", nullable = false)
    private String cuentaDestino;

    @NotNull
    @Column(name = "monto", nullable = false)
    private String monto;

    @Column(name = "id_company")
    private Integer idCompany;

    @Column(name = "fecha")
    private ZonedDateTime fecha;

    @Column(name = "id_banco_origen")
    private Integer idBancoOrigen;

    @Column(name = "id_banco_destino")
    private Integer idBancoDestino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcepto() {
        return concepto;
    }

    public Transferencia concepto(String concepto) {
        this.concepto = concepto;
        return this;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    public Transferencia cuentaOrigen(String cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
        return this;
    }

    public void setCuentaOrigen(String cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public Transferencia cuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
        return this;
    }

    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public String getMonto() {
        return monto;
    }

    public Transferencia monto(String monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public Integer getIdCompany() {
        return idCompany;
    }

    public Transferencia idCompany(Integer idCompany) {
        this.idCompany = idCompany;
        return this;
    }

    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public Transferencia fecha(ZonedDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public Integer getIdBancoOrigen() {
        return idBancoOrigen;
    }

    public Transferencia idBancoOrigen(Integer idBancoOrigen) {
        this.idBancoOrigen = idBancoOrigen;
        return this;
    }

    public void setIdBancoOrigen(Integer idBancoOrigen) {
        this.idBancoOrigen = idBancoOrigen;
    }

    public Integer getIdBancoDestino() {
        return idBancoDestino;
    }

    public Transferencia idBancoDestino(Integer idBancoDestino) {
        this.idBancoDestino = idBancoDestino;
        return this;
    }

    public void setIdBancoDestino(Integer idBancoDestino) {
        this.idBancoDestino = idBancoDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transferencia transferencia = (Transferencia) o;
        if (transferencia.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transferencia.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transferencia{" +
            "id=" + getId() +
            ", concepto='" + getConcepto() + "'" +
            ", cuentaOrigen='" + getCuentaOrigen() + "'" +
            ", cuentaDestino='" + getCuentaDestino() + "'" +
            ", monto='" + getMonto() + "'" +
            ", idCompany='" + getIdCompany() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", idBancoOrigen='" + getIdBancoOrigen() + "'" +
            ", idBancoDestino='" + getIdBancoDestino() + "'" +
            "}";
    }
}
