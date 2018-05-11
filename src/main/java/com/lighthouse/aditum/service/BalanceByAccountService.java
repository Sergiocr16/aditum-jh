package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.BalanceByAccount;
import com.lighthouse.aditum.repository.BalanceByAccountRepository;
import com.lighthouse.aditum.service.dto.BalanceByAccountDTO;
import com.lighthouse.aditum.service.mapper.BalanceByAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<BalanceByAccountDTO> findByDatesBetweenAndAccount(Pageable pageable, String initialTime, String finalTime, Long accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<BalanceByAccount> result = balanceByAccountRepository.findByDatesBetweenAndAccount(pageable,zd_initialTime,zd_finalTime,accountId);
//        Collections.reverse(result);
        return result.map(balanceByAccount -> balanceByAccountMapper.toDto(balanceByAccount));
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
}