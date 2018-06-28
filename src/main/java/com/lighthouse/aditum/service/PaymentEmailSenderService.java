package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.User;
import com.lighthouse.aditum.service.dto.ChargeDTO;
import com.lighthouse.aditum.service.dto.PaymentDTO;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.mapper.HouseMapper;
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
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@Transactional
public class PaymentEmailSenderService {

    private final Logger log = LoggerFactory.getLogger(PaymentEmailSenderService.class);
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

    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final HouseService houseService;
    private final HouseMapper houseMapper;
    private final ChargeService chargeService;
    private final MailService mailService;
    private final SpringTemplateEngine templateEngine;


    public PaymentEmailSenderService(SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties, MailService mailService, CompanyService companyService, CompanyMapper companyMapper, HouseService houseService, HouseMapper houseMapper, ChargeService chargeService){
        this.chargeService = chargeService;
        this.companyMapper = companyMapper;
        this.houseService = houseService;
        this.houseMapper = houseMapper;
        this.companyService = companyService;
        this.mailService = mailService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
    }


    private PaymentDTO formatPayment(PaymentDTO payment,boolean isCancellingFromPayment){
        if(payment.getComments()==null || payment.getComments()==""){
            payment.setComments("No hay comentarios");
        }
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        payment.getCharges().forEach(chargeDTO -> {
            chargeDTO.setAmmount(currencyFormatter.format(Double.parseDouble(chargeDTO.getAmmount())).substring(1));
            if(payment.getTransaction().equals("1")) {
                chargeDTO.setPaymentAmmount(currencyFormatter.format(Double.parseDouble(chargeDTO.getPaymentAmmount())).substring(1));
            }else{
                chargeDTO.setPaymentAmmount(currencyFormatter.format(Double.parseDouble(chargeDTO.getAmmount())).substring(1));
            }
        });
        if(payment.getTransaction().equals("1")){
            payment.setConcept("Abono a cuotas");
        }else if(payment.getTransaction().equals("2")){
            if(payment.getCharges().size()==0){
                ChargeDTO adelanto = new ChargeDTO();
                adelanto.setPaymentAmmount(currencyFormatter.format(Double.parseDouble(payment.getAmmount())).substring(1));
                adelanto.setConcept(payment.getConcept());
                adelanto.setType(4);
                payment.getCharges().add(adelanto);
            }
            payment.setConcept("Adelanto de condómino");
            if(isCancellingFromPayment==true){
                payment.setConcept("Abono a cuotas");
            }
        }

        payment.setAmmount(currencyFormatter.format(Double.parseDouble(payment.getAmmount())).substring(1));
        if(payment.getAmmountLeft()!=null) {
            payment.setAmmountLeft(currencyFormatter.format(Double.parseDouble(payment.getAmmountLeft())).substring(1));
        }
        payment.setAccount(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(payment.getDate()));

        return payment;
    }


    @Async
    public void sendPaymentEmail(PaymentDTO payment,boolean isCancellingFromPayment) {
        User user = new User();
        user.setEmail("sergiojcr16@gmail.com");
        Context context = new Context();
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(Long.valueOf(payment.getCompanyId())));
        House house = houseMapper.houseDTOToHouse(houseService.findOne(Long.valueOf(payment.getHouseId())));
        context.setVariable(COMPANY,company);
        context.setVariable(USER, user);
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
            contextTemplate.setVariable(CONTACTO,"Jhon Doe");
            contextTemplate.setVariable(CHARGES_SIZE,payment.getCharges().size());
            String contentTemplate = templateEngine.process("paymentTemplate", contextTemplate);
            OutputStream outputStream = new FileOutputStream(fileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(contentTemplate);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileName);
            this.mailService.sendEmailWithAtachment
                (user.getEmail(), subject, content, true, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String defineSubjectPaymentEmail(PaymentDTO payment,Company company,House house,boolean isCancellingFromPayment){
        String subject = "Comprobante de Pago - "+payment.getReceiptNumber()+" - "+company.getName();
        if(payment.getTransaction().equals("1") || payment.getTransaction().equals("2") && payment.getCharges().size()>0){
            subject = "Comprobante de Pago - Filial # "+house.getHousenumber()+" - "+company.getName()+ " (Abono a cuotas)";
        }else if(payment.getTransaction().equals("2")){
            subject = "Comprobante de Pago - Filial # "+house.getHousenumber()+" - "+company.getName()+ " (Adelanto de condómino)";
        }
        return subject;
    }

    private String defineFileNamePaymentEmail(PaymentDTO payment){
        String fileName = "ComprobanteDePago "+payment.getReceiptNumber()+".pdf";
        return fileName;
    }

}

