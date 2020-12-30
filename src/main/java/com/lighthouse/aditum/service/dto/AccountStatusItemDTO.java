package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.util.RandomUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

public class AccountStatusItemDTO {
    private Locale locale = new Locale("es", "CR");
    private ZonedDateTime date;
    private String concept;
    private double charge;
    private double recharge;
    private double total;
    private double saldo;
    private double abono;

    private String chargeFormatted;
    private String rechargeFormatted;
    private String totalFormatted;
    private String saldoFormatted;
    private String abonoFormatted;

    private String dateFormatted;
    private List<PaymentChargeDTO> charges;
    private boolean showDetail;
    private boolean hasDetail;


    public AccountStatusItemDTO(String currency,ZonedDateTime date,String concept, double charge,double recharge){
        this.date = date;
        this.concept = concept;
        this.charge = charge;
        this.recharge = recharge;
        this.total = charge + recharge;
        this.chargeFormatted = RandomUtil.formatMoney(currency,charge);
        this.rechargeFormatted = RandomUtil.formatMoney(currency,recharge);
        this.totalFormatted = RandomUtil.formatMoney(currency,this.total);
        this.showDetail = false;

    }

    public AccountStatusItemDTO(String currency,ZonedDateTime date,int transaction, double abono, List<PaymentChargeDTO> charges){
        this.date = date;
        if(transaction==1){
            this.concept = "Abono a cuotas";
        }else if(transaction==2){
            this.concept = "Abono saldos a favor";
        }
        this.abono = abono;
        this.charges = charges;
        if(charges.size()>0){
            this.hasDetail=true;
        }
        for (PaymentChargeDTO c : charges){
            c.setAmmountFormatted(currency,c.getAmmount());
        }
        this.abonoFormatted = RandomUtil.formatMoney(currency,this.abono);
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

    public double getRecharge() {
        return recharge;
    }

    public void setRecharge(double recharge) {
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

    public void setSaldo(String currency,double saldo) {
        this.saldo = saldo;
    this.saldoFormatted = RandomUtil.formatMoney(currency,this.saldo);
    }

    public double getAbono() {
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

    public List<PaymentChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<PaymentChargeDTO> charges) {
        this.charges = charges;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }

    public String getChargeFormatted() {
        return chargeFormatted;
    }

    public void setChargeFormatted(String chargeFormatted) {
        this.chargeFormatted = chargeFormatted;
    }

    public String getRechargeFormatted() {
        return this.rechargeFormatted;
    }

    public void setRechargeFormatted(String rechargeFormatted) {
        this.rechargeFormatted = rechargeFormatted;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public String getSaldoFormatted() {
        return saldoFormatted;
    }

    public void setSaldoFormatted(String saldoFormatted) {
        this.saldoFormatted = saldoFormatted;
    }

    public String getAbonoFormatted() {
        return abonoFormatted;
    }

    public void setAbonoFormatted(String abonoFormatted) {
        this.abonoFormatted = abonoFormatted;
    }
}
