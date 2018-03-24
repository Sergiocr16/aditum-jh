package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.HistoricalChargeService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.HistoricalChargeDTO;
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
 * REST controller for managing HistoricalCharge.
 */
@RestController
@RequestMapping("/api")
public class HistoricalChargeResource {

    private final Logger log = LoggerFactory.getLogger(HistoricalChargeResource.class);

    private static final String ENTITY_NAME = "historicalCharge";

    private final HistoricalChargeService historicalChargeService;

    public HistoricalChargeResource(HistoricalChargeService historicalChargeService) {
        this.historicalChargeService = historicalChargeService;
    }

    /**
     * POST  /historical-charges : Create a new historicalCharge.
     *
     * @param historicalChargeDTO the historicalChargeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historicalChargeDTO, or with status 400 (Bad Request) if the historicalCharge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/historical-charges")
    @Timed
    public ResponseEntity<HistoricalChargeDTO> createHistoricalCharge(@Valid @RequestBody HistoricalChargeDTO historicalChargeDTO) throws URISyntaxException {
        log.debug("REST request to save HistoricalCharge : {}", historicalChargeDTO);
        if (historicalChargeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new historicalCharge cannot already have an ID")).body(null);
        }
        HistoricalChargeDTO result = historicalChargeService.save(historicalChargeDTO);
        return ResponseEntity.created(new URI("/api/historical-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /historical-charges : Updates an existing historicalCharge.
     *
     * @param historicalChargeDTO the historicalChargeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historicalChargeDTO,
     * or with status 400 (Bad Request) if the historicalChargeDTO is not valid,
     * or with status 500 (Internal Server Error) if the historicalChargeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/historical-charges")
    @Timed
    public ResponseEntity<HistoricalChargeDTO> updateHistoricalCharge(@Valid @RequestBody HistoricalChargeDTO historicalChargeDTO) throws URISyntaxException {
        log.debug("REST request to update HistoricalCharge : {}", historicalChargeDTO);
        if (historicalChargeDTO.getId() == null) {
            return createHistoricalCharge(historicalChargeDTO);
        }
        HistoricalChargeDTO result = historicalChargeService.save(historicalChargeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historicalChargeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historical-charges : get all the historicalCharges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of historicalCharges in body
     */
    @GetMapping("/historical-charges")
    @Timed
    public ResponseEntity<List<HistoricalChargeDTO>> getAllHistoricalCharges(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of HistoricalCharges");
        Page<HistoricalChargeDTO> page = historicalChargeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/historical-charges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /historical-charges/:id : get the "id" historicalCharge.
     *
     * @param id the id of the historicalChargeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historicalChargeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/historical-charges/{id}")
    @Timed
    public ResponseEntity<HistoricalChargeDTO> getHistoricalCharge(@PathVariable Long id) {
        log.debug("REST request to get HistoricalCharge : {}", id);
        HistoricalChargeDTO historicalChargeDTO = historicalChargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(historicalChargeDTO));
    }

    /**
     * DELETE  /historical-charges/:id : delete the "id" historicalCharge.
     *
     * @param id the id of the historicalChargeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/historical-charges/{id}")
    @Timed
    public ResponseEntity<Void> deleteHistoricalCharge(@PathVariable Long id) {
        log.debug("REST request to delete HistoricalCharge : {}", id);
        historicalChargeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
