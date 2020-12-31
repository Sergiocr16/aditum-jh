package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Balance entity.
 */
public class BalanceDTO implements Serializable {

    private Long id;

    @NotNull
    private String extraordinary;

    @NotNull
    private String commonAreas;

    @NotNull
    private String maintenance;

    private String waterCharge;

    private String others;

    private String multa;

    private Long houseId;

    private String houseHousenumber;

    private String total;

    private String totalFavor;

    private Long companyId;

    public String getTotalFavor() {
        return totalFavor;
    }

    public void setTotalFavor(String totalFavor) {
        this.totalFavor = totalFavor;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtraordinary() {
        return extraordinary;
    }

    public void setExtraordinary(String extraordinary) {
        this.extraordinary = extraordinary;
    }

    public String getCommonAreas() {
        return commonAreas;
    }

    public void setCommonAreas(String commonAreas) {
        this.commonAreas = commonAreas;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getWaterCharge() {
        return waterCharge;
    }

    public void setWaterCharge(String waterCharge) {
        this.waterCharge = waterCharge;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getHouseHousenumber() {
        return houseHousenumber;
    }

    public void setHouseHousenumber(String houseHousenumber) {
        this.houseHousenumber = houseHousenumber;
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

        BalanceDTO balanceDTO = (BalanceDTO) o;
        if(balanceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), balanceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BalanceDTO{" +
            "id=" + getId() +
            ", extraordinary='" + getExtraordinary() + "'" +
            ", commonAreas='" + getCommonAreas() + "'" +
            ", maintenance='" + getMaintenance() + "'" +
            ", waterCharge='" + getWaterCharge() + "'" +
            ", others='" + getOthers() + "'" +
            ", multa='" + getMulta() + "'" +
            "}";
    }
}
