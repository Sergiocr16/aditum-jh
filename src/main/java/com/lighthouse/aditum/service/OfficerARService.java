package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.OfficerAR;
import com.lighthouse.aditum.repository.OfficerARRepository;
import com.lighthouse.aditum.service.dto.OfficerARDTO;
import com.lighthouse.aditum.service.mapper.OfficerARMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing OfficerAR.
 */
@Service
@Transactional
public class OfficerARService {

    private final Logger log = LoggerFactory.getLogger(OfficerARService.class);

    private final OfficerARRepository officerARRepository;

    private final OfficerARMapper officerARMapper;

    public OfficerARService(OfficerARRepository officerARRepository, OfficerARMapper officerARMapper) {
        this.officerARRepository = officerARRepository;
        this.officerARMapper = officerARMapper;
    }

    /**
     * Save a officerAR.
     *
     * @param officerARDTO the entity to save
     * @return the persisted entity
     */
    public OfficerARDTO save(OfficerARDTO officerARDTO) {
        log.debug("Request to save OfficerAR : {}", officerARDTO);
        OfficerAR officerAR = officerARMapper.toEntity(officerARDTO);
        officerAR = officerARRepository.save(officerAR);
        return officerARMapper.toDto(officerAR);
    }

    /**
     * Get all the officerARS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OfficerARDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OfficerARS");
        return officerARRepository.findAll(pageable)
            .map(officerARMapper::toDto);
    }

    /**
     * Get one officerAR by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OfficerARDTO findOne(Long id) {
        log.debug("Request to get OfficerAR : {}", id);
        OfficerAR officerAR = officerARRepository.findOneWithEagerRelationships(id);
        return officerARMapper.toDto(officerAR);
    }

    /**
     * Delete the officerAR by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OfficerAR : {}", id);
        officerARRepository.delete(id);
    }
}
