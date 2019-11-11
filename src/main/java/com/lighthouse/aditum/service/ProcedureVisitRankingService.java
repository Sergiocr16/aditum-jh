package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ProcedureVisitRanking;
import com.lighthouse.aditum.repository.ProcedureVisitRankingRepository;
import com.lighthouse.aditum.service.dto.ProcedureVisitRankingDTO;
import com.lighthouse.aditum.service.mapper.ProcedureVisitRankingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProcedureVisitRanking.
 */
@Service
@Transactional
public class ProcedureVisitRankingService {

    private final Logger log = LoggerFactory.getLogger(ProcedureVisitRankingService.class);

    private final ProcedureVisitRankingRepository procedureVisitRankingRepository;

    private final ProcedureVisitRankingMapper procedureVisitRankingMapper;

    public ProcedureVisitRankingService(ProcedureVisitRankingRepository procedureVisitRankingRepository, ProcedureVisitRankingMapper procedureVisitRankingMapper) {
        this.procedureVisitRankingRepository = procedureVisitRankingRepository;
        this.procedureVisitRankingMapper = procedureVisitRankingMapper;
    }

    /**
     * Save a procedureVisitRanking.
     *
     * @param procedureVisitRankingDTO the entity to save
     * @return the persisted entity
     */
    public ProcedureVisitRankingDTO save(ProcedureVisitRankingDTO procedureVisitRankingDTO) {
        log.debug("Request to save ProcedureVisitRanking : {}", procedureVisitRankingDTO);
        ProcedureVisitRanking procedureVisitRanking = procedureVisitRankingMapper.toEntity(procedureVisitRankingDTO);
        procedureVisitRanking = procedureVisitRankingRepository.save(procedureVisitRanking);
        return procedureVisitRankingMapper.toDto(procedureVisitRanking);
    }

    /**
     * Get all the procedureVisitRankings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProcedureVisitRankingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcedureVisitRankings");
        return procedureVisitRankingRepository.findAll(pageable)
            .map(procedureVisitRankingMapper::toDto);
    }

    /**
     * Get one procedureVisitRanking by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProcedureVisitRankingDTO findOne(Long id) {
        log.debug("Request to get ProcedureVisitRanking : {}", id);
        ProcedureVisitRanking procedureVisitRanking = procedureVisitRankingRepository.findOne(id);
        return procedureVisitRankingMapper.toDto(procedureVisitRanking);
    }

    /**
     * Delete the procedureVisitRanking by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProcedureVisitRanking : {}", id);
        procedureVisitRankingRepository.delete(id);
    }
}
