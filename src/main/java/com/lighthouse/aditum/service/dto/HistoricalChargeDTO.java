package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the HistoricalCharge entity.
 */
public class HistoricalChargeDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private String concept;

    @NotNull
    private String ammount;

    private String abono;

    @NotNull
    private Integer type;

    @NotNull
    private Integer state;

    @NotNull
    private Integer deleted;

    private String total;

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

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getAbono() {
        return abono;
    }

    public void setAbono(String abono) {
        this.abono = abono;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

        HistoricalChargeDTO historicalChargeDTO = (HistoricalChargeDTO) o;
        if(historicalChargeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historicalChargeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoricalChargeDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", concept='" + getConcept() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", abono='" + getAbono() + "'" +
            ", type='" + getType() + "'" +
            ", state='" + getState() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", total='" + getTotal() + "'" +
            "}";
    }
}
