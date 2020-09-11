package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import com.lighthouse.aditum.service.util.RandomUtil;
import com.lowagie.text.DocumentException;
import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.io.FileDeleteStrategy;
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
    private static final String RESIDENTE = "residente";
    private static final String EMAIL = "email";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String HOUSE = "house";
    private static final String BASE_URL = "baseUrl";
    private static final String IS_CANCELLING_FROM_PAYMENT = "isCancellingFromPayment";
    private static final String PAYMENT_TOTAL = "paymentTotal";
    private static final String PAYMENT_DATE = "paymentDate";
    private static final String CHARGES_SIZE = "chargesSize";
    private static final String CURRENT_DATE = "currentDate";
    private static final String CUENTAS_BANCARIAS = "cuentasBancarias";

    private static final String FECHA_VENCIMIENTO = "fechaVencimiento";
    private static final String CURRENCY = "currency";
    private static final String WATER_CONSUMPTION = "waterConsumption";

    private static final String ADMIN_EMAIL = "adminEmail";
    private static final String ADMIN_NUMBER = "adminNumber";
    private static final String LOGO = "logo";
    private static final String LOGO_ADMIN = "logoAdmin";


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
    private final ChargeService chargeService;
    private final ResidentService residentService;
    private final HouseMapper houseMapper;
    private final WaterConsumptionService waterConsumptionService;
    private final MailService mailService;
    private final SpringTemplateEngine templateEngine;
    private final CompanyConfigurationService companyConfigurationService;


    public PaymentDocumentService(ChargeService chargeService, CompanyConfigurationService companyConfigurationService, ResidentService residentService, SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties, MailService mailService, CompanyService companyService, CompanyMapper companyMapper, HouseService houseService, HouseMapper houseMapper, WaterConsumptionService waterConsumptionService) {
        this.waterConsumptionService = waterConsumptionService;
        this.companyMapper = companyMapper;
        this.houseService = houseService;
        this.houseMapper = houseMapper;
        this.companyService = companyService;
        this.mailService = mailService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.residentService = residentService;
        this.companyConfigurationService = companyConfigurationService;
        this.chargeService = chargeService;
    }


    private PaymentDTO formatPayment(PaymentDTO payment, boolean isCancellingFromPayment) {
        if (payment.getComments() == null || payment.getComments() == "") {
            payment.setComments("No hay comentarios");
        }
        String currency = companyConfigurationService.getByCompanyId(null, Long.parseLong(payment.getCompanyId() + "")).getContent().get(0).getCurrency();
        payment.getCharges().forEach(chargeDTO -> {
            if (payment.getTransaction().equals("1")) {
                chargeDTO.setPaymentAmmount(formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
                chargeDTO.setAbonado(currency, Double.parseDouble(chargeDTO.getAmmount()));
                chargeDTO.setAmmount(formatMoneyString(currency, chargeDTO.getAmmount()));
//                chargeDTO.setSubcharge(formatMoneyString(currency, chargeDTO.getSubcharge()));
            } else {
                chargeDTO.setPaymentAmmount(formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
                chargeDTO.setAbonado(currency, Double.parseDouble(chargeDTO.getAmmount()));
                chargeDTO.setAmmount(formatMoneyString(currency, chargeDTO.getAmmount()));
//                chargeDTO.setSubcharge(formatMoneyString(currency, chargeDTO.getSubcharge()));
            }
        });
        if (payment.getTransaction().equals("3")) {
        } else if (payment.getTransaction().equals("1")) {
            payment.setConcept("Abono a cuotas");
        } else if (payment.getTransaction().equals("2")) {
            if (payment.getCharges().size() == 0) {
                ChargeDTO adelanto = new ChargeDTO();
                adelanto.setPaymentAmmount(formatMoneyString(currency, payment.getAmmount()));
                adelanto.setConcept(payment.getConcept());
                adelanto.setType(4);
                payment.getCharges().add(adelanto);
            }
            payment.setConcept("Adelanto de condómino");
            if (isCancellingFromPayment == true) {
                payment.setConcept("Abono a cuotas");
            }
        }
        payment.setAmmount(formatMoneyString(currency, payment.getAmmount()));
        if (payment.getAmmountLeft() != null) {
            payment.setAmmountLeft(formatMoneyString(currency, payment.getAmmountLeft()));
        }
        payment.setAccount(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(payment.getDate()));
        return payment;
    }

    private IncomeReportDTO formatIncomeReport(IncomeReportDTO income, String currency) {
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(locale);
        income.getPayments().forEach(paymentDTO -> {
            paymentDTO.setAmmount(formatMoneyString(currency, paymentDTO.getAmmount()));
            paymentDTO.setStringDate(pattern.ofPattern("dd MMMM yyyy").format(paymentDTO.getDate()));
        });
        return income;

    }

    public File obtainFileToPrint(PaymentDTO payment, boolean isCancellingFromPayment) {
        String contactoPrincipal = "No definido";
        String numtelefono = "No definido";
        String email = "No definido";
        ResidentDTO resident = null;
        for (int i = 0; i < payment.getEmailTo().size(); i++) {
            if (payment.getEmailTo().get(i).getPrincipalContact() == 1) {
                resident = payment.getEmailTo().get(i);
                contactoPrincipal = resident.getName() + " " + resident.getLastname();
                numtelefono = resident.getPhonenumber() != null ? resident.getPhonenumber() : "No definido";
                email = resident.getEmail();
            }
        }
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(Long.valueOf(payment.getCompanyId())));
        House house = null;
        if (payment.getHouseId() != null) {
            house = houseMapper.houseDTOToHouse(houseService.findOne(Long.valueOf(payment.getHouseId())));
        }
        String fileName = this.defineFileNamePaymentEmail(payment);
//        ENVIO DE COMPROBANTE DE PAGO
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY, company);
            contextTemplate.setVariable(HOUSE, house);
            payment = this.formatPayment(payment, isCancellingFromPayment);
            contextTemplate.setVariable(PAYMENT, payment);
            contextTemplate.setVariable(IS_CANCELLING_FROM_PAYMENT, isCancellingFromPayment);
            String paymentDate = payment.getAccount();
            String paymentTotal = payment.getAmmount();
            String currency = companyConfigurationService.getByCompanyId(null, Long.parseLong(payment.getCompanyId() + "")).getContent().get(0).getCurrency();
            contextTemplate.setVariable(CURRENCY, currency);
            if (isCancellingFromPayment == true) {
                paymentDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(payment.getCharges().get(0).getPaymentDate());
                paymentTotal = payment.getCharges().stream().mapToDouble(o -> o.getAbonado()).sum() + "";
                paymentTotal = formatMoneyString(currency, paymentTotal);
            }
            contextTemplate.setVariable(PAYMENT_DATE, paymentDate);
            contextTemplate.setVariable(PAYMENT_TOTAL, paymentTotal);
            contextTemplate.setVariable(CONTACTO, contactoPrincipal);
            contextTemplate.setVariable(EMAIL, email);
            contextTemplate.setVariable(PHONE_NUMBER, numtelefono);
            contextTemplate.setVariable(USER, resident);
            contextTemplate.setVariable(LOGO, company.getLogoUrl());
            contextTemplate.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE, timeNowFormatted);
            contextTemplate.setVariable(CHARGES_SIZE, payment.getCharges().size());
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
    public void sendPaymentEmail(PaymentDTO payment, boolean isCancellingFromPayment) {
        String contactoPrincipal = "";
        String numtelefono = "No definido";
        ResidentDTO resident = null;
        for (int i = 0; i < payment.getEmailTo().size(); i++) {
            if (payment.getEmailTo().get(i).getPrincipalContact() == 1) {
                resident = payment.getEmailTo().get(i);
                numtelefono = resident.getPhonenumber() != null ? resident.getPhonenumber() : "No definido";
                contactoPrincipal = resident.getName() + " " + resident.getLastname();
            }
        }
        Context context = new Context();
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(Long.parseLong(payment.getCompanyId() + "")));
        House house = houseMapper.houseDTOToHouse(houseService.findOne(Long.valueOf(payment.getHouseId())));
        context.setVariable(COMPANY, company);
        context.setVariable(USER, resident);
        context.setVariable(PHONE_NUMBER, numtelefono);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(ADMIN_EMAIL, company.getEmail());
        context.setVariable(ADMIN_NUMBER, company.getPhoneNumber());
        context.setVariable(LOGO, company.getLogoUrl());
        String currency = companyConfigurationService.getByCompanyId(null, company.getId()).getContent().get(0).getCurrency();
        context.setVariable(CURRENCY, currency);
        context.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
        String content = "";
        if (companyService.findOne(Long.valueOf(payment.getCompanyId())).getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
            content = templateEngine.process("paymentMade", context);
        } else {
            content = templateEngine.process("paymentMadeNoAditum", context);
        }
        String subject = this.defineSubjectPaymentEmail(payment, company, house, isCancellingFromPayment);

        String fileName = this.defineFileNamePaymentEmail(payment);
