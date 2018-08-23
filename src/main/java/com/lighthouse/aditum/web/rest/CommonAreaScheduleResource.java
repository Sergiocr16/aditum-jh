package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.CommonAreaScheduleService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.CommonAreaScheduleDTO;
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
 * REST controller for managing CommonAreaSchedule.
 */
@RestController
@RequestMapping("/api")
public class CommonAreaScheduleResource {

    private final Logger log = LoggerFactory.getLogger(CommonAreaScheduleResource.class);

    private static final String ENTITY_NAME = "commonAreaSchedule";

    private final CommonAreaScheduleService commonAreaScheduleService;

    public CommonAreaScheduleResource(CommonAreaScheduleService commonAreaScheduleService) {
        this.commonAreaScheduleService = commonAreaScheduleService;
    }

    /**
     * POST  /common-area-schedules : Create a new commonAreaSchedule.
     *
     * @param commonAreaScheduleDTO the commonAreaScheduleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commonAreaScheduleDTO, or with status 400 (Bad Request) if the commonAreaSchedule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/common-area-schedules")
    @Timed
    public ResponseEntity<CommonAreaScheduleDTO> createCommonAreaSchedule(@RequestBody CommonAreaScheduleDTO commonAreaScheduleDTO) throws URISyntaxException {
        log.debug("REST request to save CommonAreaSchedule : {}", commonAreaScheduleDTO);
        if (commonAreaScheduleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new commonAreaSchedule cannot already have an ID")).body(null);
        }
        CommonAreaScheduleDTO result = commonAreaScheduleService.save(commonAreaScheduleDTO);
        return ResponseEntity.created(new URI("/api/common-area-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /common-area-schedules : Updates an existing commonAreaSchedule.
     *
     * @param commonAreaScheduleDTO the commonAreaScheduleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commonAreaScheduleDTO,
     * or with status 400 (Bad Request) if the commonAreaScheduleDTO is not valid,
     * or with status 500 (Internal Server Error) if the commonAreaScheduleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/common-area-schedules")
    @Timed
    public ResponseEntity<CommonAreaScheduleDTO> updateCommonAreaSchedule(@RequestBody CommonAreaScheduleDTO commonAreaScheduleDTO) throws URISyntaxException {
        log.debug("REST request to update CommonAreaSchedule : {}", commonAreaScheduleDTO);
        if (commonAreaScheduleDTO.getId() == null) {
            return createCommonAreaSchedule(commonAreaScheduleDTO);
        }
        CommonAreaScheduleDTO result = commonAreaScheduleService.save(commonAreaScheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commonAreaScheduleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /common-area-schedules : get all the commonAreaSchedules.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of commonAreaSchedules in body
     */
    @GetMapping("/common-area-schedules")
    @Timed
    public List<CommonAreaScheduleDTO> getAllCommonAreaSchedules() {
        log.debug("REST request to get all CommonAreaSchedules");
        return commonAreaScheduleService.findAll();
    }

    /**
     * GET  /common-area-schedules/:id : get the "id" commonAreaSchedule.
     *
     * @param id the id of the commonAreaScheduleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commonAreaScheduleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/common-area-schedules/{id}")
    @Timed
    public ResponseEntity<CommonAreaScheduleDTO> getCommonAreaSchedule(@PathVariable Long id) {
        log.debug("REST request to get CommonAreaSchedule : {}", id);
        CommonAreaScheduleDTO commonAreaScheduleDTO = commonAreaScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commonAreaScheduleDTO));
    }

    /**
     * DELETE  /common-area-schedules/:id : delete the "id" commonAreaSchedule.
     *
     * @param id the id of the commonAreaScheduleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/common-area-schedules/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommonAreaSchedule(@PathVariable Long id) {
        log.debug("REST request to delete CommonAreaSchedule : {}", id);
        commonAreaScheduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
