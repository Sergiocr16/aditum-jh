package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.HistoricalDefaulterCharge;
import com.lighthouse.aditum.repository.HistoricalDefaulterChargeRepository;
import com.lighthouse.aditum.service.dto.HistoricalDefaulterChargeDTO;
import com.lighthouse.aditum.service.mapper.HistoricalDefaulterChargeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing HistoricalDefaulterCharge.
 */
@Service
@Transactional
public class HistoricalDefaulterChargeService {

    private final Logger log = LoggerFactory.getLogger(HistoricalDefaulterChargeService.class);

    private final HistoricalDefaulterChargeRepository historicalDefaulterChargeRepository;

    private final HistoricalDefaulterChargeMapper historicalDefaulterChargeMapper;

    public HistoricalDefaulterChargeService(HistoricalDefaulterChargeRepository historicalDefaulterChargeRepository, HistoricalDefaulterChargeMapper historicalDefaulterChargeMapper) {
        this.historicalDefaulterChargeRepository = historicalDefaulterChargeRepository;
        this.historicalDefaulterChargeMapper = historicalDefaulterChargeMapper;
    }

    /**
     * Save a historicalDefaulterCharge.
     *
     * @param historicalDefaulterChargeDTO the entity to save
     * @return the persisted entity
     */
    public HistoricalDefaulterChargeDTO save(HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO) {
        log.debug("Request to save HistoricalDefaulterCharge : {}", historicalDefaulterChargeDTO);
        HistoricalDefaulterCharge historicalDefaulterCharge = historicalDefaulterChargeMapper.toEntity(historicalDefaulterChargeDTO);
        historicalDefaulterCharge = historicalDefaulterChargeRepository.save(historicalDefaulterCharge);
        return historicalDefaulterChargeMapper.toDto(historicalDefaulterCharge);
    }

    /**
     * Get all the historicalDefaulterCharges.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<HistoricalDefaulterChargeDTO> findAll() {
        log.debug("Request to get all HistoricalDefaulterCharges");
        return historicalDefaulterChargeRepository.findAll().stream()
            .map(historicalDefaulterChargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<HistoricalDefaulterChargeDTO> findAllbyHistoricalDefaulter(Long historicalDefaulter) {
        log.debug("Request to get all HistoricalDefaulterCharges");
        return historicalDefaulterChargeRepository.findAllByHistoricalDefaulterId(historicalDefaulter).stream()
            .map(historicalDefaulterChargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one historicalDefaulterCharge by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HistoricalDefaulterChargeDTO findOne(Long id) {
        log.debug("Request to get HistoricalDefaulterCharge : {}", id);
        HistoricalDefaulterCharge historicalDefaulterCharge = historicalDefaulterChargeRepository.findOne(id);
        return historicalDefaulterChargeMapper.toDto(historicalDefaulterCharge);
    }

    /**
     * Delete the historicalDefaulterCharge by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HistoricalDefaulterCharge : {}", id);
        historicalDefaulterChargeRepository.delete(id);
    }
}
