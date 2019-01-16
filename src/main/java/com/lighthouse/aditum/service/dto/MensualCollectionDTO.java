package com.lighthouse.aditum.service.dto;

import java.io.Serializable;

/**
 * Created by Sergio on 13/07/2018.
 */
public class MensualCollectionDTO implements Serializable {


    private double mensualBalance;

    private String mensualBalanceToShow;

    private String month;

    private String style;


    public double getMensualBalance() {
        return mensualBalance;
    }

    public void setMensualBalance(double mensualBalance) {
        this.mensualBalance = mensualBalance;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getMensualBalanceToShow() {
        return mensualBalanceToShow;
    }

    public void setMensualBalanceToShow(String mensualBalanceToShow) {
        this.mensualBalanceToShow = mensualBalanceToShow;
    }
}
