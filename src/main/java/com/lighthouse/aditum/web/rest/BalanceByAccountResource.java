package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.BalanceByAccountService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.BalanceByAccountDTO;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BalanceByAccount.
 */
@RestController
@RequestMapping("/api")
public class BalanceByAccountResource {

    private final Logger log = LoggerFactory.getLogger(BalanceByAccountResource.class);

    private static final String ENTITY_NAME = "balanceByAccount";

    private final BalanceByAccountService balanceByAccountService;

    public BalanceByAccountResource(BalanceByAccountService balanceByAccountService) {
        this.balanceByAccountService = balanceByAccountService;
    }

    /**
     * POST  /balance-by-accounts : Create a new balanceByAccount.
     *
     * @param balanceByAccountDTO the balanceByAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new balanceByAccountDTO, or with status 400 (Bad Request) if the balanceByAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/balance-by-accounts")
    @Timed
    public ResponseEntity<BalanceByAccountDTO> createBalanceByAccount(@RequestBody BalanceByAccountDTO balanceByAccountDTO) throws URISyntaxException {
        log.debug("REST request to save BalanceByAccount : {}", balanceByAccountDTO);
        if (balanceByAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new balanceByAccount cannot already have an ID")).body(null);
        }
        BalanceByAccountDTO result = balanceByAccountService.save(balanceByAccountDTO);
        return ResponseEntity.created(new URI("/api/balance-by-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /balance-by-accounts : Updates an existing balanceByAccount.
     *
     * @param balanceByAccountDTO the balanceByAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated balanceByAccountDTO,
     * or with status 400 (Bad Request) if the balanceByAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the balanceByAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/balance-by-accounts")
    @Timed
    public ResponseEntity<BalanceByAccountDTO> updateBalanceByAccount(@RequestBody BalanceByAccountDTO balanceByAccountDTO) throws URISyntaxException {
        log.debug("REST request to update BalanceByAccount : {}", balanceByAccountDTO);
        if (balanceByAccountDTO.getId() == null) {
            return createBalanceByAccount(balanceByAccountDTO);
        }
        BalanceByAccountDTO result = balanceByAccountService.save(balanceByAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, balanceByAccountDTO.getId().toString()))
            .body(result);
    }
    @GetMapping("/balance-by-account/between/{initial_time}/{final_time}/byAccount/{accountId}")
    @Timed
    public ResponseEntity<List<BalanceByAccountDTO>> getBetweenDatesAndCompanyAndAccount(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "accountId")  Long accountId,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<BalanceByAccountDTO> page = balanceByAccountService.findByDatesBetweenAndAccount(pageable,initial_time,final_time,accountId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/balance-by-account");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /balance-by-accounts : get all the balanceByAccounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of balanceByAccounts in body
     */
    @GetMapping("/balance-by-accounts")
    @Timed
    public List<BalanceByAccountDTO> getAllBalanceByAccounts() {
        log.debug("REST request to get all BalanceByAccounts");
        return balanceByAccountService.findAll();
    }

    /**
     * GET  /balance-by-accounts/:id : get the "id" balanceByAccount.
     *
     * @param id the id of the balanceByAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the balanceByAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/balance-by-accounts/{id}")
    @Timed
    public ResponseEntity<BalanceByAccountDTO> getBalanceByAccount(@PathVariable Long id) {
        log.debug("REST request to get BalanceByAccount : {}", id);
        BalanceByAccountDTO balanceByAccountDTO = balanceByAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(balanceByAccountDTO));
    }

    /**
     * DELETE  /balance-by-accounts/:id : delete the "id" balanceByAccount.
     *
     * @param id the id of the balanceByAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/balance-by-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteBalanceByAccount(@PathVariable Long id) {
        log.debug("REST request to delete BalanceByAccount : {}", id);
        balanceByAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
