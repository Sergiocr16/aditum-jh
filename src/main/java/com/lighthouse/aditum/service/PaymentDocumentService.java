package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import com.lighthouse.aditum.service.util.RandomUtil;
import com.lowagie.text.DocumentException;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;
import static com.lighthouse.aditum.service.util.RandomUtil.formatMoneyString;

@Service
@Transactional
public class PaymentDocumentService {

    private final Logger log = LoggerFactory.getLogger(PaymentDocumentService.class);
    private static final String USER = "user";
    private static final String COMPANY = "company";
    private static final String PAYMENT = "payment";
    private static final String CONTACTO = "contacto";
    private static final String HOUSE = "house";
    private static final String BASE_URL = "baseUrl";
    private static final String IS_CANCELLING_FROM_PAYMENT = "isCancellingFromPayment";
    private static final String PAYMENT_TOTAL = "paymentTotal";
    private static final String PAYMENT_DATE = "paymentDate";
    private static final String CHARGES_SIZE = "chargesSize";
    private static final String CURRENT_DATE = "currentDate";

//    INCOME REPORT
    private static final String INCOME_REPORT = "incomeReport";
    private static final String RANGO_FECHAS = "rangoFechas";


//    REMINDER SUBCHARGE
    private static final String CHARGES = "charges";
    private static final String SUBCHARGE_TOTAL = "subchargeTotal";
    private static final String ADMINISTRATION_CONFIGURATION = "administrationConfiguration";

    private static final String CHARGE = "charge";




    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final HouseService houseService;
    private final ResidentService residentService;
    private final HouseMapper houseMapper;
    private final ChargeService chargeService;
    private final MailService mailService;
    private final SpringTemplateEngine templateEngine;




    public PaymentDocumentService(ResidentService residentService,SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties, MailService mailService, CompanyService companyService, CompanyMapper companyMapper, HouseService houseService, HouseMapper houseMapper, ChargeService chargeService){
        this.chargeService = chargeService;
        this.companyMapper = companyMapper;
        this.houseService = houseService;
        this.houseMapper = houseMapper;
        this.companyService = companyService;
        this.mailService = mailService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.residentService = residentService;
    }



    private PaymentDTO formatPayment(PaymentDTO payment,boolean isCancellingFromPayment){
        if(payment.getComments()==null || payment.getComments()==""){
            payment.setComments("No hay comentarios");
        }
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        payment.getCharges().forEach(chargeDTO -> {
            if(payment.getTransaction().equals("1")) {
                chargeDTO.setPaymentAmmount(formatMoney(Double.parseDouble(chargeDTO.getAmmount())+Double.parseDouble(chargeDTO.getSubcharge())));
                chargeDTO.setAmmount(formatMoneyString(chargeDTO.getAmmount()));
                chargeDTO.setSubcharge(formatMoneyString(chargeDTO.getSubcharge()));
            }else{
                chargeDTO.setPaymentAmmount(formatMoney(Double.parseDouble(chargeDTO.getAmmount())+Double.parseDouble(chargeDTO.getSubcharge())));
                chargeDTO.setAmmount(formatMoneyString(chargeDTO.getAmmount()));
                chargeDTO.setSubcharge(formatMoneyString(chargeDTO.getSubcharge()));
            }
        });
        if(payment.getTransaction().equals("3")){

        }else  if(payment.getTransaction().equals("1")){
            payment.setConcept("Abono a cuotas");
        }else if(payment.getTransaction().equals("2")){
            if(payment.getCharges().size()==0){
                ChargeDTO adelanto = new ChargeDTO();
                adelanto.setPaymentAmmount(formatMoneyString(payment.getAmmount()));
                adelanto.setConcept(payment.getConcept());
                adelanto.setType(4);
                payment.getCharges().add(adelanto);
            }
            payment.setConcept("Adelanto de condómino");
            if(isCancellingFromPayment==true){
                payment.setConcept("Abono a cuotas");
            }
        }
        payment.setAmmount(formatMoneyString(payment.getAmmount()));
        if(payment.getAmmountLeft()!=null) {
            payment.setAmmountLeft(formatMoneyString(payment.getAmmountLeft()));
        }
        payment.setAccount(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(payment.getDate()));
        return payment;
    }

