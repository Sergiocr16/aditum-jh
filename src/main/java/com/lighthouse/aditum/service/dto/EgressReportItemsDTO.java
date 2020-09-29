package com.lighthouse.aditum.service.dto;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EgressReportItemsDTO {
    private String proovedor;
    private int tipo;
    private double total = 0;
    private String totalFormatted;
    private List<EgressDTO> egresosFormatted = new ArrayList<>();
    private List<String> headingCampos = new ArrayList<>();
    private int camposQuantity = 0;

    private double defineTotal(EgressDTO e, String currency){
        if(e.getCurrency().equals(currency)){
            return Double.parseDouble(e.getTotal());
        }else{
            if(e.getAmmountDoubleMoney()!=null){
                return Double.parseDouble(e.getAmmountDoubleMoney());
            }else{
                return Double.parseDouble(e.getTotal());
            }
        }
    }

    public EgressReportItemsDTO(String proovedor, List<EgressDTO> egresos, String[] proveedorsParts,String currency) {
        this.proovedor = proovedor;
        if(proovedor.equals("Devoluciones de dinero")){
            this.tipo = 2;
        }else{
            this.tipo = 1;
        }
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "ES"));
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        egresos.forEach(egreso -> {
            egreso.setTotal(defineTotal(egreso,currency)+"");
            this.total = this.total + defineTotal(egreso,currency);
        });
        this.setTotalFormatted(currencyFormatter.format(this.total).substring(1));
        for (int i = 0; i < egresos.size(); i++) {
            EgressDTO egressDTO = new EgressDTO();
            egressDTO.setId(egresos.get(i).getId());
            egressDTO.setCompanyId(egresos.get(i).getCompanyId());
            for (int j = 0; j < proveedorsParts.length; j++) {
                if (proveedorsParts[j].equals("concept")) {
                    egressDTO.setConcept(egresos.get(i).getConcept());
                    if(i==0){
                        getHeadingCampos().add("Concepto");
                        this.camposQuantity = camposQuantity + 1;
                    }

                }
                if (proveedorsParts[j].equals("folio") && !proovedor.equals("Devoluciones de dinero")) {
                    egressDTO.setFolio(egresos.get(i).getFolio());
                    if(i==0){
                        getHeadingCampos().add("Folio");
                        this.camposQuantity = camposQuantity + 1;
                    }

                }
                if (proveedorsParts[j].equals("date") && !proovedor.equals("Devoluciones de dinero")) {
                    egressDTO.setDate(egresos.get(i).getDate());
                    if(i==0){
                        getHeadingCampos().add("Fecha cobro");
                        this.camposQuantity = camposQuantity + 1;
                    }

                    egressDTO.setDateFormatted(spanish.format(egresos.get(i).getDate()));
                }
                if (proveedorsParts[j].equals("expirationDate") && !proovedor.equals("Devoluciones de dinero")) {
                    egressDTO.setExpirationDate(egresos.get(i).getExpirationDate());
                    if(i==0){
                        getHeadingCampos().add("Fecha vencimiento");
                        this.camposQuantity = camposQuantity + 1;
                    }

                    egressDTO.setExpirationDateFormatted(spanish.format(egresos.get(i).getExpirationDate()));
                }
                if (proveedorsParts[j].equals("paymentDate")) {
                    egressDTO.setPaymentDate(egresos.get(i).getPaymentDate());
                    egressDTO.setPaymentDateSelected(true);
                    if(i==0){
                        getHeadingCampos().add("Pago realizado");
                        this.camposQuantity = camposQuantity + 1;
                    }

                    if (egresos.get(i).getPaymentDate() != null) {
                        egressDTO.setPaymentDateFormatted(spanish.format(egresos.get(i).getPaymentDate()));
                    }else{
                        egressDTO.setPaymentDateFormatted("No pagado");
                    }
                }
                if (proveedorsParts[j].equals("billNumber") && !proovedor.equals("Devoluciones de dinero")) {
                    if (egresos.get(i).getBillNumber() == null) {
                        egressDTO.setBillNumber("Sin registrar");
                    } else {
                        egressDTO.setBillNumber(egresos.get(i).getBillNumber());
                    }
                    if(i==0){
                        getHeadingCampos().add("# Factura");
                        this.camposQuantity = camposQuantity + 1;
                    }

                }
                if (proveedorsParts[j].equals("reference")) {
                    if (egresos.get(i).getReference() == null) {
                        egressDTO.setReference("Sin registrar");
                    } else {
                        egressDTO.setReference(egresos.get(i).getReference());
                    }
                    if(i==0){
                        getHeadingCampos().add("Referencia");
                        this.camposQuantity = camposQuantity + 1;
                    }

                }
                if (proveedorsParts[j].equals("account")) {
                    egressDTO.setAccount(egresos.get(i).getAccount());

                    if(i==0){
                        getHeadingCampos().add("Cuenta");
                        this.camposQuantity = camposQuantity + 1;
                    }
                }
                if (proveedorsParts[j].equals("total")) {
                    egressDTO.setTotal(egresos.get(i).getTotal());
                    if(i==0){
                        getHeadingCampos().add("Monto");
                        this.camposQuantity = camposQuantity + 1;
                    }
                    egressDTO.setTotalFormatted(currencyFormatter.format(Double.parseDouble(egresos.get(i).getTotal())).substring(1));
                }

            }
            egresosFormatted.add(egressDTO);
        }
    }

    public String getProovedor() {
        return proovedor;
    }

    public void setProovedor(String proovedor) {
        this.proovedor = proovedor;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<EgressDTO> getEgresosFormatted() {
        return egresosFormatted;
    }

    public void setEgresosFormatted(List<EgressDTO> egresosFormatted) {
        this.egresosFormatted = egresosFormatted;
    }


    public List<String> getHeadingCampos() {
        return headingCampos;
    }

    public void setHeadingCampos(List<String> headingCampos) {
        this.headingCampos = headingCampos;
    }

    public int getCamposQuantity() {
        return camposQuantity;
    }

    public void setCamposQuantity(int camposQuantity) {
        this.camposQuantity = camposQuantity;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
