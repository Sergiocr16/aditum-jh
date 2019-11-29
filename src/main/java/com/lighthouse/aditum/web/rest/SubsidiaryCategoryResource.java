package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.SubsidiaryCategoryService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.SubsidiaryCategoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SubsidiaryCategory.
 */
@RestController
@RequestMapping("/api")
public class SubsidiaryCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubsidiaryCategoryResource.class);

    private static final String ENTITY_NAME = "subsidiaryCategory";

    private final SubsidiaryCategoryService subsidiaryCategoryService;

    public SubsidiaryCategoryResource(SubsidiaryCategoryService subsidiaryCategoryService) {
        this.subsidiaryCategoryService = subsidiaryCategoryService;
    }

    /**
     * POST  /subsidiary-categories : Create a new subsidiaryCategory.
     *
     * @param subsidiaryCategoryDTO the subsidiaryCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subsidiaryCategoryDTO, or with status 400 (Bad Request) if the subsidiaryCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subsidiary-categories")
    @Timed
    public ResponseEntity<SubsidiaryCategoryDTO> createSubsidiaryCategory(@Valid @RequestBody SubsidiaryCategoryDTO subsidiaryCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save SubsidiaryCategory : {}", subsidiaryCategoryDTO);
        if (subsidiaryCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        SubsidiaryCategoryDTO result = subsidiaryCategoryService.save(subsidiaryCategoryDTO);
        return ResponseEntity.created(new URI("/api/subsidiary-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subsidiary-categories : Updates an existing subsidiaryCategory.
     *
     * @param subsidiaryCategoryDTO the subsidiaryCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subsidiaryCategoryDTO,
     * or with status 400 (Bad Request) if the subsidiaryCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the subsidiaryCategoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/subsidiary-categories")
    @Timed
    public ResponseEntity<SubsidiaryCategoryDTO> updateSubsidiaryCategory(@Valid @RequestBody SubsidiaryCategoryDTO subsidiaryCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update SubsidiaryCategory : {}", subsidiaryCategoryDTO);
        if (subsidiaryCategoryDTO.getId() == null) {
            return createSubsidiaryCategory(subsidiaryCategoryDTO);
        }
        SubsidiaryCategoryDTO result = subsidiaryCategoryService.save(subsidiaryCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subsidiaryCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subsidiary-categories : get all the subsidiaryCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subsidiaryCategories in body
     */
    @GetMapping("/subsidiary-categories")
    @Timed
    public ResponseEntity<List<SubsidiaryCategoryDTO>> getAllSubsidiaryCategories(Pageable pageable)  throws URISyntaxException{
        log.debug("REST request to get a page of SubsidiaryCategories");
        Page<SubsidiaryCategoryDTO> page = subsidiaryCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subsidiary-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /subsidiary-categories/:id : get the "id" subsidiaryCategory.
     *
     * @param id the id of the subsidiaryCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subsidiaryCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/subsidiary-categories/{id}")
    @Timed
    public ResponseEntity<SubsidiaryCategoryDTO> getSubsidiaryCategory(@PathVariable Long id) {
        log.debug("REST request to get SubsidiaryCategory : {}", id);
        SubsidiaryCategoryDTO subsidiaryCategoryDTO = subsidiaryCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subsidiaryCategoryDTO));
    }

    /**
     * DELETE  /subsidiary-categories/:id : delete the "id" subsidiaryCategory.
     *
     * @param id the id of the subsidiaryCategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subsidiary-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubsidiaryCategory(@PathVariable Long id) {
        log.debug("REST request to delete SubsidiaryCategory : {}", id);
        subsidiaryCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
