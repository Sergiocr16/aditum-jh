package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ResolutionCommentsService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ResolutionCommentsDTO;
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
 * REST controller for managing ResolutionComments.
 */
@RestController
@RequestMapping("/api")
public class ResolutionCommentsResource {

    private final Logger log = LoggerFactory.getLogger(ResolutionCommentsResource.class);

    private static final String ENTITY_NAME = "resolutionComments";

    private final ResolutionCommentsService resolutionCommentsService;

    public ResolutionCommentsResource(ResolutionCommentsService resolutionCommentsService) {
        this.resolutionCommentsService = resolutionCommentsService;
    }

    /**
     * POST  /resolution-comments : Create a new resolutionComments.
     *
     * @param resolutionCommentsDTO the resolutionCommentsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resolutionCommentsDTO, or with status 400 (Bad Request) if the resolutionComments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resolution-comments")
    @Timed
    public ResponseEntity<ResolutionCommentsDTO> createResolutionComments(@RequestBody ResolutionCommentsDTO resolutionCommentsDTO) throws URISyntaxException {
        log.debug("REST request to save ResolutionComments : {}", resolutionCommentsDTO);
        if (resolutionCommentsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new resolutionComments cannot already have an ID")).body(null);

        }
        ResolutionCommentsDTO result = resolutionCommentsService.save(resolutionCommentsDTO);
        return ResponseEntity.created(new URI("/api/resolution-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resolution-comments : Updates an existing resolutionComments.
     *
     * @param resolutionCommentsDTO the resolutionCommentsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resolutionCommentsDTO,
     * or with status 400 (Bad Request) if the resolutionCommentsDTO is not valid,
     * or with status 500 (Internal Server Error) if the resolutionCommentsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resolution-comments")
    @Timed
    public ResponseEntity<ResolutionCommentsDTO> updateResolutionComments(@RequestBody ResolutionCommentsDTO resolutionCommentsDTO) throws URISyntaxException {
        log.debug("REST request to update ResolutionComments : {}", resolutionCommentsDTO);
        if (resolutionCommentsDTO.getId() == null) {
            return createResolutionComments(resolutionCommentsDTO);
        }
        ResolutionCommentsDTO result = resolutionCommentsService.save(resolutionCommentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resolutionCommentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resolution-comments : get all the resolutionComments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resolutionComments in body
     */
    @GetMapping("/resolution-comments")
    @Timed
    public ResponseEntity<List<ResolutionCommentsDTO>> getAllResolutionComments(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of ResolutionComments");
        Page<ResolutionCommentsDTO> page = resolutionCommentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resolution-comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resolution-comments/:id : get the "id" resolutionComments.
     *
     * @param id the id of the resolutionCommentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resolutionCommentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/resolution-comments/{id}")
    @Timed
    public ResponseEntity<ResolutionCommentsDTO> getResolutionComments(@PathVariable Long id) {
        log.debug("REST request to get ResolutionComments : {}", id);
        ResolutionCommentsDTO resolutionCommentsDTO = resolutionCommentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(resolutionCommentsDTO));
    }

    /**
     * DELETE  /resolution-comments/:id : delete the "id" resolutionComments.
     *
     * @param id the id of the resolutionCommentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resolution-comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteResolutionComments(@PathVariable Long id) {
        log.debug("REST request to delete ResolutionComments : {}", id);
        resolutionCommentsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
