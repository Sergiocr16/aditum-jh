package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the HistoricalDefaulterCharge entity.
 */
public class HistoricalDefaulterChargeDTO implements Serializable {

    private Long id;

    private Integer type;

    private ZonedDateTime date;

    private String concept;

    private String consecutive;

    private String ammount;

    private String leftToPay;

    private String abonado;

    private String defaultersDay;

    private Long historicalDefaulterId;

    private String category;

    private String billNumber;

    private String formatedDate;

    private String abonadoFormated;

    private String totalFormated;

    private String paymentDateFormated;

    public String getPaymentDateFormated() {
        return paymentDateFormated;
    }

    public void setPaymentDateFormated(String paymentDateFormated) {
        this.paymentDateFormated = paymentDateFormated;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public String getAbonadoFormated() {
        return abonadoFormated;
    }

    public void setAbonadoFormated(String abonadoFormated) {
        this.abonadoFormated = abonadoFormated;
    }

    public String getTotalFormated() {
        return totalFormated;
    }

    public void setTotalFormated(String totalFormated) {
        this.totalFormated = totalFormated;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int bilNumber) {
        this.billNumber = this.formatBillNumber(bilNumber);
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

    public HistoricalDefaulterChargeDTO() {
    }


    public HistoricalDefaulterChargeDTO(Long id, Integer type, ZonedDateTime date, String concept, String consecutive, String ammount, String leftToPay, String abonado, String defaultersDay, Long historicalDefaulterId) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.concept = concept;
        this.consecutive = consecutive;
        this.ammount = ammount;
        this.leftToPay = leftToPay;
        this.abonado = abonado;
        this.defaultersDay = defaultersDay;
        this.historicalDefaulterId = historicalDefaulterId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(String consecutive) {
        this.consecutive = consecutive;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getLeftToPay() {
        return leftToPay;
    }

    public void setLeftToPay(String leftToPay) {
        this.leftToPay = leftToPay;
    }

    public String getAbonado() {
        return abonado;
    }

    public void setAbonado(String abonado) {
        this.abonado = abonado;
    }

    public String getDefaultersDay() {
        return defaultersDay;
    }

    public void setDefaultersDay(String defaultersDay) {
        this.defaultersDay = defaultersDay;
    }

    public Long getHistoricalDefaulterId() {
        return historicalDefaulterId;
    }

    public void setHistoricalDefaulterId(Long historicalDefaulterId) {
        this.historicalDefaulterId = historicalDefaulterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO = (HistoricalDefaulterChargeDTO) o;
        if (historicalDefaulterChargeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historicalDefaulterChargeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoricalDefaulterChargeDTO{" +
            "id=" + getId() +
            ", type=" + getType() +
            ", date='" + getDate() + "'" +
            ", concept='" + getConcept() + "'" +
            ", consecutive='" + getConsecutive() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", leftToPay='" + getLeftToPay() + "'" +
            ", abonado='" + getAbonado() + "'" +
            ", defaultersDay='" + getDefaultersDay() + "'" +
            "}";
    }
}
