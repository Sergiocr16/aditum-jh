package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Watch entity.
 */
public class WatchDTO implements Serializable {

    private Long id;

    private ZonedDateTime initialtime;

    private ZonedDateTime finaltime;

    private List<OfficerDTO> officers;

    @NotNull
    private String responsableofficer;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getInitialtime() {
        return initialtime;
    }

    public void setInitialtime(ZonedDateTime initialtime) {
        this.initialtime = initialtime;
    }
    public ZonedDateTime getFinaltime() {
        return finaltime;
    }

    public void setFinaltime(ZonedDateTime finaltime) {
        this.finaltime = finaltime;
    }
    public String getResponsableofficer() {
        return responsableofficer;
    }

    public void setResponsableofficer(String responsableofficer) {
        this.responsableofficer = responsableofficer;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WatchDTO watchDTO = (WatchDTO) o;

        if ( ! Objects.equals(id, watchDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WatchDTO{" +
            "id=" + id +
            ", initialtime='" + initialtime + "'" +
            ", finaltime='" + finaltime + "'" +
            ", responsableofficer='" + responsableofficer + "'" +
            '}';
    }

    public List<OfficerDTO> getOfficers() {
        return officers;
    }

    public void setOfficers(List<OfficerDTO> officers) {
        this.officers = officers;
    }
}
