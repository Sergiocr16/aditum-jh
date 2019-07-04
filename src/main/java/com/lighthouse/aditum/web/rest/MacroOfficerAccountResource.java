package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.MacroOfficerAccountService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.MacroOfficerAccountDTO;
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
 * REST controller for managing MacroOfficerAccount.
 */
@RestController
@RequestMapping("/api")
public class MacroOfficerAccountResource {

    private final Logger log = LoggerFactory.getLogger(MacroOfficerAccountResource.class);

    private static final String ENTITY_NAME = "macroOfficerAccount";

    private final MacroOfficerAccountService macroOfficerAccountService;

    public MacroOfficerAccountResource(MacroOfficerAccountService macroOfficerAccountService) {
        this.macroOfficerAccountService = macroOfficerAccountService;
    }

    /**
     * POST  /macro-officer-accounts : Create a new macroOfficerAccount.
     *
     * @param macroOfficerAccountDTO the macroOfficerAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new macroOfficerAccountDTO, or with status 400 (Bad Request) if the macroOfficerAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/macro-officer-accounts")
    @Timed
    public ResponseEntity<MacroOfficerAccountDTO> createMacroOfficerAccount(@Valid @RequestBody MacroOfficerAccountDTO macroOfficerAccountDTO) throws URISyntaxException {
        log.debug("REST request to save MacroOfficerAccount : {}", macroOfficerAccountDTO);
        if (macroOfficerAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new macroOfficerAccount cannot already have an ID")).body(null);
        }
        MacroOfficerAccountDTO result = macroOfficerAccountService.save(macroOfficerAccountDTO);
        return ResponseEntity.created(new URI("/api/macro-officer-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /macro-officer-accounts : Updates an existing macroOfficerAccount.
     *
     * @param macroOfficerAccountDTO the macroOfficerAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated macroOfficerAccountDTO,
     * or with status 400 (Bad Request) if the macroOfficerAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the macroOfficerAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/macro-officer-accounts")
    @Timed
    public ResponseEntity<MacroOfficerAccountDTO> updateMacroOfficerAccount(@Valid @RequestBody MacroOfficerAccountDTO macroOfficerAccountDTO) throws URISyntaxException {
        log.debug("REST request to update MacroOfficerAccount : {}", macroOfficerAccountDTO);
        if (macroOfficerAccountDTO.getId() == null) {
            return createMacroOfficerAccount(macroOfficerAccountDTO);
        }
        MacroOfficerAccountDTO result = macroOfficerAccountService.save(macroOfficerAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, macroOfficerAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /macro-officer-accounts : get all the macroOfficerAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of macroOfficerAccounts in body
     */
    @GetMapping("/macro-officer-accounts/")
    @Timed
    public ResponseEntity<List<MacroOfficerAccountDTO>> getAllMacroOfficerAccounts(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of MacroOfficerAccounts");
        Page<MacroOfficerAccountDTO> page = macroOfficerAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/macro-officer-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/macro-officer-accounts/byMacro/{macroId}")
    @Timed
    public ResponseEntity<List<MacroOfficerAccountDTO>> getAllMacroOfficerAccountsByMacro(Pageable pageable,@PathVariable Long macroId) throws URISyntaxException {
        log.debug("REST request to get a page of MacroOfficerAccounts");
        Page<MacroOfficerAccountDTO> page = macroOfficerAccountService.findAllByMacro(pageable,macroId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/macro-officer-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/macro-officer-accounts/findByUserId/{id}")
    @Timed
    public ResponseEntity<MacroOfficerAccountDTO> getByUserId(@PathVariable Long id) {
        MacroOfficerAccountDTO juntaDirectivaAccountDTO = macroOfficerAccountService.findOneByUserId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(juntaDirectivaAccountDTO));
    }
    /**
     * GET  /macro-officer-accounts/:id : get the "id" macroOfficerAccount.
     *
     * @param id the id of the macroOfficerAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the macroOfficerAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/macro-officer-accounts/{id}")
    @Timed
    public ResponseEntity<MacroOfficerAccountDTO> getMacroOfficerAccount(@PathVariable Long id) {
        log.debug("REST request to get MacroOfficerAccount : {}", id);
        MacroOfficerAccountDTO macroOfficerAccountDTO = macroOfficerAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(macroOfficerAccountDTO));
    }

    /**
     * DELETE  /macro-officer-accounts/:id : delete the "id" macroOfficerAccount.
     *
     * @param id the id of the macroOfficerAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/macro-officer-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteMacroOfficerAccount(@PathVariable Long id) {
        log.debug("REST request to delete MacroOfficerAccount : {}", id);
        macroOfficerAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
