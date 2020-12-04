package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ActivityResidentService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ActivityResidentDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ActivityResident.
 */
@RestController
@RequestMapping("/api")
public class ActivityResidentResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResidentResource.class);

    private static final String ENTITY_NAME = "activityResident";

    private final ActivityResidentService activityResidentService;

    public ActivityResidentResource(ActivityResidentService activityResidentService) {
        this.activityResidentService = activityResidentService;
    }

    /**
     * POST  /activity-residents : Create a new activityResident.
     *
     * @param activityResidentDTO the activityResidentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activityResidentDTO, or with status 400 (Bad Request) if the activityResident has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activity-residents")
    @Timed
    public ResponseEntity<ActivityResidentDTO> createActivityResident(@RequestBody ActivityResidentDTO activityResidentDTO) throws URISyntaxException {
        log.debug("REST request to save ActivityResident : {}", activityResidentDTO);
        if (activityResidentDTO.getId() != null) {
//            throw new BadRequestAlertException("A new activityResident cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityResidentDTO result = activityResidentService.save(activityResidentDTO);
        return ResponseEntity.created(new URI("/api/activity-residents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-residents : Updates an existing activityResident.
     *
     * @param activityResidentDTO the activityResidentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activityResidentDTO,
     * or with status 400 (Bad Request) if the activityResidentDTO is not valid,
     * or with status 500 (Internal Server Error) if the activityResidentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activity-residents")
    @Timed
    public ResponseEntity<ActivityResidentDTO> updateActivityResident(@RequestBody ActivityResidentDTO activityResidentDTO) throws URISyntaxException {
        log.debug("REST request to update ActivityResident : {}", activityResidentDTO);
        if (activityResidentDTO.getId() == null) {
            return createActivityResident(activityResidentDTO);
        }
        ActivityResidentDTO result = activityResidentService.save(activityResidentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activityResidentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-residents : get all the activityResidents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activityResidents in body
     */
    @GetMapping("/activity-residents")
    @Timed
    public ResponseEntity<List<ActivityResidentDTO>> getAllActivityResidents(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of ActivityResidents");
        Page<ActivityResidentDTO> page = activityResidentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/activity-residents-by-user/{userId}")
    @Timed
    public ResponseEntity<List<ActivityResidentDTO>> getAllActivityResidentsByUser(Pageable pageable,@PathVariable Long userId) throws URISyntaxException {
        log.debug("REST request to get a page of ActivityResidents");
        Page<ActivityResidentDTO> page = activityResidentService.findAllByUserId(pageable,userId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/activity-residents-by-user/not-seeing/{userId}")
    @Timed
    public ResponseEntity<List<ActivityResidentDTO>> getLastActivityNotSeeingByUser(Pageable pageable,@PathVariable Long userId) throws URISyntaxException {
        log.debug("REST request to get a page of ActivityResidents");
        Page<ActivityResidentDTO> page = activityResidentService.getLastActivityNotSeeingByUser(pageable,userId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activity-residents/:id : get the "id" activityResident.
     *
     * @param id the id of the activityResidentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activityResidentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/activity-residents/{id}")
    @Timed
    public ResponseEntity<ActivityResidentDTO> getActivityResident(@PathVariable Long id) {
        log.debug("REST request to get ActivityResident : {}", id);
        ActivityResidentDTO activityResidentDTO = activityResidentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(activityResidentDTO));
    }

    /**
     * DELETE  /activity-residents/:id : delete the "id" activityResident.
     *
     * @param id the id of the activityResidentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activity-residents/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivityResident(@PathVariable Long id) {
        log.debug("REST request to delete ActivityResident : {}", id);
        activityResidentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
