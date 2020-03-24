package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.RevisionService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.service.RevisionTaskService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.RevisionDTO;
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
 * REST controller for managing Revision.
 */
@RestController
@RequestMapping("/api")
public class RevisionResource {

    private final Logger log = LoggerFactory.getLogger(RevisionResource.class);

    private static final String ENTITY_NAME = "revision";

    private final RevisionService revisionService;

    private final RevisionTaskService revisionTaskService;

    public RevisionResource( RevisionTaskService revisionTaskService,RevisionService revisionService) {
        this.revisionService = revisionService;
        this.revisionTaskService = revisionTaskService;
    }

    /**
     * POST  /revisions : Create a new revision.
     *
     * @param revisionDTO the revisionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new revisionDTO, or with status 400 (Bad Request) if the revision has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/revisions")
    @Timed
    public ResponseEntity<RevisionDTO> createRevision(@RequestBody RevisionDTO revisionDTO) throws URISyntaxException {
        log.debug("REST request to save Revision : {}", revisionDTO);
        if (revisionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new revisionTask cannot already have an ID")).body(null);
        }
        RevisionDTO result = revisionService.save(revisionDTO);
        return ResponseEntity.created(new URI("/api/revisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/revisions/create-from/{revisionConfigId}/{revisionName}")
    @Timed
    public ResponseEntity<RevisionDTO> createFromRevisionConfig(@PathVariable Long revisionConfigId, @PathVariable String revisionName) throws URISyntaxException {
        RevisionDTO result = revisionService.createFromRevisionConfig(revisionConfigId,revisionName);
        return ResponseEntity.created(new URI("/api/revisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /revisions : Updates an existing revision.
     *
     * @param revisionDTO the revisionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated revisionDTO,
     * or with status 400 (Bad Request) if the revisionDTO is not valid,
     * or with status 500 (Internal Server Error) if the revisionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/revisions")
    @Timed
    public ResponseEntity<RevisionDTO> updateRevision(@RequestBody RevisionDTO revisionDTO) throws URISyntaxException {
        log.debug("REST request to update Revision : {}", revisionDTO);
        if (revisionDTO.getId() == null) {
            return createRevision(revisionDTO);
        }
        RevisionDTO result = revisionService.save(revisionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, revisionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /revisions : get all the revisions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of revisions in body
     */
    @GetMapping("/revisions")
    @Timed
    public ResponseEntity<List<RevisionDTO>> getAllRevisions(Pageable pageable, Long companyId) throws URISyntaxException {
        log.debug("REST request to get a page of Revisions");
        Page<RevisionDTO> page = revisionService.findAll(pageable, companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/revisions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /revisions/:id : get the "id" revision.
     *
     * @param id the id of the revisionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the revisionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/revisions/{id}")
    @Timed
    public ResponseEntity<RevisionDTO> getRevision(@PathVariable Long id) {
        log.debug("REST request to get Revision : {}", id);
        RevisionDTO revisionDTO = revisionService.findOne(id);
        revisionDTO.setRevisionTasks(this.revisionTaskService.findAllByRevision(revisionDTO.getId()));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(revisionDTO));
    }

    /**
     * DELETE  /revisions/:id : delete the "id" revision.
     *
     * @param id the id of the revisionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/revisions/{id}")
    @Timed
    public ResponseEntity<Void> deleteRevision(@PathVariable Long id) {
        log.debug("REST request to delete Revision : {}", id);
        revisionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
