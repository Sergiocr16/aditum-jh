package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.SubsidiaryService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.SubsidiaryDTO;
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
 * REST controller for managing Subsidiary.
 */
@RestController
@RequestMapping("/api")
public class SubsidiaryResource {

    private final Logger log = LoggerFactory.getLogger(SubsidiaryResource.class);

    private static final String ENTITY_NAME = "subsidiary";

    private final SubsidiaryService subsidiaryService;

    public SubsidiaryResource(SubsidiaryService subsidiaryService) {
        this.subsidiaryService = subsidiaryService;
    }

    /**
     * POST  /subsidiaries : Create a new subsidiary.
     *
     * @param subsidiaryDTO the subsidiaryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subsidiaryDTO, or with status 400 (Bad Request) if the subsidiary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subsidiaries")
    @Timed
    public ResponseEntity<SubsidiaryDTO> createSubsidiary(@Valid @RequestBody SubsidiaryDTO subsidiaryDTO) throws URISyntaxException {
        log.debug("REST request to save Subsidiary : {}", subsidiaryDTO);
        if (subsidiaryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        SubsidiaryDTO result = subsidiaryService.save(subsidiaryDTO);
        return ResponseEntity.created(new URI("/api/subsidiaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subsidiaries : Updates an existing subsidiary.
     *
     * @param subsidiaryDTO the subsidiaryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subsidiaryDTO,
     * or with status 400 (Bad Request) if the subsidiaryDTO is not valid,
     * or with status 500 (Internal Server Error) if the subsidiaryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/subsidiaries")
    @Timed
    public ResponseEntity<SubsidiaryDTO> updateSubsidiary(@Valid @RequestBody SubsidiaryDTO subsidiaryDTO) throws URISyntaxException {
        log.debug("REST request to update Subsidiary : {}", subsidiaryDTO);
        if (subsidiaryDTO.getId() == null) {
            return createSubsidiary(subsidiaryDTO);
        }
        SubsidiaryDTO result = subsidiaryService.save(subsidiaryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subsidiaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subsidiaries : get all the subsidiaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subsidiaries in body
     */
    @GetMapping("/subsidiaries")
    @Timed
    public ResponseEntity<List<SubsidiaryDTO>> getAllSubsidiaries(Pageable pageable)throws URISyntaxException {
        log.debug("REST request to get a page of Subsidiaries");
        Page<SubsidiaryDTO> page = subsidiaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subsidiaries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /subsidiaries/:id : get the "id" subsidiary.
     *
     * @param id the id of the subsidiaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subsidiaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/subsidiaries/{id}")
    @Timed
    public ResponseEntity<SubsidiaryDTO> getSubsidiary(@PathVariable Long id) {
        log.debug("REST request to get Subsidiary : {}", id);
        SubsidiaryDTO subsidiaryDTO = subsidiaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subsidiaryDTO));
    }

    /**
     * DELETE  /subsidiaries/:id : delete the "id" subsidiary.
     *
     * @param id the id of the subsidiaryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subsidiaries/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubsidiary(@PathVariable Long id) {
        log.debug("REST request to delete Subsidiary : {}", id);
        subsidiaryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
