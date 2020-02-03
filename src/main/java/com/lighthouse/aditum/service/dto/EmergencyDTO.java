package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
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

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer isAttended;

    private String observation;

    private String file_url;

    private String tipo;

    private ZonedDateTime reportedDate;

    private Long companyId;

    private Long houseId;

    private String houseNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsAttended() {
        return isAttended;
    }

    public void setIsAttended(Integer isAttended) {
        this.isAttended = isAttended;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTime getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(ZonedDateTime reportedDate) {
        this.reportedDate = reportedDate;
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
        if(emergencyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emergencyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmergencyDTO{" +
            "id=" + getId() +
            ", isAttended=" + getIsAttended() +
            ", observation='" + getObservation() + "'" +
            ", file_url='" + getFile_url() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", reportedDate='" + getReportedDate() + "'" +
            "}";
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
