package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.HistoricalCharge;
import com.lighthouse.aditum.repository.HistoricalChargeRepository;
import com.lighthouse.aditum.service.dto.HistoricalChargeDTO;
import com.lighthouse.aditum.service.mapper.HistoricalChargeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing HistoricalCharge.
 */
@Service
@Transactional
public class HistoricalChargeService {

    private final Logger log = LoggerFactory.getLogger(HistoricalChargeService.class);

    private final HistoricalChargeRepository historicalChargeRepository;

    private final HistoricalChargeMapper historicalChargeMapper;

    public HistoricalChargeService(HistoricalChargeRepository historicalChargeRepository, HistoricalChargeMapper historicalChargeMapper) {
        this.historicalChargeRepository = historicalChargeRepository;
        this.historicalChargeMapper = historicalChargeMapper;
    }

    /**
     * Save a historicalCharge.
     *
     * @param historicalChargeDTO the entity to save
     * @return the persisted entity
     */
    public HistoricalChargeDTO save(HistoricalChargeDTO historicalChargeDTO) {
        log.debug("Request to save HistoricalCharge : {}", historicalChargeDTO);
        HistoricalCharge historicalCharge = historicalChargeMapper.toEntity(historicalChargeDTO);
        historicalCharge = historicalChargeRepository.save(historicalCharge);
        return historicalChargeMapper.toDto(historicalCharge);
    }

    /**
     *  Get all the historicalCharges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HistoricalChargeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoricalCharges");
        return historicalChargeRepository.findAll(pageable)
            .map(historicalChargeMapper::toDto);
    }

    /**
     *  Get one historicalCharge by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public HistoricalChargeDTO findOne(Long id) {
        log.debug("Request to get HistoricalCharge : {}", id);
        HistoricalCharge historicalCharge = historicalChargeRepository.findOne(id);
        return historicalChargeMapper.toDto(historicalCharge);
    }

    /**
     *  Delete the  historicalCharge by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HistoricalCharge : {}", id);
        historicalChargeRepository.delete(id);
    }
}
