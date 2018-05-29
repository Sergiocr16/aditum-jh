package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DetallePresupuesto.
 */
@Entity
@Table(name = "detalle_presupuesto")
public class DetallePresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "value_per_month")
    private String valuePerMonth;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "presupuesto_id", nullable = false)
    private String presupuestoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public DetallePresupuesto category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValuePerMonth() {
        return valuePerMonth;
    }

    public DetallePresupuesto valuePerMonth(String valuePerMonth) {
        this.valuePerMonth = valuePerMonth;
        return this;
    }

    public void setValuePerMonth(String valuePerMonth) {
        this.valuePerMonth = valuePerMonth;
    }

    public String getType() {
        return type;
    }

    public DetallePresupuesto type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPresupuestoId() {
        return presupuestoId;
    }

    public DetallePresupuesto presupuestoId(String presupuestoId) {
        this.presupuestoId = presupuestoId;
        return this;
    }

    public void setPresupuestoId(String presupuestoId) {
        this.presupuestoId = presupuestoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DetallePresupuesto detallePresupuesto = (DetallePresupuesto) o;
        if (detallePresupuesto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), detallePresupuesto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DetallePresupuesto{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", valuePerMonth='" + getValuePerMonth() + "'" +
            ", type='" + getType() + "'" +
            ", presupuestoId='" + getPresupuestoId() + "'" +
            "}";
    }
}
