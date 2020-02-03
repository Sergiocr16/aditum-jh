package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.RevisionConfigTaskService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.RevisionConfigTaskDTO;
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
 * REST controller for managing RevisionConfigTask.
 */
@RestController
@RequestMapping("/api")
public class RevisionConfigTaskResource {

    private final Logger log = LoggerFactory.getLogger(RevisionConfigTaskResource.class);

    private static final String ENTITY_NAME = "revisionConfigTask";

    private final RevisionConfigTaskService revisionConfigTaskService;

    public RevisionConfigTaskResource(RevisionConfigTaskService revisionConfigTaskService) {
        this.revisionConfigTaskService = revisionConfigTaskService;
    }

    /**
     * POST  /revision-config-tasks : Create a new revisionConfigTask.
     *
     * @param revisionConfigTaskDTO the revisionConfigTaskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new revisionConfigTaskDTO, or with status 400 (Bad Request) if the revisionConfigTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/revision-config-tasks")
    @Timed
    public ResponseEntity<RevisionConfigTaskDTO> createRevisionConfigTask(@RequestBody RevisionConfigTaskDTO revisionConfigTaskDTO) throws URISyntaxException {
        log.debug("REST request to save RevisionConfigTask : {}", revisionConfigTaskDTO);
        if (revisionConfigTaskDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new chapter cannot already have an ID")).body(null);
        }
        RevisionConfigTaskDTO result = revisionConfigTaskService.save(revisionConfigTaskDTO);
        return ResponseEntity.created(new URI("/api/revision-config-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /revision-config-tasks : Updates an existing revisionConfigTask.
     *
     * @param revisionConfigTaskDTO the revisionConfigTaskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated revisionConfigTaskDTO,
     * or with status 400 (Bad Request) if the revisionConfigTaskDTO is not valid,
     * or with status 500 (Internal Server Error) if the revisionConfigTaskDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/revision-config-tasks")
    @Timed
    public ResponseEntity<RevisionConfigTaskDTO> updateRevisionConfigTask(@RequestBody RevisionConfigTaskDTO revisionConfigTaskDTO) throws URISyntaxException {
        log.debug("REST request to update RevisionConfigTask : {}", revisionConfigTaskDTO);
        if (revisionConfigTaskDTO.getId() == null) {
            return createRevisionConfigTask(revisionConfigTaskDTO);
        }
        RevisionConfigTaskDTO result = revisionConfigTaskService.save(revisionConfigTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, revisionConfigTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /revision-config-tasks : get all the revisionConfigTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of revisionConfigTasks in body
     */
    @GetMapping("/revision-config-tasks")
    @Timed
    public ResponseEntity<List<RevisionConfigTaskDTO>> getAllRevisionConfigTasks(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of RevisionConfigTasks");
        Page<RevisionConfigTaskDTO> page = revisionConfigTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/revision-config-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /revision-config-tasks/:id : get the "id" revisionConfigTask.
     *
     * @param id the id of the revisionConfigTaskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the revisionConfigTaskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/revision-config-tasks/{id}")
    @Timed
    public ResponseEntity<RevisionConfigTaskDTO> getRevisionConfigTask(@PathVariable Long id) {
        log.debug("REST request to get RevisionConfigTask : {}", id);
        RevisionConfigTaskDTO revisionConfigTaskDTO = revisionConfigTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(revisionConfigTaskDTO));
    }

    /**
     * DELETE  /revision-config-tasks/:id : delete the "id" revisionConfigTask.
     *
     * @param id the id of the revisionConfigTaskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/revision-config-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteRevisionConfigTask(@PathVariable Long id) {
        log.debug("REST request to delete RevisionConfigTask : {}", id);
        revisionConfigTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
