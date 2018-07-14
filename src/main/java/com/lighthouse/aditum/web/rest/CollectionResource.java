package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.CollectionService;
import com.lighthouse.aditum.service.dto.HouseYearCollectionDTO;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.CollectionDTO;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Collection.
 */
@RestController
@RequestMapping("/api")
public class CollectionResource {

    private final Logger log = LoggerFactory.getLogger(CollectionResource.class);

    private static final String ENTITY_NAME = "collection";

    private final CollectionService collectionService;

    public CollectionResource(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    /**
     * POST  /collections : Create a new collection.
     *
     * @param collectionDTO the collectionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collectionDTO, or with status 400 (Bad Request) if the collection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/collections")
    @Timed
    public ResponseEntity<CollectionDTO> createCollection(@Valid @RequestBody CollectionDTO collectionDTO) throws URISyntaxException {
        log.debug("REST request to save Collection : {}", collectionDTO);
        if (collectionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new collection cannot already have an ID")).body(null);
        }
        CollectionDTO result = collectionService.save(collectionDTO);
        return ResponseEntity.created(new URI("/api/collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collections : Updates an existing collection.
     *
     * @param collectionDTO the collectionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collectionDTO,
     * or with status 400 (Bad Request) if the collectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the collectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/collections")
    @Timed
    public ResponseEntity<CollectionDTO> updateCollection(@Valid @RequestBody CollectionDTO collectionDTO) throws URISyntaxException {
        log.debug("REST request to update Collection : {}", collectionDTO);
        if (collectionDTO.getId() == null) {
            return createCollection(collectionDTO);
        }
        CollectionDTO result = collectionService.save(collectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, collectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collections : get all the collections.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of collections in body
     */
    @GetMapping("/collections")
    @Timed
    public ResponseEntity<List<CollectionDTO>> getAllCollections(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Collections");
        Page<CollectionDTO> page = collectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/collections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /collections/:id : get the "id" collection.
     *
     * @param id the id of the collectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/collections/{id}")
    @Timed
    public ResponseEntity<CollectionDTO> getCollection(@PathVariable Long id) {
        log.debug("REST request to get Collection : {}", id);
        CollectionDTO collectionDTO = collectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(collectionDTO));
    }

    @GetMapping("/collections/{companyId}/{year}")
    @Timed
    public ResponseEntity<List<HouseYearCollectionDTO>> getCollectionsPerYear(@PathVariable Long companyId, @PathVariable String year) {
        log.debug("REST request to get Collections by year : {}", year);
        List<HouseYearCollectionDTO> collectionDTO = collectionService.findCollectionsByYear(companyId,year);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(collectionDTO));
    }

    /**
     * DELETE  /collections/:id : delete the "id" collection.
     *
     * @param id the id of the collectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/collections/{id}")
    @Timed
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        log.debug("REST request to delete Collection : {}", id);
        collectionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
