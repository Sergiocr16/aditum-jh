package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ComplaintService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ComplaintDTO;
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
 * REST controller for managing Complaint.
 */
@RestController
@RequestMapping("/api")
public class ComplaintResource {

    private final Logger log = LoggerFactory.getLogger(ComplaintResource.class);

    private static final String ENTITY_NAME = "complaint";

    private final ComplaintService complaintService;

    public ComplaintResource(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    /**
     * POST  /complaints : Create a new complaint.
     *
     * @param complaintDTO the complaintDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new complaintDTO, or with status 400 (Bad Request) if the complaint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/complaints")
    @Timed
    public ResponseEntity<ComplaintDTO> createComplaint(@Valid @RequestBody ComplaintDTO complaintDTO) throws URISyntaxException {
        log.debug("REST request to save Complaint : {}", complaintDTO);
        if (complaintDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new complaint cannot already have an ID")).body(null);
        }
        ComplaintDTO result = complaintService.save(complaintDTO);
        return ResponseEntity.created(new URI("/api/complaints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /complaints : Updates an existing complaint.
     *
     * @param complaintDTO the complaintDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated complaintDTO,
     * or with status 400 (Bad Request) if the complaintDTO is not valid,
     * or with status 500 (Internal Server Error) if the complaintDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/complaints")
    @Timed
    public ResponseEntity<ComplaintDTO> updateComplaint(@Valid @RequestBody ComplaintDTO complaintDTO) throws URISyntaxException {
        log.debug("REST request to update Complaint : {}", complaintDTO);
        if (complaintDTO.getId() == null) {
            return createComplaint(complaintDTO);
        }
        ComplaintDTO result = complaintService.save(complaintDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, complaintDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /complaints : get all the complaints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of complaints in body
     */
    @GetMapping("/complaints/admin/{companyId}")
    @Timed
    public ResponseEntity<List<ComplaintDTO>> getAllComplaints(Pageable pageable,@PathVariable Long companyId) throws URISyntaxException {
        log.debug("REST request to get a page of Complaints");
        Page<ComplaintDTO> page = complaintService.findAll(pageable, companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/complaints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/complaints/admin/{companyId}/status/{status}")
    @Timed
    public ResponseEntity<List<ComplaintDTO>> getAllComplaintsByStatus(Pageable pageable,@PathVariable Long companyId, @PathVariable int status) throws URISyntaxException {
        log.debug("REST request to get a page of Complaints");
        Page<ComplaintDTO> page = complaintService.findAllByStatus(pageable, companyId, status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/complaints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/complaints/user/{residentId}")
    @Timed
    public ResponseEntity<List<ComplaintDTO>> getAllComplaintsByUser(Pageable pageable,@PathVariable Long residentId) throws URISyntaxException {
        log.debug("REST request to get a page of Complaints");
        Page<ComplaintDTO> page = complaintService.findAllByResident(pageable, residentId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/complaints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /complaints/:id : get the "id" complaint.
     *
     * @param id the id of the complaintDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the complaintDTO, or with status 404 (Not Found)
     */
    @GetMapping("/complaints/{id}")
    @Timed
    public ResponseEntity<ComplaintDTO> getComplaint(@PathVariable Long id) {
        log.debug("REST request to get Complaint : {}", id);
        ComplaintDTO complaintDTO = complaintService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(complaintDTO));
    }

    /**
     * DELETE  /complaints/:id : delete the "id" complaint.
     *
     * @param id the id of the complaintDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/complaints/{id}")
    @Timed
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        log.debug("REST request to delete Complaint : {}", id);
        complaintService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
