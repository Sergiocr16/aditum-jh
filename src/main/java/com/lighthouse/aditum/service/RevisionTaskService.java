package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.RevisionTask;
import com.lighthouse.aditum.repository.RevisionTaskRepository;
import com.lighthouse.aditum.service.dto.RevisionTaskDTO;
import com.lighthouse.aditum.service.mapper.RevisionTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing RevisionTask.
 */
@Service
@Transactional
public class RevisionTaskService {

    private final Logger log = LoggerFactory.getLogger(RevisionTaskService.class);

    private final RevisionTaskRepository revisionTaskRepository;

    private final RevisionTaskMapper revisionTaskMapper;

    public RevisionTaskService(RevisionTaskRepository revisionTaskRepository, RevisionTaskMapper revisionTaskMapper) {
        this.revisionTaskRepository = revisionTaskRepository;
        this.revisionTaskMapper = revisionTaskMapper;
    }

    /**
     * Save a revisionTask.
     *
     * @param revisionTaskDTO the entity to save
     * @return the persisted entity
     */
    public RevisionTaskDTO save(RevisionTaskDTO revisionTaskDTO) {
        log.debug("Request to save RevisionTask : {}", revisionTaskDTO);
        RevisionTask revisionTask = revisionTaskMapper.toEntity(revisionTaskDTO);
        revisionTask = revisionTaskRepository.save(revisionTask);
        return revisionTaskMapper.toDto(revisionTask);
    }

    /**
     * Get all the revisionTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RevisionTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RevisionTasks");
        return revisionTaskRepository.findAll(pageable)
            .map(revisionTaskMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<RevisionTaskDTO> findAllByRevision(Long revisionId) {
        log.debug("Request to get all RevisionTasks");
        List<RevisionTaskDTO> mappedList = new ArrayList<>();
        revisionTaskRepository.findAllByRevisionId(revisionId).forEach(revisionTask -> {
            mappedList.add(this.revisionTaskMapper.toDto(revisionTask));
        });
        return mappedList;
    }

    /**
     * Get one revisionTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RevisionTaskDTO findOne(Long id) {
        log.debug("Request to get RevisionTask : {}", id);
        RevisionTask revisionTask = revisionTaskRepository.findOne(id);
        return revisionTaskMapper.toDto(revisionTask);
    }

    /**
     * Delete the revisionTask by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RevisionTask : {}", id);
        revisionTaskRepository.delete(id);
    }
}
