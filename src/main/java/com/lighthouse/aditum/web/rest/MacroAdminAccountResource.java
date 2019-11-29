package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.MacroAdminAccountService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.MacroAdminAccountDTO;
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
 * REST controller for managing MacroAdminAccount.
 */
@RestController
@RequestMapping("/api")
public class MacroAdminAccountResource {

    private final Logger log = LoggerFactory.getLogger(MacroAdminAccountResource.class);

    private static final String ENTITY_NAME = "macroAdminAccount";

    private final MacroAdminAccountService macroAdminAccountService;

    public MacroAdminAccountResource(MacroAdminAccountService macroAdminAccountService) {
        this.macroAdminAccountService = macroAdminAccountService;
    }

    /**
     * POST  /macro-admin-accounts : Create a new macroAdminAccount.
     *
     * @param macroAdminAccountDTO the macroAdminAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new macroAdminAccountDTO, or with status 400 (Bad Request) if the macroAdminAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/macro-admin-accounts")
    @Timed
    public ResponseEntity<MacroAdminAccountDTO> createMacroAdminAccount(@Valid @RequestBody MacroAdminAccountDTO macroAdminAccountDTO) throws URISyntaxException {
        log.debug("REST request to save MacroAdminAccount : {}", macroAdminAccountDTO);
        if (macroAdminAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new macroAdminAccount cannot already have an ID")).body(null);
        }
        MacroAdminAccountDTO result = macroAdminAccountService.save(macroAdminAccountDTO);
        return ResponseEntity.created(new URI("/api/macro-admin-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /macro-admin-accounts : Updates an existing macroAdminAccount.
     *
     * @param macroAdminAccountDTO the macroAdminAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated macroAdminAccountDTO,
     * or with status 400 (Bad Request) if the macroAdminAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the macroAdminAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/macro-admin-accounts")
    @Timed
    public ResponseEntity<MacroAdminAccountDTO> updateMacroAdminAccount(@Valid @RequestBody MacroAdminAccountDTO macroAdminAccountDTO) throws URISyntaxException {
        log.debug("REST request to update MacroAdminAccount : {}", macroAdminAccountDTO);
        if (macroAdminAccountDTO.getId() == null) {
            return createMacroAdminAccount(macroAdminAccountDTO);
        }
        MacroAdminAccountDTO result = macroAdminAccountService.save(macroAdminAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, macroAdminAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /macro-admin-accounts : get all the macroAdminAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of macroAdminAccounts in body
     */
    @GetMapping("/macro-admin-accounts")
    @Timed
    public ResponseEntity<List<MacroAdminAccountDTO>> getAllMacroAdminAccounts(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of MacroAdminAccounts");
        Page<MacroAdminAccountDTO> page = macroAdminAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/macro-admin-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/macro-admin-accounts/byMacro/{macroId}")
    @Timed
    public ResponseEntity<List<MacroAdminAccountDTO>> getAllMacroOfficerAccountsByMacro(Pageable pageable,@PathVariable Long macroId) throws URISyntaxException {
        log.debug("REST request to get a page of MacroOfficerAccounts");
        Page<MacroAdminAccountDTO> page = macroAdminAccountService.findAllByMacro(pageable,macroId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/macro-admin-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /macro-admin-accounts/:id : get the "id" macroAdminAccount.
     *
     * @param id the id of the macroAdminAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the macroAdminAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/macro-admin-accounts/{id}")
    @Timed
    public ResponseEntity<MacroAdminAccountDTO> getMacroAdminAccount(@PathVariable Long id) {
        log.debug("REST request to get MacroAdminAccount : {}", id);
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(macroAdminAccountDTO));
    }
    @GetMapping("/macro-admin-accounts/findByUserId/{id}")
    @Timed
    public ResponseEntity<MacroAdminAccountDTO> getByUserId(@PathVariable Long id) {
        MacroAdminAccountDTO juntaDirectivaAccountDTO = macroAdminAccountService.findOneByUserId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(juntaDirectivaAccountDTO));
    }
    /**
     * DELETE  /macro-admin-accounts/:id : delete the "id" macroAdminAccount.
     *
     * @param id the id of the macroAdminAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/macro-admin-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteMacroAdminAccount(@PathVariable Long id) {
        log.debug("REST request to delete MacroAdminAccount : {}", id);
        macroAdminAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
