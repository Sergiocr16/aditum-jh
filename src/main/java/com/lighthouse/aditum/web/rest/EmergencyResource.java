package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.EmergencyService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.EmergencyDTO;
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
 * REST controller for managing Emergency.
 */
@RestController
@RequestMapping("/api")
public class EmergencyResource {

    private final Logger log = LoggerFactory.getLogger(EmergencyResource.class);

    private static final String ENTITY_NAME = "emergency";
        
    private final EmergencyService emergencyService;

    public EmergencyResource(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    /**
     * POST  /emergencies : Create a new emergency.
     *
     * @param emergencyDTO the emergencyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new emergencyDTO, or with status 400 (Bad Request) if the emergency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/emergencies")
    @Timed
    public ResponseEntity<EmergencyDTO> createEmergency(@Valid @RequestBody EmergencyDTO emergencyDTO) throws URISyntaxException {
        log.debug("REST request to save Emergency : {}", emergencyDTO);
        if (emergencyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new emergency cannot already have an ID")).body(null);
        }
        EmergencyDTO result = emergencyService.save(emergencyDTO);
        return ResponseEntity.created(new URI("/api/emergencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /emergencies : Updates an existing emergency.
     *
     * @param emergencyDTO the emergencyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated emergencyDTO,
     * or with status 400 (Bad Request) if the emergencyDTO is not valid,
     * or with status 500 (Internal Server Error) if the emergencyDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/emergencies")
    @Timed
    public ResponseEntity<EmergencyDTO> updateEmergency(@Valid @RequestBody EmergencyDTO emergencyDTO) throws URISyntaxException {
        log.debug("REST request to update Emergency : {}", emergencyDTO);
        if (emergencyDTO.getId() == null) {
            return createEmergency(emergencyDTO);
        }
        EmergencyDTO result = emergencyService.save(emergencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emergencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /emergencies : get all the emergencies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of emergencies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/emergencies")
    @Timed
    public ResponseEntity<List<EmergencyDTO>> getAllEmergencies(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Emergencies");
        Page<EmergencyDTO> page = emergencyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/emergencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /emergencies/:id : get the "id" emergency.
     *
     * @param id the id of the emergencyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the emergencyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/emergencies/{id}")
    @Timed
    public ResponseEntity<EmergencyDTO> getEmergency(@PathVariable Long id) {
        log.debug("REST request to get Emergency : {}", id);
        EmergencyDTO emergencyDTO = emergencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(emergencyDTO));
    }

    /**
     * DELETE  /emergencies/:id : delete the "id" emergency.
     *
     * @param id the id of the emergencyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/emergencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmergency(@PathVariable Long id) {
        log.debug("REST request to delete Emergency : {}", id);
        emergencyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