    private IncomeReportDTO formatIncomeReport(IncomeReportDTO income){
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(locale);
        income.getPayments().forEach(paymentDTO -> {
                paymentDTO.setAmmount(formatMoneyString(paymentDTO.getAmmount()));
            paymentDTO.setStringDate( pattern.ofPattern("dd MMMM yyyy").format(paymentDTO.getDate()));
        });
        return income;

    }
    public File obtainFileToPrint(PaymentDTO payment,boolean isCancellingFromPayment) {
        String contactoPrincipal = "No definido";
        ResidentDTO resident = null;
        for (int i = 0; i < payment.getEmailTo().size(); i++) {
            if(payment.getEmailTo().get(i).getPrincipalContact()==1){
                resident = payment.getEmailTo().get(i);
                contactoPrincipal = resident.getName()+" "+ resident.getLastname()+" "+resident.getSecondlastname();
            }
        }
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(Long.valueOf(payment.getCompanyId())));
        House house = null;
        if(payment.getHouseId()!=null) {
            house = houseMapper.houseDTOToHouse(houseService.findOne(Long.valueOf(payment.getHouseId())));
        }
        String fileName = this.defineFileNamePaymentEmail(payment);
//        ENVIO DE COMPROBANTE DE PAGO
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(HOUSE,house);
            payment = this.formatPayment(payment,isCancellingFromPayment);
            contextTemplate.setVariable(PAYMENT,payment);
            contextTemplate.setVariable(IS_CANCELLING_FROM_PAYMENT,isCancellingFromPayment);
            String paymentDate = payment.getAccount();
            String paymentTotal = payment.getAmmount();
            if(isCancellingFromPayment == true) {
                paymentDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(payment.getCharges().get(0).getPaymentDate());
                paymentTotal = payment.getCharges().stream().mapToDouble(o -> o.getTotal()).sum()+"";
                paymentTotal=formatMoneyString(paymentTotal);
            }
            contextTemplate.setVariable(PAYMENT_DATE,paymentDate);
            contextTemplate.setVariable(PAYMENT_TOTAL,paymentTotal);
            contextTemplate.setVariable(CONTACTO,contactoPrincipal);
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);
            contextTemplate.setVariable(CHARGES_SIZE,payment.getCharges().size());
            String contentTemplate = templateEngine.process("paymentTemplate", contextTemplate);
            OutputStream outputStream = new FileOutputStream(fileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(contentTemplate);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileName);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Async
    public void sendPaymentEmail(PaymentDTO payment,boolean isCancellingFromPayment) {
        String contactoPrincipal = "";
        ResidentDTO resident = null;
        for (int i = 0; i < payment.getEmailTo().size(); i++) {
            if(payment.getEmailTo().get(i).getPrincipalContact()==1){
                resident = payment.getEmailTo().get(i);
                contactoPrincipal = resident.getName()+" "+ resident.getLastname()+" "+resident.getLastname();
            }
        }
        Context context = new Context();
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(Long.valueOf(payment.getCompanyId())));
        House house = houseMapper.houseDTOToHouse(houseService.findOne(Long.valueOf(payment.getHouseId())));
        context.setVariable(COMPANY,company);
        context.setVariable(USER, resident);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("paymentMade", context);
        String subject = this.defineSubjectPaymentEmail(payment,company,house,isCancellingFromPayment);
        String fileName = this.defineFileNamePaymentEmail(payment);
