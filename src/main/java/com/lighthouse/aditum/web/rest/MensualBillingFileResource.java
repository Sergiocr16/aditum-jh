package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.MensualBillingFileService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.MensualBillingFileDTO;
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
 * REST controller for managing MensualBillingFile.
 */
@RestController
@RequestMapping("/api")
public class MensualBillingFileResource {

    private final Logger log = LoggerFactory.getLogger(MensualBillingFileResource.class);

    private static final String ENTITY_NAME = "mensualBillingFile";

    private final MensualBillingFileService mensualBillingFileService;

    public MensualBillingFileResource(MensualBillingFileService mensualBillingFileService) {
        this.mensualBillingFileService = mensualBillingFileService;
    }

    /**
     * POST  /mensual-billing-files : Create a new mensualBillingFile.
     *
     * @param mensualBillingFileDTO the mensualBillingFileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mensualBillingFileDTO, or with status 400 (Bad Request) if the mensualBillingFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mensual-billing-files")
    @Timed
    public ResponseEntity<MensualBillingFileDTO> createMensualBillingFile(@RequestBody MensualBillingFileDTO mensualBillingFileDTO) throws URISyntaxException {
        log.debug("REST request to save MensualBillingFile : {}", mensualBillingFileDTO);
        if (mensualBillingFileDTO.getId() != null) {
//            throw new BadRequestAlertException("A new mensualBillingFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MensualBillingFileDTO result = mensualBillingFileService.save(mensualBillingFileDTO);
        return ResponseEntity.created(new URI("/api/mensual-billing-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mensual-billing-files : Updates an existing mensualBillingFile.
     *
     * @param mensualBillingFileDTO the mensualBillingFileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mensualBillingFileDTO,
     * or with status 400 (Bad Request) if the mensualBillingFileDTO is not valid,
     * or with status 500 (Internal Server Error) if the mensualBillingFileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mensual-billing-files")
    @Timed
    public ResponseEntity<MensualBillingFileDTO> updateMensualBillingFile(@RequestBody MensualBillingFileDTO mensualBillingFileDTO) throws URISyntaxException {
        log.debug("REST request to update MensualBillingFile : {}", mensualBillingFileDTO);
        if (mensualBillingFileDTO.getId() == null) {
            return createMensualBillingFile(mensualBillingFileDTO);
        }
        MensualBillingFileDTO result = mensualBillingFileService.save(mensualBillingFileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mensualBillingFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mensual-billing-files : get all the mensualBillingFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mensualBillingFiles in body
     */
    @GetMapping("/mensual-billing-files/{companyId}/{month}/{year}")
    @Timed
    public List<MensualBillingFileDTO> getAllMensualBillingFiles(@PathVariable Long companyId, @PathVariable int month, @PathVariable String year) {
        log.debug("REST request to get all MensualBillingFiles");
        return mensualBillingFileService.findAll(companyId, month, year);
    }

    @GetMapping("/mensual-billing-files-resident/{companyId}/{month}/{year}")
    @Timed
    public List<MensualBillingFileDTO> getAllMensualBillingFilesResident(@PathVariable Long companyId, @PathVariable int month, @PathVariable String year) {
        log.debug("REST request to get all MensualBillingFiles");
        return mensualBillingFileService.findAllPrivacy(companyId, month, year);
    }

    /**
     * GET  /mensual-billing-files/:id : get the "id" mensualBillingFile.
     *
     * @param id the id of the mensualBillingFileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mensualBillingFileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mensual-billing-files/{id}")
    @Timed
    public ResponseEntity<MensualBillingFileDTO> getMensualBillingFile(@PathVariable Long id) {
        log.debug("REST request to get MensualBillingFile : {}", id);
        MensualBillingFileDTO mensualBillingFileDTO = mensualBillingFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mensualBillingFileDTO));
    }

    /**
     * DELETE  /mensual-billing-files/:id : delete the "id" mensualBillingFile.
     *
     * @param id the id of the mensualBillingFileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mensual-billing-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteMensualBillingFile(@PathVariable Long id) {
        log.debug("REST request to delete MensualBillingFile : {}", id);
        mensualBillingFileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
