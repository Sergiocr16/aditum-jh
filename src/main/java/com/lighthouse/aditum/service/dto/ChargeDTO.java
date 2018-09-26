package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Charge entity.
 */
public class ChargeDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer type;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private String concept;

    @NotNull
    private String ammount;

    @NotNull
    private Integer state;

    @NotNull
    private Integer deleted;

    private ZonedDateTime paymentDate;

    private String subcharge;

    private String paymentAmmount;

    private String left;

    private double total;

    private Long houseId;

    private Long paymentId;

    private Long companyId;

    public ChargeDTO() {
    }
    public ChargeDTO(String concept,int total ) {
        this.concept = concept;
        this.total = total;

    }

    public ChargeDTO(Long id, @NotNull Integer type, @NotNull ZonedDateTime date, @NotNull String concept, @NotNull String ammount, @NotNull Integer state, @NotNull Integer deleted, ZonedDateTime paymentDate, String subcharge, String paymentAmmount, String left, double total, Long houseId, Long paymentId, Long companyId) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.concept = concept;
        this.ammount = ammount;
        this.state = state;
        this.deleted = deleted;
        this.paymentDate = paymentDate;
        this.subcharge = subcharge;
        this.paymentAmmount = paymentAmmount;
        this.left = left;
        this.total = total;
        this.houseId = houseId;
        this.paymentId = paymentId;
        this.companyId = companyId;
    }

    public ChargeDTO(String ammount, ZonedDateTime date, Long companyId, Long id, Long houseId) {
        this.concept = "Pagos anticipados";
        this.ammount = ammount;
        this.date = date;
        this.state = 2;
        this.type = 1;
        this.companyId = companyId;
        this.id = id;
        this.houseId = houseId;
        this.deleted = 0;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getSubcharge() {
        return subcharge;
    }

    public void setSubcharge(String subcharge) {
        this.subcharge = subcharge;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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

        ChargeDTO chargeDTO = (ChargeDTO) o;
        if(chargeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chargeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChargeDTO{" +
            "id=" + getId() +
            ", type=" + getType() +
            ", date='" + getDate() + "'" +
            ", concept='" + getConcept() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", state=" + getState() +
            ", deleted=" + getDeleted() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", subcharge='" + getSubcharge() + "'" +
            "}";
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPaymentAmmount() {
        return paymentAmmount;
    }

    public void setPaymentAmmount(String paymentAmmount) {
        this.paymentAmmount = paymentAmmount;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }
}
