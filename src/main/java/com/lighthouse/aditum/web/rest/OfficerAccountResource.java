package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.OfficerAccountService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.OfficerAccountDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing OfficerAccount.
 */
@RestController
@RequestMapping("/api")
public class OfficerAccountResource {

    private final Logger log = LoggerFactory.getLogger(OfficerAccountResource.class);

    private static final String ENTITY_NAME = "officerAccount";

    private final OfficerAccountService officerAccountService;

    public OfficerAccountResource(OfficerAccountService officerAccountService) {
        this.officerAccountService = officerAccountService;
    }
    @GetMapping("/officer-accounts/findByUserId/{id}")
    @Timed
    public ResponseEntity<OfficerAccountDTO> getROfficerAccountByUserId(@PathVariable Long id) {
        log.debug("REST request to get Resident : {}", id);
        OfficerAccountDTO officerAccountDTO = officerAccountService.findOneByUserId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(officerAccountDTO));
    }
    /**
     * POST  /officer-accounts : Create a new officerAccount.
     *
     * @param officerAccountDTO the officerAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new officerAccountDTO, or with status 400 (Bad Request) if the officerAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/officer-accounts")
    @Timed
    public ResponseEntity<OfficerAccountDTO> createOfficerAccount(@RequestBody OfficerAccountDTO officerAccountDTO) throws URISyntaxException {
        log.debug("REST request to save OfficerAccount : {}", officerAccountDTO);
        if (officerAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new officerAccount cannot already have an ID")).body(null);
        }
        OfficerAccountDTO result = officerAccountService.save(officerAccountDTO);
        return ResponseEntity.created(new URI("/api/officer-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /officer-accounts : Updates an existing officerAccount.
     *
     * @param officerAccountDTO the officerAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated officerAccountDTO,
     * or with status 400 (Bad Request) if the officerAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the officerAccountDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/officer-accounts")
    @Timed
    public ResponseEntity<OfficerAccountDTO> updateOfficerAccount(@RequestBody OfficerAccountDTO officerAccountDTO) throws URISyntaxException {
        log.debug("REST request to update OfficerAccount : {}", officerAccountDTO);
        if (officerAccountDTO.getId() == null) {
            return createOfficerAccount(officerAccountDTO);
        }
        OfficerAccountDTO result = officerAccountService.save(officerAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, officerAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /officer-accounts : get all the officerAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of officerAccounts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/officer-accounts")
    @Timed
    public ResponseEntity<List<OfficerAccountDTO>> getAllOfficerAccounts(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OfficerAccounts");
        Page<OfficerAccountDTO> page = officerAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/officer-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /officer-accounts/:id : get the "id" officerAccount.
     *
     * @param id the id of the officerAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the officerAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/officer-accounts/{id}")
    @Timed
    public ResponseEntity<OfficerAccountDTO> getOfficerAccount(@PathVariable Long id) {
        log.debug("REST request to get OfficerAccount : {}", id);
        OfficerAccountDTO officerAccountDTO = officerAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(officerAccountDTO));
    }

    /**
     * DELETE  /officer-accounts/:id : delete the "id" officerAccount.
     *
     * @param id the id of the officerAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/officer-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteOfficerAccount(@PathVariable Long id) {
        log.debug("REST request to delete OfficerAccount : {}", id);
        officerAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
