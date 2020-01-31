package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Revision;
import com.lighthouse.aditum.domain.RevisionTask;
import com.lighthouse.aditum.domain.RevisionTaskCategory;
import com.lighthouse.aditum.repository.RevisionRepository;
import com.lighthouse.aditum.service.dto.RevisionConfigDTO;
import com.lighthouse.aditum.service.dto.RevisionDTO;
import com.lighthouse.aditum.service.mapper.RevisionMapper;
import com.lighthouse.aditum.service.mapper.RevisionTaskCategoryMapper;
import com.lighthouse.aditum.service.mapper.RevisionTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Revision.
 */
@Service
@Transactional
public class RevisionService {

    private final Logger log = LoggerFactory.getLogger(RevisionService.class);

    private final RevisionRepository revisionRepository;

    private final RevisionMapper revisionMapper;

    private final RevisionConfigService revisionConfigService;

    private final RevisionTaskService revisionTaskService;

    private final RevisionTaskCategoryService revisionTaskCategoryService;

    private final RevisionTaskCategoryMapper revisionTaskCategoryMapper;

    private final RevisionTaskMapper revisionTaskMapper;

    public RevisionService(RevisionTaskMapper revisionTaskMapper, RevisionTaskCategoryMapper revisionTaskCategoryMapper, RevisionTaskCategoryService revisionTaskCategoryService, RevisionTaskService revisionTaskService, RevisionConfigService revisionConfigService, RevisionRepository revisionRepository, RevisionMapper revisionMapper) {
        this.revisionRepository = revisionRepository;
        this.revisionMapper = revisionMapper;
        this.revisionConfigService = revisionConfigService;
        this.revisionTaskService = revisionTaskService;
        this.revisionTaskCategoryMapper = revisionTaskCategoryMapper;
        this.revisionTaskCategoryService = revisionTaskCategoryService;
        this.revisionTaskMapper = revisionTaskMapper;
    }

    /**
     * Save a revision.
     *
     * @param revisionDTO the entity to save
     * @return the persisted entity
     */
    public RevisionDTO save(RevisionDTO revisionDTO) {
        log.debug("Request to save Revision : {}", revisionDTO);
        Revision revision = revisionMapper.toEntity(revisionDTO);
        revision = revisionRepository.save(revision);
        return revisionMapper.toDto(revision);
    }

    public RevisionDTO createFromRevisionConfig(Long revisionConfigId, String revisionName) {
        RevisionConfigDTO revisionConfig = this.revisionConfigService.findOne(revisionConfigId);
        RevisionDTO revisionDTO = new RevisionDTO();
        revisionDTO.setCompanyId(revisionConfig.getCompanyId());
        revisionDTO.setName(revisionName);
        revisionDTO.setStatus(0);
        revisionDTO.setDeleted(0);
        revisionDTO = this.save(revisionDTO);
        RevisionDTO finalRevisionDTO = revisionDTO;
        revisionConfig.getConfigTasks().forEach(revisionConfigTaskDTO -> {
            RevisionTask revisionTask = new RevisionTask();
            revisionTask.setDescription(revisionConfigTaskDTO.getDescription());
            revisionTask.setDone(false);
            revisionTask.setHasObservations(0);
            revisionTask.setRevision(this.revisionMapper.toEntity(finalRevisionDTO));
            revisionTask.setRevisionTaskCategory(this.revisionTaskCategoryMapper.toEntity(this.revisionTaskCategoryService.findOne(revisionConfigTaskDTO.getRevisionTaskCategoryId())));
            this.revisionTaskService.save(this.revisionTaskMapper.toDto(revisionTask));
        });
        return revisionDTO;
    }


    /**
     * Get all the revisions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RevisionDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all Revisions");
        return revisionRepository.findAllByCompanyIdAndDeleted(pageable, companyId, 0)
            .map(revisionMapper::toDto);
    }


    /**
     * Get one revision by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RevisionDTO findOne(Long id) {
        log.debug("Request to get Revision : {}", id);
        Revision revision = revisionRepository.findOne(id);
        return revisionMapper.toDto(revision);
    }

    /**
     * Delete the revision by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Revision : {}", id);
        Revision revision = revisionRepository.findOne(id);
        revision.setDeleted(1);
        revisionRepository.save(revision);
    }
}
