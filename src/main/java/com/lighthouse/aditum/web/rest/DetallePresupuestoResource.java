package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.DetallePresupuestoService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.DetallePresupuestoDTO;
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
 * REST controller for managing DetallePresupuesto.
 */
@RestController
@RequestMapping("/api")
public class DetallePresupuestoResource {

    private final Logger log = LoggerFactory.getLogger(DetallePresupuestoResource.class);

    private static final String ENTITY_NAME = "detallePresupuesto";

    private final DetallePresupuestoService detallePresupuestoService;

    public DetallePresupuestoResource(DetallePresupuestoService detallePresupuestoService) {
        this.detallePresupuestoService = detallePresupuestoService;
    }

    /**
     * POST  /detalle-presupuestos : Create a new detallePresupuesto.
     *
     * @param detallePresupuestoDTO the detallePresupuestoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new detallePresupuestoDTO, or with status 400 (Bad Request) if the detallePresupuesto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/detalle-presupuestos")
    @Timed
    public ResponseEntity<DetallePresupuestoDTO> createDetallePresupuesto(@Valid @RequestBody DetallePresupuestoDTO detallePresupuestoDTO) throws URISyntaxException {
        log.debug("REST request to save DetallePresupuesto : {}", detallePresupuestoDTO);
        if (detallePresupuestoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new detallePresupuesto cannot already have an ID")).body(null);
        }
        DetallePresupuestoDTO result = detallePresupuestoService.save(detallePresupuestoDTO);
        return ResponseEntity.created(new URI("/api/detalle-presupuestos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /detalle-presupuestos : Updates an existing detallePresupuesto.
     *
     * @param detallePresupuestoDTO the detallePresupuestoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated detallePresupuestoDTO,
     * or with status 400 (Bad Request) if the detallePresupuestoDTO is not valid,
     * or with status 500 (Internal Server Error) if the detallePresupuestoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/detalle-presupuestos")
    @Timed
    public ResponseEntity<DetallePresupuestoDTO> updateDetallePresupuesto(@Valid @RequestBody DetallePresupuestoDTO detallePresupuestoDTO) throws URISyntaxException {
        log.debug("REST request to update DetallePresupuesto : {}", detallePresupuestoDTO);
        if (detallePresupuestoDTO.getId() == null) {
            return createDetallePresupuesto(detallePresupuestoDTO);
        }
        DetallePresupuestoDTO result = detallePresupuestoService.save(detallePresupuestoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, detallePresupuestoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /detalle-presupuestos : get all the detallePresupuestos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of detallePresupuestos in body
     */
    @GetMapping("/detalle-presupuestos")
    @Timed
    public List<DetallePresupuestoDTO> getAllDetallePresupuestos() {
        log.debug("REST request to get all DetallePresupuestos");
        return detallePresupuestoService.findAll();
    }

    /**
     * GET  /detalle-presupuestos/:id : get the "id" detallePresupuesto.
     *
     * @param id the id of the detallePresupuestoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the detallePresupuestoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/detalle-presupuestos/{id}")
    @Timed
    public ResponseEntity<DetallePresupuestoDTO> getDetallePresupuesto(@PathVariable Long id) {
        log.debug("REST request to get DetallePresupuesto : {}", id);
        DetallePresupuestoDTO detallePresupuestoDTO = detallePresupuestoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(detallePresupuestoDTO));
    }

    /**
     * DELETE  /detalle-presupuestos/:id : delete the "id" detallePresupuesto.
     *
     * @param id the id of the detallePresupuestoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/detalle-presupuestos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDetallePresupuesto(@PathVariable Long id) {
        log.debug("REST request to delete DetallePresupuesto : {}", id);
        detallePresupuestoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
