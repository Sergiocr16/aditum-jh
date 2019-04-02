package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.service.TransferenciaService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Transferencia.
 */
@RestController
@RequestMapping("/api")
public class TransferenciaResource {

    private final Logger log = LoggerFactory.getLogger(TransferenciaResource.class);

    private static final String ENTITY_NAME = "transferencia";

    private final TransferenciaService transferenciaService;

    public TransferenciaResource(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    /**
     * POST  /transferencias : Create a new transferencia.
     *
     * @param transferencia the transferencia to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transferencia, or with status 400 (Bad Request) if the transferencia has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transferencias")
    @Timed
    public ResponseEntity<Transferencia> createTransferencia(@Valid @RequestBody Transferencia transferencia) throws URISyntaxException {
        log.debug("REST request to save Transferencia : {}", transferencia);
        if (transferencia.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transferencia cannot already have an ID")).body(null);
        }
        Transferencia result = transferenciaService.save(transferencia);
        return ResponseEntity.created(new URI("/api/transferencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    @GetMapping("/transferencias/between/{initial_time}/{final_time}/incomingTransfer/{accountId}")
    @Timed
    public ResponseEntity<List<Transferencia>> getBetweenDatesByInComingTransfer(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "accountId")  int accountId,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<Transferencia> page = transferenciaService.getBetweenDatesByInComingTransfer(pageable,initial_time,final_time,accountId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transferencia");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/transferencias/between/{initial_time}/{final_time}/outgoingTransfer/{accountId}")
    @Timed
    public ResponseEntity<List<Transferencia>> getBetweenDatesByOutgoingTransfer(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "accountId")  int accountId,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<Transferencia> page = transferenciaService.getBetweenDatesByOutgoingTransfer(pageable,initial_time,final_time,accountId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transferencia");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * PUT  /transferencias : Updates an existing transferencia.
     *
     * @param transferencia the transferencia to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transferencia,
     * or with status 400 (Bad Request) if the transferencia is not valid,
     * or with status 500 (Internal Server Error) if the transferencia couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transferencias")
    @Timed
    public ResponseEntity<Transferencia> updateTransferencia(@Valid @RequestBody Transferencia transferencia) throws URISyntaxException {
        log.debug("REST request to update Transferencia : {}", transferencia);
        if (transferencia.getId() == null) {
            return createTransferencia(transferencia);
        }
        Transferencia result = transferenciaService.save(transferencia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transferencia.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transferencias : get all the transferencias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transferencias in body
     */
    @GetMapping("/transferencias")
    @Timed
    public ResponseEntity<List<Transferencia>> getAllTransferencias(@ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Transferencias");
        Page<Transferencia> page = transferenciaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transferencias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transferencias/:id : get the "id" transferencia.
     *
     * @param id the id of the transferencia to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transferencia, or with status 404 (Not Found)
     */
    @GetMapping("/transferencias/{id}")
    @Timed
    public ResponseEntity<Transferencia> getTransferencia(@PathVariable Long id) {
        log.debug("REST request to get Transferencia : {}", id);
        Transferencia transferencia = transferenciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transferencia));
    }

    /**
     * DELETE  /transferencias/:id : delete the "id" transferencia.
     *
     * @param id the id of the transferencia to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transferencias/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransferencia(@PathVariable Long id) {
        log.debug("REST request to delete Transferencia : {}", id);
        transferenciaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
