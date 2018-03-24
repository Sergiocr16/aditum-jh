package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Collection entity.
 */
public class CollectionDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private String mensualBalance;

    @NotNull
    private Integer deleted;

    private Long houseId;

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

    public String getMensualBalance() {
        return mensualBalance;
    }

    public void setMensualBalance(String mensualBalance) {
        this.mensualBalance = mensualBalance;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CollectionDTO collectionDTO = (CollectionDTO) o;
        if(collectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CollectionDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", mensualBalance='" + getMensualBalance() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
