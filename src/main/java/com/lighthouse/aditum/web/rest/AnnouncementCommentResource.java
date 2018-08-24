package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AnnouncementCommentService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.AnnouncementCommentDTO;
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
 * REST controller for managing AnnouncementComment.
 */
@RestController
@RequestMapping("/api")
public class AnnouncementCommentResource {

    private final Logger log = LoggerFactory.getLogger(AnnouncementCommentResource.class);

    private static final String ENTITY_NAME = "announcementComment";

    private final AnnouncementCommentService announcementCommentService;

    public AnnouncementCommentResource(AnnouncementCommentService announcementCommentService) {
        this.announcementCommentService = announcementCommentService;
    }

    /**
     * POST  /announcement-comments : Create a new announcementComment.
     *
     * @param announcementCommentDTO the announcementCommentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new announcementCommentDTO, or with status 400 (Bad Request) if the announcementComment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/announcement-comments")
    @Timed
    public ResponseEntity<AnnouncementCommentDTO> createAnnouncementComment(@Valid @RequestBody AnnouncementCommentDTO announcementCommentDTO) throws URISyntaxException {
        log.debug("REST request to save AnnouncementComment : {}", announcementCommentDTO);
        if (announcementCommentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new announcementComment cannot already have an ID")).body(null);

        }
        AnnouncementCommentDTO result = announcementCommentService.save(announcementCommentDTO);
        return ResponseEntity.created(new URI("/api/announcement-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /announcement-comments : Updates an existing announcementComment.
     *
     * @param announcementCommentDTO the announcementCommentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated announcementCommentDTO,
     * or with status 400 (Bad Request) if the announcementCommentDTO is not valid,
     * or with status 500 (Internal Server Error) if the announcementCommentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/announcement-comments")
    @Timed
    public ResponseEntity<AnnouncementCommentDTO> updateAnnouncementComment(@Valid @RequestBody AnnouncementCommentDTO announcementCommentDTO) throws URISyntaxException {
        log.debug("REST request to update AnnouncementComment : {}", announcementCommentDTO);
        if (announcementCommentDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new announcementComment cannot already have an ID")).body(null);
        }
        AnnouncementCommentDTO result = announcementCommentService.save(announcementCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, announcementCommentDTO.getId().toString()))
            .body(result);
    }


     /**
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of announcementComments in body
     */
    @GetMapping("/announcement-comments")
    @Timed
    public ResponseEntity<List<AnnouncementCommentDTO>> getAllAnnouncementComments(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of AnnouncementComments");
        Page<AnnouncementCommentDTO> page = announcementCommentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/announcement-comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/announcement-comments/{announcementId}")
    @Timed
    public ResponseEntity<List<AnnouncementCommentDTO>> getAllAnnouncementComments(Pageable pageable, @PathVariable Long announcementId) throws URISyntaxException {
        log.debug("REST request to get a page of AnnouncementComments");
        Page<AnnouncementCommentDTO> page = announcementCommentService.findByAnnouncement(pageable,announcementId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/announcement-comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /announcement-comments/:id : get the "id" announcementComment.
     *
     * @param id the id of the announcementCommentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the announcementCommentDTO, or with status 404 (Not Found)
     */
//    @GetMapping("/announcement-comments/{id}")
//    @Timed
//    public ResponseEntity<AnnouncementCommentDTO> getAnnouncementComment(@PathVariable Long id) {
//        log.debug("REST request to get AnnouncementComment : {}", id);
//        Optional<AnnouncementCommentDTO> announcementCommentDTO = announcementCommentService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(announcementCommentDTO);
//    }

    /**
     * DELETE  /announcement-comments/:id : delete the "id" announcementComment.
     *
     * @param id the id of the announcementCommentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/announcement-comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnnouncementComment(@PathVariable Long id) {
        log.debug("REST request to delete AnnouncementComment : {}", id);
        announcementCommentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
