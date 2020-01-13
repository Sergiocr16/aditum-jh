package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Revision;
import com.lighthouse.aditum.repository.RevisionRepository;
import com.lighthouse.aditum.service.dto.RevisionDTO;
import com.lighthouse.aditum.service.mapper.RevisionMapper;
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

    public RevisionService(RevisionRepository revisionRepository, RevisionMapper revisionMapper) {
        this.revisionRepository = revisionRepository;
        this.revisionMapper = revisionMapper;
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

    /**
     * Get all the revisions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RevisionDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all Revisions");
        return revisionRepository.findAllByCompanyId(pageable,companyId)
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
        revisionRepository.delete(id);
    }
}
