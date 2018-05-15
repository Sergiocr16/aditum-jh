package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.domain.EgressCategory;
import com.lighthouse.aditum.service.EgressCategoryService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EgressCategory.
 */
@RestController
@RequestMapping("/api")
public class EgressCategoryResource {

    private final Logger log = LoggerFactory.getLogger(EgressCategoryResource.class);

    private static final String ENTITY_NAME = "egressCategory";

    private final EgressCategoryService egressCategoryService;

    public EgressCategoryResource(EgressCategoryService egressCategoryService) {
        this.egressCategoryService = egressCategoryService;
    }

    /**
     * POST  /egress-categories : Create a new egressCategory.
     *
     * @param egressCategory the egressCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new egressCategory, or with status 400 (Bad Request) if the egressCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/egress-categories")
    @Timed
    public ResponseEntity<EgressCategory> createEgressCategory(@Valid @RequestBody EgressCategory egressCategory) throws URISyntaxException {
        log.debug("REST request to save EgressCategory : {}", egressCategory);
        if (egressCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new egressCategory cannot already have an ID")).body(null);
        }
        EgressCategory result = egressCategoryService.save(egressCategory);
        return ResponseEntity.created(new URI("/api/egress-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /egress-categories : Updates an existing egressCategory.
     *
     * @param egressCategory the egressCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated egressCategory,
     * or with status 400 (Bad Request) if the egressCategory is not valid,
     * or with status 500 (Internal Server Error) if the egressCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/egress-categories")
    @Timed
    public ResponseEntity<EgressCategory> updateEgressCategory(@Valid @RequestBody EgressCategory egressCategory) throws URISyntaxException {
        log.debug("REST request to update EgressCategory : {}", egressCategory);
        if (egressCategory.getId() == null) {
            return createEgressCategory(egressCategory);
        }
        EgressCategory result = egressCategoryService.save(egressCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, egressCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /egress-categories : get all the egressCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of egressCategories in body
     */
    @GetMapping("/egress-categories")
    @Timed
    public List<EgressCategory> getAllEgressCategories() {
        log.debug("REST request to get all EgressCategories");
        return egressCategoryService.findAll();
    }

    /**
     * GET  /egress-categories/:id : get the "id" egressCategory.
     *
     * @param id the id of the egressCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the egressCategory, or with status 404 (Not Found)
     */
    @GetMapping("/egress-categories/{id}")
    @Timed
    public ResponseEntity<EgressCategory> getEgressCategory(@PathVariable Long id) {
        log.debug("REST request to get EgressCategory : {}", id);
        EgressCategory egressCategory = egressCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(egressCategory));
    }

    /**
     * DELETE  /egress-categories/:id : delete the "id" egressCategory.
     *
     * @param id the id of the egressCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/egress-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteEgressCategory(@PathVariable Long id) {
        log.debug("REST request to delete EgressCategory : {}", id);
        egressCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
