package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.OfficerARService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.OfficerARDTO;
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
 * REST controller for managing OfficerAR.
 */
@RestController
@RequestMapping("/api")
public class OfficerARResource {

    private final Logger log = LoggerFactory.getLogger(OfficerARResource.class);

    private static final String ENTITY_NAME = "officerAR";

    private final OfficerARService officerARService;

    public OfficerARResource(OfficerARService officerARService) {
        this.officerARService = officerARService;
    }

    /**
     * POST  /officer-ars : Create a new officerAR.
     *
     * @param officerARDTO the officerARDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new officerARDTO, or with status 400 (Bad Request) if the officerAR has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/officer-ars")
    @Timed
    public ResponseEntity<OfficerARDTO> createOfficerAR(@RequestBody OfficerARDTO officerARDTO) throws URISyntaxException {
        log.debug("REST request to save OfficerAR : {}", officerARDTO);
        if (officerARDTO.getId() != null) {
//            throw new BadRequestAlertException("A new officerAR cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfficerARDTO result = officerARService.save(officerARDTO);
        return ResponseEntity.created(new URI("/api/officer-ars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /officer-ars : Updates an existing officerAR.
     *
     * @param officerARDTO the officerARDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated officerARDTO,
     * or with status 400 (Bad Request) if the officerARDTO is not valid,
     * or with status 500 (Internal Server Error) if the officerARDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/officer-ars")
    @Timed
    public ResponseEntity<OfficerARDTO> updateOfficerAR(@RequestBody OfficerARDTO officerARDTO) throws URISyntaxException {
        log.debug("REST request to update OfficerAR : {}", officerARDTO);
        if (officerARDTO.getId() == null) {
            return createOfficerAR(officerARDTO);
        }
        OfficerARDTO result = officerARService.save(officerARDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, officerARDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /officer-ars : get all the officerARS.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of officerARS in body
     */
    @GetMapping("/officer-ars")
    @Timed
    public ResponseEntity<List<OfficerARDTO>> getAllOfficerARS(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of OfficerARS");
        Page<OfficerARDTO> page = officerARService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/officer-ars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /officer-ars/:id : get the "id" officerAR.
     *
     * @param id the id of the officerARDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the officerARDTO, or with status 404 (Not Found)
     */
    @GetMapping("/officer-ars/{id}")
    @Timed
    public ResponseEntity<OfficerARDTO> getOfficerAR(@PathVariable Long id) {
        log.debug("REST request to get OfficerAR : {}", id);
        OfficerARDTO officerARDTO = officerARService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(officerARDTO));
    }

    /**
     * DELETE  /officer-ars/:id : delete the "id" officerAR.
     *
     * @param id the id of the officerARDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/officer-ars/{id}")
    @Timed
    public ResponseEntity<Void> deleteOfficerAR(@PathVariable Long id) {
        log.debug("REST request to delete OfficerAR : {}", id);
        officerARService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
