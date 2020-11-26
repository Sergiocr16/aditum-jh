package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.NotificationSendedService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.NotificationSendedDTO;
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
 * REST controller for managing NotificationSended.
 */
@RestController
@RequestMapping("/api")
public class NotificationSendedResource {

    private final Logger log = LoggerFactory.getLogger(NotificationSendedResource.class);

    private static final String ENTITY_NAME = "notificationSended";

    private final NotificationSendedService notificationSendedService;

    public NotificationSendedResource(NotificationSendedService notificationSendedService) {
        this.notificationSendedService = notificationSendedService;
    }

    /**
     * POST  /notification-sendeds : Create a new notificationSended.
     *
     * @param notificationSendedDTO the notificationSendedDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationSendedDTO, or with status 400 (Bad Request) if the notificationSended has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notification-sendeds")
    @Timed
    public ResponseEntity<NotificationSendedDTO> createNotificationSended(@RequestBody NotificationSendedDTO notificationSendedDTO) throws URISyntaxException {
        log.debug("REST request to save NotificationSended : {}", notificationSendedDTO);
        if (notificationSendedDTO.getId() != null) {
//            throw new BadRequestAlertException("A new notificationSended cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationSendedDTO result = notificationSendedService.save(notificationSendedDTO);
        return ResponseEntity.created(new URI("/api/notification-sendeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-sendeds : Updates an existing notificationSended.
     *
     * @param notificationSendedDTO the notificationSendedDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationSendedDTO,
     * or with status 400 (Bad Request) if the notificationSendedDTO is not valid,
     * or with status 500 (Internal Server Error) if the notificationSendedDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notification-sendeds")
    @Timed
    public ResponseEntity<NotificationSendedDTO> updateNotificationSended(@RequestBody NotificationSendedDTO notificationSendedDTO) throws URISyntaxException {
        log.debug("REST request to update NotificationSended : {}", notificationSendedDTO);
        if (notificationSendedDTO.getId() == null) {
            return createNotificationSended(notificationSendedDTO);
        }
        NotificationSendedDTO result = notificationSendedService.save(notificationSendedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificationSendedDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-sendeds : get all the notificationSendeds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notificationSendeds in body
     */
    @GetMapping("/notification-sendeds")
    @Timed
    public ResponseEntity<List<NotificationSendedDTO>> getAllNotificationSendeds(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of NotificationSendeds");
        Page<NotificationSendedDTO> page = notificationSendedService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notification-sendeds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notification-sendeds/:id : get the "id" notificationSended.
     *
     * @param id the id of the notificationSendedDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationSendedDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notification-sendeds/{id}")
    @Timed
    public ResponseEntity<NotificationSendedDTO> getNotificationSended(@PathVariable Long id) {
        log.debug("REST request to get NotificationSended : {}", id);
        NotificationSendedDTO notificationSendedDTO = notificationSendedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notificationSendedDTO));
    }

    /**
     * DELETE  /notification-sendeds/:id : delete the "id" notificationSended.
     *
     * @param id the id of the notificationSendedDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notification-sendeds/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotificationSended(@PathVariable Long id) {
        log.debug("REST request to delete NotificationSended : {}", id);
        notificationSendedService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
