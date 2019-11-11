package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ProcedureVisits;
import com.lighthouse.aditum.repository.ProcedureVisitsRepository;
import com.lighthouse.aditum.service.dto.ProcedureVisitsDTO;
import com.lighthouse.aditum.service.mapper.ProcedureVisitsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProcedureVisits.
 */
@Service
@Transactional
public class ProcedureVisitsService {

    private final Logger log = LoggerFactory.getLogger(ProcedureVisitsService.class);

    private final ProcedureVisitsRepository procedureVisitsRepository;

    private final ProcedureVisitsMapper procedureVisitsMapper;

    public ProcedureVisitsService(ProcedureVisitsRepository procedureVisitsRepository, ProcedureVisitsMapper procedureVisitsMapper) {
        this.procedureVisitsRepository = procedureVisitsRepository;
        this.procedureVisitsMapper = procedureVisitsMapper;
    }

    /**
     * Save a procedureVisits.
     *
     * @param procedureVisitsDTO the entity to save
     * @return the persisted entity
     */
    public ProcedureVisitsDTO save(ProcedureVisitsDTO procedureVisitsDTO) {
        log.debug("Request to save ProcedureVisits : {}", procedureVisitsDTO);
        ProcedureVisits procedureVisits = procedureVisitsMapper.toEntity(procedureVisitsDTO);
        procedureVisits = procedureVisitsRepository.save(procedureVisits);
        return procedureVisitsMapper.toDto(procedureVisits);
    }

    /**
     * Get all the procedureVisits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProcedureVisitsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcedureVisits");
        return procedureVisitsRepository.findAll(pageable)
            .map(procedureVisitsMapper::toDto);
    }

    /**
     * Get one procedureVisits by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProcedureVisitsDTO findOne(Long id) {
        log.debug("Request to get ProcedureVisits : {}", id);
        ProcedureVisits procedureVisits = procedureVisitsRepository.findOne(id);
        return procedureVisitsMapper.toDto(procedureVisits);
    }

    /**
     * Delete the procedureVisits by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProcedureVisits : {}", id);
        procedureVisitsRepository.delete(id);
    }
}
