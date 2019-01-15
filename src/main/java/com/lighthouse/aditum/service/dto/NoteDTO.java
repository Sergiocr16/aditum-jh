package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Note entity.
 */
public class NoteDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private Integer notetype;

    @NotNull
    private ZonedDateTime creationdate;

    private Long houseId;

    private Long companyId;

    private HouseDTO house;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getNotetype() {
        return notetype;
    }

    public void setNotetype(Integer notetype) {
        this.notetype = notetype;
    }
    public ZonedDateTime getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(ZonedDateTime creationdate) {
        this.creationdate = creationdate;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
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

        NoteDTO noteDTO = (NoteDTO) o;

        if ( ! Objects.equals(id, noteDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", notetype='" + notetype + "'" +
            ", creationdate='" + creationdate + "'" +
            '}';
    }

    public HouseDTO getHouse() {
        return house;
    }

    public void setHouse(HouseDTO house) {
        this.house = house;
    }
}
