package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Emergency entity.
 */
public class EmergencyDTO implements Serializable {

    private Long id;

    private Integer observation;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer isAttended;

    private Long companyId;

    private Long houseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getObservation() {
        return observation;
    }

    public void setObservation(Integer observation) {
        this.observation = observation;
    }
    public Integer getIsAttended() {
        return isAttended;
    }

    public void setIsAttended(Integer isAttended) {
        this.isAttended = isAttended;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

        EmergencyDTO emergencyDTO = (EmergencyDTO) o;

        if ( ! Objects.equals(id, emergencyDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmergencyDTO{" +
            "id=" + id +
            ", observation='" + observation + "'" +
            ", isAttended='" + isAttended + "'" +
            '}';
    }
}
