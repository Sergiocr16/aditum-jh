package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.BitacoraAccionesService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
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
 * REST controller for managing BitacoraAcciones.
 */
@RestController
@RequestMapping("/api")
public class BitacoraAccionesResource {

    private final Logger log = LoggerFactory.getLogger(BitacoraAccionesResource.class);

    private static final String ENTITY_NAME = "bitacoraAcciones";

    private final BitacoraAccionesService bitacoraAccionesService;

    public BitacoraAccionesResource(BitacoraAccionesService bitacoraAccionesService) {
        this.bitacoraAccionesService = bitacoraAccionesService;
    }

    /**
     * POST  /bitacora-acciones : Create a new bitacoraAcciones.
     *
     * @param bitacoraAccionesDTO the bitacoraAccionesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bitacoraAccionesDTO, or with status 400 (Bad Request) if the bitacoraAcciones has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bitacora-acciones")
    @Timed
    public ResponseEntity<BitacoraAccionesDTO> createBitacoraAcciones(@Valid @RequestBody BitacoraAccionesDTO bitacoraAccionesDTO) throws URISyntaxException {
        log.debug("REST request to save BitacoraAcciones : {}", bitacoraAccionesDTO);
        if (bitacoraAccionesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new commonArea cannot already have an ID")).body(null);
        }
        BitacoraAccionesDTO result = bitacoraAccionesService.save(bitacoraAccionesDTO);
        return ResponseEntity.created(new URI("/api/bitacora-acciones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bitacora-acciones : Updates an existing bitacoraAcciones.
     *
     * @param bitacoraAccionesDTO the bitacoraAccionesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bitacoraAccionesDTO,
     * or with status 400 (Bad Request) if the bitacoraAccionesDTO is not valid,
     * or with status 500 (Internal Server Error) if the bitacoraAccionesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bitacora-acciones")
    @Timed
    public ResponseEntity<BitacoraAccionesDTO> updateBitacoraAcciones(@Valid @RequestBody BitacoraAccionesDTO bitacoraAccionesDTO) throws URISyntaxException {
        log.debug("REST request to update BitacoraAcciones : {}", bitacoraAccionesDTO);
        if (bitacoraAccionesDTO.getId() == null) {
            return createBitacoraAcciones(bitacoraAccionesDTO);
        }
        BitacoraAccionesDTO result = bitacoraAccionesService.save(bitacoraAccionesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bitacoraAccionesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bitacora-acciones : get all the bitacoraAcciones.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bitacoraAcciones in body
     */
    @GetMapping("/bitacora-acciones")
    @Timed
    public ResponseEntity<List<BitacoraAccionesDTO>> getAllBitacoraAcciones(Pageable pageable,Long companyId,int type)throws URISyntaxException {
        log.debug("REST request to get a page of BitacoraAcciones");
        Page<BitacoraAccionesDTO> page = bitacoraAccionesService.findAll(pageable,companyId,type);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bitacora-acciones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bitacora-acciones/:id : get the "id" bitacoraAcciones.
     *
     * @param id the id of the bitacoraAccionesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bitacoraAccionesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bitacora-acciones/{id}")
    @Timed
    public ResponseEntity<BitacoraAccionesDTO> getBitacoraAcciones(@PathVariable Long id) {
        log.debug("REST request to get BitacoraAcciones : {}", id);
        BitacoraAccionesDTO bitacoraAccionesDTO = bitacoraAccionesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bitacoraAccionesDTO));
    }

    /**
     * DELETE  /bitacora-acciones/:id : delete the "id" bitacoraAcciones.
     *
     * @param id the id of the bitacoraAccionesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bitacora-acciones/{id}")
    @Timed
    public ResponseEntity<Void> deleteBitacoraAcciones(@PathVariable Long id) {
        log.debug("REST request to delete BitacoraAcciones : {}", id);
        bitacoraAccionesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
