package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.BalanceByAccount;
import com.lighthouse.aditum.domain.Egress;
import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.repository.BalanceByAccountRepository;
import com.lighthouse.aditum.service.dto.BalanceByAccountDTO;
import com.lighthouse.aditum.service.dto.EgressDTO;
import com.lighthouse.aditum.service.dto.PaymentDTO;
import com.lighthouse.aditum.service.mapper.BalanceByAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing BalanceByAccount.
 */
@Service
@Transactional
public class BalanceByAccountService {

    private final Logger log = LoggerFactory.getLogger(BalanceByAccountService.class);

    private final BalanceByAccountRepository balanceByAccountRepository;

    private final BalanceByAccountMapper balanceByAccountMapper;

    public BalanceByAccountService(BalanceByAccountRepository balanceByAccountRepository, BalanceByAccountMapper balanceByAccountMapper) {
        this.balanceByAccountRepository = balanceByAccountRepository;
        this.balanceByAccountMapper = balanceByAccountMapper;
    }

    /**
     *
     * Save a balanceByAccount.
     *
     * @param balanceByAccountDTO the entity to save
     * @return the persisted entity
     */
    public BalanceByAccountDTO save(BalanceByAccountDTO balanceByAccountDTO) {
        log.debug("Request to save BalanceByAccount : {}", balanceByAccountDTO);
        BalanceByAccount balanceByAccount = balanceByAccountMapper.toEntity(balanceByAccountDTO);
        balanceByAccount = balanceByAccountRepository.save(balanceByAccount);
        return balanceByAccountMapper.toDto(balanceByAccount);
    }

    /**
     *  Get all the balanceByAccounts.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BalanceByAccountDTO> findAll() {
        log.debug("Request to get all BalanceByAccounts");
        return balanceByAccountRepository.findAll().stream()
            .map(balanceByAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    @Transactional(readOnly = true)
    public Page<BalanceByAccountDTO> findByDatesBetweenAndAccount(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, Long accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<BalanceByAccount> result = balanceByAccountRepository.findByDatesBetweenAndAccount(pageable,zd_initialTime,zd_finalTime,accountId);
//        Collections.reverse(result);
        return result.map(balanceByAccount -> balanceByAccountMapper.toDto(balanceByAccount));
    }
    @Transactional(readOnly = true)
    public List<BalanceByAccountDTO> findByDatesBetweenAndAccount(ZonedDateTime initialTime, ZonedDateTime finalTime, Long accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        return balanceByAccountRepository.findByDatesBetweenAndAccount(zd_initialTime,zd_finalTime,accountId).stream()
            .map(balanceByAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

    }

    @Transactional(readOnly = true)
    public List<BalanceByAccountDTO> findAfterDate(ZonedDateTime date, Long accountId) {
        log.debug("Request to get all Visitants in last month by house");
        return balanceByAccountRepository.findByAccountAndAfterDate(date,accountId).stream()
            .map(balanceByAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    /**
     *  Get one balanceByAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BalanceByAccountDTO findOne(Long id) {
        log.debug("Request to get BalanceByAccount : {}", id);
        BalanceByAccount balanceByAccount = balanceByAccountRepository.findOne(id);
        return balanceByAccountMapper.toDto(balanceByAccount);
    }

    /**
     *  Delete the  balanceByAccount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BalanceByAccount : {}", id);
        balanceByAccountRepository.delete(id);
    }

   @Async
    public void modifyBalancesInPastPayment(Payment payment){
        double ammount = Double.parseDouble(payment.getAmmount());
        modifyBalances(payment.getDate(),ammount,1,payment.getAccount());
    }
    public void modifyBalancesInPastEgress(Egress egress){
        double ammount = Double.parseDouble(egress.getTotal());
        modifyBalances(egress.getPaymentDate(),ammount,2,egress.getAccount());
    }

    private void modifyBalances(ZonedDateTime time,double ammount,int type,String account){
        ZonedDateTime now = ZonedDateTime.now();
        time = time.withDayOfMonth(1).withHour(0).withMinute(0);
        if( time.getYear()<now.getYear()  || time.getMonthValue()<now.getMonthValue() && time.getYear()==now.getYear()){
         List<BalanceByAccountDTO> balances = this.findAfterDate(time,Long.valueOf(account));
         balances.forEach(balanceByAccountDTO -> {
             if(type==1){
                 balanceByAccountDTO.setBalance(Double.parseDouble(balanceByAccountDTO.getBalance())+ammount+"");
             }else{
                 balanceByAccountDTO.setBalance(Double.parseDouble(balanceByAccountDTO.getBalance())-ammount+"");
             }
             this.save(balanceByAccountDTO);
         });
        }
    }

}
