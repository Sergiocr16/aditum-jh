package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ActivosFijosService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ActivosFijosDTO;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
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
 * REST controller for managing ActivosFijos.
 */
@RestController
@RequestMapping("/api")
public class ActivosFijosResource {

    private final Logger log = LoggerFactory.getLogger(ActivosFijosResource.class);

    private static final String ENTITY_NAME = "activosFijos";

    private final ActivosFijosService activosFijosService;

    public ActivosFijosResource(ActivosFijosService activosFijosService) {
        this.activosFijosService = activosFijosService;
    }

    /**
     * POST  /activos-fijos : Create a new activosFijos.
     *
     * @param activosFijosDTO the activosFijosDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activosFijosDTO, or with status 400 (Bad Request) if the activosFijos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activos-fijos")
    @Timed
    public ResponseEntity<ActivosFijosDTO> createActivosFijos(@Valid @RequestBody ActivosFijosDTO activosFijosDTO) throws URISyntaxException {
        log.debug("REST request to save ActivosFijos : {}", activosFijosDTO);

        if (activosFijosDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new activosFijos cannot already have an ID")).body(null);
        }
        ActivosFijosDTO result = activosFijosService.save(activosFijosDTO);
        return ResponseEntity.created(new URI("/api/activos-fijos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activos-fijos : Updates an existing activosFijos.
     *
     * @param activosFijosDTO the activosFijosDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activosFijosDTO,
     * or with status 400 (Bad Request) if the activosFijosDTO is not valid,
     * or with status 500 (Internal Server Error) if the activosFijosDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activos-fijos")
    @Timed
    public ResponseEntity<ActivosFijosDTO> updateActivosFijos(@Valid @RequestBody ActivosFijosDTO activosFijosDTO) throws URISyntaxException {
        log.debug("REST request to update ActivosFijos : {}", activosFijosDTO);
        if (activosFijosDTO.getId() == null) {
            return createActivosFijos(activosFijosDTO);
        }
        ActivosFijosDTO result = activosFijosService.save(activosFijosDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activosFijosDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activos-fijos : get all the activosFijos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activosFijos in body
     */
    @GetMapping("/activos-fijos")
    @Timed
    public ResponseEntity<List<ActivosFijosDTO>> getAllActivosFijos(@ApiParam  Pageable pageable,Long companyId)  throws URISyntaxException{
        log.debug("REST request to get a page of ActivosFijos");
        Page<ActivosFijosDTO> page = activosFijosService.findAll(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activos-fijos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activos-fijos/:id : get the "id" activosFijos.
     *
     * @param id the id of the activosFijosDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activosFijosDTO, or with status 404 (Not Found)
     */
    @GetMapping("/activos-fijos/{id}")
    @Timed
    public ResponseEntity<ActivosFijosDTO> getActivosFijos(@PathVariable Long id) {
        log.debug("REST request to get ActivosFijos : {}", id);
        ActivosFijosDTO activosFijosDTO = activosFijosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(activosFijosDTO));
    }

    /**
     * DELETE  /activos-fijos/:id : delete the "id" activosFijos.
     *
     * @param id the id of the activosFijosDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activos-fijos/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivosFijos(@PathVariable Long id) {
        log.debug("REST request to delete ActivosFijos : {}", id);
        activosFijosService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
