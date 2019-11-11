package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Procedures;
import com.lighthouse.aditum.repository.ProceduresRepository;
import com.lighthouse.aditum.service.dto.ProceduresDTO;
import com.lighthouse.aditum.service.mapper.ProceduresMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Procedures.
 */
@Service
@Transactional
public class ProceduresService {

    private final Logger log = LoggerFactory.getLogger(ProceduresService.class);

    private final ProceduresRepository proceduresRepository;

    private final ProceduresMapper proceduresMapper;

    public ProceduresService(ProceduresRepository proceduresRepository, ProceduresMapper proceduresMapper) {
        this.proceduresRepository = proceduresRepository;
        this.proceduresMapper = proceduresMapper;
    }

    /**
     * Save a procedures.
     *
     * @param proceduresDTO the entity to save
     * @return the persisted entity
     */
    public ProceduresDTO save(ProceduresDTO proceduresDTO) {
        log.debug("Request to save Procedures : {}", proceduresDTO);
        Procedures procedures = proceduresMapper.toEntity(proceduresDTO);
        procedures = proceduresRepository.save(procedures);
        return proceduresMapper.toDto(procedures);
    }

    /**
     * Get all the procedures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProceduresDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Procedures");
        return proceduresRepository.findAll(pageable)
            .map(proceduresMapper::toDto);
    }

    /**
     * Get one procedures by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProceduresDTO findOne(Long id) {
        log.debug("Request to get Procedures : {}", id);
        Procedures procedures = proceduresRepository.findOne(id);
        return proceduresMapper.toDto(procedures);
    }

    /**
     * Delete the procedures by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Procedures : {}", id);
        proceduresRepository.delete(id);
    }
}
