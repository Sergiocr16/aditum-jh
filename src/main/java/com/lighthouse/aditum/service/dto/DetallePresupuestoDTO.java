package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DetallePresupuesto entity.
 */
public class DetallePresupuestoDTO implements Serializable {

    private Long id;

    @NotNull
    private String category;
    private String categoryName;
    private String group;

    private String valuePerMonth;

    @NotNull
    private String type;

    @NotNull
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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValuePerMonth() {
        return valuePerMonth;
    }

    public void setValuePerMonth(String valuePerMonth) {
        this.valuePerMonth = valuePerMonth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPresupuestoId() {
        return presupuestoId;
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

        DetallePresupuestoDTO detallePresupuestoDTO = (DetallePresupuestoDTO) o;
        if(detallePresupuestoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), detallePresupuestoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DetallePresupuestoDTO{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", valuePerMonth='" + getValuePerMonth() + "'" +
            ", type='" + getType() + "'" +
            ", presupuestoId='" + getPresupuestoId() + "'" +
            "}";
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
