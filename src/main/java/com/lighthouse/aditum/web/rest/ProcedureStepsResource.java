package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ProcedureStepsService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ProcedureStepsDTO;
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
 * REST controller for managing ProcedureSteps.
 */
@RestController
@RequestMapping("/api")
public class ProcedureStepsResource {

    private final Logger log = LoggerFactory.getLogger(ProcedureStepsResource.class);

    private static final String ENTITY_NAME = "procedureSteps";

    private final ProcedureStepsService procedureStepsService;

    public ProcedureStepsResource(ProcedureStepsService procedureStepsService) {
        this.procedureStepsService = procedureStepsService;
    }

    /**
     * POST  /procedure-steps : Create a new procedureSteps.
     *
     * @param procedureStepsDTO the procedureStepsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedureStepsDTO, or with status 400 (Bad Request) if the procedureSteps has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedure-steps")
    @Timed
    public ResponseEntity<ProcedureStepsDTO> createProcedureSteps(@Valid @RequestBody ProcedureStepsDTO procedureStepsDTO) throws URISyntaxException {
        log.debug("REST request to save ProcedureSteps : {}", procedureStepsDTO);
        if (procedureStepsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        ProcedureStepsDTO result = procedureStepsService.save(procedureStepsDTO);
        return ResponseEntity.created(new URI("/api/procedure-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedure-steps : Updates an existing procedureSteps.
     *
     * @param procedureStepsDTO the procedureStepsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedureStepsDTO,
     * or with status 400 (Bad Request) if the procedureStepsDTO is not valid,
     * or with status 500 (Internal Server Error) if the procedureStepsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedure-steps")
    @Timed
    public ResponseEntity<ProcedureStepsDTO> updateProcedureSteps(@Valid @RequestBody ProcedureStepsDTO procedureStepsDTO) throws URISyntaxException {
        log.debug("REST request to update ProcedureSteps : {}", procedureStepsDTO);
        if (procedureStepsDTO.getId() == null) {
            return createProcedureSteps(procedureStepsDTO);
        }
        ProcedureStepsDTO result = procedureStepsService.save(procedureStepsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedureStepsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedure-steps : get all the procedureSteps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedureSteps in body
     */
    @GetMapping("/procedure-steps")
    @Timed
    public ResponseEntity<List<ProcedureStepsDTO>> getAllProcedureSteps(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of ProcedureSteps");
        Page<ProcedureStepsDTO> page = procedureStepsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedure-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedure-steps/:id : get the "id" procedureSteps.
     *
     * @param id the id of the procedureStepsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedureStepsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/procedure-steps/{id}")
    @Timed
    public ResponseEntity<ProcedureStepsDTO> getProcedureSteps(@PathVariable Long id) {
        log.debug("REST request to get ProcedureSteps : {}", id);
        ProcedureStepsDTO procedureStepsDTO = procedureStepsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedureStepsDTO));
    }

    /**
     * DELETE  /procedure-steps/:id : delete the "id" procedureSteps.
     *
     * @param id the id of the procedureStepsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedure-steps/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedureSteps(@PathVariable Long id) {
        log.debug("REST request to delete ProcedureSteps : {}", id);
        procedureStepsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
