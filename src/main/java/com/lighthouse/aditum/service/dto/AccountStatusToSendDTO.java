package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.util.RandomUtil;

import java.util.List;
import java.util.Locale;

public class AccountStatusToSendDTO {
    private Locale locale = new Locale("es", "CR");

    List<ChargeDTO> charges;

    List<ChargeDTO> adelantos;

    double totalLeftToPay;

    HouseDTO house;

    boolean hasNegativeBalance = false;

    boolean hasPositiveBalance = false;

    public HouseDTO getHouse() {
        return house;
    }

    public void setHouse(HouseDTO house) {
        this.house = house;
    }

    public List<ChargeDTO> getAdelantos() {
        return adelantos;
    }

    public void setAdelantos(List<ChargeDTO> adelantos) {
        this.adelantos = adelantos;
    }

    public boolean isHasNegativeBalance() {
        return hasNegativeBalance;
    }

    public void setHasNegativeBalance(boolean hasNegativeBalance) {
        this.hasNegativeBalance = hasNegativeBalance;
    }

    public boolean isHasPositiveBalance() {
        return hasPositiveBalance;
    }

    public void setHasPositiveBalance(boolean hasPositiveBalance) {
        this.hasPositiveBalance = hasPositiveBalance;
    }

    String totalLeftToPayFormatted;

    public List<ChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<ChargeDTO> charges) {
        this.charges = charges;
    }

    public double getTotalLeftToPay() {
        return totalLeftToPay;
    }

    public void setTotalLeftToPay(String currency, double totalLeftToPay) {
        this.totalLeftToPay = totalLeftToPay;
        this.setTotalLeftToPayFormatted(RandomUtil.formatMoney(currency,this.totalLeftToPay));
    }

    public String getTotalLeftToPayFormatted() {
        return totalLeftToPayFormatted;
    }

    public void setTotalLeftToPayFormatted(String totalLeftToPayFormatted) {
        this.totalLeftToPayFormatted = totalLeftToPayFormatted;
    }
}
