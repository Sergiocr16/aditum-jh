package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ProcedureVisitRankingService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ProcedureVisitRankingDTO;
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
 * REST controller for managing ProcedureVisitRanking.
 */
@RestController
@RequestMapping("/api")
public class ProcedureVisitRankingResource {

    private final Logger log = LoggerFactory.getLogger(ProcedureVisitRankingResource.class);

    private static final String ENTITY_NAME = "procedureVisitRanking";

    private final ProcedureVisitRankingService procedureVisitRankingService;

    public ProcedureVisitRankingResource(ProcedureVisitRankingService procedureVisitRankingService) {
        this.procedureVisitRankingService = procedureVisitRankingService;
    }

    /**
     * POST  /procedure-visit-rankings : Create a new procedureVisitRanking.
     *
     * @param procedureVisitRankingDTO the procedureVisitRankingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedureVisitRankingDTO, or with status 400 (Bad Request) if the procedureVisitRanking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedure-visit-rankings")
    @Timed
    public ResponseEntity<ProcedureVisitRankingDTO> createProcedureVisitRanking(@RequestBody ProcedureVisitRankingDTO procedureVisitRankingDTO) throws URISyntaxException {
        log.debug("REST request to save ProcedureVisitRanking : {}", procedureVisitRankingDTO);
        if (procedureVisitRankingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        ProcedureVisitRankingDTO result = procedureVisitRankingService.save(procedureVisitRankingDTO);
        return ResponseEntity.created(new URI("/api/procedure-visit-rankings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedure-visit-rankings : Updates an existing procedureVisitRanking.
     *
     * @param procedureVisitRankingDTO the procedureVisitRankingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedureVisitRankingDTO,
     * or with status 400 (Bad Request) if the procedureVisitRankingDTO is not valid,
     * or with status 500 (Internal Server Error) if the procedureVisitRankingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedure-visit-rankings")
    @Timed
    public ResponseEntity<ProcedureVisitRankingDTO> updateProcedureVisitRanking(@RequestBody ProcedureVisitRankingDTO procedureVisitRankingDTO) throws URISyntaxException {
        log.debug("REST request to update ProcedureVisitRanking : {}", procedureVisitRankingDTO);
        if (procedureVisitRankingDTO.getId() == null) {
            return createProcedureVisitRanking(procedureVisitRankingDTO);
        }
        ProcedureVisitRankingDTO result = procedureVisitRankingService.save(procedureVisitRankingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedureVisitRankingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedure-visit-rankings : get all the procedureVisitRankings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedureVisitRankings in body
     */
    @GetMapping("/procedure-visit-rankings")
    @Timed
    public ResponseEntity<List<ProcedureVisitRankingDTO>> getAllProcedureVisitRankings(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of ProcedureVisitRankings");
        Page<ProcedureVisitRankingDTO> page = procedureVisitRankingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedure-visit-rankings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedure-visit-rankings/:id : get the "id" procedureVisitRanking.
     *
     * @param id the id of the procedureVisitRankingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedureVisitRankingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/procedure-visit-rankings/{id}")
    @Timed
    public ResponseEntity<ProcedureVisitRankingDTO> getProcedureVisitRanking(@PathVariable Long id) {
        log.debug("REST request to get ProcedureVisitRanking : {}", id);
        ProcedureVisitRankingDTO procedureVisitRankingDTO = procedureVisitRankingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedureVisitRankingDTO));
    }

    /**
     * DELETE  /procedure-visit-rankings/:id : delete the "id" procedureVisitRanking.
     *
     * @param id the id of the procedureVisitRankingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedure-visit-rankings/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedureVisitRanking(@PathVariable Long id) {
        log.debug("REST request to delete ProcedureVisitRanking : {}", id);
        procedureVisitRankingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
