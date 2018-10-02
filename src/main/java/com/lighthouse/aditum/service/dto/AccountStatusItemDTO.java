package com.lighthouse.aditum.service.dto;

import java.time.ZonedDateTime;
import java.util.List;

public class AccountStatusItemDTO {
    private ZonedDateTime date;
    private String concept;
    private double charge;
    private int recharge;
    private double total;
    private double saldo;
    private int abono;
    private List<ChargeDTO> charges;
    private boolean showDetail;

    public AccountStatusItemDTO(ZonedDateTime date,String concept, double charge,int recharge){
        this.date = date;
        this.concept = concept;
        this.charge = charge;
        this.recharge = recharge;
        this.total = charge + recharge;
        this.showDetail = false;

    }

    public AccountStatusItemDTO(ZonedDateTime date,int transaction, int abono, List<ChargeDTO> charges){
        this.date = date;
        if(transaction==1){
            this.concept = "Abono a cuotas";
        }else if(transaction==2){
            this.concept = "Adelanto de condómino";
        }

        this.abono = abono;
        this.charges = charges;
        this.showDetail = false;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public double getCharge() {
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

    public double getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
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

    public List<ChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<ChargeDTO> charges) {
        this.charges = charges;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }
}
