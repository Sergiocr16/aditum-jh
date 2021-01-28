package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AccountingNoteService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.AccountingNoteDTO;
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
 * REST controller for managing AccountingNote.
 */
@RestController
@RequestMapping("/api")
public class AccountingNoteResource {

    private final Logger log = LoggerFactory.getLogger(AccountingNoteResource.class);

    private static final String ENTITY_NAME = "accountingNote";

    private final AccountingNoteService accountingNoteService;

    public AccountingNoteResource(AccountingNoteService accountingNoteService) {
        this.accountingNoteService = accountingNoteService;
    }

    /**
     * POST  /accounting-notes : Create a new accountingNote.
     *
     * @param accountingNoteDTO the accountingNoteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountingNoteDTO, or with status 400 (Bad Request) if the accountingNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounting-notes")
    @Timed
    public ResponseEntity<AccountingNoteDTO> createAccountingNote(@RequestBody AccountingNoteDTO accountingNoteDTO) throws URISyntaxException {
        log.debug("REST request to save AccountingNote : {}", accountingNoteDTO);
        if (accountingNoteDTO.getId() != null) {
//            throw new BadRequestAlertException("A new accountingNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountingNoteDTO result = accountingNoteService.save(accountingNoteDTO);
        return ResponseEntity.created(new URI("/api/accounting-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accounting-notes : Updates an existing accountingNote.
     *
     * @param accountingNoteDTO the accountingNoteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountingNoteDTO,
     * or with status 400 (Bad Request) if the accountingNoteDTO is not valid,
     * or with status 500 (Internal Server Error) if the accountingNoteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accounting-notes")
    @Timed
    public ResponseEntity<AccountingNoteDTO> updateAccountingNote(@RequestBody AccountingNoteDTO accountingNoteDTO) throws URISyntaxException {
        log.debug("REST request to update AccountingNote : {}", accountingNoteDTO);
        if (accountingNoteDTO.getId() == null) {
            return createAccountingNote(accountingNoteDTO);
        }
        AccountingNoteDTO result = accountingNoteService.save(accountingNoteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountingNoteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accounting-notes : get all the accountingNotes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingNotes in body
     */
    @GetMapping("/accounting-notes")
    @Timed
    public ResponseEntity<List<AccountingNoteDTO>> getAllAccountingNotes(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of AccountingNotes");
        Page<AccountingNoteDTO> page = accountingNoteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounting-notes : get all the accountingNotes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingNotes in body
     */
    @GetMapping("/accounting-notes-by-house/{houseId}")
    @Timed
    public ResponseEntity<List<AccountingNoteDTO>> getAllAccountingNotes(Pageable pageable, @PathVariable Long houseId) throws URISyntaxException {
        log.debug("REST request to get a page of AccountingNotes");
        Page<AccountingNoteDTO> page = accountingNoteService.findByHouse(pageable, houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounting-notes/:id : get the "id" accountingNote.
     *
     * @param id the id of the accountingNoteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountingNoteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/accounting-notes/{id}")
    @Timed
    public ResponseEntity<AccountingNoteDTO> getAccountingNote(@PathVariable Long id) {
        log.debug("REST request to get AccountingNote : {}", id);
        AccountingNoteDTO accountingNoteDTO = accountingNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountingNoteDTO));
    }

    /**
     * DELETE  /accounting-notes/:id : delete the "id" accountingNote.
     *
     * @param id the id of the accountingNoteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounting-notes/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountingNote(@PathVariable Long id) {
        log.debug("REST request to delete AccountingNote : {}", id);
        accountingNoteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
