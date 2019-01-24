package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.EgressCategoryService;
import com.lighthouse.aditum.service.dto.EgressDTO;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.EgressCategoryDTO;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
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

import java.util.ArrayList;
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
     * @param egressCategoryDTO the egressCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new egressCategoryDTO, or with status 400 (Bad Request) if the egressCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/egress-categories")
    @Timed
    public ResponseEntity<EgressCategoryDTO> createEgressCategory(@Valid @RequestBody EgressCategoryDTO egressCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save EgressCategory : {}", egressCategoryDTO);
        if (egressCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new egressCategory cannot already have an ID")).body(null);
        }
        EgressCategoryDTO result = egressCategoryService.save(egressCategoryDTO);
        return ResponseEntity.created(new URI("/api/egress-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /egress-categories : Updates an existing egressCategory.
     *
     * @param egressCategoryDTO the egressCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated egressCategoryDTO,
     * or with status 400 (Bad Request) if the egressCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the egressCategoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/egress-categories")
    @Timed
    public ResponseEntity<EgressCategoryDTO> updateEgressCategory(@Valid @RequestBody EgressCategoryDTO egressCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update EgressCategory : {}", egressCategoryDTO);
        if (egressCategoryDTO.getId() == null) {
            return createEgressCategory(egressCategoryDTO);
        }
        EgressCategoryDTO result = egressCategoryService.update(egressCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, egressCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /egress-categories : get all the egressCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of egressCategories in body
     */

    @GetMapping("/egress-categories")
    @Timed
    public List<EgressCategoryDTO> getAllEgresses(@ApiParam Pageable pageable, Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Egresses");
        Page<EgressCategoryDTO> page = egressCategoryService.findAll(pageable,companyId);
        List<EgressCategoryDTO> egressCategories = new ArrayList<>();
        for (int i = 0; i <page.getContent().size(); i++) {
            if(!page.getContent().get(i).getGroup().equals("Otros gastos")  && !page.getContent().get(i).getCategory().equals("Devolución de dinero") ){
                egressCategories.add(page.getContent().get(i));
            }
        }

        return egressCategories;
    }
    @GetMapping("/egress-categories/allCategoriesIncludingDevolution/{companyId}")
    @Timed
    public ResponseEntity<List<EgressCategoryDTO>> allCategoriesIncludingDevolution(@ApiParam Pageable pageable,@PathVariable Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Egresses");
        Page<EgressCategoryDTO> page = egressCategoryService.findAll(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/egresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /egress-categories/:id : get the "id" egressCategory.
     *
     * @param id the id of the egressCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the egressCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/egress-categories/{id}")
    @Timed
    public ResponseEntity<EgressCategoryDTO> getEgressCategory(@PathVariable Long id) {
        log.debug("REST request to get EgressCategory : {}", id);
        EgressCategoryDTO egressCategoryDTO = egressCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(egressCategoryDTO));
    }

    /**
     * DELETE  /egress-categories/:id : delete the "id" egressCategory.
     *
     * @param id the id of the egressCategoryDTO to delete
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
