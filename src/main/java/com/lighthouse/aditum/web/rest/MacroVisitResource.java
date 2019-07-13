package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.MacroVisitService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.MacroVisitDTO;
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
 * REST controller for managing MacroVisit.
 */
@RestController
@RequestMapping("/api")
public class MacroVisitResource {

    private final Logger log = LoggerFactory.getLogger(MacroVisitResource.class);

    private static final String ENTITY_NAME = "macroVisit";

    private final MacroVisitService macroVisitService;

    public MacroVisitResource(MacroVisitService macroVisitService) {
        this.macroVisitService = macroVisitService;
    }

    /**
     * POST  /macro-visits : Create a new macroVisit.
     *
     * @param macroVisitDTO the macroVisitDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new macroVisitDTO, or with status 400 (Bad Request) if the macroVisit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/macro-visits")
    @Timed
    public ResponseEntity<MacroVisitDTO> createMacroVisit(@Valid @RequestBody MacroVisitDTO macroVisitDTO) throws URISyntaxException {
        log.debug("REST request to save MacroVisit : {}", macroVisitDTO);
        if (macroVisitDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new macroVisit cannot already have an ID")).body(null);
        }
        MacroVisitDTO result = macroVisitService.save(macroVisitDTO);
        return ResponseEntity.created(new URI("/api/macro-visits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /macro-visits : Updates an existing macroVisit.
     *
     * @param macroVisitDTO the macroVisitDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated macroVisitDTO,
     * or with status 400 (Bad Request) if the macroVisitDTO is not valid,
     * or with status 500 (Internal Server Error) if the macroVisitDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/macro-visits")
    @Timed
    public ResponseEntity<MacroVisitDTO> updateMacroVisit(@Valid @RequestBody MacroVisitDTO macroVisitDTO) throws URISyntaxException {
        log.debug("REST request to update MacroVisit : {}", macroVisitDTO);
        if (macroVisitDTO.getId() == null) {
            return createMacroVisit(macroVisitDTO);
        }
        MacroVisitDTO result = macroVisitService.save(macroVisitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, macroVisitDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /macro-visits : get all the macroVisits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of macroVisits in body
     */
    @GetMapping("/macro-visits")
    @Timed
    public ResponseEntity<List<MacroVisitDTO>> getAllMacroVisits(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of MacroVisits");
        Page<MacroVisitDTO> page = macroVisitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/macro-visits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /macro-visits/:id : get the "id" macroVisit.
     *
     * @param id the id of the macroVisitDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the macroVisitDTO, or with status 404 (Not Found)
     */
    @GetMapping("/macro-visits/{id}")
    @Timed
    public ResponseEntity<MacroVisitDTO> getMacroVisit(@PathVariable Long id) {
        log.debug("REST request to get MacroVisit : {}", id);
        MacroVisitDTO macroVisitDTO = macroVisitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(macroVisitDTO));
    }

    /**
     * DELETE  /macro-visits/:id : delete the "id" macroVisit.
     *
     * @param id the id of the macroVisitDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/macro-visits/{id}")
    @Timed
    public ResponseEntity<Void> deleteMacroVisit(@PathVariable Long id) {
        log.debug("REST request to delete MacroVisit : {}", id);
        macroVisitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
