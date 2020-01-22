package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ReservationHouseRestrictionsService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.ReservationHouseRestrictionsDTO;
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
 * REST controller for managing ReservationHouseRestrictions.
 */
@RestController
@RequestMapping("/api")
public class ReservationHouseRestrictionsResource {

    private final Logger log = LoggerFactory.getLogger(ReservationHouseRestrictionsResource.class);

    private static final String ENTITY_NAME = "reservationHouseRestrictions";

    private final ReservationHouseRestrictionsService reservationHouseRestrictionsService;

    public ReservationHouseRestrictionsResource(ReservationHouseRestrictionsService reservationHouseRestrictionsService) {
        this.reservationHouseRestrictionsService = reservationHouseRestrictionsService;
    }

    /**
     * POST  /reservation-house-restrictions : Create a new reservationHouseRestrictions.
     *
     * @param reservationHouseRestrictionsDTO the reservationHouseRestrictionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reservationHouseRestrictionsDTO, or with status 400 (Bad Request) if the reservationHouseRestrictions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reservation-house-restrictions")
    @Timed
    public ResponseEntity<ReservationHouseRestrictionsDTO> createReservationHouseRestrictions(@RequestBody ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO) throws URISyntaxException {
        log.debug("REST request to save ReservationHouseRestrictions : {}", reservationHouseRestrictionsDTO);
        if (reservationHouseRestrictionsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }
        ReservationHouseRestrictionsDTO result = reservationHouseRestrictionsService.save(reservationHouseRestrictionsDTO);
        return ResponseEntity.created(new URI("/api/reservation-house-restrictions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reservation-house-restrictions : Updates an existing reservationHouseRestrictions.
     *
     * @param reservationHouseRestrictionsDTO the reservationHouseRestrictionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reservationHouseRestrictionsDTO,
     * or with status 400 (Bad Request) if the reservationHouseRestrictionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the reservationHouseRestrictionsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reservation-house-restrictions")
    @Timed
    public ResponseEntity<ReservationHouseRestrictionsDTO> updateReservationHouseRestrictions(@RequestBody ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO) throws URISyntaxException {
        log.debug("REST request to update ReservationHouseRestrictions : {}", reservationHouseRestrictionsDTO);
        if (reservationHouseRestrictionsDTO.getId() == null) {
            return createReservationHouseRestrictions(reservationHouseRestrictionsDTO);
        }
        ReservationHouseRestrictionsDTO result = reservationHouseRestrictionsService.save(reservationHouseRestrictionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reservationHouseRestrictionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reservation-house-restrictions : get all the reservationHouseRestrictions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reservationHouseRestrictions in body
     */
    @GetMapping("/reservation-house-restrictions")
    @Timed
    public List<ReservationHouseRestrictionsDTO> getAllReservationHouseRestrictions() {
        log.debug("REST request to get all ReservationHouseRestrictions");
        return reservationHouseRestrictionsService.findAll();
        }

    /**
     * GET  /reservation-house-restrictions/:id : get the "id" reservationHouseRestrictions.
     *
     * @param id the id of the reservationHouseRestrictionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reservationHouseRestrictionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reservation-house-restrictions/{id}")
    @Timed
    public ResponseEntity<ReservationHouseRestrictionsDTO> getReservationHouseRestrictions(@PathVariable Long id) {
        log.debug("REST request to get ReservationHouseRestrictions : {}", id);
        ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO = reservationHouseRestrictionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reservationHouseRestrictionsDTO));
    }

    /**
     * DELETE  /reservation-house-restrictions/:id : delete the "id" reservationHouseRestrictions.
     *
     * @param id the id of the reservationHouseRestrictionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reservation-house-restrictions/{id}")
    @Timed
    public ResponseEntity<Void> deleteReservationHouseRestrictions(@PathVariable Long id) {
        log.debug("REST request to delete ReservationHouseRestrictions : {}", id);
        reservationHouseRestrictionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
