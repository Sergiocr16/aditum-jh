package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ProcedureVisitsService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ProcedureVisitsDTO;
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
 * REST controller for managing ProcedureVisits.
 */
@RestController
@RequestMapping("/api")
public class ProcedureVisitsResource {

    private final Logger log = LoggerFactory.getLogger(ProcedureVisitsResource.class);

    private static final String ENTITY_NAME = "procedureVisits";

    private final ProcedureVisitsService procedureVisitsService;

    public ProcedureVisitsResource(ProcedureVisitsService procedureVisitsService) {
        this.procedureVisitsService = procedureVisitsService;
    }

    /**
     * POST  /procedure-visits : Create a new procedureVisits.
     *
     * @param procedureVisitsDTO the procedureVisitsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedureVisitsDTO, or with status 400 (Bad Request) if the procedureVisits has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedure-visits")
    @Timed
    public ResponseEntity<ProcedureVisitsDTO> createProcedureVisits(@RequestBody ProcedureVisitsDTO procedureVisitsDTO) throws URISyntaxException {
        log.debug("REST request to save ProcedureVisits : {}", procedureVisitsDTO);
        if (procedureVisitsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        ProcedureVisitsDTO result = procedureVisitsService.save(procedureVisitsDTO);
        return ResponseEntity.created(new URI("/api/procedure-visits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedure-visits : Updates an existing procedureVisits.
     *
     * @param procedureVisitsDTO the procedureVisitsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedureVisitsDTO,
     * or with status 400 (Bad Request) if the procedureVisitsDTO is not valid,
     * or with status 500 (Internal Server Error) if the procedureVisitsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedure-visits")
    @Timed
    public ResponseEntity<ProcedureVisitsDTO> updateProcedureVisits(@RequestBody ProcedureVisitsDTO procedureVisitsDTO) throws URISyntaxException {
        log.debug("REST request to update ProcedureVisits : {}", procedureVisitsDTO);
        if (procedureVisitsDTO.getId() == null) {
            return createProcedureVisits(procedureVisitsDTO);
        }
        ProcedureVisitsDTO result = procedureVisitsService.save(procedureVisitsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedureVisitsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedure-visits : get all the procedureVisits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedureVisits in body
     */
    @GetMapping("/procedure-visits")
    @Timed
    public ResponseEntity<List<ProcedureVisitsDTO>> getAllProcedureVisits(Pageable pageable)  throws URISyntaxException {
        log.debug("REST request to get a page of ProcedureVisits");
        Page<ProcedureVisitsDTO> page = procedureVisitsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedure-visits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedure-visits/:id : get the "id" procedureVisits.
     *
     * @param id the id of the procedureVisitsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedureVisitsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/procedure-visits/{id}")
    @Timed
    public ResponseEntity<ProcedureVisitsDTO> getProcedureVisits(@PathVariable Long id) {
        log.debug("REST request to get ProcedureVisits : {}", id);
        ProcedureVisitsDTO procedureVisitsDTO = procedureVisitsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedureVisitsDTO));
    }

    /**
     * DELETE  /procedure-visits/:id : delete the "id" procedureVisits.
     *
     * @param id the id of the procedureVisitsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedure-visits/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedureVisits(@PathVariable Long id) {
        log.debug("REST request to delete ProcedureVisits : {}", id);
        procedureVisitsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
