package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the WaterConsumption entity.
 */
public class WaterConsumptionDTO implements Serializable {

    private Long id;

    private String consumption;

    private String month;

    private ZonedDateTime recordDate;

    private Integer status;

    private Long houseId;

    private String housenumber;

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public ZonedDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(ZonedDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

        WaterConsumptionDTO waterConsumptionDTO = (WaterConsumptionDTO) o;
        if(waterConsumptionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), waterConsumptionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WaterConsumptionDTO{" +
            "id=" + getId() +
            ", consumption='" + getConsumption() + "'" +
            ", month='" + getMonth() + "'" +
            ", recordDate='" + getRecordDate() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
