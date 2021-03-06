package com.lighthouse.aditum.service.dto;


import com.lighthouse.aditum.service.util.RandomUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the Charge entity.
 */
public class ChargeDTO implements Serializable {
    private Locale locale = new Locale("es", "CR");
    private Long id;

    @NotNull
    private Integer type;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private String concept;

    @NotNull
    private String ammount;

    private String temporalAmmount;

    private Integer state;


    private Integer deleted;

    private ZonedDateTime paymentDate;

    private String subcharge;

    private String paymentAmmount;

    private String left;

    private double total;

    private String totalFormatted;

    private Long houseId;

    private Long paymentId;

    private Long companyId;

    private String formatedDate;

    private boolean payedSubcharge;

    private Integer splited;

    private Integer splitedCharge;

    public ChargeDTO() {
    }
    public ChargeDTO(String concept,int total ) {
        this.concept = concept;
        this.total = total;
        this.totalFormatted = RandomUtil.formatMoney(this.total);

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
        this.totalFormatted = RandomUtil.formatMoney(this.total);
        this.houseId = houseId;
        this.paymentId = paymentId;
        this.companyId = companyId;
    }

    public ChargeDTO(String ammount,String subcharge, ZonedDateTime date, Long companyId, Long id, Long houseId) {
        this.concept = "Pagos anticipados";
        this.ammount = ammount;
        this.paymentAmmount = ammount;
        this.total = Double.parseDouble(ammount);
        this.totalFormatted = RandomUtil.formatMoney(this.total);
        this.left = "0";
        this.paymentId = id;
        this.subcharge = subcharge;
        this.paymentDate = date;
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
        this.totalFormatted = RandomUtil.formatMoney(this.total);
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

    public boolean isPayedSubcharge() {
        return payedSubcharge;
    }

    public void setPayedSubcharge(boolean payedSubcharge) {
        this.payedSubcharge = payedSubcharge;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public String getFormatedType() {
        String formattedType = "";
        switch (this.getType()) {
            case 1:
                formattedType =  "MANTENIMIENTO";
            break;
            case 2:
                formattedType = "EXTRAORDINARIA";
            break;
            case 3:
                formattedType = "ÁREAS COMUNES";
            break;
        }
        return formattedType;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public Integer getSplited() {
        return splited;
    }

    public void setSplited(Integer splited) {
        this.splited = splited;
    }

    public Integer getSplitedCharge() {
        return splitedCharge;
    }

    public void setSplitedCharge(Integer splitedCharge) {
        this.splitedCharge = splitedCharge;
    }

    public String getTemporalAmmount() {
        return temporalAmmount;
    }

    public void setTemporalAmmount(String temporalAmmount) {
        this.temporalAmmount = temporalAmmount;
    }
}
