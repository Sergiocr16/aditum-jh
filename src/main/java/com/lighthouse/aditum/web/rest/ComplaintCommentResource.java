package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ComplaintCommentService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ComplaintCommentDTO;
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
 * REST controller for managing ComplaintComment.
 */
@RestController
@RequestMapping("/api")
public class ComplaintCommentResource {

    private final Logger log = LoggerFactory.getLogger(ComplaintCommentResource.class);

    private static final String ENTITY_NAME = "complaintComment";

    private final ComplaintCommentService complaintCommentService;

    public ComplaintCommentResource(ComplaintCommentService complaintCommentService) {
        this.complaintCommentService = complaintCommentService;
    }

    /**
     * POST  /complaint-comments : Create a new complaintComment.
     *
     * @param complaintCommentDTO the complaintCommentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new complaintCommentDTO, or with status 400 (Bad Request) if the complaintComment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/complaint-comments")
    @Timed
    public ResponseEntity<ComplaintCommentDTO> createComplaintComment(@Valid @RequestBody ComplaintCommentDTO complaintCommentDTO) throws URISyntaxException {
        log.debug("REST request to save ComplaintComment : {}", complaintCommentDTO);
        if (complaintCommentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new complaint comment cannot already have an ID")).body(null);
        }
        ComplaintCommentDTO result = complaintCommentService.save(complaintCommentDTO);
        return ResponseEntity.created(new URI("/api/complaint-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /complaint-comments : Updates an existing complaintComment.
     *
     * @param complaintCommentDTO the complaintCommentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated complaintCommentDTO,
     * or with status 400 (Bad Request) if the complaintCommentDTO is not valid,
     * or with status 500 (Internal Server Error) if the complaintCommentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/complaint-comments")
    @Timed
    public ResponseEntity<ComplaintCommentDTO> updateComplaintComment(@Valid @RequestBody ComplaintCommentDTO complaintCommentDTO) throws URISyntaxException {
        log.debug("REST request to update ComplaintComment : {}", complaintCommentDTO);
        if (complaintCommentDTO.getId() == null) {
            return createComplaintComment(complaintCommentDTO);
        }
        ComplaintCommentDTO result = complaintCommentService.save(complaintCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, complaintCommentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /complaint-comments : get all the complaintComments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of complaintComments in body
     */
    @GetMapping("/complaint-comments")
    @Timed
    public ResponseEntity<List<ComplaintCommentDTO>> getAllComplaintComments(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of ComplaintComments");
        Page<ComplaintCommentDTO> page = complaintCommentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/complaint-comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /complaint-comments/:id : get the "id" complaintComment.
     *
     * @param id the id of the complaintCommentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the complaintCommentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/complaint-comments/{id}")
    @Timed
    public ResponseEntity<ComplaintCommentDTO> getComplaintComment(@PathVariable Long id) {
        log.debug("REST request to get ComplaintComment : {}", id);
        ComplaintCommentDTO complaintCommentDTO = complaintCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(complaintCommentDTO));
    }

    /**
     * DELETE  /complaint-comments/:id : delete the "id" complaintComment.
     *
     * @param id the id of the complaintCommentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/complaint-comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteComplaintComment(@PathVariable Long id) {
        log.debug("REST request to delete ComplaintComment : {}", id);
        complaintCommentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
