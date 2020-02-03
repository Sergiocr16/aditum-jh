package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.CondominiumRecordService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.CondominiumRecordDTO;
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
 * REST controller for managing CondominiumRecord.
 */
@RestController
@RequestMapping("/api")
public class CondominiumRecordResource {

    private final Logger log = LoggerFactory.getLogger(CondominiumRecordResource.class);

    private static final String ENTITY_NAME = "condominiumRecord";

    private final CondominiumRecordService condominiumRecordService;

    public CondominiumRecordResource(CondominiumRecordService condominiumRecordService) {
        this.condominiumRecordService = condominiumRecordService;
    }

    /**
     * POST  /condominium-records : Create a new condominiumRecord.
     *
     * @param condominiumRecordDTO the condominiumRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new condominiumRecordDTO, or with status 400 (Bad Request) if the condominiumRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/condominium-records")
    @Timed
    public ResponseEntity<CondominiumRecordDTO> createCondominiumRecord(@RequestBody CondominiumRecordDTO condominiumRecordDTO) throws URISyntaxException {
        log.debug("REST request to save CondominiumRecord : {}", condominiumRecordDTO);
        if (condominiumRecordDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new complaint cannot already have an ID")).body(null);
        }
        CondominiumRecordDTO result = condominiumRecordService.save(condominiumRecordDTO);
        return ResponseEntity.created(new URI("/api/condominium-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /condominium-records : Updates an existing condominiumRecord.
     *
     * @param condominiumRecordDTO the condominiumRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated condominiumRecordDTO,
     * or with status 400 (Bad Request) if the condominiumRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the condominiumRecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/condominium-records")
    @Timed
    public ResponseEntity<CondominiumRecordDTO> updateCondominiumRecord(@RequestBody CondominiumRecordDTO condominiumRecordDTO) throws URISyntaxException {
        log.debug("REST request to update CondominiumRecord : {}", condominiumRecordDTO);
        if (condominiumRecordDTO.getId() == null) {
            return createCondominiumRecord(condominiumRecordDTO);
        }
        CondominiumRecordDTO result = condominiumRecordService.save(condominiumRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, condominiumRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /condominium-records : get all the condominiumRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of condominiumRecords in body
     */
    @GetMapping("/condominium-records/find-all/{companyId}")
    @Timed
    public ResponseEntity<List<CondominiumRecordDTO>> getAllCondominiumRecords(Pageable pageable, @PathVariable Long companyId) throws URISyntaxException {
        log.debug("REST request to get a page of CondominiumRecords");
        Page<CondominiumRecordDTO> page = condominiumRecordService.findAll(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/condominium-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /condominium-records/:id : get the "id" condominiumRecord.
     *
     * @param id the id of the condominiumRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the condominiumRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/condominium-records/{id}")
    @Timed
    public ResponseEntity<CondominiumRecordDTO> getCondominiumRecord(@PathVariable Long id) {
        log.debug("REST request to get CondominiumRecord : {}", id);
        CondominiumRecordDTO condominiumRecordDTO = condominiumRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(condominiumRecordDTO));
    }

    /**
     * DELETE  /condominium-records/:id : delete the "id" condominiumRecord.
     *
     * @param id the id of the condominiumRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/condominium-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteCondominiumRecord(@PathVariable Long id) {
        log.debug("REST request to delete CondominiumRecord : {}", id);
        CondominiumRecordDTO condominiumRecordDTO = this.condominiumRecordService.findOne(id);
        condominiumRecordDTO.setDeleted(1);
        this.condominiumRecordService.save(condominiumRecordDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
