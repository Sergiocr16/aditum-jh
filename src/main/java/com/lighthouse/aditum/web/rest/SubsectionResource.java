package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.SubsectionService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.SubsectionDTO;
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
 * REST controller for managing Subsection.
 */
@RestController
@RequestMapping("/api")
public class SubsectionResource {

    private final Logger log = LoggerFactory.getLogger(SubsectionResource.class);

    private static final String ENTITY_NAME = "subsection";

    private final SubsectionService subsectionService;

    public SubsectionResource(SubsectionService subsectionService) {
        this.subsectionService = subsectionService;
    }

    /**
     * POST  /subsections : Create a new subsection.
     *
     * @param subsectionDTO the subsectionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subsectionDTO, or with status 400 (Bad Request) if the subsection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subsections")
    @Timed
    public ResponseEntity<SubsectionDTO> createSubsection(@RequestBody SubsectionDTO subsectionDTO) throws URISyntaxException {
        log.debug("REST request to save Subsection : {}", subsectionDTO);
        if (subsectionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new subsection cannot already have an ID")).body(null);

        }
        SubsectionDTO result = subsectionService.save(subsectionDTO);
        return ResponseEntity.created(new URI("/api/subsections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subsections : Updates an existing subsection.
     *
     * @param subsectionDTO the subsectionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subsectionDTO,
     * or with status 400 (Bad Request) if the subsectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the subsectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/subsections")
    @Timed
    public ResponseEntity<SubsectionDTO> updateSubsection(@RequestBody SubsectionDTO subsectionDTO) throws URISyntaxException {
        log.debug("REST request to update Subsection : {}", subsectionDTO);
        if (subsectionDTO.getId() == null) {
            return createSubsection(subsectionDTO);
        }
        SubsectionDTO result = subsectionService.save(subsectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subsectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subsections : get all the subsections.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subsections in body
     */
    @GetMapping("/subsections")
    @Timed
    public ResponseEntity<List<SubsectionDTO>> getAllSubsections(Pageable pageable, Long articleId) throws URISyntaxException  {
        log.debug("REST request to get a page of Subsections");
        Page<SubsectionDTO> page = subsectionService.findAll(pageable,articleId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subsections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /subsections/:id : get the "id" subsection.
     *
     * @param id the id of the subsectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subsectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/subsections/{id}")
    @Timed
    public ResponseEntity<SubsectionDTO> getSubsection(@PathVariable Long id) {
        log.debug("REST request to get Subsection : {}", id);
        SubsectionDTO subsectionDTO = subsectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subsectionDTO));
    }

    /**
     * DELETE  /subsections/:id : delete the "id" subsection.
     *
     * @param id the id of the subsectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subsections/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubsection(@PathVariable Long id) {
        log.debug("REST request to delete Subsection : {}", id);
        subsectionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
