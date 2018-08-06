package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AnnouncementService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.AnnouncementDTO;
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
 * REST controller for managing Announcement.
 */
@RestController
@RequestMapping("/api")
public class AnnouncementResource {

    private final Logger log = LoggerFactory.getLogger(AnnouncementResource.class);

    private static final String ENTITY_NAME = "announcement";

    private final AnnouncementService announcementService;

    public AnnouncementResource(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * POST  /announcements : Create a new announcement.
     *
     * @param announcementDTO the announcementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new announcementDTO, or with status 400 (Bad Request) if the announcement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/announcements")
    @Timed
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@Valid @RequestBody AnnouncementDTO announcementDTO) throws URISyntaxException {
        log.debug("REST request to save Announcement : {}", announcementDTO);
        if (announcementDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new announcement cannot already have an ID")).body(null);
        }
        AnnouncementDTO result = announcementService.save(announcementDTO);
        return ResponseEntity.created(new URI("/api/announcements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /announcements : Updates an existing announcement.
     *
     * @param announcementDTO the announcementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated announcementDTO,
     * or with status 400 (Bad Request) if the announcementDTO is not valid,
     * or with status 500 (Internal Server Error) if the announcementDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/announcements")
    @Timed
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(@Valid @RequestBody AnnouncementDTO announcementDTO) throws URISyntaxException {
        log.debug("REST request to update Announcement : {}", announcementDTO);
        if (announcementDTO.getId() == null) {
            return createAnnouncement(announcementDTO);
        }
        AnnouncementDTO result = announcementService.save(announcementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, announcementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /announcements : get all the announcements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of announcements in body
     */
    @GetMapping("/announcements")
    @Timed
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements(@ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Announcements");
        Page<AnnouncementDTO> page = announcementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/announcements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /announcements/:id : get the "id" announcement.
     *
     * @param id the id of the announcementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the announcementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/announcements/{id}")
    @Timed
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@PathVariable Long id) {
        log.debug("REST request to get Announcement : {}", id);
        AnnouncementDTO announcementDTO = announcementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(announcementDTO));
    }

    /**
     * DELETE  /announcements/:id : delete the "id" announcement.
     *
     * @param id the id of the announcementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/announcements/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        log.debug("REST request to delete Announcement : {}", id);
        announcementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
