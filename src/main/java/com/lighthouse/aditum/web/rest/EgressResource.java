package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.EgressService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.EgressDTO;
import io.swagger.annotations.ApiParam;
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
 * REST controller for managing Egress.
 */
@RestController
@RequestMapping("/api")
public class EgressResource {

    private final Logger log = LoggerFactory.getLogger(EgressResource.class);

    private static final String ENTITY_NAME = "egress";

    private final EgressService egressService;

    public EgressResource(EgressService egressService) {
        this.egressService = egressService;
    }

    /**
     * POST  /egresses : Create a new egress.
     *
     * @param egressDTO the egressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new egressDTO, or with status 400 (Bad Request) if the egress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/egresses")
    @Timed
    public ResponseEntity<EgressDTO> createEgress(@Valid @RequestBody EgressDTO egressDTO) throws URISyntaxException {
        log.debug("REST request to save Egress : {}", egressDTO);
        if (egressDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new egress cannot already have an ID")).body(null);
        }
        EgressDTO result = egressService.save(egressDTO);
        return ResponseEntity.created(new URI("/api/egresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /egresses : Updates an existing egress.
     *
     * @param egressDTO the egressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated egressDTO,
     * or with status 400 (Bad Request) if the egressDTO is not valid,
     * or with status 500 (Internal Server Error) if the egressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/egresses")
    @Timed
    public ResponseEntity<EgressDTO> updateEgress(@Valid @RequestBody EgressDTO egressDTO) throws URISyntaxException {
        log.debug("REST request to update Egress : {}", egressDTO);
        if (egressDTO.getId() == null) {
            return createEgress(egressDTO);
        }
        EgressDTO result = egressService.save(egressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, egressDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /egresses : get all the egresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of egresses in body
     */
    @GetMapping("/egresses")
    @Timed
    public ResponseEntity<List<EgressDTO>> getAllEgresses(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Egresses");
        Page<EgressDTO> page = egressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/egresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /egresses/:id : get the "id" egress.
     *
     * @param id the id of the egressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the egressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/egresses/{id}")
    @Timed
    public ResponseEntity<EgressDTO> getEgress(@PathVariable Long id) {
        log.debug("REST request to get Egress : {}", id);
        EgressDTO egressDTO = egressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(egressDTO));
    }

    /**
     * DELETE  /egresses/:id : delete the "id" egress.
     *
     * @param id the id of the egressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/egresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteEgress(@PathVariable Long id) {
        log.debug("REST request to delete Egress : {}", id);
        egressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
