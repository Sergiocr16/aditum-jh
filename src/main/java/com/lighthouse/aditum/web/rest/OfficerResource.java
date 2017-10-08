package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.OfficerService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.OfficerDTO;
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
 * REST controller for managing Officer.
 */
@RestController
@RequestMapping("/api")
public class OfficerResource {

    private final Logger log = LoggerFactory.getLogger(OfficerResource.class);

    private static final String ENTITY_NAME = "officer";

    private final OfficerService officerService;

    public OfficerResource(OfficerService officerService) {
        this.officerService = officerService;
    }

    /**
     * POST  /officers : Create a new officer.
     *
     * @param officerDTO the officerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new officerDTO, or with status 400 (Bad Request) if the officer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/officers")
    @Timed
    public ResponseEntity<OfficerDTO> createOfficer(@Valid @RequestBody OfficerDTO officerDTO) throws URISyntaxException {
        log.debug("REST request to save Officer : {}", officerDTO);
        if (officerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new officer cannot already have an ID")).body(null);
        }
        OfficerDTO result = officerService.save(officerDTO);
        return ResponseEntity.created(new URI("/api/officers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /officers : Updates an existing officer.
     *
     * @param officerDTO the officerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated officerDTO,
     * or with status 400 (Bad Request) if the officerDTO is not valid,
     * or with status 500 (Internal Server Error) if the officerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/officers")
    @Timed
    public ResponseEntity<OfficerDTO> updateOfficer(@Valid @RequestBody OfficerDTO officerDTO) throws URISyntaxException {
        log.debug("REST request to update Officer : {}", officerDTO);
        if (officerDTO.getId() == null) {
            return createOfficer(officerDTO);
        }
        OfficerDTO result = officerService.save(officerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, officerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /officers : get all the officers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of officers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/officers")
    @Timed
    public ResponseEntity<List<OfficerDTO>> getAllOfficers(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Officers");
        Page<OfficerDTO> page = officerService.findAll(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/officers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/officersEnabled")
    @Timed
    public ResponseEntity<List<OfficerDTO>> getEnabledOfficers(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<OfficerDTO> page = officerService.findEnabled(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/officersEnabled");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/officersDisabled")
    @Timed
    public ResponseEntity<List<OfficerDTO>> getDisabledOfficers(@ApiParam Pageable pageable, Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<OfficerDTO> page = officerService.findDisabled(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/officersDisabled");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/officersDisabledEnterprise")
    @Timed
    public ResponseEntity<List<OfficerDTO>> getDisabledOfficersEnterprise(@ApiParam Pageable pageable, Long rhAccountId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<OfficerDTO> page = officerService.findDisabledByEnterprise(pageable,rhAccountId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/officersDisabled");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/officersEnabledEnterprise")
    @Timed
    public ResponseEntity<List<OfficerDTO>> getEnabledfficersEnterprise(@ApiParam Pageable pageable, Long rhAccountId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<OfficerDTO> page = officerService.findEnabledByEnterprise(pageable,rhAccountId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/officersDisabled");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /officers/:id : get the "id" officer.
     *
     * @param id the id of the officerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the officerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/officers/{id}")
    @Timed
    public ResponseEntity<OfficerDTO> getOfficer(@PathVariable Long id) {
        log.debug("REST request to get Officer : {}", id);
        OfficerDTO officerDTO = officerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(officerDTO));
    }
    @GetMapping("/officers/findByCompanyAndIdentification/{companyId}/{identificationID}")
    @Timed
    public ResponseEntity<OfficerDTO> getOfficer(@PathVariable(value = "identificationID")  String  identificationID,
                                                   @PathVariable(value = "companyId")  Long companyId) {

        OfficerDTO officerDTO = officerService.findOneByCompanyAndIdentification(companyId,identificationID);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(officerDTO));
    }


    /**
     * DELETE  /officers/:id : delete the "id" officer.
     *
     * @param id the id of the officerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/officers/{id}")
    @Timed
    public ResponseEntity<Void> deleteOfficer(@PathVariable Long id) {
        log.debug("REST request to delete Officer : {}", id);
        officerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
