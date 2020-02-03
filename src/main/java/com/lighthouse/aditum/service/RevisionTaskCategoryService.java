package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.RevisionTaskCategory;
import com.lighthouse.aditum.repository.RevisionTaskCategoryRepository;
import com.lighthouse.aditum.service.dto.RevisionTaskCategoryDTO;
import com.lighthouse.aditum.service.mapper.RevisionTaskCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing RevisionTaskCategory.
 */
@Service
@Transactional
public class RevisionTaskCategoryService {

    private final Logger log = LoggerFactory.getLogger(RevisionTaskCategoryService.class);

    private final RevisionTaskCategoryRepository revisionTaskCategoryRepository;

    private final RevisionTaskCategoryMapper revisionTaskCategoryMapper;

    public RevisionTaskCategoryService(RevisionTaskCategoryRepository revisionTaskCategoryRepository, RevisionTaskCategoryMapper revisionTaskCategoryMapper) {
        this.revisionTaskCategoryRepository = revisionTaskCategoryRepository;
        this.revisionTaskCategoryMapper = revisionTaskCategoryMapper;
    }

    /**
     * Save a revisionTaskCategory.
     *
     * @param revisionTaskCategoryDTO the entity to save
     * @return the persisted entity
     */
    public RevisionTaskCategoryDTO save(RevisionTaskCategoryDTO revisionTaskCategoryDTO) {
        log.debug("Request to save RevisionTaskCategory : {}", revisionTaskCategoryDTO);
        RevisionTaskCategory revisionTaskCategory = revisionTaskCategoryMapper.toEntity(revisionTaskCategoryDTO);
        revisionTaskCategory = revisionTaskCategoryRepository.save(revisionTaskCategory);
        return revisionTaskCategoryMapper.toDto(revisionTaskCategory);
    }

    /**
     * Get all the revisionTaskCategories.
     * <p>
     * //     * @param pageable the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<RevisionTaskCategoryDTO> findAll(Long companyId) {
        log.debug("Request to get all RevisionTaskCategories");
        List<RevisionTaskCategoryDTO> revisionTaskCategoryDTOS = new ArrayList<>();
        revisionTaskCategoryRepository.findAllByCompanyId(companyId).forEach(revisionTaskCategory -> {
            revisionTaskCategoryDTOS.add(revisionTaskCategoryMapper.toDto(revisionTaskCategory));
        });
        return revisionTaskCategoryDTOS;
    }

    /**
     * Get one revisionTaskCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RevisionTaskCategoryDTO findOne(Long id) {
        log.debug("Request to get RevisionTaskCategory : {}", id);
        RevisionTaskCategory revisionTaskCategory = revisionTaskCategoryRepository.findOne(id);
        return revisionTaskCategoryMapper.toDto(revisionTaskCategory);
    }

    /**
     * Delete the revisionTaskCategory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RevisionTaskCategory : {}", id);
        revisionTaskCategoryRepository.delete(id);
    }
}
