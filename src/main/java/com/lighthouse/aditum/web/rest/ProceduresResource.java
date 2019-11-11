package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ProceduresService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ProceduresDTO;
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
 * REST controller for managing Procedures.
 */
@RestController
@RequestMapping("/api")
public class ProceduresResource {

    private final Logger log = LoggerFactory.getLogger(ProceduresResource.class);

    private static final String ENTITY_NAME = "procedures";

    private final ProceduresService proceduresService;

    public ProceduresResource(ProceduresService proceduresService) {
        this.proceduresService = proceduresService;
    }

    /**
     * POST  /procedures : Create a new procedures.
     *
     * @param proceduresDTO the proceduresDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proceduresDTO, or with status 400 (Bad Request) if the procedures has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedures")
    @Timed
    public ResponseEntity<ProceduresDTO> createProcedures(@Valid @RequestBody ProceduresDTO proceduresDTO) throws URISyntaxException {
        log.debug("REST request to save Procedures : {}", proceduresDTO);
        if (proceduresDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        ProceduresDTO result = proceduresService.save(proceduresDTO);
        return ResponseEntity.created(new URI("/api/procedures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedures : Updates an existing procedures.
     *
     * @param proceduresDTO the proceduresDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proceduresDTO,
     * or with status 400 (Bad Request) if the proceduresDTO is not valid,
     * or with status 500 (Internal Server Error) if the proceduresDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedures")
    @Timed
    public ResponseEntity<ProceduresDTO> updateProcedures(@Valid @RequestBody ProceduresDTO proceduresDTO) throws URISyntaxException {
        log.debug("REST request to update Procedures : {}", proceduresDTO);
        if (proceduresDTO.getId() == null) {
            return createProcedures(proceduresDTO);
        }
        ProceduresDTO result = proceduresService.save(proceduresDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, proceduresDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedures : get all the procedures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedures in body
     */
    @GetMapping("/procedures")
    @Timed
    public ResponseEntity<List<ProceduresDTO>> getAllProcedures(Pageable pageable) throws URISyntaxException  {
        log.debug("REST request to get a page of Procedures");
        Page<ProceduresDTO> page = proceduresService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/proveedors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedures/:id : get the "id" procedures.
     *
     * @param id the id of the proceduresDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proceduresDTO, or with status 404 (Not Found)
     */
    @GetMapping("/procedures/{id}")
    @Timed
    public ResponseEntity<ProceduresDTO> getProcedures(@PathVariable Long id) {
        log.debug("REST request to get Procedures : {}", id);
        ProceduresDTO proceduresDTO = proceduresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(proceduresDTO));
    }

    /**
     * DELETE  /procedures/:id : delete the "id" procedures.
     *
     * @param id the id of the proceduresDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedures/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedures(@PathVariable Long id) {
        log.debug("REST request to delete Procedures : {}", id);
        proceduresService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
