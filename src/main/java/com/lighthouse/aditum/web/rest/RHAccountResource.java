package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.RHAccountService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.RHAccountDTO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing RHAccount.
 */
@RestController
@RequestMapping("/api")
public class RHAccountResource {

    private final Logger log = LoggerFactory.getLogger(RHAccountResource.class);

    private static final String ENTITY_NAME = "rHAccount";
        
    private final RHAccountService rHAccountService;

    public RHAccountResource(RHAccountService rHAccountService) {
        this.rHAccountService = rHAccountService;
    }

    /**
     * POST  /r-h-accounts : Create a new rHAccount.
     *
     * @param rHAccountDTO the rHAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rHAccountDTO, or with status 400 (Bad Request) if the rHAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/r-h-accounts")
    @Timed
    public ResponseEntity<RHAccountDTO> createRHAccount(@Valid @RequestBody RHAccountDTO rHAccountDTO) throws URISyntaxException {
        log.debug("REST request to save RHAccount : {}", rHAccountDTO);
        if (rHAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rHAccount cannot already have an ID")).body(null);
        }
        RHAccountDTO result = rHAccountService.save(rHAccountDTO);
        return ResponseEntity.created(new URI("/api/r-h-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /r-h-accounts : Updates an existing rHAccount.
     *
     * @param rHAccountDTO the rHAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rHAccountDTO,
     * or with status 400 (Bad Request) if the rHAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the rHAccountDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/r-h-accounts")
    @Timed
    public ResponseEntity<RHAccountDTO> updateRHAccount(@Valid @RequestBody RHAccountDTO rHAccountDTO) throws URISyntaxException {
        log.debug("REST request to update RHAccount : {}", rHAccountDTO);
        if (rHAccountDTO.getId() == null) {
            return createRHAccount(rHAccountDTO);
        }
        RHAccountDTO result = rHAccountService.save(rHAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rHAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /r-h-accounts : get all the rHAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rHAccounts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/r-h-accounts")
    @Timed
    public ResponseEntity<List<RHAccountDTO>> getAllRHAccounts(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RHAccounts");
        Page<RHAccountDTO> page = rHAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/r-h-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /r-h-accounts/:id : get the "id" rHAccount.
     *
     * @param id the id of the rHAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rHAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/r-h-accounts/{id}")
    @Timed
    public ResponseEntity<RHAccountDTO> getRHAccount(@PathVariable Long id) {
        log.debug("REST request to get RHAccount : {}", id);
        RHAccountDTO rHAccountDTO = rHAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rHAccountDTO));
    }

    /**
     * DELETE  /r-h-accounts/:id : delete the "id" rHAccount.
     *
     * @param id the id of the rHAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/r-h-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteRHAccount(@PathVariable Long id) {
        log.debug("REST request to delete RHAccount : {}", id);
        rHAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
