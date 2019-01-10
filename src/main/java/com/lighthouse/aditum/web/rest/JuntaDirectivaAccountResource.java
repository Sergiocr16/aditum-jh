package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.JuntaDirectivaAccountService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.JuntaDirectivaAccountDTO;
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
 * REST controller for managing JuntaDirectivaAccount.
 */
@RestController
@RequestMapping("/api")
public class JuntaDirectivaAccountResource {

    private final Logger log = LoggerFactory.getLogger(JuntaDirectivaAccountResource.class);

    private static final String ENTITY_NAME = "juntaDirectivaAccount";

    private final JuntaDirectivaAccountService juntaDirectivaAccountService;

    public JuntaDirectivaAccountResource(JuntaDirectivaAccountService juntaDirectivaAccountService) {
        this.juntaDirectivaAccountService = juntaDirectivaAccountService;
    }

    /**
     * POST  /junta-directiva-accounts : Create a new juntaDirectivaAccount.
     *
     * @param juntaDirectivaAccountDTO the juntaDirectivaAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new juntaDirectivaAccountDTO, or with status 400 (Bad Request) if the juntaDirectivaAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/junta-directiva-accounts")
    @Timed
    public ResponseEntity<JuntaDirectivaAccountDTO> createJuntaDirectivaAccount(@Valid @RequestBody JuntaDirectivaAccountDTO juntaDirectivaAccountDTO) throws URISyntaxException {
        log.debug("REST request to save JuntaDirectivaAccount : {}", juntaDirectivaAccountDTO);
        if (juntaDirectivaAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new juntaDirectivaAccount cannot already have an ID")).body(null);
        }
        JuntaDirectivaAccountDTO result = juntaDirectivaAccountService.save(juntaDirectivaAccountDTO);
        return ResponseEntity.created(new URI("/api/junta-directiva-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    @GetMapping("/junta-directiva-accounts/findByCompanyId/{companyId}")
    @Timed
    public ResponseEntity<JuntaDirectivaAccountDTO> getByCompanyId(@PathVariable(value = "companyId")  Long  companyId) {
        int a = 34;
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountService.findByCompanyId(companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(juntaDirectivaAccountDTO));
    }
    /**
     * PUT  /junta-directiva-accounts : Updates an existing juntaDirectivaAccount.
     *
     * @param juntaDirectivaAccountDTO the juntaDirectivaAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated juntaDirectivaAccountDTO,
     * or with status 400 (Bad Request) if the juntaDirectivaAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the juntaDirectivaAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/junta-directiva-accounts")
    @Timed
    public ResponseEntity<JuntaDirectivaAccountDTO> updateJuntaDirectivaAccount(@Valid @RequestBody JuntaDirectivaAccountDTO juntaDirectivaAccountDTO) throws URISyntaxException {
        log.debug("REST request to update JuntaDirectivaAccount : {}", juntaDirectivaAccountDTO);
        if (juntaDirectivaAccountDTO.getId() == null) {
            return createJuntaDirectivaAccount(juntaDirectivaAccountDTO);
        }
        JuntaDirectivaAccountDTO result = juntaDirectivaAccountService.save(juntaDirectivaAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, juntaDirectivaAccountDTO.getId().toString()))
            .body(result);
    }
    @GetMapping("/junta-directiva-accounts//findByUserId/{id}")
    @Timed
    public ResponseEntity<JuntaDirectivaAccountDTO> getByUserId(@PathVariable Long id) {
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountService.findOneByUserId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(juntaDirectivaAccountDTO));
    }
    /**
     * GET  /junta-directiva-accounts : get all the juntaDirectivaAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of juntaDirectivaAccounts in body
     */
    @GetMapping("/junta-directiva-accounts")
    @Timed
    public ResponseEntity<List<JuntaDirectivaAccountDTO>> getAllJuntaDirectivaAccounts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of JuntaDirectivaAccounts");
        Page<JuntaDirectivaAccountDTO> page = juntaDirectivaAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/junta-directiva-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /junta-directiva-accounts/:id : get the "id" juntaDirectivaAccount.
     *
     * @param id the id of the juntaDirectivaAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the juntaDirectivaAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/junta-directiva-accounts/{id}")
    @Timed
    public ResponseEntity<JuntaDirectivaAccountDTO> getJuntaDirectivaAccount(@PathVariable Long id) {
        log.debug("REST request to get JuntaDirectivaAccount : {}", id);
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(juntaDirectivaAccountDTO));
    }

    /**
     * DELETE  /junta-directiva-accounts/:id : delete the "id" juntaDirectivaAccount.
     *
     * @param id the id of the juntaDirectivaAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/junta-directiva-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteJuntaDirectivaAccount(@PathVariable Long id) {
        log.debug("REST request to delete JuntaDirectivaAccount : {}", id);
        juntaDirectivaAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
