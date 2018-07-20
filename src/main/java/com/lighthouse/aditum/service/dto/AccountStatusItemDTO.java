package com.lighthouse.aditum.service.dto;

import java.time.ZonedDateTime;

public class AccountStatusItemDTO {
    private ZonedDateTime date;
    private String concept;
    private int charge;
    private int recharge;
    private int total;
    private int saldo;
    private int abono;

    public AccountStatusItemDTO(ZonedDateTime date,String concept, int charge,int recharge){
        this.date = date;
        this.concept = concept;
        this.charge = charge;
        this.recharge = recharge;
        this.total = charge + recharge;

    }

    public AccountStatusItemDTO(ZonedDateTime date,String concept, int abono){
        this.date = date;
        this.concept = concept;
        this.abono = abono;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getRecharge() {
        return recharge;
    }

    public void setRecharge(int recharge) {
        this.recharge = recharge;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getAbono() {
        return abono;
    }

    public void setAbono(int abono) {
        this.abono = abono;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
