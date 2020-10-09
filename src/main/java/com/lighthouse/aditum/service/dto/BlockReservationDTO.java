package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the BlockReservation entity.
 */
public class BlockReservationDTO implements Serializable {

    private Long id;

    private Integer blocked;

    private String comments;

    private Long houseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBlocked() {
        return blocked;
    }

    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

        BlockReservationDTO blockReservationDTO = (BlockReservationDTO) o;
        if(blockReservationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blockReservationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlockReservationDTO{" +
            "id=" + getId() +
            ", blocked=" + getBlocked() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
