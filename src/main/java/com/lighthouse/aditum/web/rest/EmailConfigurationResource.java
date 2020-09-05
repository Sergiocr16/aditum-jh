package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.EmailConfigurationService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.EmailConfigurationDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EmailConfiguration.
 */
@RestController
@RequestMapping("/api")
public class EmailConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(EmailConfigurationResource.class);

    private static final String ENTITY_NAME = "emailConfiguration";

    private final EmailConfigurationService emailConfigurationService;

    public EmailConfigurationResource(EmailConfigurationService emailConfigurationService) {
        this.emailConfigurationService = emailConfigurationService;
    }

    /**
     * POST  /email-configurations : Create a new emailConfiguration.
     *
     * @param emailConfigurationDTO the emailConfigurationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new emailConfigurationDTO, or with status 400 (Bad Request) if the emailConfiguration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/email-configurations")
    @Timed
    public ResponseEntity<EmailConfigurationDTO> createEmailConfiguration(@RequestBody EmailConfigurationDTO emailConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to save EmailConfiguration : {}", emailConfigurationDTO);
        if (emailConfigurationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new emergency cannot already have an ID")).body(null);
        }
        EmailConfigurationDTO result = emailConfigurationService.save(emailConfigurationDTO);
        return ResponseEntity.created(new URI("/api/email-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /email-configurations : Updates an existing emailConfiguration.
     *
     * @param emailConfigurationDTO the emailConfigurationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated emailConfigurationDTO,
     * or with status 400 (Bad Request) if the emailConfigurationDTO is not valid,
     * or with status 500 (Internal Server Error) if the emailConfigurationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/email-configurations")
    @Timed
    public ResponseEntity<EmailConfigurationDTO> updateEmailConfiguration(@RequestBody EmailConfigurationDTO emailConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to update EmailConfiguration : {}", emailConfigurationDTO);
        if (emailConfigurationDTO.getId() == null) {
            return createEmailConfiguration(emailConfigurationDTO);
        }
        EmailConfigurationDTO result = emailConfigurationService.save(emailConfigurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emailConfigurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /email-configurations : get all the emailConfigurations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of emailConfigurations in body
     */
    @GetMapping("/email-configurations")
    @Timed
    public List<EmailConfigurationDTO> getAllEmailConfigurations() {
        log.debug("REST request to get all EmailConfigurations");
        return emailConfigurationService.findAll();
        }

    /**
     * GET  /email-configurations/:id : get the "id" emailConfiguration.
     *
     * @param id the id of the emailConfigurationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the emailConfigurationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/email-configurations/{id}")
    @Timed
    public ResponseEntity<EmailConfigurationDTO> getEmailConfiguration(@PathVariable Long id) {
        log.debug("REST request to get EmailConfiguration : {}", id);
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(emailConfigurationDTO));
    }

    @GetMapping("/email-configurations/by-company/{companyId}")
    @Timed
    public ResponseEntity<EmailConfigurationDTO> getEmailConfigurationByCompany(@PathVariable Long companyId) {
        log.debug("REST request to get EmailConfiguration : {}", companyId);
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationService.findOneByCompanyId(companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(emailConfigurationDTO));
    }

    /**
     * DELETE  /email-configurations/:id : delete the "id" emailConfiguration.
     *
     * @param id the id of the emailConfigurationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/email-configurations/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmailConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete EmailConfiguration : {}", id);
        emailConfigurationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