//        ENVIO DE COMPROBANTE DE PAGO
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY, company);
            contextTemplate.setVariable(HOUSE, house);
            payment = this.formatPayment(payment, isCancellingFromPayment);
            contextTemplate.setVariable(PAYMENT, payment);
            contextTemplate.setVariable(LOGO, company.getLogoUrl());
            contextTemplate.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
            contextTemplate.setVariable(IS_CANCELLING_FROM_PAYMENT, isCancellingFromPayment);
            String paymentDate = payment.getAccount();
            String paymentTotal = payment.getAmmount();
            if (isCancellingFromPayment == true) {
                paymentDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(payment.getCharges().get(0).getPaymentDate());
                paymentTotal = payment.getCharges().get(0).getAmmount();
            }
            contextTemplate.setVariable(PAYMENT_DATE, paymentDate);
            contextTemplate.setVariable(PAYMENT_TOTAL, paymentTotal);
            contextTemplate.setVariable(CONTACTO, contactoPrincipal);
            contextTemplate.setVariable(USER, resident);
            contextTemplate.setVariable(CHARGES_SIZE, payment.getCharges().size());
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE, timeNowFormatted);
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
                    (company.getId(), payment.getEmailTo().get(i).getEmail(), subject, content, true, file, emailsToSend - 1, i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String defineSubjectPaymentEmail(PaymentDTO payment, Company company, House house, boolean isCancellingFromPayment) {
        String subject = "Recibo de Pago " + payment.getReceiptNumber() + " - " + company.getName();
        if (payment.getTransaction().equals("1") || payment.getTransaction().equals("2") && payment.getCharges().size() > 0) {
            subject = "Recibo de Pago " + payment.getReceiptNumber() + " Filial # " + house.getHousenumber() + " - " + company.getName() + " (Abono a cuotas)";
        } else if (payment.getTransaction().equals("2")) {
            subject = "Recibo de Pago " + payment.getReceiptNumber() + " Filial # " + house.getHousenumber() + " - " + company.getName() + " (Adelanto de condómino)";
        }
        return subject;
    }

    private String defineFileNamePaymentEmail(PaymentDTO payment) {
        String fileName = "Recibo de pago " + payment.getReceiptNumber() + ".pdf";
        return fileName;
    }

    public File obtainIncomeReportToPrint(IncomeReportDTO incomeReportDTO, Long companyId, ZonedDateTime fechaInicio, ZonedDateTime fechaFinal) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(companyId));
        String fileName = "Reporte de ingresos.pdf";
