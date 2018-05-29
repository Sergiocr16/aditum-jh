package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.PresupuestoService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.PresupuestoDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Presupuesto.
 */
@RestController
@RequestMapping("/api")
public class PresupuestoResource {

    private final Logger log = LoggerFactory.getLogger(PresupuestoResource.class);

    private static final String ENTITY_NAME = "presupuesto";

    private final PresupuestoService presupuestoService;

    public PresupuestoResource(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    /**
     * POST  /presupuestos : Create a new presupuesto.
     *
     * @param presupuestoDTO the presupuestoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new presupuestoDTO, or with status 400 (Bad Request) if the presupuesto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/presupuestos")
    @Timed
    public ResponseEntity<PresupuestoDTO> createPresupuesto(@Valid @RequestBody PresupuestoDTO presupuestoDTO) throws URISyntaxException {
        log.debug("REST request to save Presupuesto : {}", presupuestoDTO);
        if (presupuestoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new presupuesto cannot already have an ID")).body(null);
        }
        PresupuestoDTO result = presupuestoService.save(presupuestoDTO);
        return ResponseEntity.created(new URI("/api/presupuestos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /presupuestos : Updates an existing presupuesto.
     *
     * @param presupuestoDTO the presupuestoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated presupuestoDTO,
     * or with status 400 (Bad Request) if the presupuestoDTO is not valid,
     * or with status 500 (Internal Server Error) if the presupuestoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/presupuestos")
    @Timed
    public ResponseEntity<PresupuestoDTO> updatePresupuesto(@Valid @RequestBody PresupuestoDTO presupuestoDTO) throws URISyntaxException {
        log.debug("REST request to update Presupuesto : {}", presupuestoDTO);
        if (presupuestoDTO.getId() == null) {
            return createPresupuesto(presupuestoDTO);
        }
        PresupuestoDTO result = presupuestoService.save(presupuestoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, presupuestoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /presupuestos : get all the presupuestos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of presupuestos in body
     */
    @GetMapping("/presupuestos")
    @Timed
    public List<PresupuestoDTO> getAllPresupuestos(Long companyId) {
        log.debug("REST request to get all Presupuestos");
        return presupuestoService.findAll(companyId);
    }

    /**
     * GET  /presupuestos/:id : get the "id" presupuesto.
     *
     * @param id the id of the presupuestoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the presupuestoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/presupuestos/{id}")
    @Timed
    public ResponseEntity<PresupuestoDTO> getPresupuesto(@PathVariable Long id) {
        log.debug("REST request to get Presupuesto : {}", id);
        PresupuestoDTO presupuestoDTO = presupuestoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(presupuestoDTO));
    }

    /**
     * DELETE  /presupuestos/:id : delete the "id" presupuesto.
     *
     * @param id the id of the presupuestoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/presupuestos/{id}")
    @Timed
    public ResponseEntity<Void> deletePresupuesto(@PathVariable Long id) {
        log.debug("REST request to delete Presupuesto : {}", id);
        presupuestoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
