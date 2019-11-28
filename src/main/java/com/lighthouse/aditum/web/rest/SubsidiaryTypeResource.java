package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.SubsidiaryTypeService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.SubsidiaryTypeDTO;
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
 * REST controller for managing SubsidiaryType.
 */
@RestController
@RequestMapping("/api")
public class SubsidiaryTypeResource {

    private final Logger log = LoggerFactory.getLogger(SubsidiaryTypeResource.class);

    private static final String ENTITY_NAME = "subsidiaryType";

    private final SubsidiaryTypeService subsidiaryTypeService;

    public SubsidiaryTypeResource(SubsidiaryTypeService subsidiaryTypeService) {
        this.subsidiaryTypeService = subsidiaryTypeService;
    }

    /**
     * POST  /subsidiary-types : Create a new subsidiaryType.
     *
     * @param subsidiaryTypeDTO the subsidiaryTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subsidiaryTypeDTO, or with status 400 (Bad Request) if the subsidiaryType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subsidiary-types")
    @Timed
    public ResponseEntity<SubsidiaryTypeDTO> createSubsidiaryType(@Valid @RequestBody SubsidiaryTypeDTO subsidiaryTypeDTO) throws URISyntaxException {
        log.debug("REST request to save SubsidiaryType : {}", subsidiaryTypeDTO);
        if (subsidiaryTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        SubsidiaryTypeDTO result = subsidiaryTypeService.save(subsidiaryTypeDTO);
        return ResponseEntity.created(new URI("/api/subsidiary-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subsidiary-types : Updates an existing subsidiaryType.
     *
     * @param subsidiaryTypeDTO the subsidiaryTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subsidiaryTypeDTO,
     * or with status 400 (Bad Request) if the subsidiaryTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the subsidiaryTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/subsidiary-types")
    @Timed
    public ResponseEntity<SubsidiaryTypeDTO> updateSubsidiaryType(@Valid @RequestBody SubsidiaryTypeDTO subsidiaryTypeDTO) throws URISyntaxException {
        log.debug("REST request to update SubsidiaryType : {}", subsidiaryTypeDTO);
        if (subsidiaryTypeDTO.getId() == null) {
            return createSubsidiaryType(subsidiaryTypeDTO);
        }
        SubsidiaryTypeDTO result = subsidiaryTypeService.save(subsidiaryTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subsidiaryTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subsidiary-types : get all the subsidiaryTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subsidiaryTypes in body
     */
    @GetMapping("/subsidiary-types")
    @Timed
    public ResponseEntity<List<SubsidiaryTypeDTO>> getAllSubsidiaryTypes(Pageable pageable)throws URISyntaxException {
        log.debug("REST request to get a page of SubsidiaryTypes");
        Page<SubsidiaryTypeDTO> page = subsidiaryTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subsidiary-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /subsidiary-types/:id : get the "id" subsidiaryType.
     *
     * @param id the id of the subsidiaryTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subsidiaryTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/subsidiary-types/{id}")
    @Timed
    public ResponseEntity<SubsidiaryTypeDTO> getSubsidiaryType(@PathVariable Long id) {
        log.debug("REST request to get SubsidiaryType : {}", id);
        SubsidiaryTypeDTO subsidiaryTypeDTO = subsidiaryTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subsidiaryTypeDTO));
    }

    /**
     * DELETE  /subsidiary-types/:id : delete the "id" subsidiaryType.
     *
     * @param id the id of the subsidiaryTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subsidiary-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubsidiaryType(@PathVariable Long id) {
        log.debug("REST request to delete SubsidiaryType : {}", id);
        subsidiaryTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