//        ENVIO DE COMPROBANTE DE PAGO
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY, company);
            ZonedDateTime date = ZonedDateTime.now();
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE, timeNowFormatted);
            String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
            incomeReportDTO = this.formatIncomeReport(incomeReportDTO, currency);
            contextTemplate.setVariable(INCOME_REPORT, incomeReportDTO);
            contextTemplate.setVariable(RANGO_FECHAS, this.formatRangoFechas(fechaInicio, fechaFinal));
            contextTemplate.setVariable(LOGO, company.getLogoUrl());
            contextTemplate.setVariable(CURRENCY, currency);
            contextTemplate.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
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


    private String formatRangoFechas(ZonedDateTime fechaInicial, ZonedDateTime fechaFinal) {
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "ES"));
        return "Del " + spanish.format(fechaInicial) + " al " + spanish.format(fechaFinal);
    }


    @Async
    public void sendReminderEmail(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO house, List<ChargeDTO> chargesDTOS, String templateName) {
        ResidentDTO residentDTO = this.residentService.findPrincipalContactByHouse(house.getId());
        if (residentDTO != null) {
            String currency = companyConfigurationService.getByCompanyId(null, house.getCompanyId()).getContent().get(0).getCurrency();
            Context contextTemplate = new Context();
            contextTemplate.setVariable(CONTACTO, residentDTO.getName() + " " + residentDTO.getLastname());
            contextTemplate.setVariable(HOUSE, house);
            contextTemplate.setVariable(ADMINISTRATION_CONFIGURATION, administrationConfigurationDTO);
            Locale locale = new Locale("es", "CR");
            DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale);
            double subchargeTotal = 0;
            for (int i = 0; i < chargesDTOS.size(); i++) {
                ChargeDTO chargeDTO = chargesDTOS.get(i);
                chargeDTO.setFormatedDate(spanish.format(chargeDTO.getDate()));
                if (templateName.equals("subchargeReminderEmail")) {
                    subchargeTotal = subchargeTotal + Double.parseDouble(chargeDTO.getSubcharge());
                }
                chargeDTO.setAmmount(formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
                chargeDTO.setSubcharge(formatMoney(currency, Double.parseDouble(chargeDTO.getSubcharge())));
                chargeDTO.setPaymentAmmount(formatMoney(currency, chargeDTO.getTotal()));
            }
            CompanyDTO company = this.companyService.findOne(house.getCompanyId());
            contextTemplate.setVariable(COMPANY, company);
            if (templateName.equals("subchargeReminderEmail")) {
                contextTemplate.setVariable(SUBCHARGE_TOTAL, formatMoney(currency, subchargeTotal).substring(1));
            }
            contextTemplate.setVariable(CHARGES, chargesDTOS);
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            String content = templateEngine.process(templateName, contextTemplate);
            String subject = "Recordatorio de pago, Filial # " + house.getHousenumber() + " ," + company.getName();
            this.mailService.sendEmail(residentDTO.getCompanyId(), residentDTO.getEmail(), subject, content, false, true);
        }
    }


    public File getChargeBillFile(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO house, ChargeDTO chargesDTO, ResidentDTO residentDTO) throws IOException, DocumentException {
        if (residentDTO != null) {
            Context contextTemplate = new Context();
            Context contextBillTemplate = new Context();
            contextTemplate.setVariable(CONTACTO, residentDTO.getName() + " " + residentDTO.getLastname());
            contextBillTemplate.setVariable(CONTACTO, residentDTO.getName() + " " + residentDTO.getLastname());

            contextTemplate.setVariable(HOUSE, house);
            contextBillTemplate.setVariable(HOUSE, house);

            contextTemplate.setVariable(ADMINISTRATION_CONFIGURATION, administrationConfigurationDTO);
            contextBillTemplate.setVariable(ADMINISTRATION_CONFIGURATION, administrationConfigurationDTO);

            Locale locale = new Locale("es", "CR");
            DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd/MM/yyyy", locale);
            ChargeDTO chargeDTO = chargesDTO;

            CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationService.getByCompanyId(null, house.getCompanyId()).getContent().get(0);
            String currency = companyConfigurationDTO.getCurrency();
            chargeDTO = this.chargeService.formatCharge(currency, chargeDTO);
            chargeDTO.setFormatedDate(spanish.format(chargeDTO.getDate()));
            double total = Double.parseDouble(chargeDTO.getTotal() + "");
            chargeDTO.setAmmount(formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
            chargeDTO.setPaymentAmmount(formatMoney(currency, chargeDTO.getTotal()));
            chargeDTO.setTotal(currency, total);
            chargeDTO.setTotalFormatted(formatMoney(currency, total));
            CompanyDTO company = this.companyService.findOne(house.getCompanyId());
            contextTemplate.setVariable(CURRENCY, currency);
            if (chargeDTO.getType() == 6) {
                WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
                if (wc != null) {
                    chargeDTO.setWaterConsumption(wc);
                    contextTemplate.setVariable(WATER_CONSUMPTION, chargeDTO.getWaterConsumption());
                }
            }
            contextBillTemplate.setVariable(CUENTAS_BANCARIAS, companyConfigurationDTO.getBankAccounts());
            contextBillTemplate.setVariable(CURRENCY, currency);
            contextTemplate.setVariable(COMPANY, company);
            contextBillTemplate.setVariable(COMPANY, company);
            chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
            contextTemplate.setVariable(CHARGE, chargeDTO);
            contextBillTemplate.setVariable(CHARGE, chargeDTO);
            String fechaCobro = spanish.format(chargeDTO.getDate());
            ZonedDateTime fechaVencimiento = chargeDTO.getDate().plusDays(administrationConfigurationDTO.getDaysTobeDefaulter());
            contextBillTemplate.setVariable(FECHA_VENCIMIENTO, spanish.format(fechaVencimiento));
            contextBillTemplate.setVariable(CURRENT_DATE, fechaCobro);
            contextBillTemplate.setVariable(PHONE_NUMBER, residentDTO.getPhonenumber() == null ? "No definido" : residentDTO.getPhonenumber());
            contextBillTemplate.setVariable(RESIDENTE, residentDTO);

            contextTemplate.setVariable(ADMIN_EMAIL, company.getEmail());
            contextBillTemplate.setVariable(ADMIN_EMAIL, company.getEmail());

            contextTemplate.setVariable(ADMIN_NUMBER, company.getPhoneNumber());
            contextBillTemplate.setVariable(ADMIN_NUMBER, company.getPhoneNumber());

            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextBillTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextBillTemplate.setVariable(LOGO, company.getLogoUrl());
            contextBillTemplate.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
            String content = "";
            if (company.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
                content = templateEngine.process("newChargeEmail", contextTemplate);
            } else {
                content = templateEngine.process("newChargeEmailNoAditum", contextTemplate);
            }
            String subject = "" + chargeDTO.getConcept() + ", Filial " + house.getHousenumber() + " - " + company.getName();
            String fileNumber = "Factura_" + chargeDTO.getBillNumber() + "-" + house.getHousenumber() + ".pdf";
            OutputStream outputStream = new FileOutputStream(fileNumber);
            ITextRenderer renderer = new ITextRenderer();
            String contentTemplateBillNumber = templateEngine.process("billChargeTemplate", contextBillTemplate);
            renderer.setDocumentFromString(contentTemplateBillNumber);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileNumber);
            return file;
//            this.mailService.sendEmailWithAtachment(company.getId(), residentDTO.getEmail(), subject, content, true, file);
//            new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        this.sleep(40000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    file.delete();
//               }
//            }.start();
        }
        return null;
    }

    @Async
    public void sendChargeManualEmail(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO house, ChargeDTO chargesDTO, ResidentDTO residentDTO) throws IOException, DocumentException {
        if (residentDTO != null) {
            Context contextTemplate = new Context();
            Context contextBillTemplate = new Context();
            contextTemplate.setVariable(CONTACTO, residentDTO.getName() + " " + residentDTO.getLastname());
            contextBillTemplate.setVariable(CONTACTO, residentDTO.getName() + " " + residentDTO.getLastname());

            contextTemplate.setVariable(HOUSE, house);
            contextBillTemplate.setVariable(HOUSE, house);

            contextTemplate.setVariable(ADMINISTRATION_CONFIGURATION, administrationConfigurationDTO);
            contextBillTemplate.setVariable(ADMINISTRATION_CONFIGURATION, administrationConfigurationDTO);
            contextBillTemplate.setVariable(RESIDENTE, residentDTO);

            Locale locale = new Locale("es", "CR");
            DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd/MM/yyyy", locale);
            ChargeDTO chargeDTO = chargesDTO;

            CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationService.getByCompanyId(null, house.getCompanyId()).getContent().get(0);
            String currency = companyConfigurationDTO.getCurrency();
            chargeDTO = this.chargeService.formatCharge(currency, chargeDTO);
            chargeDTO.setFormatedDate(spanish.format(chargeDTO.getDate()));
            double total = Double.parseDouble(chargeDTO.getTotal() + "");
            chargeDTO.setAmmount(formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
            chargeDTO.setPaymentAmmount(formatMoney(currency, chargeDTO.getTotal()));
            chargeDTO.setTotal(currency, total);
            chargeDTO.setTotalFormatted(formatMoney(currency, total));
            CompanyDTO company = this.companyService.findOne(house.getCompanyId());
            contextTemplate.setVariable(CURRENCY, currency);
            if (chargeDTO.getType() == 6) {
                WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
                if (wc != null) {
                    chargeDTO.setWaterConsumption(wc);
                    contextTemplate.setVariable(WATER_CONSUMPTION, chargeDTO.getWaterConsumption());
                }
            }
            contextBillTemplate.setVariable(CURRENCY, currency);
            contextTemplate.setVariable(COMPANY, company);
            contextBillTemplate.setVariable(COMPANY, company);
            chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
            contextTemplate.setVariable(CHARGE, chargeDTO);
            contextBillTemplate.setVariable(CHARGE, chargeDTO);
            String fechaCobro = spanish.format(chargeDTO.getDate());
            ZonedDateTime fechaVencimiento = chargeDTO.getDate().plusDays(administrationConfigurationDTO.getDaysTobeDefaulter());
            contextBillTemplate.setVariable(FECHA_VENCIMIENTO, spanish.format(fechaVencimiento));
            contextBillTemplate.setVariable(CURRENT_DATE, fechaCobro);
            contextBillTemplate.setVariable(PHONE_NUMBER, residentDTO.getPhonenumber() == null ? "No definido" : residentDTO.getPhonenumber());

            contextTemplate.setVariable(ADMIN_EMAIL, company.getEmail());
            contextBillTemplate.setVariable(ADMIN_EMAIL, company.getEmail());

            contextTemplate.setVariable(ADMIN_NUMBER, company.getPhoneNumber());
            contextBillTemplate.setVariable(ADMIN_NUMBER, company.getPhoneNumber());
            contextBillTemplate.setVariable(CUENTAS_BANCARIAS, companyConfigurationDTO.getBankAccounts());
            contextBillTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextBillTemplate.setVariable(LOGO, company.getLogoUrl());
            contextBillTemplate.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
            String content = "";
            if (company.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
                content = templateEngine.process("newChargeEmail", contextTemplate);
            } else {
                content = templateEngine.process("newChargeEmailNoAditum", contextTemplate);
            }
            String subject = "" + chargeDTO.getConcept() + ", Filial " + house.getHousenumber() + " - " + company.getName();
            String fileNumber = "Factura_" + chargeDTO.getBillNumber() + "-" + house.getHousenumber() + ".pdf";
            OutputStream outputStream = new FileOutputStream(fileNumber);
            ITextRenderer renderer = new ITextRenderer();
            String contentTemplateBillNumber = templateEngine.process("billChargeTemplate", contextBillTemplate);
            renderer.setDocumentFromString(contentTemplateBillNumber);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileNumber);
            this.mailService.sendEmailWithAtachment(company.getId(), residentDTO.getEmail(), subject, content, true, file);
            new Thread() {
                @Override
                public void run() {
                    try {
                        this.sleep(40000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    file.delete();
                }
            }.start();
        }
    }

    @Async
    public void sendChargeEmail(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO house, ChargeDTO chargesDTO) throws IOException, DocumentException {

        ResidentDTO residentDTO = this.residentService.findPrincipalContactByHouse(house.getId());
        if (residentDTO != null) {
            Context contextTemplate = new Context();
            Context contextBillTemplate = new Context();
            contextTemplate.setVariable(CONTACTO, residentDTO.getName() + " " + residentDTO.getLastname());
            contextBillTemplate.setVariable(CONTACTO, residentDTO.getName() + " " + residentDTO.getLastname());

            contextTemplate.setVariable(HOUSE, house);
            contextBillTemplate.setVariable(HOUSE, house);
            contextBillTemplate.setVariable(RESIDENTE, residentDTO);

            contextTemplate.setVariable(ADMINISTRATION_CONFIGURATION, administrationConfigurationDTO);
            contextBillTemplate.setVariable(ADMINISTRATION_CONFIGURATION, administrationConfigurationDTO);

            Locale locale = new Locale("es", "CR");
            DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd/MM/yyyy", locale);
            ChargeDTO chargeDTO = chargesDTO;

            CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationService.getByCompanyId(null, house.getCompanyId()).getContent().get(0);
            String currency = companyConfigurationDTO.getCurrency();
            chargeDTO = this.chargeService.formatCharge(currency, chargeDTO);
            chargeDTO.setFormatedDate(spanish.format(chargeDTO.getDate()));
            double total = Double.parseDouble(chargeDTO.getTotal() + "");
            chargeDTO.setAmmount(formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
            chargeDTO.setPaymentAmmount(formatMoney(currency, chargeDTO.getTotal()));
            chargeDTO.setTotal(currency, total);
            chargeDTO.setTotalFormatted(formatMoney(currency, total));
            contextBillTemplate.setVariable(CUENTAS_BANCARIAS, companyConfigurationDTO.getBankAccounts());
            CompanyDTO company = this.companyService.findOne(house.getCompanyId());
            contextTemplate.setVariable(CURRENCY, currency);
            if (chargeDTO.getType() == 6) {
                WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
                if (wc != null) {
                    chargeDTO.setWaterConsumption(wc);
                    contextTemplate.setVariable(WATER_CONSUMPTION, chargeDTO.getWaterConsumption().substring(1));
                }
            }
            contextBillTemplate.setVariable(CURRENCY, currency);
            contextTemplate.setVariable(COMPANY, company);
            contextBillTemplate.setVariable(COMPANY, company);
            chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
            contextTemplate.setVariable(CHARGE, chargeDTO);
            contextBillTemplate.setVariable(CHARGE, chargeDTO);
            String fechaCobro = spanish.format(chargeDTO.getDate());
            ZonedDateTime fechaVencimiento = chargeDTO.getDate().plusDays(administrationConfigurationDTO.getDaysTobeDefaulter());
            contextBillTemplate.setVariable(FECHA_VENCIMIENTO, spanish.format(fechaVencimiento));
            contextBillTemplate.setVariable(CURRENT_DATE, fechaCobro);
            contextBillTemplate.setVariable(PHONE_NUMBER, residentDTO.getPhonenumber() == null ? "No definido" : residentDTO.getPhonenumber());

            contextTemplate.setVariable(ADMIN_EMAIL, company.getEmail());
            contextBillTemplate.setVariable(ADMIN_EMAIL, company.getEmail());

            contextTemplate.setVariable(ADMIN_NUMBER, company.getPhoneNumber());
            contextBillTemplate.setVariable(ADMIN_NUMBER, company.getPhoneNumber());

            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextBillTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextBillTemplate.setVariable(LOGO, company.getLogoUrl());
            contextBillTemplate.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
            String content = "";
            if (company.getEmailConfiguration().getAdminCompanyName().equals("ADITUM")) {
                content = templateEngine.process("newChargeEmail", contextTemplate);
            } else {
                content = templateEngine.process("newChargeEmailNoAditum", contextTemplate);
            }
            String subject = chargeDTO.getConcept() + ", Filial " + house.getHousenumber() + " - " + company.getName();
            String fileNumber = "Factura_" + chargeDTO.getBillNumber() + "-" + house.getHousenumber() + ".pdf";
            OutputStream outputStream = new FileOutputStream(fileNumber);
            ITextRenderer renderer = new ITextRenderer();
            String contentTemplateBillNumber = templateEngine.process("billChargeTemplate", contextBillTemplate);
            renderer.setDocumentFromString(contentTemplateBillNumber);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileNumber);
            this.mailService.sendEmailWithAtachment(company.getId(), residentDTO.getEmail(), subject, content, true, file);
            new Thread() {
                @Override
                public void run() {
                    try {
                        this.sleep(40000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    file.delete();
                }
            }.start();
        }
    }

    public File obtainFileBillCharge(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO house, ChargeDTO chargesDTO) throws IOException, DocumentException {
        ResidentDTO residentDTO = this.residentService.findPrincipalContactByHouse(house.getId());

        Context contextTemplate = new Context();
        Context contextBillTemplate = new Context();
        if (residentDTO != null) {
            contextBillTemplate.setVariable(CONTACTO, residentDTO.getName() + " " + residentDTO.getLastname());
        } else {
            contextBillTemplate.setVariable(CONTACTO, "No definido");
        }
        contextBillTemplate.setVariable(RESIDENTE, residentDTO);

        contextBillTemplate.setVariable(HOUSE, house);
        contextBillTemplate.setVariable(ADMINISTRATION_CONFIGURATION, administrationConfigurationDTO);
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd/MM/yyyy", locale);
        ChargeDTO chargeDTO = chargesDTO;
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationService.getByCompanyId(null, house.getCompanyId()).getContent().get(0);
        String currency = companyConfigurationDTO.getCurrency();
        chargeDTO.setFormatedDate(spanish.format(chargeDTO.getDate()));
        chargeDTO.setAmmount(formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
        chargeDTO.setPaymentAmmount(formatMoney(currency, chargeDTO.getTotal()));
        chargeDTO.setTotal(currency, chargeDTO.getTotal());
        chargeDTO.setTotalFormatted(formatMoney(currency, chargeDTO.getTotal()));
        CompanyDTO company = this.companyService.findOne(house.getCompanyId());
        contextTemplate.setVariable(CURRENCY, currency);
        contextBillTemplate.setVariable(CUENTAS_BANCARIAS, companyConfigurationDTO.getBankAccounts());
        contextBillTemplate.setVariable(CURRENCY, currency);
        contextBillTemplate.setVariable(COMPANY, company);
        chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
        contextBillTemplate.setVariable(CHARGE, chargeDTO);
        String fechaCobro = spanish.format(chargeDTO.getDate());
        ZonedDateTime fechaVencimiento = chargeDTO.getDate().plusDays(administrationConfigurationDTO.getDaysTobeDefaulter());
        contextBillTemplate.setVariable(FECHA_VENCIMIENTO, spanish.format(fechaVencimiento));
        contextBillTemplate.setVariable(CURRENT_DATE, fechaCobro);
        if (residentDTO != null) {
            contextBillTemplate.setVariable(PHONE_NUMBER, residentDTO.getPhonenumber() == null ? "No definido" : residentDTO.getPhonenumber());
        } else {
            contextBillTemplate.setVariable(PHONE_NUMBER, "No definido");
        }
        contextTemplate.setVariable(ADMIN_EMAIL, company.getEmail());
        contextBillTemplate.setVariable(ADMIN_EMAIL, company.getEmail());
        contextTemplate.setVariable(ADMIN_NUMBER, company.getPhoneNumber());
        contextBillTemplate.setVariable(ADMIN_NUMBER, company.getPhoneNumber());
        contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        contextBillTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        contextBillTemplate.setVariable(LOGO, company.getLogoUrl());
        contextBillTemplate.setVariable(LOGO_ADMIN, company.getAdminLogoUrl());
        String fileNumber = "Factura_" + chargeDTO.getBillNumber() + "-" + house.getHousenumber() + ".pdf";
        OutputStream outputStream = new FileOutputStream(fileNumber);
        ITextRenderer renderer = new ITextRenderer();
        String contentTemplateBillNumber = templateEngine.process("billChargeTemplate", contextBillTemplate);
        renderer.setDocumentFromString(contentTemplateBillNumber);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        File file = new File(fileNumber);
        return file;
    }
}

