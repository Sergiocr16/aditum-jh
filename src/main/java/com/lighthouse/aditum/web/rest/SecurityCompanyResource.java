package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.domain.SecurityCompany;

import com.lighthouse.aditum.repository.SecurityCompanyRepository;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.SecurityCompanyDTO;
import com.lighthouse.aditum.service.mapper.SecurityCompanyMapper;
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
 * REST controller for managing SecurityCompany.
 */
@RestController
@RequestMapping("/api")
public class SecurityCompanyResource {

    private final Logger log = LoggerFactory.getLogger(SecurityCompanyResource.class);

    private static final String ENTITY_NAME = "securityCompany";

    private final SecurityCompanyRepository securityCompanyRepository;

    private final SecurityCompanyMapper securityCompanyMapper;

    public SecurityCompanyResource(SecurityCompanyRepository securityCompanyRepository, SecurityCompanyMapper securityCompanyMapper) {
        this.securityCompanyRepository = securityCompanyRepository;
        this.securityCompanyMapper = securityCompanyMapper;
    }

    /**
     * POST  /security-companies : Create a new securityCompany.
     *
     * @param securityCompanyDTO the securityCompanyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new securityCompanyDTO, or with status 400 (Bad Request) if the securityCompany has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/security-companies")
    @Timed
    public ResponseEntity<SecurityCompanyDTO> createSecurityCompany(@RequestBody SecurityCompanyDTO securityCompanyDTO) throws URISyntaxException {
        log.debug("REST request to save SecurityCompany : {}", securityCompanyDTO);
        if (securityCompanyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new securityCompany cannot already have an ID")).body(null);
        }
        SecurityCompany securityCompany = securityCompanyMapper.toEntity(securityCompanyDTO);
        securityCompany = securityCompanyRepository.save(securityCompany);
        SecurityCompanyDTO result = securityCompanyMapper.toDto(securityCompany);
        return ResponseEntity.created(new URI("/api/security-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /security-companies : Updates an existing securityCompany.
     *
     * @param securityCompanyDTO the securityCompanyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated securityCompanyDTO,
     * or with status 400 (Bad Request) if the securityCompanyDTO is not valid,
     * or with status 500 (Internal Server Error) if the securityCompanyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/security-companies")
    @Timed
    public ResponseEntity<SecurityCompanyDTO> updateSecurityCompany(@RequestBody SecurityCompanyDTO securityCompanyDTO) throws URISyntaxException {
        log.debug("REST request to update SecurityCompany : {}", securityCompanyDTO);
        if (securityCompanyDTO.getId() == null) {
            return createSecurityCompany(securityCompanyDTO);
        }
        SecurityCompany securityCompany = securityCompanyMapper.toEntity(securityCompanyDTO);
        securityCompany = securityCompanyRepository.save(securityCompany);
        SecurityCompanyDTO result = securityCompanyMapper.toDto(securityCompany);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, securityCompanyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /security-companies : get all the securityCompanies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of securityCompanies in body
     */
    @GetMapping("/security-companies")
    @Timed
    public ResponseEntity<List<SecurityCompanyDTO>> getAllSecurityCompanies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SecurityCompanies");
        Page<SecurityCompany> page = securityCompanyRepository.findAll(pageable);
        HttpHeaders headers = null;
        try {
             headers =   PaginationUtil.generatePaginationHttpHeaders(page, "/api/security-companies");
        }catch(Exception e){
        }
        return new ResponseEntity<>(securityCompanyMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /security-companies/:id : get the "id" securityCompany.
     *
     * @param id the id of the securityCompanyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the securityCompanyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/security-companies/{id}")
    @Timed
    public ResponseEntity<SecurityCompanyDTO> getSecurityCompany(@PathVariable Long id) {
        log.debug("REST request to get SecurityCompany : {}", id);
        SecurityCompany securityCompany = securityCompanyRepository.findOne(id);
        SecurityCompanyDTO securityCompanyDTO = securityCompanyMapper.toDto(securityCompany);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(securityCompanyDTO));
    }

    /**
     * DELETE  /security-companies/:id : delete the "id" securityCompany.
     *
     * @param id the id of the securityCompanyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/security-companies/{id}")
    @Timed
    public ResponseEntity<Void> deleteSecurityCompany(@PathVariable Long id) {
        log.debug("REST request to delete SecurityCompany : {}", id);
        securityCompanyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
