package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ProcedureComments;
import com.lighthouse.aditum.repository.ProcedureCommentsRepository;
import com.lighthouse.aditum.service.dto.ProcedureCommentsDTO;
import com.lighthouse.aditum.service.mapper.ProcedureCommentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProcedureComments.
 */
@Service
@Transactional
public class ProcedureCommentsService {

    private final Logger log = LoggerFactory.getLogger(ProcedureCommentsService.class);

    private final ProcedureCommentsRepository procedureCommentsRepository;

    private final ProcedureCommentsMapper procedureCommentsMapper;

    public ProcedureCommentsService(ProcedureCommentsRepository procedureCommentsRepository, ProcedureCommentsMapper procedureCommentsMapper) {
        this.procedureCommentsRepository = procedureCommentsRepository;
        this.procedureCommentsMapper = procedureCommentsMapper;
    }

    /**
     * Save a procedureComments.
     *
     * @param procedureCommentsDTO the entity to save
     * @return the persisted entity
     */
    public ProcedureCommentsDTO save(ProcedureCommentsDTO procedureCommentsDTO) {
        log.debug("Request to save ProcedureComments : {}", procedureCommentsDTO);
        ProcedureComments procedureComments = procedureCommentsMapper.toEntity(procedureCommentsDTO);
        procedureComments = procedureCommentsRepository.save(procedureComments);
        return procedureCommentsMapper.toDto(procedureComments);
    }

    /**
     * Get all the procedureComments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProcedureCommentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcedureComments");
        return procedureCommentsRepository.findAll(pageable)
            .map(procedureCommentsMapper::toDto);
    }

    /**
     * Get one procedureComments by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProcedureCommentsDTO findOne(Long id) {
        log.debug("Request to get ProcedureComments : {}", id);
        ProcedureComments procedureComments = procedureCommentsRepository.findOne(id);
        return procedureCommentsMapper.toDto(procedureComments);
    }

    /**
     * Delete the procedureComments by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProcedureComments : {}", id);
        procedureCommentsRepository.delete(id);
    }
}
