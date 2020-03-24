package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.RevisionTaskService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.RevisionTaskDTO;
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
 * REST controller for managing RevisionTask.
 */
@RestController
@RequestMapping("/api")
public class RevisionTaskResource {

    private final Logger log = LoggerFactory.getLogger(RevisionTaskResource.class);

    private static final String ENTITY_NAME = "revisionTask";

    private final RevisionTaskService revisionTaskService;

    public RevisionTaskResource(RevisionTaskService revisionTaskService) {
        this.revisionTaskService = revisionTaskService;
    }

    /**
     * POST  /revision-tasks : Create a new revisionTask.
     *
     * @param revisionTaskDTO the revisionTaskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new revisionTaskDTO, or with status 400 (Bad Request) if the revisionTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/revision-tasks")
    @Timed
    public ResponseEntity<RevisionTaskDTO> createRevisionTask(@RequestBody RevisionTaskDTO revisionTaskDTO) throws URISyntaxException {
        log.debug("REST request to save RevisionTask : {}", revisionTaskDTO);
        if (revisionTaskDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new revisionTask cannot already have an ID")).body(null);
        }
        RevisionTaskDTO result = revisionTaskService.save(revisionTaskDTO);
        return ResponseEntity.created(new URI("/api/revision-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /revision-tasks : Updates an existing revisionTask.
     *
     * @param revisionTaskDTO the revisionTaskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated revisionTaskDTO,
     * or with status 400 (Bad Request) if the revisionTaskDTO is not valid,
     * or with status 500 (Internal Server Error) if the revisionTaskDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/revision-tasks")
    @Timed
    public ResponseEntity<RevisionTaskDTO> updateRevisionTask(@RequestBody RevisionTaskDTO revisionTaskDTO) throws URISyntaxException {
        log.debug("REST request to update RevisionTask : {}", revisionTaskDTO);
        if (revisionTaskDTO.getId() == null) {
            return createRevisionTask(revisionTaskDTO);
        }
        RevisionTaskDTO result = revisionTaskService.save(revisionTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, revisionTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /revision-tasks : get all the revisionTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of revisionTasks in body
     */
    @GetMapping("/revision-tasks")
    @Timed
    public ResponseEntity<List<RevisionTaskDTO>> getAllRevisionTasks(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of RevisionTasks");
        Page<RevisionTaskDTO> page = revisionTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/revision-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /revision-tasks/:id : get the "id" revisionTask.
     *
     * @param id the id of the revisionTaskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the revisionTaskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/revision-tasks/{id}")
    @Timed
    public ResponseEntity<RevisionTaskDTO> getRevisionTask(@PathVariable Long id) {
        log.debug("REST request to get RevisionTask : {}", id);
        RevisionTaskDTO revisionTaskDTO = revisionTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(revisionTaskDTO));
    }

    /**
     * DELETE  /revision-tasks/:id : delete the "id" revisionTask.
     *
     * @param id the id of the revisionTaskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/revision-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteRevisionTask(@PathVariable Long id) {
        log.debug("REST request to delete RevisionTask : {}", id);
        revisionTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
