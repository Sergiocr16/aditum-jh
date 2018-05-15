package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.EgressCategory;
import com.lighthouse.aditum.repository.EgressCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing EgressCategory.
 */
@Service
@Transactional
public class EgressCategoryService {

    private final Logger log = LoggerFactory.getLogger(EgressCategoryService.class);

    private final EgressCategoryRepository egressCategoryRepository;

    public EgressCategoryService(EgressCategoryRepository egressCategoryRepository) {
        this.egressCategoryRepository = egressCategoryRepository;
    }

    /**
     * Save a egressCategory.
     *
     * @param egressCategory the entity to save
     * @return the persisted entity
     */
    public EgressCategory save(EgressCategory egressCategory) {
        log.debug("Request to save EgressCategory : {}", egressCategory);
        return egressCategoryRepository.save(egressCategory);
    }

    /**
     *  Get all the egressCategories.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EgressCategory> findAll() {
        log.debug("Request to get all EgressCategories");
        return egressCategoryRepository.findAll();
    }

    /**
     *  Get one egressCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EgressCategory findOne(Long id) {
        log.debug("Request to get EgressCategory : {}", id);
        return egressCategoryRepository.findOne(id);
    }

    /**
     *  Delete the  egressCategory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EgressCategory : {}", id);
        egressCategoryRepository.delete(id);
    }
}
