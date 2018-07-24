package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.CommonAreaService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.CommonAreaDTO;
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
 * REST controller for managing CommonArea.
 */
@RestController
@RequestMapping("/api")
public class CommonAreaResource {

    private final Logger log = LoggerFactory.getLogger(CommonAreaResource.class);

    private static final String ENTITY_NAME = "commonArea";

    private final CommonAreaService commonAreaService;

    public CommonAreaResource(CommonAreaService commonAreaService) {
        this.commonAreaService = commonAreaService;
    }

    /**
     * POST  /common-areas : Create a new commonArea.
     *
     * @param commonAreaDTO the commonAreaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commonAreaDTO, or with status 400 (Bad Request) if the commonArea has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/common-areas")
    @Timed
    public ResponseEntity<CommonAreaDTO> createCommonArea(@Valid @RequestBody CommonAreaDTO commonAreaDTO) throws URISyntaxException {
        log.debug("REST request to save CommonArea : {}", commonAreaDTO);
        if (commonAreaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new commonArea cannot already have an ID")).body(null);
        }
        CommonAreaDTO result = commonAreaService.save(commonAreaDTO);
        return ResponseEntity.created(new URI("/api/common-areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /common-areas : Updates an existing commonArea.
     *
     * @param commonAreaDTO the commonAreaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commonAreaDTO,
     * or with status 400 (Bad Request) if the commonAreaDTO is not valid,
     * or with status 500 (Internal Server Error) if the commonAreaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/common-areas")
    @Timed
    public ResponseEntity<CommonAreaDTO> updateCommonArea(@Valid @RequestBody CommonAreaDTO commonAreaDTO) throws URISyntaxException {
        log.debug("REST request to update CommonArea : {}", commonAreaDTO);
        if (commonAreaDTO.getId() == null) {
            return createCommonArea(commonAreaDTO);
        }
        CommonAreaDTO result = commonAreaService.save(commonAreaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commonAreaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /common-areas : get all the commonAreas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commonAreas in body
     */
    @GetMapping("/common-areas")
    @Timed
    public ResponseEntity<List<CommonAreaDTO>> getAllCommonAreas(@ApiParam Pageable pageable) throws URISyntaxException  {
        log.debug("REST request to get a page of CommonAreas");
        Page<CommonAreaDTO> page = commonAreaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/common-areas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /common-areas/:id : get the "id" commonArea.
     *
     * @param id the id of the commonAreaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commonAreaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/common-areas/{id}")
    @Timed
    public ResponseEntity<CommonAreaDTO> getCommonArea(@PathVariable Long id) {
        log.debug("REST request to get CommonArea : {}", id);
        CommonAreaDTO commonAreaDTO = commonAreaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commonAreaDTO));
    }

    /**
     * DELETE  /common-areas/:id : delete the "id" commonArea.
     *
     * @param id the id of the commonAreaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/common-areas/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommonArea(@PathVariable Long id) {
        log.debug("REST request to delete CommonArea : {}", id);
        commonAreaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
