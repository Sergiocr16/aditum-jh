package com.lighthouse.aditum.service.dto;

import java.io.Serializable;

/**
 * Created by Sergio on 13/07/2018.
 */
public class MensualCollectionDTO implements Serializable {


    private int mensualBalance;

    private String month;

    private String bgColor;


    public int getMensualBalance() {
        return mensualBalance;
    }

    public void setMensualBalance(int mensualBalance) {
        this.mensualBalance = mensualBalance;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
}