//        ENVIO DE COMPROBANTE DE PAGO
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(HOUSE,house);
            payment = this.formatPayment(payment,isCancellingFromPayment);
            contextTemplate.setVariable(PAYMENT,payment);
            contextTemplate.setVariable(IS_CANCELLING_FROM_PAYMENT,isCancellingFromPayment);
            String paymentDate = payment.getAccount();
            String paymentTotal = payment.getAmmount();
            if(isCancellingFromPayment == true) {
                paymentDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(payment.getCharges().get(0).getPaymentDate());
                paymentTotal = payment.getCharges().get(0).getAmmount();
            }
            contextTemplate.setVariable(PAYMENT_DATE,paymentDate);
            contextTemplate.setVariable(PAYMENT_TOTAL,paymentTotal);
            contextTemplate.setVariable(CONTACTO,contactoPrincipal);
            contextTemplate.setVariable(CHARGES_SIZE,payment.getCharges().size());
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);
            String contentTemplate = templateEngine.process("paymentTemplate", contextTemplate);
            OutputStream outputStream = new FileOutputStream(fileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(contentTemplate);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileName);
            int emailsToSend = payment.getEmailTo().size();
            for (int i = 0; i < payment.getEmailTo().size(); i++) {
                this.mailService.sendEmailWithAtachment
                    (payment.getEmailTo().get(i).getEmail(), subject, content, true, file,emailsToSend-1,i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String defineSubjectPaymentEmail(PaymentDTO payment,Company company,House house,boolean isCancellingFromPayment){
        String subject = "Comprobante de Pago "+payment.getReceiptNumber()+" - "+company.getName();
        if(payment.getTransaction().equals("1") || payment.getTransaction().equals("2") && payment.getCharges().size()>0){
            subject = "Comprobante de Pago "+payment.getReceiptNumber()+" Filial # "+house.getHousenumber()+" - "+company.getName()+ " (Abono a cuotas)";
        }else if(payment.getTransaction().equals("2")){
            subject = "Comprobante de Pago "+payment.getReceiptNumber()+" Filial # "+house.getHousenumber()+" - "+company.getName()+ " (Adelanto de condómino)";
        }
        return subject;
    }

    private String defineFileNamePaymentEmail(PaymentDTO payment){
        String fileName = "ComprobanteDePago "+payment.getReceiptNumber()+".pdf";
        return fileName;
    }

    public File obtainIncomeReportToPrint(IncomeReportDTO incomeReportDTO,Long companyId,ZonedDateTime fechaInicio,ZonedDateTime fechaFinal) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(companyId));
        String fileName = "Reporte de ingresos.pdf";
//        ENVIO DE COMPROBANTE DE PAGO
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY,company);
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);
            incomeReportDTO = this.formatIncomeReport(incomeReportDTO);
            contextTemplate.setVariable(INCOME_REPORT,incomeReportDTO);
            contextTemplate.setVariable(RANGO_FECHAS,this.formatRangoFechas(fechaInicio,fechaFinal));
            String contentTemplate = templateEngine.process("incomeReportTemplate", contextTemplate);
            OutputStream outputStream = new FileOutputStream(fileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(contentTemplate);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileName);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String formatRangoFechas(ZonedDateTime fechaInicial,ZonedDateTime fechaFinal){
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es","ES"));
        return "Del "+spanish.format(fechaInicial) + " al "+ spanish.format(fechaFinal);
    }



    @Async
    public void sendReminderEmail(AdministrationConfigurationDTO administrationConfigurationDTO,HouseDTO house, List<ChargeDTO> chargesDTOS,String templateName){
        ResidentDTO residentDTO = this.residentService.findPrincipalContactByHouse(house.getId());
        if(residentDTO!=null){
            Context contextTemplate = new Context();
            contextTemplate.setVariable(CONTACTO,residentDTO.getName()+" "+residentDTO.getLastname()+" "+residentDTO.getSecondlastname());
            contextTemplate.setVariable(HOUSE,house);
            contextTemplate.setVariable(ADMINISTRATION_CONFIGURATION,administrationConfigurationDTO);
            Locale locale = new Locale("es", "CR");
            DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale);
            double subchargeTotal = 0;
            for (int i = 0; i < chargesDTOS.size(); i++) {
                ChargeDTO chargeDTO = chargesDTOS.get(i);
                chargeDTO.setFormatedDate(spanish.format(chargeDTO.getDate()));
                if(templateName.equals("subchargeReminderEmail")) {
                    subchargeTotal = subchargeTotal + Double.parseDouble(chargeDTO.getSubcharge());
                }
                chargeDTO.setAmmount("₡"+formatMoney(Double.parseDouble(chargeDTO.getAmmount())));
                chargeDTO.setSubcharge("₡"+formatMoney(Double.parseDouble(chargeDTO.getSubcharge())));
                chargeDTO.setPaymentAmmount("₡"+formatMoney(chargeDTO.getTotal()));
            }
            CompanyDTO company = this.companyService.findOne(house.getCompanyId());
            contextTemplate.setVariable(COMPANY,company);
            if(templateName.equals("subchargeReminderEmail")) {
                contextTemplate.setVariable(SUBCHARGE_TOTAL,formatMoney(subchargeTotal).substring(1));
            }
            contextTemplate.setVariable(CHARGES,chargesDTOS);
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            String content = templateEngine.process(templateName, contextTemplate);
            String subject = "Recordatorio de pago, Filial # "+house.getHousenumber()+" ,"+company.getName();
            this.mailService.sendEmail(residentDTO.getEmail(), subject, content,false,true);
        }
    }



    @Async
    public void sendChargeEmail(AdministrationConfigurationDTO administrationConfigurationDTO,HouseDTO house, ChargeDTO chargesDTO){
        ResidentDTO residentDTO = this.residentService.findPrincipalContactByHouse(house.getId());
        if(residentDTO!=null){
            Context contextTemplate = new Context();
            contextTemplate.setVariable(CONTACTO,residentDTO.getName()+" "+residentDTO.getLastname()+" "+residentDTO.getSecondlastname());
            contextTemplate.setVariable(HOUSE,house);
            contextTemplate.setVariable(ADMINISTRATION_CONFIGURATION,administrationConfigurationDTO);
            Locale locale = new Locale("es", "CR");
            DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale);
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            ChargeDTO chargeDTO = chargesDTO;
            chargeDTO.setFormatedDate(spanish.format(chargeDTO.getDate()));
            double total = Double.parseDouble(chargeDTO.getAmmount())+Double.parseDouble(chargeDTO.getSubcharge());
            chargeDTO.setAmmount("₡" + formatMoney(Double.parseDouble(chargeDTO.getAmmount())));
            chargeDTO.setSubcharge("₡" + formatMoney(Double.parseDouble(chargeDTO.getSubcharge()!=null?chargeDTO.getSubcharge():"0")));
            chargeDTO.setPaymentAmmount("₡" + formatMoney(chargeDTO.getTotal()));
            chargeDTO.setTotal(total);
            chargeDTO.setTotalFormatted("₡" +chargeDTO.getTotalFormatted());
            CompanyDTO company = this.companyService.findOne(house.getCompanyId());
            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(CHARGE,chargeDTO);
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            String content = templateEngine.process("newChargeEmail", contextTemplate);
            String subject = "Nueva cuota '"+ chargeDTO.getConcept()+"', Filial # "+house.getHousenumber()+" , "+company.getName();
            this.mailService.sendEmail(residentDTO.getEmail(), subject, content,false,true);
        }
    }
}

