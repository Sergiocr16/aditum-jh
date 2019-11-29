package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.CompanyConfigurationService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.CompanyConfigurationDTO;
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
 * REST controller for managing CompanyConfiguration.
 */
@RestController
@RequestMapping("/api")
public class CompanyConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(CompanyConfigurationResource.class);

    private static final String ENTITY_NAME = "companyConfiguration";

    private final CompanyConfigurationService companyConfigurationService;

    public CompanyConfigurationResource(CompanyConfigurationService companyConfigurationService) {
        this.companyConfigurationService = companyConfigurationService;
    }

    /**
     * POST  /company-configurations : Create a new companyConfiguration.
     *
     * @param companyConfigurationDTO the companyConfigurationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyConfigurationDTO, or with status 400 (Bad Request) if the companyConfiguration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-configurations")
    @Timed
    public ResponseEntity<CompanyConfigurationDTO> createCompanyConfiguration(@Valid @RequestBody CompanyConfigurationDTO companyConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to save CompanyConfiguration : {}", companyConfigurationDTO);
        if (companyConfigurationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new companyConfiguration cannot already have an ID")).body(null);
        }
        CompanyConfigurationDTO result = companyConfigurationService.save(companyConfigurationDTO);
        return ResponseEntity.created(new URI("/api/company-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-configurations : Updates an existing companyConfiguration.
     *
     * @param companyConfigurationDTO the companyConfigurationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyConfigurationDTO,
     * or with status 400 (Bad Request) if the companyConfigurationDTO is not valid,
     * or with status 500 (Internal Server Error) if the companyConfigurationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-configurations")
    @Timed
    public ResponseEntity<CompanyConfigurationDTO> updateCompanyConfiguration(@Valid @RequestBody CompanyConfigurationDTO companyConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to update CompanyConfiguration : {}", companyConfigurationDTO);
        if (companyConfigurationDTO.getId() == null) {
            return createCompanyConfiguration(companyConfigurationDTO);
        }
        CompanyConfigurationDTO result = companyConfigurationService.save(companyConfigurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyConfigurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-configurations : get all the companyConfigurations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companyConfigurations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/company-configurations")
    @Timed
    public ResponseEntity<List<CompanyConfigurationDTO>> getAllCompanyConfigurations(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CompanyConfigurations");
        Page<CompanyConfigurationDTO> page = companyConfigurationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-configurations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-configurations/:id : get the "id" companyConfiguration.
     *
     * @param id the id of the companyConfigurationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyConfigurationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/company-configurations/{id}")
    @Timed
    public ResponseEntity<CompanyConfigurationDTO> getCompanyConfiguration(@PathVariable Long id) {
        log.debug("REST request to get CompanyConfiguration : {}", id);
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(companyConfigurationDTO));
    }

    /**
     * DELETE  /company-configurations/:id : delete the "id" companyConfiguration.
     *
     * @param id the id of the companyConfigurationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-configurations/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete CompanyConfiguration : {}", id);
        companyConfigurationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/getByCompanyId")
    @Timed
    public ResponseEntity<List<CompanyConfigurationDTO>> getByCompanyId(@ApiParam Pageable pageable, Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<CompanyConfigurationDTO> page = companyConfigurationService.getByCompanyId(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/getByCompanyId");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
