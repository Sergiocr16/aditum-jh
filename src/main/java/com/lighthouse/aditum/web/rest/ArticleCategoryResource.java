package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ArticleCategoryService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ArticleCategoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ArticleCategory.
 */
@RestController
@RequestMapping("/api")
public class ArticleCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ArticleCategoryResource.class);

    private static final String ENTITY_NAME = "articleCategory";

    private final ArticleCategoryService articleCategoryService;

    public ArticleCategoryResource(ArticleCategoryService articleCategoryService) {
        this.articleCategoryService = articleCategoryService;
    }

    /**
     * POST  /article-categories : Create a new articleCategory.
     *
     * @param articleCategoryDTO the articleCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new articleCategoryDTO, or with status 400 (Bad Request) if the articleCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/article-categories")
    @Timed
    public ResponseEntity<ArticleCategoryDTO> createArticleCategory(@RequestBody ArticleCategoryDTO articleCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save ArticleCategory : {}", articleCategoryDTO);
        if (articleCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);

        }
        ArticleCategoryDTO result = articleCategoryService.save(articleCategoryDTO);
        return ResponseEntity.created(new URI("/api/article-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /article-categories : Updates an existing articleCategory.
     *
     * @param articleCategoryDTO the articleCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated articleCategoryDTO,
     * or with status 400 (Bad Request) if the articleCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the articleCategoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/article-categories")
    @Timed
    public ResponseEntity<ArticleCategoryDTO> updateArticleCategory(@RequestBody ArticleCategoryDTO articleCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update ArticleCategory : {}", articleCategoryDTO);
        if (articleCategoryDTO.getId() == null) {
            return createArticleCategory(articleCategoryDTO);
        }
        ArticleCategoryDTO result = articleCategoryService.save(articleCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, articleCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /article-categories : get all the articleCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of articleCategories in body
     */
    @GetMapping("/article-categories")
    @Timed
    public ResponseEntity<List<ArticleCategoryDTO>> getAllArticleCategories(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of ArticleCategories");
        Page<ArticleCategoryDTO> page = articleCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/article-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /article-categories/:id : get the "id" articleCategory.
     *
     * @param id the id of the articleCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the articleCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/article-categories/{id}")
    @Timed
    public ResponseEntity<ArticleCategoryDTO> getArticleCategory(@PathVariable Long id) {
        log.debug("REST request to get ArticleCategory : {}", id);
        ArticleCategoryDTO articleCategoryDTO = articleCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(articleCategoryDTO));
    }

    /**
     * DELETE  /article-categories/:id : delete the "id" articleCategory.
     *
     * @param id the id of the articleCategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/article-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteArticleCategory(@PathVariable Long id) {
        log.debug("REST request to delete ArticleCategory : {}", id);
        articleCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
