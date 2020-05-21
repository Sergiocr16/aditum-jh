package com.lighthouse.aditum.service.dto;


import com.lighthouse.aditum.domain.House;
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

    private boolean sendEmail;

    private int defaulterDays;

    private Integer deleted;

    private ZonedDateTime paymentDate;

    private String subcharge;

    private String paymentAmmount;

    private String left;

    private double leftToPay;

    private String leftToPayFormatted;

    private double total;

    private double abonado;

    private String abonadoFormatted;

    private String waterConsumption;

    public int getDefaulterDays() {
        return defaulterDays;
    }

    public void setDefaulterDays(int defaulterDays) {
        this.defaulterDays = defaulterDays;
    }

    public String getWaterConsumption() {
        return this.waterConsumption==null?"":"/ "+waterConsumption+" m³";
    }

    public void setWaterConsumption(String waterConsumption) {
        this.waterConsumption = waterConsumption;
    }

    public double getAbonado() {
        return abonado;
    }

    public double getLeftToPay() {
        return leftToPay;
    }

    public void setLeftToPay(String currency, double leftToPay) {
        this.leftToPay = leftToPay;
        this.leftToPayFormatted = RandomUtil.formatMoney(currency, this.leftToPay);
    }

    public void setAbonado(String currency, double abonado) {
        this.abonado = abonado;
        this.abonadoFormatted = RandomUtil.formatMoney(currency, this.abonado);
    }

    private String totalFormatted;

    private Long houseId;

    private Long paymentId;

    private Boolean downloading;

    private Long companyId;

    private String formatedDate;

    private ResidentDTO responsable;

    private HouseDTO house;

    private String houseNumber;

    private boolean payedSubcharge;

    private Integer splited;

    private Integer consecutive;

    private Integer splitedCharge;

    private String billNumber;


    public ChargeDTO() {
    }

    public ChargeDTO(String concept, int total) {
        this.concept = concept;
        this.total = total;
//        this.totalFormatted = RandomUtil.formatMoney(this.total);

    }

    public ChargeDTO(String currency, Long id, @NotNull Integer type, @NotNull ZonedDateTime date, @NotNull String concept, @NotNull String ammount, @NotNull Integer state, @NotNull Integer deleted, ZonedDateTime paymentDate, String subcharge, String paymentAmmount, String left, double total, Long houseId, Long paymentId, Long companyId) {
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
        this.totalFormatted = RandomUtil.formatMoney(currency, this.total);
        this.houseId = houseId;
        this.paymentId = paymentId;
        this.companyId = companyId;
    }

    public ChargeDTO(String currency, String ammount, String subcharge, ZonedDateTime date, Long companyId, Long id, Long houseId) {
        this.concept = "Pagos anticipados";
        this.ammount = ammount;
        this.paymentAmmount = ammount;
        this.total = Double.parseDouble(ammount);
        this.totalFormatted = RandomUtil.formatMoney(currency, this.total);
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

    public Integer getConsecutive() {

        return consecutive;
    }

    public String formatBillNumber(int billNumber) {
        int zerosToAdd = 4;
        String consecutive = billNumber + "";
        int digits = consecutive.length();
        int zeros = zerosToAdd - digits;
        String zerosFormatted = "";
        if (zeros < 1) {
            return consecutive;
        } else {
            for (int i = 0; i < zeros; i++) {
                zerosFormatted += "0";
            }
            return zerosFormatted + consecutive;
        }
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public void setConsecutive(Integer consecutive) {
        this.consecutive = consecutive;
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
        if (chargeDTO.getId() == null || getId() == null) {
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

    public void setTotal(String currency, double total) {
        this.total = total;
        this.totalFormatted = RandomUtil.formatMoney(currency, this.total);
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

    public void setLeft(String currency, double left) {
        this.left = RandomUtil.formatMoney(currency, left);
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
                formattedType = "MANTENIMIENTO";
                break;
            case 2:
                formattedType = "EXTRAORDINARIA";
                break;
            case 3:
                formattedType = "ÁREAS COMUNES";
                break;
            case 4:
                formattedType = "MULTA";
                break;
            case 6:
                formattedType = "CUOTA AGUA";
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

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public ResidentDTO getResponsable() {
        return responsable;
    }

    public void setResponsable(ResidentDTO responsable) {
        this.responsable = responsable;
    }

    public HouseDTO getHouse() {
        return house;
    }

    public void setHouse(HouseDTO house) {
        this.house = house;
    }

    public Boolean getDownloading() {
        return downloading;
    }

    public void setDownloading(Boolean downloading) {
        this.downloading = downloading;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
