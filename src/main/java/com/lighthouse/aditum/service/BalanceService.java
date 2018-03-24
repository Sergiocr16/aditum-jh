package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Balance;
import com.lighthouse.aditum.repository.BalanceRepository;
import com.lighthouse.aditum.service.dto.BalanceDTO;
import com.lighthouse.aditum.service.mapper.BalanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Balance.
 */
@Service
@Transactional
public class BalanceService {

    private final Logger log = LoggerFactory.getLogger(BalanceService.class);

    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;

    public BalanceService(BalanceRepository balanceRepository, BalanceMapper balanceMapper) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
    }

    /**
     * Save a balance.
     *
     * @param balanceDTO the entity to save
     * @return the persisted entity
     */
    public BalanceDTO save(BalanceDTO balanceDTO) {
        log.debug("Request to save Balance : {}", balanceDTO);
        Balance balance = balanceMapper.toEntity(balanceDTO);
        balance = balanceRepository.save(balance);
        return balanceMapper.toDto(balance);
    }

    /**
     *  Get all the balances.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BalanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Balances");
        return balanceRepository.findAll(pageable)
            .map(balanceMapper::toDto);
    }

    /**
     *  Get one balance by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BalanceDTO findOne(Long id) {
        log.debug("Request to get Balance : {}", id);
        Balance balance = balanceRepository.findOne(id);
        return balanceMapper.toDto(balance);
    }

    /**
     *  Delete the  balance by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Balance : {}", id);
        balanceRepository.delete(id);
    }
}
