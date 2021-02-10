package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.HistoricalDefaulterService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.service.ScheduledTasks;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.HistoricalDefaulterDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing HistoricalDefaulter.
 */
@RestController
@RequestMapping("/api")
public class HistoricalDefaulterResource {

    private final Logger log = LoggerFactory.getLogger(HistoricalDefaulterResource.class);

    private static final String ENTITY_NAME = "historicalDefaulter";

    private final HistoricalDefaulterService historicalDefaulterService;
    private final ScheduledTasks scheduledTasks;

    public HistoricalDefaulterResource(ScheduledTasks scheduledTasks, HistoricalDefaulterService historicalDefaulterService) {
        this.historicalDefaulterService = historicalDefaulterService;
        this.scheduledTasks = scheduledTasks;
    }

    /**
     * POST  /historical-defaulters : Create a new historicalDefaulter.
     *
     * @param historicalDefaulterDTO the historicalDefaulterDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historicalDefaulterDTO, or with status 400 (Bad Request) if the historicalDefaulter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/historical-defaulters")
    @Timed
    public ResponseEntity<HistoricalDefaulterDTO> createHistoricalDefaulter(@RequestBody HistoricalDefaulterDTO historicalDefaulterDTO) throws URISyntaxException {
        log.debug("REST request to save HistoricalDefaulter : {}", historicalDefaulterDTO);
        if (historicalDefaulterDTO.getId() != null) {
//            throw new BadRequestAlertException("A new historicalDefaulter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoricalDefaulterDTO result = historicalDefaulterService.save(historicalDefaulterDTO);
        return ResponseEntity.created(new URI("/api/historical-defaulters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /historical-defaulters : Updates an existing historicalDefaulter.
     *
     * @param historicalDefaulterDTO the historicalDefaulterDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historicalDefaulterDTO,
     * or with status 400 (Bad Request) if the historicalDefaulterDTO is not valid,
     * or with status 500 (Internal Server Error) if the historicalDefaulterDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/historical-defaulters")
    @Timed
    public ResponseEntity<HistoricalDefaulterDTO> updateHistoricalDefaulter(@RequestBody HistoricalDefaulterDTO historicalDefaulterDTO) throws URISyntaxException {
        log.debug("REST request to update HistoricalDefaulter : {}", historicalDefaulterDTO);
        if (historicalDefaulterDTO.getId() == null) {
            return createHistoricalDefaulter(historicalDefaulterDTO);
        }
        HistoricalDefaulterDTO result = historicalDefaulterService.save(historicalDefaulterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historicalDefaulterDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historical-defaulters : get all the historicalDefaulters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of historicalDefaulters in body
     */
    @GetMapping("/historical-defaulters")
    @Timed
    public List<HistoricalDefaulterDTO> getAllHistoricalDefaulters() {
        log.debug("REST request to get all HistoricalDefaulters");
        return historicalDefaulterService.findAll();
    }


    @GetMapping("/historical-defaulters/format-company/{companyId}")
    @Timed
    public void formatCompany(@PathVariable Long companyId) throws URISyntaxException {
        log.debug("REST request to get all HistoricalDefaulters");
        this.scheduledTasks.formatOptimizeAsync(companyId, 1, 1);
    }

    @GetMapping("/historical-defaulters/format-all-company/{monthNumber}")
    @Timed
    public void formatAllCompany(@PathVariable int monthNumber) throws URISyntaxException {
        log.debug("REST request to get all HistoricalDefaulters");
        this.scheduledTasks.formatAllOptimize(monthNumber);
    }

    @GetMapping("/historical-defaulters/format-company/{companyId}/{monthNumber}")
    @Timed
    public void formatAllCompany(@PathVariable Long companyId,@PathVariable int monthNumber) throws URISyntaxException {
        log.debug("REST request to get all HistoricalDefaulters");
        this.scheduledTasks.formatHistorical(companyId,monthNumber);
    }


    @GetMapping("/historical-defaulters/format-old/{companyId}")
    @Timed
    public void getAllHistoricalDefaultersFormatOld(@PathVariable Long companyId) {
        log.debug("REST request to get all HistoricalDefaulters");
        historicalDefaulterService.formatHistorialDefaulterReportCompany(companyId);
    }

    @GetMapping("/create-rounds/{companyId}")
    @Timed
    public void createRounds(@PathVariable Long companyId) {
        log.debug("REST request to get all HistoricalDefaulters");
        try {
            scheduledTasks.crearRondasEnCondo(companyId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * GET  /historical-defaulters/:id : get the "id" historicalDefaulter.
     *
     * @param id the id of the historicalDefaulterDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historicalDefaulterDTO, or with status 404 (Not Found)
     */
    @GetMapping("/historical-defaulters/{id}")
    @Timed
    public ResponseEntity<HistoricalDefaulterDTO> getHistoricalDefaulter(@PathVariable Long id) {
        log.debug("REST request to get HistoricalDefaulter : {}", id);
        HistoricalDefaulterDTO historicalDefaulterDTO = historicalDefaulterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(historicalDefaulterDTO));
    }

    /**
     * DELETE  /historical-defaulters/:id : delete the "id" historicalDefaulter.
     *
     * @param id the id of the historicalDefaulterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/historical-defaulters/{id}")
    @Timed
    public ResponseEntity<Void> deleteHistoricalDefaulter(@PathVariable Long id) {
        log.debug("REST request to delete HistoricalDefaulter : {}", id);
        historicalDefaulterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
