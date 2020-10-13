package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.CustomChargeTypeService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.CustomChargeTypeDTO;
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
 * REST controller for managing CustomChargeType.
 */
@RestController
@RequestMapping("/api")
public class CustomChargeTypeResource {

    private final Logger log = LoggerFactory.getLogger(CustomChargeTypeResource.class);

    private static final String ENTITY_NAME = "customChargeType";

    private final CustomChargeTypeService customChargeTypeService;

    public CustomChargeTypeResource(CustomChargeTypeService customChargeTypeService) {
        this.customChargeTypeService = customChargeTypeService;
    }

    /**
     * POST  /custom-charge-types : Create a new customChargeType.
     *
     * @param customChargeTypeDTO the customChargeTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customChargeTypeDTO, or with status 400 (Bad Request) if the customChargeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/custom-charge-types")
    @Timed
    public ResponseEntity<CustomChargeTypeDTO> createCustomChargeType(@RequestBody CustomChargeTypeDTO customChargeTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CustomChargeType : {}", customChargeTypeDTO);
        if (customChargeTypeDTO.getId() != null) {
//            throw new BadRequestAlertException("A new customChargeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomChargeTypeDTO result = customChargeTypeService.save(customChargeTypeDTO);
        return ResponseEntity.created(new URI("/api/custom-charge-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /custom-charge-types : Updates an existing customChargeType.
     *
     * @param customChargeTypeDTO the customChargeTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customChargeTypeDTO,
     * or with status 400 (Bad Request) if the customChargeTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the customChargeTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/custom-charge-types")
    @Timed
    public ResponseEntity<CustomChargeTypeDTO> updateCustomChargeType(@RequestBody CustomChargeTypeDTO customChargeTypeDTO) throws URISyntaxException {
        log.debug("REST request to update CustomChargeType : {}", customChargeTypeDTO);
        if (customChargeTypeDTO.getId() == null) {
            return createCustomChargeType(customChargeTypeDTO);
        }
        CustomChargeTypeDTO result = customChargeTypeService.save(customChargeTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customChargeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /custom-charge-types : get all the customChargeTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customChargeTypes in body
     */
    @GetMapping("/custom-charge-types")
    @Timed
    public List<CustomChargeTypeDTO> getAllCustomChargeTypes() {
        log.debug("REST request to get all CustomChargeTypes");
        return customChargeTypeService.findAll();
        }

    /**
     * GET  /custom-charge-types/:id : get the "id" customChargeType.
     *
     * @param id the id of the customChargeTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customChargeTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/custom-charge-types/{id}")
    @Timed
    public ResponseEntity<CustomChargeTypeDTO> getCustomChargeType(@PathVariable Long id) {
        log.debug("REST request to get CustomChargeType : {}", id);
        CustomChargeTypeDTO customChargeTypeDTO = customChargeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customChargeTypeDTO));
    }

    /**
     * DELETE  /custom-charge-types/:id : delete the "id" customChargeType.
     *
     * @param id the id of the customChargeTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/custom-charge-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomChargeType(@PathVariable Long id) {
        log.debug("REST request to delete CustomChargeType : {}", id);
        customChargeTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
