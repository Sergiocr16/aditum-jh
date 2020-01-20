package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.RevisionConfigService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.RevisionConfigDTO;
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
 * REST controller for managing RevisionConfig.
 */
@RestController
@RequestMapping("/api")
public class RevisionConfigResource {

    private final Logger log = LoggerFactory.getLogger(RevisionConfigResource.class);

    private static final String ENTITY_NAME = "revisionConfig";

    private final RevisionConfigService revisionConfigService;

    public RevisionConfigResource(RevisionConfigService revisionConfigService) {
        this.revisionConfigService = revisionConfigService;
    }

    /**
     * POST  /revision-configs : Create a new revisionConfig.
     *
     * @param revisionConfigDTO the revisionConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new revisionConfigDTO, or with status 400 (Bad Request) if the revisionConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/revision-configs")
    @Timed
    public ResponseEntity<RevisionConfigDTO> createRevisionConfig(@RequestBody RevisionConfigDTO revisionConfigDTO) throws URISyntaxException {
        log.debug("REST request to save RevisionConfig : {}", revisionConfigDTO);
        if (revisionConfigDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new chapter cannot already have an ID")).body(null);
        }
        RevisionConfigDTO result = revisionConfigService.save(revisionConfigDTO);
        return ResponseEntity.created(new URI("/api/revision-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /revision-configs : Updates an existing revisionConfig.
     *
     * @param revisionConfigDTO the revisionConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated revisionConfigDTO,
     * or with status 400 (Bad Request) if the revisionConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the revisionConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/revision-configs")
    @Timed
    public ResponseEntity<RevisionConfigDTO> updateRevisionConfig(@RequestBody RevisionConfigDTO revisionConfigDTO) throws URISyntaxException {
        log.debug("REST request to update RevisionConfig : {}", revisionConfigDTO);
        if (revisionConfigDTO.getId() == null) {
            return createRevisionConfig(revisionConfigDTO);
        }
        RevisionConfigDTO result = revisionConfigService.save(revisionConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, revisionConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /revision-configs : get all the revisionConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of revisionConfigs in body
     */
    @GetMapping("/revision-configs")
    @Timed
    public ResponseEntity<List<RevisionConfigDTO>> getAllRevisionConfigs(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of RevisionConfigs");
        Page<RevisionConfigDTO> page = revisionConfigService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/revision-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /revision-configs/:id : get the "id" revisionConfig.
     *
     * @param id the id of the revisionConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the revisionConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/revision-configs/{id}")
    @Timed
    public ResponseEntity<RevisionConfigDTO> getRevisionConfig(@PathVariable Long id) {
        log.debug("REST request to get RevisionConfig : {}", id);
        RevisionConfigDTO revisionConfigDTO = revisionConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(revisionConfigDTO));
    }

    /**
     * DELETE  /revision-configs/:id : delete the "id" revisionConfig.
     *
     * @param id the id of the revisionConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/revision-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteRevisionConfig(@PathVariable Long id) {
        log.debug("REST request to delete RevisionConfig : {}", id);
        revisionConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
