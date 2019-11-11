package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ProcedureSteps;
import com.lighthouse.aditum.repository.ProcedureStepsRepository;
import com.lighthouse.aditum.service.dto.ProcedureStepsDTO;
import com.lighthouse.aditum.service.mapper.ProcedureStepsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProcedureSteps.
 */
@Service
@Transactional
public class ProcedureStepsService {

    private final Logger log = LoggerFactory.getLogger(ProcedureStepsService.class);

    private final ProcedureStepsRepository procedureStepsRepository;

    private final ProcedureStepsMapper procedureStepsMapper;

    public ProcedureStepsService(ProcedureStepsRepository procedureStepsRepository, ProcedureStepsMapper procedureStepsMapper) {
        this.procedureStepsRepository = procedureStepsRepository;
        this.procedureStepsMapper = procedureStepsMapper;
    }

    /**
     * Save a procedureSteps.
     *
     * @param procedureStepsDTO the entity to save
     * @return the persisted entity
     */
    public ProcedureStepsDTO save(ProcedureStepsDTO procedureStepsDTO) {
        log.debug("Request to save ProcedureSteps : {}", procedureStepsDTO);
        ProcedureSteps procedureSteps = procedureStepsMapper.toEntity(procedureStepsDTO);
        procedureSteps = procedureStepsRepository.save(procedureSteps);
        return procedureStepsMapper.toDto(procedureSteps);
    }

    /**
     * Get all the procedureSteps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProcedureStepsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcedureSteps");
        return procedureStepsRepository.findAll(pageable)
            .map(procedureStepsMapper::toDto);
    }

    /**
     * Get one procedureSteps by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProcedureStepsDTO findOne(Long id) {
        log.debug("Request to get ProcedureSteps : {}", id);
        ProcedureSteps procedureSteps = procedureStepsRepository.findOne(id);
        return procedureStepsMapper.toDto(procedureSteps);
    }

    /**
     * Delete the procedureSteps by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProcedureSteps : {}", id);
        procedureStepsRepository.delete(id);
    }
}
