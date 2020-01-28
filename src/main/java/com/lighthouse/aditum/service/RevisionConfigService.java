package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.RevisionConfig;
import com.lighthouse.aditum.repository.RevisionConfigRepository;
import com.lighthouse.aditum.service.dto.RevisionConfigDTO;
import com.lighthouse.aditum.service.mapper.RevisionConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing RevisionConfig.
 */
@Service
@Transactional
public class RevisionConfigService {

    private final Logger log = LoggerFactory.getLogger(RevisionConfigService.class);

    private final RevisionConfigRepository revisionConfigRepository;

    private final RevisionConfigMapper revisionConfigMapper;

    private final RevisionConfigTaskService revisionConfigTaskService;

    public RevisionConfigService(RevisionConfigTaskService revisionConfigTaskService, RevisionConfigRepository revisionConfigRepository, RevisionConfigMapper revisionConfigMapper) {
        this.revisionConfigRepository = revisionConfigRepository;
        this.revisionConfigMapper = revisionConfigMapper;
        this.revisionConfigTaskService = revisionConfigTaskService;
    }

    /**
     * Save a revisionConfig.
     *
     * @param revisionConfigDTO the entity to save
     * @return the persisted entity
     */
    public RevisionConfigDTO save(RevisionConfigDTO revisionConfigDTO) {
        log.debug("Request to save RevisionConfig : {}", revisionConfigDTO);
        RevisionConfig revisionConfig = revisionConfigMapper.toEntity(revisionConfigDTO);
        revisionConfig = revisionConfigRepository.save(revisionConfig);
        RevisionConfig finalRevisionConfig = revisionConfig;
        revisionConfigDTO.getConfigTasks().forEach(revisionConfigTaskDTO -> {
            revisionConfigTaskDTO.setRevisionConfigId(finalRevisionConfig.getId());
            if (revisionConfigTaskDTO.getDeleted() == 1) {
                if (revisionConfigTaskDTO.getId() != null) {
                    this.revisionConfigTaskService.delete(revisionConfigTaskDTO.getId());
                }
            } else {
                this.revisionConfigTaskService.save(revisionConfigTaskDTO);
            }
        });
        return revisionConfigMapper.toDto(revisionConfig);
    }

    /**
     * Get all the revisionConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RevisionConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RevisionConfigs");
        return revisionConfigRepository.findAll(pageable)
            .map(revisionConfigMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<RevisionConfigDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all RevisionConfigs");
        return revisionConfigRepository.findAllByCompanyIdAndDeleted(pageable, companyId, 0)
            .map(revisionConfigMapper::toDto);
    }

    /**
     * Get one revisionConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RevisionConfigDTO findOne(Long id) {
        log.debug("Request to get RevisionConfig : {}", id);
        RevisionConfig revisionConfig = revisionConfigRepository.findOne(id);
        RevisionConfigDTO revisionConfigDTO = revisionConfigMapper.toDto(revisionConfig);
        revisionConfigDTO.setConfigTasks(this.revisionConfigTaskService.findAllByRevisionConfig(revisionConfigDTO.getId()));
        return revisionConfigDTO;
    }

    /**
     * Delete the revisionConfig by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RevisionConfig : {}", id);
        RevisionConfigDTO revisionConfig = this.findOne(id);
        revisionConfig.setDeleted(1);
        this.save(revisionConfig);
    }
}
