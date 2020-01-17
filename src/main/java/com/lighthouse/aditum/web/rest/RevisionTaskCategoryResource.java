package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.RevisionTaskCategoryService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.RevisionTaskCategoryDTO;
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
 * REST controller for managing RevisionTaskCategory.
 */
@RestController
@RequestMapping("/api")
public class RevisionTaskCategoryResource {

    private final Logger log = LoggerFactory.getLogger(RevisionTaskCategoryResource.class);

    private static final String ENTITY_NAME = "revisionTaskCategory";

    private final RevisionTaskCategoryService revisionTaskCategoryService;

    public RevisionTaskCategoryResource(RevisionTaskCategoryService revisionTaskCategoryService) {
        this.revisionTaskCategoryService = revisionTaskCategoryService;
    }

    /**
     * POST  /revision-task-categories : Create a new revisionTaskCategory.
     *
     * @param revisionTaskCategoryDTO the revisionTaskCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new revisionTaskCategoryDTO, or with status 400 (Bad Request) if the revisionTaskCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/revision-task-categories")
    @Timed
    public ResponseEntity<RevisionTaskCategoryDTO> createRevisionTaskCategory(@RequestBody RevisionTaskCategoryDTO revisionTaskCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save RevisionTaskCategory : {}", revisionTaskCategoryDTO);
        if (revisionTaskCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new revisionTask cannot already have an ID")).body(null);
        }
        RevisionTaskCategoryDTO result = revisionTaskCategoryService.save(revisionTaskCategoryDTO);
        return ResponseEntity.created(new URI("/api/revision-task-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /revision-task-categories : Updates an existing revisionTaskCategory.
     *
     * @param revisionTaskCategoryDTO the revisionTaskCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated revisionTaskCategoryDTO,
     * or with status 400 (Bad Request) if the revisionTaskCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the revisionTaskCategoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/revision-task-categories")
    @Timed
    public ResponseEntity<RevisionTaskCategoryDTO> updateRevisionTaskCategory(@RequestBody RevisionTaskCategoryDTO revisionTaskCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update RevisionTaskCategory : {}", revisionTaskCategoryDTO);
        if (revisionTaskCategoryDTO.getId() == null) {
            return createRevisionTaskCategory(revisionTaskCategoryDTO);
        }
        RevisionTaskCategoryDTO result = revisionTaskCategoryService.save(revisionTaskCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, revisionTaskCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /revision-task-categories : get all the revisionTaskCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of revisionTaskCategories in body
     */
    @GetMapping("/revision-task-categories")
    @Timed
    public ResponseEntity<List<RevisionTaskCategoryDTO>> getAllRevisionTaskCategories(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of RevisionTaskCategories");
        Page<RevisionTaskCategoryDTO> page = revisionTaskCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/revision-task-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /revision-task-categories/:id : get the "id" revisionTaskCategory.
     *
     * @param id the id of the revisionTaskCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the revisionTaskCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/revision-task-categories/{id}")
    @Timed
    public ResponseEntity<RevisionTaskCategoryDTO> getRevisionTaskCategory(@PathVariable Long id) {
        log.debug("REST request to get RevisionTaskCategory : {}", id);
        RevisionTaskCategoryDTO revisionTaskCategoryDTO = revisionTaskCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(revisionTaskCategoryDTO));
    }

    /**
     * DELETE  /revision-task-categories/:id : delete the "id" revisionTaskCategory.
     *
     * @param id the id of the revisionTaskCategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/revision-task-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteRevisionTaskCategory(@PathVariable Long id) {
        log.debug("REST request to delete RevisionTaskCategory : {}", id);
        revisionTaskCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}