package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.HistoricalPositiveService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.HistoricalPositiveDTO;
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
 * REST controller for managing HistoricalPositive.
 */
@RestController
@RequestMapping("/api")
public class HistoricalPositiveResource {

    private final Logger log = LoggerFactory.getLogger(HistoricalPositiveResource.class);

    private static final String ENTITY_NAME = "historicalPositive";

    private final HistoricalPositiveService historicalPositiveService;

    public HistoricalPositiveResource(HistoricalPositiveService historicalPositiveService) {
        this.historicalPositiveService = historicalPositiveService;
    }

    /**
     * POST  /historical-positives : Create a new historicalPositive.
     *
     * @param historicalPositiveDTO the historicalPositiveDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historicalPositiveDTO, or with status 400 (Bad Request) if the historicalPositive has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/historical-positives")
    @Timed
    public ResponseEntity<HistoricalPositiveDTO> createHistoricalPositive(@RequestBody HistoricalPositiveDTO historicalPositiveDTO) throws URISyntaxException {
        log.debug("REST request to save HistoricalPositive : {}", historicalPositiveDTO);
        if (historicalPositiveDTO.getId() != null) {
//            throw new BadRequestAlertException("A new historicalPositive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoricalPositiveDTO result = historicalPositiveService.save(historicalPositiveDTO);
        return ResponseEntity.created(new URI("/api/historical-positives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    @GetMapping("/historical-positives/format-old/{companyId}")
    @Timed
    public void getAllHistoricalPositiveFormatOld(@PathVariable Long companyId) {
        log.debug("REST request to get all HistoricalDefaulters");
        historicalPositiveService.formatHistoricalPositiveReportCompany(companyId);
    }
    /**
     * PUT  /historical-positives : Updates an existing historicalPositive.
     *
     * @param historicalPositiveDTO the historicalPositiveDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historicalPositiveDTO,
     * or with status 400 (Bad Request) if the historicalPositiveDTO is not valid,
     * or with status 500 (Internal Server Error) if the historicalPositiveDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/historical-positives")
    @Timed
    public ResponseEntity<HistoricalPositiveDTO> updateHistoricalPositive(@RequestBody HistoricalPositiveDTO historicalPositiveDTO) throws URISyntaxException {
        log.debug("REST request to update HistoricalPositive : {}", historicalPositiveDTO);
        if (historicalPositiveDTO.getId() == null) {
            return createHistoricalPositive(historicalPositiveDTO);
        }
        HistoricalPositiveDTO result = historicalPositiveService.save(historicalPositiveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historicalPositiveDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historical-positives : get all the historicalPositives.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of historicalPositives in body
     */
    @GetMapping("/historical-positives")
    @Timed
    public List<HistoricalPositiveDTO> getAllHistoricalPositives() {
        log.debug("REST request to get all HistoricalPositives");
        return historicalPositiveService.findAll();
        }

    /**
     * GET  /historical-positives/:id : get the "id" historicalPositive.
     *
     * @param id the id of the historicalPositiveDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historicalPositiveDTO, or with status 404 (Not Found)
     */
    @GetMapping("/historical-positives/{id}")
    @Timed
    public ResponseEntity<HistoricalPositiveDTO> getHistoricalPositive(@PathVariable Long id) {
        log.debug("REST request to get HistoricalPositive : {}", id);
        HistoricalPositiveDTO historicalPositiveDTO = historicalPositiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(historicalPositiveDTO));
    }

    /**
     * DELETE  /historical-positives/:id : delete the "id" historicalPositive.
     *
     * @param id the id of the historicalPositiveDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/historical-positives/{id}")
    @Timed
    public ResponseEntity<Void> deleteHistoricalPositive(@PathVariable Long id) {
        log.debug("REST request to delete HistoricalPositive : {}", id);
        historicalPositiveService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
