package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.RevisionConfigTask;
import com.lighthouse.aditum.repository.RevisionConfigTaskRepository;
import com.lighthouse.aditum.service.dto.RevisionConfigTaskDTO;
import com.lighthouse.aditum.service.mapper.RevisionConfigTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing RevisionConfigTask.
 */
@Service
@Transactional
public class RevisionConfigTaskService {

    private final Logger log = LoggerFactory.getLogger(RevisionConfigTaskService.class);

    private final RevisionConfigTaskRepository revisionConfigTaskRepository;

    private final RevisionConfigTaskMapper revisionConfigTaskMapper;

    public RevisionConfigTaskService(RevisionConfigTaskRepository revisionConfigTaskRepository, RevisionConfigTaskMapper revisionConfigTaskMapper) {
        this.revisionConfigTaskRepository = revisionConfigTaskRepository;
        this.revisionConfigTaskMapper = revisionConfigTaskMapper;
    }

    /**
     * Save a revisionConfigTask.
     *
     * @param revisionConfigTaskDTO the entity to save
     * @return the persisted entity
     */
    public RevisionConfigTaskDTO save(RevisionConfigTaskDTO revisionConfigTaskDTO) {
        log.debug("Request to save RevisionConfigTask : {}", revisionConfigTaskDTO);
        RevisionConfigTask revisionConfigTask = revisionConfigTaskMapper.toEntity(revisionConfigTaskDTO);
        revisionConfigTask = revisionConfigTaskRepository.save(revisionConfigTask);
        return revisionConfigTaskMapper.toDto(revisionConfigTask);
    }

    /**
     * Get all the revisionConfigTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RevisionConfigTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RevisionConfigTasks");
        return revisionConfigTaskRepository.findAll(pageable)
            .map(revisionConfigTaskMapper::toDto);
    }

    /**
     * Get one revisionConfigTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RevisionConfigTaskDTO findOne(Long id) {
        log.debug("Request to get RevisionConfigTask : {}", id);
        RevisionConfigTask revisionConfigTask = revisionConfigTaskRepository.findOne(id);
        return revisionConfigTaskMapper.toDto(revisionConfigTask);
    }

    /**
     * Delete the revisionConfigTask by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RevisionConfigTask : {}", id);
        revisionConfigTaskRepository.delete(id);
    }
}
