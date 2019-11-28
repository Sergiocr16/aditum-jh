package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.SubsidiaryCategory;
import com.lighthouse.aditum.repository.SubsidiaryCategoryRepository;
import com.lighthouse.aditum.service.dto.SubsidiaryCategoryDTO;
import com.lighthouse.aditum.service.mapper.SubsidiaryCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing SubsidiaryCategory.
 */
@Service
@Transactional
public class SubsidiaryCategoryService {

    private final Logger log = LoggerFactory.getLogger(SubsidiaryCategoryService.class);

    private final SubsidiaryCategoryRepository subsidiaryCategoryRepository;

    private final SubsidiaryCategoryMapper subsidiaryCategoryMapper;

    public SubsidiaryCategoryService(SubsidiaryCategoryRepository subsidiaryCategoryRepository, SubsidiaryCategoryMapper subsidiaryCategoryMapper) {
        this.subsidiaryCategoryRepository = subsidiaryCategoryRepository;
        this.subsidiaryCategoryMapper = subsidiaryCategoryMapper;
    }

    /**
     * Save a subsidiaryCategory.
     *
     * @param subsidiaryCategoryDTO the entity to save
     * @return the persisted entity
     */
    public SubsidiaryCategoryDTO save(SubsidiaryCategoryDTO subsidiaryCategoryDTO) {
        log.debug("Request to save SubsidiaryCategory : {}", subsidiaryCategoryDTO);
        SubsidiaryCategory subsidiaryCategory = subsidiaryCategoryMapper.toEntity(subsidiaryCategoryDTO);
        subsidiaryCategory = subsidiaryCategoryRepository.save(subsidiaryCategory);
        return subsidiaryCategoryMapper.toDto(subsidiaryCategory);
    }

    /**
     * Get all the subsidiaryCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SubsidiaryCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubsidiaryCategories");
        return subsidiaryCategoryRepository.findAll(pageable)
            .map(subsidiaryCategoryMapper::toDto);
    }

    /**
     * Get one subsidiaryCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SubsidiaryCategoryDTO findOne(Long id) {
        log.debug("Request to get SubsidiaryCategory : {}", id);
        SubsidiaryCategory subsidiaryCategory = subsidiaryCategoryRepository.findOne(id);
        return subsidiaryCategoryMapper.toDto(subsidiaryCategory);
    }

    /**
     * Delete the subsidiaryCategory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SubsidiaryCategory : {}", id);
        subsidiaryCategoryRepository.delete(id);
    }
}
