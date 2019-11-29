package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.SoporteService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.SoporteDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Soporte.
 */
@RestController
@RequestMapping("/api")
public class SoporteResource {

    private final Logger log = LoggerFactory.getLogger(SoporteResource.class);

    private static final String ENTITY_NAME = "soporte";

    private final SoporteService soporteService;

    public SoporteResource(SoporteService soporteService) {
        this.soporteService = soporteService;
    }

    /**
     * POST  /soportes : Create a new soporte.
     *
     * @param soporteDTO the soporteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new soporteDTO, or with status 400 (Bad Request) if the soporte has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/soportes")
    @Timed
    public ResponseEntity<SoporteDTO> createSoporte(@RequestBody SoporteDTO soporteDTO) throws URISyntaxException {
        log.debug("REST request to save Soporte : {}", soporteDTO);
        if (soporteDTO.getId() != null) {
//            throw new BadRequestAlertException("A new soporte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoporteDTO result = soporteService.save(soporteDTO);
        return ResponseEntity.created(new URI("/api/soportes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /soportes : Updates an existing soporte.
     *
     * @param soporteDTO the soporteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated soporteDTO,
     * or with status 400 (Bad Request) if the soporteDTO is not valid,
     * or with status 500 (Internal Server Error) if the soporteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/soportes")
    @Timed
    public ResponseEntity<SoporteDTO> updateSoporte(@RequestBody SoporteDTO soporteDTO) throws URISyntaxException {
        log.debug("REST request to update Soporte : {}", soporteDTO);
        if (soporteDTO.getId() == null) {
            return createSoporte(soporteDTO);
        }
        SoporteDTO result = soporteService.save(soporteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, soporteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /soportes : get all the soportes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of soportes in body
     */
    @GetMapping("/soportes")
    @Timed
    public List<SoporteDTO> getAllSoportes() {
        log.debug("REST request to get all Soportes");
        return soporteService.findAll();
        }

    /**
     * GET  /soportes/:id : get the "id" soporte.
     *
     * @param id the id of the soporteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the soporteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/soportes/{id}")
    @Timed
    public ResponseEntity<SoporteDTO> getSoporte(@PathVariable Long id) {
        log.debug("REST request to get Soporte : {}", id);
        SoporteDTO soporteDTO = soporteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(soporteDTO));
    }

    /**
     * DELETE  /soportes/:id : delete the "id" soporte.
     *
     * @param id the id of the soporteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/soportes/{id}")
    @Timed
    public ResponseEntity<Void> deleteSoporte(@PathVariable Long id) {
        log.debug("REST request to delete Soporte : {}", id);
        soporteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
