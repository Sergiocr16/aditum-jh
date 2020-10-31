package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the HistoricalDefaulter entity.
 */
public class HistoricalDefaulterDTO implements Serializable {

    private Long id;

    private String total;

    private ZonedDateTime date;

    private String categories;

    private String housenumber;

    private Long companyId;

    private Long houseId;

    private List<HistoricalDefaulterChargeDTO> charges;


    private String totalFormated;

    public String getTotalFormated() {
        return totalFormated;
    }

    public void setTotalFormated(String totalFormated) {
        this.totalFormated = totalFormated;
    }

    public HistoricalDefaulterDTO() {
    }

    public HistoricalDefaulterDTO(Long id, String total, ZonedDateTime date, String categories, String housenumber, Long companyId, Long houseId) {
        this.id = id;
        this.total = total;
        this.date = date;
        this.categories = categories;
        this.housenumber = housenumber;
        this.companyId = companyId;
        this.houseId = houseId;
    }

    public List<HistoricalDefaulterChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<HistoricalDefaulterChargeDTO> charges) {
        this.charges = charges;
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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
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

        HistoricalDefaulterDTO historicalDefaulterDTO = (HistoricalDefaulterDTO) o;
        if (historicalDefaulterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historicalDefaulterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoricalDefaulterDTO{" +
            "id=" + getId() +
            ", total='" + getTotal() + "'" +
            ", date='" + getDate() + "'" +
            ", categories='" + getCategories() + "'" +
            ", housenumber='" + getHousenumber() + "'" +
            "}";
    }
}
