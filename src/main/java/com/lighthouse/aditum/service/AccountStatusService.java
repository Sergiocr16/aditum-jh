package com.lighthouse.aditum.service;

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

    public AccountStatusService(ChargeService chargeService, PaymentService paymentService) {
        this.chargeService = chargeService;
        this.paymentService = paymentService;
    }

    public AccountStatusDTO getAccountStatusDTO(Pageable pageable, Long houseId, ZonedDateTime initial_time, ZonedDateTime final_time, boolean resident_account, ZonedDateTime today_time) {
        AccountStatusDTO accountStatusDTO = new AccountStatusDTO();
        accountStatusDTO.setListaAccountStatusItems(new ArrayList<>());
        double saldoInicial = this.getSaldoInicial(pageable, houseId, initial_time);
        accountStatusDTO.setSaldoInicial(saldoInicial);
        accountStatusDTO.setSaldo(saldoInicial);
        Page<PaymentDTO> payments = this.paymentService.findByHouseFilteredByDate(pageable, houseId, initial_time, final_time);

        if (resident_account) {
            Page<ChargeDTO> charges = this.chargeService.findAllByHouseAndBetweenDateResidentAccount(houseId, initial_time, final_time, today_time);

            this.setAccountStatusItem(payments, charges, accountStatusDTO);
        } else {
            Page<ChargeDTO> Allcharges = this.chargeService.findAllByHouseAndBetweenDate(houseId, initial_time, final_time);
            List<ChargeDTO> finalCharges = new ArrayList<>();
            List<ChargeDTO> allWithoutSplited = Allcharges.getContent().stream().filter(p -> p.getSplited() == null && p.getSplitedCharge() == null).collect(Collectors.toList());
            finalCharges.addAll(allWithoutSplited);
            List<ChargeDTO> allWIthOneSplited = Allcharges.getContent().stream().filter(p -> p.getSplitedCharge() != null).collect(Collectors.toList());

            allWIthOneSplited.forEach(chargeDTO -> {
                ChargeDTO splitedCharge = this.chargeService.findOne(Long.valueOf(chargeDTO.getSplitedCharge()));
                chargeDTO.setAmmount(Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(splitedCharge.getAmmount()) + "");
                chargeDTO.setSubcharge(Double.parseDouble(chargeDTO.getSubcharge()) + Double.parseDouble(splitedCharge.getSubcharge()) + "");
                chargeDTO.setTotal(Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(splitedCharge.getSubcharge()));
                finalCharges.add(chargeDTO);
            });
            Page<ChargeDTO> charges = new PageImpl<ChargeDTO>(finalCharges);
            this.setAccountStatusItem(payments, charges, accountStatusDTO);
        }


        return accountStatusDTO;
    }

    private void setAccountStatusItem(Page<PaymentDTO> payments, Page<ChargeDTO> charges, AccountStatusDTO accountStatusDTO) {
        for (int i = 0; i < charges.getContent().size(); i++) {
            AccountStatusItemDTO object = new AccountStatusItemDTO(charges.getContent().get(i).getDate(), charges.getContent().get(i).getConcept(), Double.parseDouble(charges.getContent().get(i).getAmmount()), Double.parseDouble(charges.getContent().get(i).getSubcharge()));
            accountStatusDTO.getListaAccountStatusItems().add(object);
        }
        for (int i = 0; i < payments.getContent().size(); i++) {
            AccountStatusItemDTO object = new AccountStatusItemDTO(payments.getContent().get(i).getDate(), Integer.parseInt(payments.getContent().get(i).getTransaction()), Integer.parseInt(payments.getContent().get(i).getAmmount()), payments.getContent().get(i).getCharges());

            accountStatusDTO.getListaAccountStatusItems().add(object);
        }

        Collections.sort(accountStatusDTO.getListaAccountStatusItems(), Comparator.comparing(AccountStatusItemDTO::getDate));

        for (int i = 0; i < accountStatusDTO.getListaAccountStatusItems().size(); i++) {

            if (accountStatusDTO.getListaAccountStatusItems().get(i).getAbono() > 0) {
                double saldo = accountStatusDTO.getSaldo() + accountStatusDTO.getListaAccountStatusItems().get(i).getAbono();
                accountStatusDTO.getListaAccountStatusItems().get(i).setSaldo(saldo);
                accountStatusDTO.setTotalAbono(accountStatusDTO.getListaAccountStatusItems().get(i).getAbono());
                accountStatusDTO.setSaldo(saldo);

            } else if (accountStatusDTO.getListaAccountStatusItems().get(i).getTotal() > 0) {
                double saldo = accountStatusDTO.getSaldo() - accountStatusDTO.getListaAccountStatusItems().get(i).getTotal();
                accountStatusDTO.setTotalCharge(accountStatusDTO.getListaAccountStatusItems().get(i).getCharge());
                accountStatusDTO.setTotalRecharge(accountStatusDTO.getListaAccountStatusItems().get(i).getRecharge());
                accountStatusDTO.setTotalTotal(accountStatusDTO.getListaAccountStatusItems().get(i).getTotal());
                accountStatusDTO.getListaAccountStatusItems().get(i).setSaldo(saldo);
                accountStatusDTO.setSaldo(saldo);

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
