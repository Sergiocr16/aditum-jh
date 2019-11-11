package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ProcedureCommentsService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ProcedureCommentsDTO;
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
 * REST controller for managing ProcedureComments.
 */
@RestController
@RequestMapping("/api")
public class ProcedureCommentsResource {

    private final Logger log = LoggerFactory.getLogger(ProcedureCommentsResource.class);

    private static final String ENTITY_NAME = "procedureComments";

    private final ProcedureCommentsService procedureCommentsService;

    public ProcedureCommentsResource(ProcedureCommentsService procedureCommentsService) {
        this.procedureCommentsService = procedureCommentsService;
    }

    /**
     * POST  /procedure-comments : Create a new procedureComments.
     *
     * @param procedureCommentsDTO the procedureCommentsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedureCommentsDTO, or with status 400 (Bad Request) if the procedureComments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedure-comments")
    @Timed
    public ResponseEntity<ProcedureCommentsDTO> createProcedureComments(@Valid @RequestBody ProcedureCommentsDTO procedureCommentsDTO) throws URISyntaxException {
        log.debug("REST request to save ProcedureComments : {}", procedureCommentsDTO);
        if (procedureCommentsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        ProcedureCommentsDTO result = procedureCommentsService.save(procedureCommentsDTO);
        return ResponseEntity.created(new URI("/api/procedure-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedure-comments : Updates an existing procedureComments.
     *
     * @param procedureCommentsDTO the procedureCommentsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedureCommentsDTO,
     * or with status 400 (Bad Request) if the procedureCommentsDTO is not valid,
     * or with status 500 (Internal Server Error) if the procedureCommentsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedure-comments")
    @Timed
    public ResponseEntity<ProcedureCommentsDTO> updateProcedureComments(@Valid @RequestBody ProcedureCommentsDTO procedureCommentsDTO) throws URISyntaxException {
        log.debug("REST request to update ProcedureComments : {}", procedureCommentsDTO);
        if (procedureCommentsDTO.getId() == null) {
            return createProcedureComments(procedureCommentsDTO);
        }
        ProcedureCommentsDTO result = procedureCommentsService.save(procedureCommentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedureCommentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedure-comments : get all the procedureComments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedureComments in body
     */
    @GetMapping("/procedure-comments")
    @Timed
    public ResponseEntity<List<ProcedureCommentsDTO>> getAllProcedureComments(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of ProcedureComments");
        Page<ProcedureCommentsDTO> page = procedureCommentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedure-comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedure-comments/:id : get the "id" procedureComments.
     *
     * @param id the id of the procedureCommentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedureCommentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/procedure-comments/{id}")
    @Timed
    public ResponseEntity<ProcedureCommentsDTO> getProcedureComments(@PathVariable Long id) {
        log.debug("REST request to get ProcedureComments : {}", id);
        ProcedureCommentsDTO procedureCommentsDTO = procedureCommentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedureCommentsDTO));
    }

    /**
     * DELETE  /procedure-comments/:id : delete the "id" procedureComments.
     *
     * @param id the id of the procedureCommentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedure-comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedureComments(@PathVariable Long id) {
        log.debug("REST request to delete ProcedureComments : {}", id);
        procedureCommentsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
