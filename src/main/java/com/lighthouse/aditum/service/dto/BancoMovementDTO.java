package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.util.RandomUtil;

import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.Locale;

public class BancoMovementDTO {

    private String folio;

    private String concept;

    private String bankReference;

    private ZonedDateTime date;

    private int type;

    private double ingress;

    private double egress;

    private String dateFormatted;

    private String ingressFormatted;

    private String egressFormatted;

    private double balance;

    private String balanceFormatted;

    private String bancoSalienteOentrante;

    public BancoMovementDTO(String folio, String concept, ZonedDateTime date, int type, double ingress, double egress) {
        this.folio = folio;
        this.concept = concept;
        this.date = date;
        this.type = type;
        this.ingress = ingress;
        this.egress = egress;

    }

    public BancoMovementDTO(String folio, String concept, ZonedDateTime date, int type, double ingress, double egress, String bancoSalienteOentrante) {
        this.folio = folio;
        this.concept = concept;
        this.date = date;
        this.type = type;
        this.ingress = ingress;
        this.egress = egress;
        this.setBancoSalienteOentrante(bancoSalienteOentrante);
    }

    public String getBankReference() {
        return bankReference;
    }

    public void setBankReference(String bankReference) {
        this.bankReference = bankReference;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getIngress() {
        return ingress;
    }

    public void setIngress(double ingress) {
        this.ingress = ingress;
    }

    public double getEgress() {
        return egress;
    }

    public void setEgress(double egress) {
        this.egress = egress;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(String currency, double balance) {
        this.balance = balance;
        this.setBalanceFormatted(RandomUtil.formatMoney(currency,balance));
    }

    public String getBancoSalienteOentrante() {
        return bancoSalienteOentrante;
    }

    public void setBancoSalienteOentrante(String bancoSalienteOentrante) {
        this.bancoSalienteOentrante = bancoSalienteOentrante;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }

    public String getIngressFormatted() {
        return ingressFormatted;
    }

    public void setIngressFormatted(String ingressFormatted) {
        this.ingressFormatted = ingressFormatted;
    }

    public String getEgressFormatted() {
        return egressFormatted;
    }

    public void setEgressFormatted(String egressFormatted) {
        this.egressFormatted = egressFormatted;
    }

    public String getBalanceFormatted() {
        return balanceFormatted;
    }

    public void setBalanceFormatted(String balanceFormatted) {
        this.balanceFormatted = balanceFormatted;
    }

}
