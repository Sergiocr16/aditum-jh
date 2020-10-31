package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.HistoricalDefaulterChargeService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.HistoricalDefaulterChargeDTO;
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
 * REST controller for managing HistoricalDefaulterCharge.
 */
@RestController
@RequestMapping("/api")
public class HistoricalDefaulterChargeResource {

    private final Logger log = LoggerFactory.getLogger(HistoricalDefaulterChargeResource.class);

    private static final String ENTITY_NAME = "historicalDefaulterCharge";

    private final HistoricalDefaulterChargeService historicalDefaulterChargeService;

    public HistoricalDefaulterChargeResource(HistoricalDefaulterChargeService historicalDefaulterChargeService) {
        this.historicalDefaulterChargeService = historicalDefaulterChargeService;
    }

    /**
     * POST  /historical-defaulter-charges : Create a new historicalDefaulterCharge.
     *
     * @param historicalDefaulterChargeDTO the historicalDefaulterChargeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historicalDefaulterChargeDTO, or with status 400 (Bad Request) if the historicalDefaulterCharge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/historical-defaulter-charges")
    @Timed
    public ResponseEntity<HistoricalDefaulterChargeDTO> createHistoricalDefaulterCharge(@RequestBody HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO) throws URISyntaxException {
        log.debug("REST request to save HistoricalDefaulterCharge : {}", historicalDefaulterChargeDTO);
        if (historicalDefaulterChargeDTO.getId() != null) {
//            throw new BadRequestAlertException("A new historicalDefaulterCharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoricalDefaulterChargeDTO result = historicalDefaulterChargeService.save(historicalDefaulterChargeDTO);
        return ResponseEntity.created(new URI("/api/historical-defaulter-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /historical-defaulter-charges : Updates an existing historicalDefaulterCharge.
     *
     * @param historicalDefaulterChargeDTO the historicalDefaulterChargeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historicalDefaulterChargeDTO,
     * or with status 400 (Bad Request) if the historicalDefaulterChargeDTO is not valid,
     * or with status 500 (Internal Server Error) if the historicalDefaulterChargeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/historical-defaulter-charges")
    @Timed
    public ResponseEntity<HistoricalDefaulterChargeDTO> updateHistoricalDefaulterCharge(@RequestBody HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO) throws URISyntaxException {
        log.debug("REST request to update HistoricalDefaulterCharge : {}", historicalDefaulterChargeDTO);
        if (historicalDefaulterChargeDTO.getId() == null) {
            return createHistoricalDefaulterCharge(historicalDefaulterChargeDTO);
        }
        HistoricalDefaulterChargeDTO result = historicalDefaulterChargeService.save(historicalDefaulterChargeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historicalDefaulterChargeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historical-defaulter-charges : get all the historicalDefaulterCharges.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of historicalDefaulterCharges in body
     */
    @GetMapping("/historical-defaulter-charges")
    @Timed
    public List<HistoricalDefaulterChargeDTO> getAllHistoricalDefaulterCharges() {
        log.debug("REST request to get all HistoricalDefaulterCharges");
        return historicalDefaulterChargeService.findAll();
        }

    /**
     * GET  /historical-defaulter-charges/:id : get the "id" historicalDefaulterCharge.
     *
     * @param id the id of the historicalDefaulterChargeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historicalDefaulterChargeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/historical-defaulter-charges/{id}")
    @Timed
    public ResponseEntity<HistoricalDefaulterChargeDTO> getHistoricalDefaulterCharge(@PathVariable Long id) {
        log.debug("REST request to get HistoricalDefaulterCharge : {}", id);
        HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO = historicalDefaulterChargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(historicalDefaulterChargeDTO));
    }

    /**
     * DELETE  /historical-defaulter-charges/:id : delete the "id" historicalDefaulterCharge.
     *
     * @param id the id of the historicalDefaulterChargeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/historical-defaulter-charges/{id}")
    @Timed
    public ResponseEntity<Void> deleteHistoricalDefaulterCharge(@PathVariable Long id) {
        log.debug("REST request to delete HistoricalDefaulterCharge : {}", id);
        historicalDefaulterChargeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
