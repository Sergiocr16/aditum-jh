package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.util.RandomUtil;
import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountStatusService {

    private final Logger log = LoggerFactory.getLogger(AccountStatusService.class);
    private final ChargeService chargeService;
    private final PaymentService paymentService;
    private final CompanyConfigurationService companyConfigurationService;
    private final HouseService houseService;
    private final AccountStatusDocumentService accountStatusDocumentService;


    public AccountStatusService(AccountStatusDocumentService accountStatusDocumentService,HouseService houseService, CompanyConfigurationService companyConfigurationService, ChargeService chargeService, PaymentService paymentService) {
        this.chargeService = chargeService;
        this.paymentService = paymentService;
        this.companyConfigurationService = companyConfigurationService;
        this.houseService = houseService;
        this.accountStatusDocumentService = accountStatusDocumentService;
    }


    public void sendAccountStatusByEmail(Long houseId, Long companyId, String emailTo, ZonedDateTime monthDate,String currency,AdministrationConfigurationDTO administrationConfigurationDTO) throws IOException, DocumentException {
        ZonedDateTime lastDay = monthDate.with(TemporalAdjusters.lastDayOfMonth()).withMinute(59).withHour(23).withSecond(59);
        ZonedDateTime firstDay = monthDate.with(TemporalAdjusters.firstDayOfMonth()).withMinute(0).withHour(0).withSecond(0);
        ChargesToPayReportDTO chargesToPayReport = chargeService.findChargesToPay(lastDay, 10, companyId, houseId);
        List<ChargeDTO> chargesMonth = chargeService.findAllByHouseAndBetweenDate(currency,houseId,firstDay,lastDay).getContent();
        HistoricalReportPositiveBalanceDTO positiveBalanceReport = chargeService.findHistoricalReportPositiveBalance(ZonedDateTime.now(), lastDay, companyId, houseId);
        List<ChargeDTO> chargestoPay = new ArrayList<>();
        if(chargesToPayReport.getDueHouses().size()>0){
            for (int i = 0; i < chargesToPayReport.getDueHouses().get(0).getDues().size(); i++) {
                chargestoPay.add(chargesToPayReport.getDueHouses().get(0).getDues().get(i));
            }
       }
        for (int i = 0; i < chargesMonth.size(); i++) {
            ChargeDTO c = chargesMonth.get(i);
            if(!chargestoPay.contains(c)){
                chargestoPay.add(c);
            }
        }
        AccountStatusToSendDTO accountStatusToSend = new AccountStatusToSendDTO();
        accountStatusToSend.setHasNegativeBalance(false);
        accountStatusToSend.setHasNegativeBalance(false);

        accountStatusToSend.setHouse(this.houseService.findOneClean(houseId));
        accountStatusToSend.setCharges(new ArrayList<>());
        accountStatusToSend.setAdelantos(new ArrayList<>());
        double totalLeftToPay = 0;
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("d MMMM yyyy", locale);
        for (int i = 0; i < chargestoPay.size(); i++) {
            accountStatusToSend.setHasNegativeBalance(true);
            ChargeDTO c = chargestoPay.get(i);
            c.setTotalFormatted(RandomUtil.formatMoney(currency,c.getTotal()));
            c.setLeftToPay(currency,c.getLeftToPay());
            c.setAbonado(currency,c.getAbonado());
            c.setFormatedDate(spanish.format(c.getDate()));
            totalLeftToPay = totalLeftToPay + c.getLeftToPay();
            c.setTemporalAmmount("c");
            accountStatusToSend.getCharges().add(c);
        }
        if(positiveBalanceReport.getTotalDue()>0){
            accountStatusToSend.setHasPositiveBalance(true);
            ChargeDTO c = new ChargeDTO();
            c.setConcept("Saldo a favor");
            c.setTemporalAmmount("a");
            c.setType(10);
            c.setLeftToPay(currency,positiveBalanceReport.getTotalDue());
            accountStatusToSend.getAdelantos().add(c);
        }
        accountStatusToSend.setTotalLeftToPay(currency,totalLeftToPay);
        this.accountStatusDocumentService.sendAccountsStatusAcumulative(accountStatusToSend,emailTo,monthDate,currency,administrationConfigurationDTO);
    }


    public AccountStatusDTO getAccountStatusDTO(Pageable pageable, Long houseId, ZonedDateTime initial_time, ZonedDateTime final_time, boolean resident_account, ZonedDateTime today_time) {
        AccountStatusDTO accountStatusDTO = new AccountStatusDTO();
        Long companyId = this.houseService.findOne(houseId).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        accountStatusDTO.setListaAccountStatusItems(new ArrayList<>());
        double saldoInicial = this.getSaldoInicial(pageable, houseId, initial_time);
        accountStatusDTO.setSaldoInicial(saldoInicial);
        accountStatusDTO.setSaldo(currency, saldoInicial);
        Page<PaymentDTO> payments = this.paymentService.findByHouseFilteredByDate(pageable, houseId, initial_time, final_time);
        if (resident_account) {
            Page<ChargeDTO> charges = this.chargeService.findAllByHouseAndBetweenDateResidentAccount(houseId, initial_time, final_time, today_time);
            this.setAccountStatusItem(currency, payments, charges, accountStatusDTO);
        } else {
            Page<ChargeDTO> charges = this.chargeService.findAccountStatusCharges(initial_time, final_time, companyId, houseId.toString(), "empty");
//            List<ChargeDTO> finalCharges = new ArrayList<>();
//            List<ChargeDTO> allWithoutSplited = Allcharges.getContent().stream().filter(p -> p.getSplited() == null && p.getSplitedCharge() == null).collect(Collectors.toList());
//            finalCharges.addAll(allWithoutSplited);
//            List<ChargeDTO> allWIthOneSplited = Allcharges.getContent().stream().filter(p -> p.getSplitedCharge() != null).collect(Collectors.toList());
//
//            allWIthOneSplited.forEach(chargeDTO -> {
//                ChargeDTO splitedCharge = this.chargeService.findOne(Long.valueOf(chargeDTO.getSplitedCharge()));
//                chargeDTO.setAmmount(Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(splitedCharge.getAmmount()) + "");
//                chargeDTO.setSubcharge(Double.parseDouble(chargeDTO.getSubcharge()) + Double.parseDouble(splitedCharge.getSubcharge()) + "");
//                chargeDTO.setTotal(currency,Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(splitedCharge.getSubcharge()));
//                finalCharges.add(chargeDTO);
//            });

            this.setAccountStatusItem(currency, payments, charges, accountStatusDTO);
        }


        return accountStatusDTO;
    }

    private void setAccountStatusItem(String currency, Page<PaymentDTO> payments, Page<ChargeDTO> charges, AccountStatusDTO accountStatusDTO) {
        for (int i = 0; i < charges.getContent().size(); i++) {
            AccountStatusItemDTO object = new AccountStatusItemDTO(currency, charges.getContent().get(i).getDate(), charges.getContent().get(i).getConcept(), Double.parseDouble(charges.getContent().get(i).getAmmount()), Double.parseDouble(charges.getContent().get(i).getSubcharge() == null ? "0" : charges.getContent().get(i).getSubcharge()));
            accountStatusDTO.getListaAccountStatusItems().add(object);
        }
        for (int i = 0; i < payments.getContent().size(); i++) {
            AccountStatusItemDTO object = new AccountStatusItemDTO(currency, payments.getContent().get(i).getDate(), Integer.parseInt(payments.getContent().get(i).getTransaction()), Double.parseDouble(payments.getContent().get(i).getAmmount()), payments.getContent().get(i).getCharges());
            accountStatusDTO.getListaAccountStatusItems().add(object);
        }

        Collections.sort(accountStatusDTO.getListaAccountStatusItems(), Comparator.comparing(AccountStatusItemDTO::getDate));

        for (int i = 0; i < accountStatusDTO.getListaAccountStatusItems().size(); i++) {
            if (accountStatusDTO.getListaAccountStatusItems().get(i).getAbono() > 0) {
                double saldo = accountStatusDTO.getSaldo() + accountStatusDTO.getListaAccountStatusItems().get(i).getAbono();
                accountStatusDTO.getListaAccountStatusItems().get(i).setSaldo(currency, saldo);
                accountStatusDTO.setTotalAbono(currency, accountStatusDTO.getListaAccountStatusItems().get(i).getAbono());
                accountStatusDTO.setSaldo(currency, saldo);
            } else if (accountStatusDTO.getListaAccountStatusItems().get(i).getTotal() > 0) {
                double saldo = accountStatusDTO.getSaldo() - accountStatusDTO.getListaAccountStatusItems().get(i).getTotal();
                accountStatusDTO.setTotalCharge(currency, accountStatusDTO.getListaAccountStatusItems().get(i).getCharge());
                accountStatusDTO.setTotalRecharge(currency, accountStatusDTO.getListaAccountStatusItems().get(i).getRecharge());
                accountStatusDTO.setTotalTotal(currency, accountStatusDTO.getListaAccountStatusItems().get(i).getTotal());
                accountStatusDTO.getListaAccountStatusItems().get(i).setSaldo(currency, saldo);
                accountStatusDTO.setSaldo(currency, saldo);
            }

        }
    }

    private double getSaldoInicial(Pageable pageable, Long houseId, ZonedDateTime initial_time) {
        double saldoInicial = 0;
        double totalCharges = 0;
        double totalPayments = 0;
        Page<ChargeDTO> charges = this.chargeService.findAllByHouseAndUnderDate(houseId, initial_time);
        Page<PaymentDTO> payments = this.paymentService.findByHouseUnderDate(pageable, houseId, initial_time);
        for (int i = 0; i < charges.getContent().size(); i++) {
            totalCharges = totalCharges + charges.getContent().get(i).getTotal();
        }
        for (int i = 0; i < payments.getContent().size(); i++) {
            totalPayments = totalPayments + Double.parseDouble(payments.getContent().get(i).getAmmount());
        }
        saldoInicial = totalPayments - totalCharges;
        return saldoInicial;
    }
}
