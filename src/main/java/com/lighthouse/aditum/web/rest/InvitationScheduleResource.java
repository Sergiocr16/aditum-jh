package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.InvitationScheduleService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.InvitationScheduleDTO;
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
 * REST controller for managing InvitationSchedule.
 */
@RestController
@RequestMapping("/api")
public class InvitationScheduleResource {

    private final Logger log = LoggerFactory.getLogger(InvitationScheduleResource.class);

    private static final String ENTITY_NAME = "invitationSchedule";

    private final InvitationScheduleService invitationScheduleService;

    public InvitationScheduleResource(InvitationScheduleService invitationScheduleService) {
        this.invitationScheduleService = invitationScheduleService;
    }

    /**
     * POST  /invitation-schedules : Create a new invitationSchedule.
     *
     * @param invitationScheduleDTO the invitationScheduleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invitationScheduleDTO, or with status 400 (Bad Request) if the invitationSchedule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invitation-schedules")
    @Timed
    public ResponseEntity<InvitationScheduleDTO> createInvitationSchedule(@RequestBody InvitationScheduleDTO invitationScheduleDTO) throws URISyntaxException {
        log.debug("REST request to save InvitationSchedule : {}", invitationScheduleDTO);
        if (invitationScheduleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new invitationSchedule cannot already have an ID")).body(null);

        }
        InvitationScheduleDTO result = invitationScheduleService.save(invitationScheduleDTO);
        return ResponseEntity.created(new URI("/api/invitation-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invitation-schedules : Updates an existing invitationSchedule.
     *
     * @param invitationScheduleDTO the invitationScheduleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invitationScheduleDTO,
     * or with status 400 (Bad Request) if the invitationScheduleDTO is not valid,
     * or with status 500 (Internal Server Error) if the invitationScheduleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invitation-schedules")
    @Timed
    public ResponseEntity<InvitationScheduleDTO> updateInvitationSchedule(@RequestBody InvitationScheduleDTO invitationScheduleDTO) throws URISyntaxException {
        log.debug("REST request to update InvitationSchedule : {}", invitationScheduleDTO);
        if (invitationScheduleDTO.getId() == null) {
            return createInvitationSchedule(invitationScheduleDTO);
        }
        InvitationScheduleDTO result = invitationScheduleService.save(invitationScheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invitationScheduleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invitation-schedules : get all the invitationSchedules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invitationSchedules in body
     */
    @GetMapping("/invitation-schedules")
    @Timed
    public ResponseEntity<List<InvitationScheduleDTO>> getAllInvitationSchedules(Pageable pageable)throws URISyntaxException {
        log.debug("REST request to get a page of InvitationSchedules");
        Page<InvitationScheduleDTO> page = invitationScheduleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invitation-schedules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/invitation-schedules/byInvitation/{invitationId}")
    @Timed
    public List<InvitationScheduleDTO> findSchedulesByCommonArea(@PathVariable (value = "invitationId") Long invitationId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        return invitationScheduleService.findSchedulesByInvitation(invitationId);
    }
    /**
     * GET  /invitation-schedules/:id : get the "id" invitationSchedule.
     *
     * @param id the id of the invitationScheduleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invitationScheduleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invitation-schedules/{id}")
    @Timed
    public ResponseEntity<InvitationScheduleDTO> getInvitationSchedule(@PathVariable Long id) {
        log.debug("REST request to get InvitationSchedule : {}", id);
        InvitationScheduleDTO invitationScheduleDTO = invitationScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(invitationScheduleDTO));
    }

    /**
     * DELETE  /invitation-schedules/:id : delete the "id" invitationSchedule.
     *
     * @param id the id of the invitationScheduleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invitation-schedules/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvitationSchedule(@PathVariable Long id) {
        log.debug("REST request to delete InvitationSchedule : {}", id);
        invitationScheduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
