package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ResolutionComments;
import com.lighthouse.aditum.repository.ResolutionCommentsRepository;
import com.lighthouse.aditum.service.dto.ResolutionCommentsDTO;
import com.lighthouse.aditum.service.mapper.ResolutionCommentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ResolutionComments.
 */
@Service
@Transactional
public class ResolutionCommentsService {

    private final Logger log = LoggerFactory.getLogger(ResolutionCommentsService.class);

    private final ResolutionCommentsRepository resolutionCommentsRepository;

    private final ResolutionCommentsMapper resolutionCommentsMapper;

    public ResolutionCommentsService(ResolutionCommentsRepository resolutionCommentsRepository, ResolutionCommentsMapper resolutionCommentsMapper) {
        this.resolutionCommentsRepository = resolutionCommentsRepository;
        this.resolutionCommentsMapper = resolutionCommentsMapper;
    }

    /**
     * Save a resolutionComments.
     *
     * @param resolutionCommentsDTO the entity to save
     * @return the persisted entity
     */
    public ResolutionCommentsDTO save(ResolutionCommentsDTO resolutionCommentsDTO) {
        log.debug("Request to save ResolutionComments : {}", resolutionCommentsDTO);
        ResolutionComments resolutionComments = resolutionCommentsMapper.toEntity(resolutionCommentsDTO);
        resolutionComments = resolutionCommentsRepository.save(resolutionComments);
        return resolutionCommentsMapper.toDto(resolutionComments);
    }

    /**
     * Get all the resolutionComments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ResolutionCommentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResolutionComments");
        return resolutionCommentsRepository.findAll(pageable)
            .map(resolutionCommentsMapper::toDto);
    }

    /**
     * Get one resolutionComments by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ResolutionCommentsDTO findOne(Long id) {
        log.debug("Request to get ResolutionComments : {}", id);
        ResolutionComments resolutionComments = resolutionCommentsRepository.findOne(id);
        return resolutionCommentsMapper.toDto(resolutionComments);
    }

    /**
     * Delete the resolutionComments by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ResolutionComments : {}", id);
        resolutionCommentsRepository.delete(id);
    }
}
