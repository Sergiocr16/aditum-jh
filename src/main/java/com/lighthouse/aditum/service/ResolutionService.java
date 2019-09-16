package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Resolution;
import com.lighthouse.aditum.repository.ResolutionRepository;
import com.lighthouse.aditum.service.dto.ResolutionDTO;
import com.lighthouse.aditum.service.mapper.ResolutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Resolution.
 */
@Service
@Transactional
public class ResolutionService {

    private final Logger log = LoggerFactory.getLogger(ResolutionService.class);

    private final ResolutionRepository resolutionRepository;

    private final ResolutionMapper resolutionMapper;

    public ResolutionService(ResolutionRepository resolutionRepository, ResolutionMapper resolutionMapper) {
        this.resolutionRepository = resolutionRepository;
        this.resolutionMapper = resolutionMapper;
    }

    /**
     * Save a resolution.
     *
     * @param resolutionDTO the entity to save
     * @return the persisted entity
     */
    public ResolutionDTO save(ResolutionDTO resolutionDTO) {
        log.debug("Request to save Resolution : {}", resolutionDTO);
        Resolution resolution = resolutionMapper.toEntity(resolutionDTO);
        resolution = resolutionRepository.save(resolution);
        return resolutionMapper.toDto(resolution);
    }

    /**
     * Get all the resolutions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ResolutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Resolutions");
        return resolutionRepository.findAll(pageable)
            .map(resolutionMapper::toDto);
    }

    /**
     * Get one resolution by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ResolutionDTO findOne(Long id) {
        log.debug("Request to get Resolution : {}", id);
        Resolution resolution = resolutionRepository.findOneWithEagerRelationships(id);
        return resolutionMapper.toDto(resolution);
    }

    /**
     * Delete the resolution by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Resolution : {}", id);
        resolutionRepository.delete(id);
    }
}
