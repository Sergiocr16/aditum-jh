package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.service.dto.AccountStatusDTO;
import com.lighthouse.aditum.service.dto.AccountStatusItemDTO;
import com.lighthouse.aditum.service.dto.ChargeDTO;
import com.lighthouse.aditum.service.dto.PaymentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountStatusService {

    private final Logger log = LoggerFactory.getLogger(AccountStatusService.class);
    private final ChargeService chargeService;
    private final PaymentService paymentService;
    private final CompanyConfigurationService companyConfigurationService;
    private final HouseService houseService;

    public AccountStatusService(HouseService houseService,CompanyConfigurationService companyConfigurationService, ChargeService chargeService, PaymentService paymentService) {
        this.chargeService = chargeService;
        this.paymentService = paymentService;
        this.companyConfigurationService = companyConfigurationService;
        this.houseService = houseService;
    }

    public AccountStatusDTO getAccountStatusDTO(Pageable pageable, Long houseId, ZonedDateTime initial_time, ZonedDateTime final_time, boolean resident_account, ZonedDateTime today_time) {
        AccountStatusDTO accountStatusDTO = new AccountStatusDTO();
        String currency = companyConfigurationService.getByCompanyId(null,this.houseService.findOne(houseId).getCompanyId()).getContent().get(0).getCurrency();
        accountStatusDTO.setListaAccountStatusItems(new ArrayList<>());
        double saldoInicial = this.getSaldoInicial(pageable, houseId, initial_time);
        accountStatusDTO.setSaldoInicial(saldoInicial);
        accountStatusDTO.setSaldo(currency,saldoInicial);
        Page<PaymentDTO> payments = this.paymentService.findByHouseFilteredByDate(pageable, houseId, initial_time, final_time);

        if (resident_account) {
            Page<ChargeDTO> charges = this.chargeService.findAllByHouseAndBetweenDateResidentAccount(houseId, initial_time, final_time, today_time);
            this.setAccountStatusItem(currency,payments, charges, accountStatusDTO);
        } else {
            Page<ChargeDTO> Allcharges = this.chargeService.findAllByHouseAndBetweenDate(currency,houseId, initial_time, final_time);
            List<ChargeDTO> finalCharges = new ArrayList<>();
            List<ChargeDTO> allWithoutSplited = Allcharges.getContent().stream().filter(p -> p.getSplited() == null && p.getSplitedCharge() == null).collect(Collectors.toList());
            finalCharges.addAll(allWithoutSplited);
            List<ChargeDTO> allWIthOneSplited = Allcharges.getContent().stream().filter(p -> p.getSplitedCharge() != null).collect(Collectors.toList());

            allWIthOneSplited.forEach(chargeDTO -> {
                ChargeDTO splitedCharge = this.chargeService.findOne(Long.valueOf(chargeDTO.getSplitedCharge()));
                chargeDTO.setAmmount(Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(splitedCharge.getAmmount()) + "");
                chargeDTO.setSubcharge(Double.parseDouble(chargeDTO.getSubcharge()) + Double.parseDouble(splitedCharge.getSubcharge()) + "");
                chargeDTO.setTotal(currency,Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(splitedCharge.getSubcharge()));
                finalCharges.add(chargeDTO);
            });
            Page<ChargeDTO> charges = new PageImpl<ChargeDTO>(finalCharges);

            this.setAccountStatusItem(currency, payments, charges, accountStatusDTO);
        }


        return accountStatusDTO;
    }

    private void setAccountStatusItem(String currency,Page<PaymentDTO> payments, Page<ChargeDTO> charges, AccountStatusDTO accountStatusDTO) {
        for (int i = 0; i < charges.getContent().size(); i++) {
            AccountStatusItemDTO object = new AccountStatusItemDTO(currency,charges.getContent().get(i).getDate(), charges.getContent().get(i).getConcept(), Double.parseDouble(charges.getContent().get(i).getAmmount()), Double.parseDouble(charges.getContent().get(i).getSubcharge()));
            accountStatusDTO.getListaAccountStatusItems().add(object);
        }
        for (int i = 0; i < payments.getContent().size(); i++) {
            AccountStatusItemDTO object = new AccountStatusItemDTO(currency,payments.getContent().get(i).getDate(), Integer.parseInt(payments.getContent().get(i).getTransaction()), Double.parseDouble(payments.getContent().get(i).getAmmount()), payments.getContent().get(i).getCharges());

            accountStatusDTO.getListaAccountStatusItems().add(object);
        }

        Collections.sort(accountStatusDTO.getListaAccountStatusItems(), Comparator.comparing(AccountStatusItemDTO::getDate));

        for (int i = 0; i < accountStatusDTO.getListaAccountStatusItems().size(); i++) {

            if (accountStatusDTO.getListaAccountStatusItems().get(i).getAbono() > 0) {
                double saldo = accountStatusDTO.getSaldo() + accountStatusDTO.getListaAccountStatusItems().get(i).getAbono();
                accountStatusDTO.getListaAccountStatusItems().get(i).setSaldo(currency,saldo);
                accountStatusDTO.setTotalAbono(currency,accountStatusDTO.getListaAccountStatusItems().get(i).getAbono());
                accountStatusDTO.setSaldo(currency,saldo);

            } else if (accountStatusDTO.getListaAccountStatusItems().get(i).getTotal() > 0) {
                double saldo = accountStatusDTO.getSaldo() - accountStatusDTO.getListaAccountStatusItems().get(i).getTotal();
                accountStatusDTO.setTotalCharge(currency,accountStatusDTO.getListaAccountStatusItems().get(i).getCharge());
                accountStatusDTO.setTotalRecharge(currency,accountStatusDTO.getListaAccountStatusItems().get(i).getRecharge());
                accountStatusDTO.setTotalTotal(currency,accountStatusDTO.getListaAccountStatusItems().get(i).getTotal());
                accountStatusDTO.getListaAccountStatusItems().get(i).setSaldo(currency,saldo);
                accountStatusDTO.setSaldo(currency,saldo);
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
