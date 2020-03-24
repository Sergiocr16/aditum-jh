package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.HouseLoginTrackerService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.HouseLoginTrackerDTO;
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
 * REST controller for managing HouseLoginTracker.
 */
@RestController
@RequestMapping("/api")
public class HouseLoginTrackerResource {

    private final Logger log = LoggerFactory.getLogger(HouseLoginTrackerResource.class);

    private static final String ENTITY_NAME = "houseLoginTracker";

    private final HouseLoginTrackerService houseLoginTrackerService;

    public HouseLoginTrackerResource(HouseLoginTrackerService houseLoginTrackerService) {
        this.houseLoginTrackerService = houseLoginTrackerService;
    }

    /**
     * POST  /house-login-trackers : Create a new houseLoginTracker.
     *
     * @param houseLoginTrackerDTO the houseLoginTrackerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new houseLoginTrackerDTO, or with status 400 (Bad Request) if the houseLoginTracker has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/house-login-trackers")
    @Timed
    public ResponseEntity<HouseLoginTrackerDTO> createHouseLoginTracker(@RequestBody HouseLoginTrackerDTO houseLoginTrackerDTO) throws URISyntaxException {
        log.debug("REST request to save HouseLoginTracker : {}", houseLoginTrackerDTO);
        if (houseLoginTrackerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new historicalCharge cannot already have an ID")).body(null);
        }
        HouseLoginTrackerDTO result = houseLoginTrackerService.save(houseLoginTrackerDTO);
        return ResponseEntity.created(new URI("/api/house-login-trackers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /house-login-trackers : Updates an existing houseLoginTracker.
     *
     * @param houseLoginTrackerDTO the houseLoginTrackerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated houseLoginTrackerDTO,
     * or with status 400 (Bad Request) if the houseLoginTrackerDTO is not valid,
     * or with status 500 (Internal Server Error) if the houseLoginTrackerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/house-login-trackers")
    @Timed
    public ResponseEntity<HouseLoginTrackerDTO> updateHouseLoginTracker(@RequestBody HouseLoginTrackerDTO houseLoginTrackerDTO) throws URISyntaxException {
        log.debug("REST request to update HouseLoginTracker : {}", houseLoginTrackerDTO);
        if (houseLoginTrackerDTO.getId() == null) {
            return createHouseLoginTracker(houseLoginTrackerDTO);
        }
        HouseLoginTrackerDTO result = houseLoginTrackerService.save(houseLoginTrackerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, houseLoginTrackerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /house-login-trackers : get all the houseLoginTrackers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of houseLoginTrackers in body
     */
    @GetMapping("/house-login-trackers")
    @Timed
    public List<HouseLoginTrackerDTO> getAllHouseLoginTrackers(Long companyId) {
        log.debug("REST request to get all HouseLoginTrackers");
        return houseLoginTrackerService.findAll(companyId);
        }

    /**
     * GET  /house-login-trackers/:id : get the "id" houseLoginTracker.
     *
     * @param id the id of the houseLoginTrackerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the houseLoginTrackerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/house-login-trackers/{id}")
    @Timed
    public ResponseEntity<HouseLoginTrackerDTO> getHouseLoginTracker(@PathVariable Long id) {
        log.debug("REST request to get HouseLoginTracker : {}", id);
        HouseLoginTrackerDTO houseLoginTrackerDTO = houseLoginTrackerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(houseLoginTrackerDTO));
    }

    /**
     * DELETE  /house-login-trackers/:id : delete the "id" houseLoginTracker.
     *
     * @param id the id of the houseLoginTrackerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/house-login-trackers/{id}")
    @Timed
    public ResponseEntity<Void> deleteHouseLoginTracker(@PathVariable Long id) {
        log.debug("REST request to delete HouseLoginTracker : {}", id);
        houseLoginTrackerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
