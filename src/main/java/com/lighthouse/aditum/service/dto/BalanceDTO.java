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

    private Long houseId;

    private String houseHousenumber;

    private int debit;

    private String total;

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
            ", houseId='" + getHouseId() + "'" +
            "}";
    }



    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
