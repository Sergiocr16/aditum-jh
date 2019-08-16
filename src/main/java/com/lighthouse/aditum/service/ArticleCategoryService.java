package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ArticleCategory;
import com.lighthouse.aditum.repository.ArticleCategoryRepository;
import com.lighthouse.aditum.service.dto.ArticleCategoryDTO;
import com.lighthouse.aditum.service.mapper.ArticleCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ArticleCategory.
 */
@Service
@Transactional
public class ArticleCategoryService {

    private final Logger log = LoggerFactory.getLogger(ArticleCategoryService.class);

    private final ArticleCategoryRepository articleCategoryRepository;

    private final ArticleCategoryMapper articleCategoryMapper;

    public ArticleCategoryService(ArticleCategoryRepository articleCategoryRepository, ArticleCategoryMapper articleCategoryMapper) {
        this.articleCategoryRepository = articleCategoryRepository;
        this.articleCategoryMapper = articleCategoryMapper;
    }

    /**
     * Save a articleCategory.
     *
     * @param articleCategoryDTO the entity to save
     * @return the persisted entity
     */
    public ArticleCategoryDTO save(ArticleCategoryDTO articleCategoryDTO) {
        log.debug("Request to save ArticleCategory : {}", articleCategoryDTO);
        ArticleCategory articleCategory = articleCategoryMapper.toEntity(articleCategoryDTO);
        articleCategory = articleCategoryRepository.save(articleCategory);
        return articleCategoryMapper.toDto(articleCategory);
    }

    /**
     * Get all the articleCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ArticleCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArticleCategories");
        return articleCategoryRepository.findAll(pageable)
            .map(articleCategoryMapper::toDto);
    }

    /**
     * Get one articleCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ArticleCategoryDTO findOne(Long id) {
        log.debug("Request to get ArticleCategory : {}", id);
        ArticleCategory articleCategory = articleCategoryRepository.findOne(id);
        return articleCategoryMapper.toDto(articleCategory);
    }

    /**
     * Delete the articleCategory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ArticleCategory : {}", id);
        articleCategoryRepository.delete(id);
    }
}
