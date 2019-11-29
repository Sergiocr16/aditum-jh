package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ResolutionService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ResolutionDTO;
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
 * REST controller for managing Resolution.
 */
@RestController
@RequestMapping("/api")
public class ResolutionResource {

    private final Logger log = LoggerFactory.getLogger(ResolutionResource.class);

    private static final String ENTITY_NAME = "resolution";

    private final ResolutionService resolutionService;

    public ResolutionResource(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    /**
     * POST  /resolutions : Create a new resolution.
     *
     * @param resolutionDTO the resolutionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resolutionDTO, or with status 400 (Bad Request) if the resolution has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resolutions")
    @Timed
    public ResponseEntity<ResolutionDTO> createResolution(@Valid @RequestBody ResolutionDTO resolutionDTO) throws URISyntaxException {
        log.debug("REST request to save Resolution : {}", resolutionDTO);
        if (resolutionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new chapter cannot already have an ID")).body(null);

        }
        ResolutionDTO result = resolutionService.save(resolutionDTO);
        return ResponseEntity.created(new URI("/api/resolutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resolutions : Updates an existing resolution.
     *
     * @param resolutionDTO the resolutionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resolutionDTO,
     * or with status 400 (Bad Request) if the resolutionDTO is not valid,
     * or with status 500 (Internal Server Error) if the resolutionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resolutions")
    @Timed
    public ResponseEntity<ResolutionDTO> updateResolution(@Valid @RequestBody ResolutionDTO resolutionDTO) throws URISyntaxException {
        log.debug("REST request to update Resolution : {}", resolutionDTO);
        if (resolutionDTO.getId() == null) {
            return createResolution(resolutionDTO);
        }
        ResolutionDTO result = resolutionService.save(resolutionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resolutionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resolutions : get all the resolutions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resolutions in body
     */
    @GetMapping("/resolutions")
    @Timed
    public ResponseEntity<List<ResolutionDTO>> getAllResolutions(Pageable pageable) throws URISyntaxException{
        log.debug("REST request to get a page of Resolutions");
        Page<ResolutionDTO> page = resolutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resolutions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resolutions/:id : get the "id" resolution.
     *
     * @param id the id of the resolutionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resolutionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/resolutions/{id}")
    @Timed
    public ResponseEntity<ResolutionDTO> getResolution(@PathVariable Long id) {
        log.debug("REST request to get Resolution : {}", id);
        ResolutionDTO resolutionDTO = resolutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(resolutionDTO));
    }

    /**
     * DELETE  /resolutions/:id : delete the "id" resolution.
     *
     * @param id the id of the resolutionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resolutions/{id}")
    @Timed
    public ResponseEntity<Void> deleteResolution(@PathVariable Long id) {
        log.debug("REST request to delete Resolution : {}", id);
        resolutionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
