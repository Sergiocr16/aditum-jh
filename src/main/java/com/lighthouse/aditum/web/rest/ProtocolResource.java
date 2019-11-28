package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ProtocolService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ProtocolDTO;
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
 * REST controller for managing Protocol.
 */
@RestController
@RequestMapping("/api")
public class ProtocolResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolResource.class);

    private static final String ENTITY_NAME = "protocol";

    private final ProtocolService protocolService;

    public ProtocolResource(ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    /**
     * POST  /protocols : Create a new protocol.
     *
     * @param protocolDTO the protocolDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new protocolDTO, or with status 400 (Bad Request) if the protocol has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/protocols")
    @Timed
    public ResponseEntity<ProtocolDTO> createProtocol(@Valid @RequestBody ProtocolDTO protocolDTO) throws URISyntaxException {
        log.debug("REST request to save Protocol : {}", protocolDTO);
        if (protocolDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        ProtocolDTO result = protocolService.save(protocolDTO);
        return ResponseEntity.created(new URI("/api/protocols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /protocols : Updates an existing protocol.
     *
     * @param protocolDTO the protocolDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated protocolDTO,
     * or with status 400 (Bad Request) if the protocolDTO is not valid,
     * or with status 500 (Internal Server Error) if the protocolDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/protocols")
    @Timed
    public ResponseEntity<ProtocolDTO> updateProtocol(@Valid @RequestBody ProtocolDTO protocolDTO) throws URISyntaxException {
        log.debug("REST request to update Protocol : {}", protocolDTO);
        if (protocolDTO.getId() == null) {
            return createProtocol(protocolDTO);
        }
        ProtocolDTO result = protocolService.save(protocolDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, protocolDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /protocols : get all the protocols.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of protocols in body
     */
    @GetMapping("/protocols")
    @Timed
    public ResponseEntity<List<ProtocolDTO>> getAllProtocols(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Protocols");
        Page<ProtocolDTO> page = protocolService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/protocols");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /protocols/:id : get the "id" protocol.
     *
     * @param id the id of the protocolDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the protocolDTO, or with status 404 (Not Found)
     */
    @GetMapping("/protocols/{id}")
    @Timed
    public ResponseEntity<ProtocolDTO> getProtocol(@PathVariable Long id) {
        log.debug("REST request to get Protocol : {}", id);
        ProtocolDTO protocolDTO = protocolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(protocolDTO));
    }

    /**
     * DELETE  /protocols/:id : delete the "id" protocol.
     *
     * @param id the id of the protocolDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/protocols/{id}")
    @Timed
    public ResponseEntity<Void> deleteProtocol(@PathVariable Long id) {
        log.debug("REST request to delete Protocol : {}", id);
        protocolService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
