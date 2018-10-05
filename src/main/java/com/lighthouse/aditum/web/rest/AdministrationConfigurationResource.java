package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AdministrationConfigurationService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.AdministrationConfigurationDTO;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AdministrationConfiguration.
 */
@RestController
@RequestMapping("/api")
public class AdministrationConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(AdministrationConfigurationResource.class);

    private static final String ENTITY_NAME = "administrationConfiguration";

    private final AdministrationConfigurationService administrationConfigurationService;

    public AdministrationConfigurationResource(AdministrationConfigurationService administrationConfigurationService) {
        this.administrationConfigurationService = administrationConfigurationService;
    }

    /**
     * POST  /administration-configurations : Create a new administrationConfiguration.
     *
     * @param administrationConfigurationDTO the administrationConfigurationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new administrationConfigurationDTO, or with status 400 (Bad Request) if the administrationConfiguration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/administration-configurations")
    @Timed
    public ResponseEntity<AdministrationConfigurationDTO> createAdministrationConfiguration(@RequestBody AdministrationConfigurationDTO administrationConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to save AdministrationConfiguration : {}", administrationConfigurationDTO);
        if (administrationConfigurationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new administrationConfiguration cannot already have an ID")).body(null);
        }
         administrationConfigurationDTO.setHasSubcharges(true);
        AdministrationConfigurationDTO result = administrationConfigurationService.save(administrationConfigurationDTO);
        return ResponseEntity.created(new URI("/api/administration-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /administration-configurations : Updates an existing administrationConfiguration.
     *
     * @param administrationConfigurationDTO the administrationConfigurationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated administrationConfigurationDTO,
     * or with status 400 (Bad Request) if the administrationConfigurationDTO is not valid,
     * or with status 500 (Internal Server Error) if the administrationConfigurationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/administration-configurations")
    @Timed
    public ResponseEntity<AdministrationConfigurationDTO> updateAdministrationConfiguration(@RequestBody AdministrationConfigurationDTO administrationConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to update AdministrationConfiguration : {}", administrationConfigurationDTO);
        if (administrationConfigurationDTO.getId() == null) {
            return createAdministrationConfiguration(administrationConfigurationDTO);
        }
        AdministrationConfigurationDTO result = administrationConfigurationService.save(administrationConfigurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, administrationConfigurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /administration-configurations : get all the administrationConfigurations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of administrationConfigurations in body
     */
    @GetMapping("/administration-configurations")
    @Timed
    public ResponseEntity<List<AdministrationConfigurationDTO>> getAllAdministrationConfigurations(@ApiParam Pageable pageable)
            throws URISyntaxException {

            log.debug("REST request to get a page of AdministrationConfigurations");
        Page<AdministrationConfigurationDTO> page = administrationConfigurationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/administration-configurations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /administration-configurations/:id : get the "id" administrationConfiguration.
     *
     * @param id the id of the administrationConfigurationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the administrationConfigurationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/administration-configurationsByCompanyId/{companyId}")
    @Timed
    public ResponseEntity<AdministrationConfigurationDTO> getAdministrationConfiguration(@PathVariable Long companyId) {
        log.debug("REST request to get AdministrationConfiguration : {}", companyId);
        AdministrationConfigurationDTO administrationConfigurationDTO = administrationConfigurationService.findOneByCompanyId(companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(administrationConfigurationDTO));
    }

    /**
     * DELETE  /administration-configurations/:id : delete the "id" administrationConfiguration.
     *
     * @param id the id of the administrationConfigurationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/administration-configurations/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdministrationConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete AdministrationConfiguration : {}", id);
        administrationConfigurationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
