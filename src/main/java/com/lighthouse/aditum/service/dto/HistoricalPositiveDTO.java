package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the HistoricalPositive entity.
 */
public class HistoricalPositiveDTO implements Serializable {

    private Long id;

    private String total;

    private String housenumber;

    private ZonedDateTime date;

    private String formatedDate;

    private String totalFormated;

    private Long companyId;

    private Long houseId;

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public String getTotalFormated() {
        return totalFormated;
    }

    public void setTotalFormated(String totalFormated) {
        this.totalFormated = totalFormated;
    }

    public HistoricalPositiveDTO(Long id, String total, String housenumber, ZonedDateTime date, Long companyId, Long houseId) {
        this.id = id;
        this.total = total;
        this.housenumber = housenumber;
        this.date = date;
        this.companyId = companyId;
        this.houseId = houseId;
    }

    public HistoricalPositiveDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
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

        HistoricalPositiveDTO historicalPositiveDTO = (HistoricalPositiveDTO) o;
        if (historicalPositiveDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historicalPositiveDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoricalPositiveDTO{" +
            "id=" + getId() +
            ", total='" + getTotal() + "'" +
            ", housenumber='" + getHousenumber() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
