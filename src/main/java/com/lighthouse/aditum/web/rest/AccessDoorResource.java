package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AccessDoorService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.AccessDoorDTO;
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
 * REST controller for managing AccessDoor.
 */
@RestController
@RequestMapping("/api")
public class AccessDoorResource {

    private final Logger log = LoggerFactory.getLogger(AccessDoorResource.class);

    private static final String ENTITY_NAME = "accessDoor";
        
    private final AccessDoorService accessDoorService;

    public AccessDoorResource(AccessDoorService accessDoorService) {
        this.accessDoorService = accessDoorService;
    }

    /**
     * POST  /access-doors : Create a new accessDoor.
     *
     * @param accessDoorDTO the accessDoorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accessDoorDTO, or with status 400 (Bad Request) if the accessDoor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/access-doors")
    @Timed
    public ResponseEntity<AccessDoorDTO> createAccessDoor(@Valid @RequestBody AccessDoorDTO accessDoorDTO) throws URISyntaxException {
        log.debug("REST request to save AccessDoor : {}", accessDoorDTO);
        if (accessDoorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accessDoor cannot already have an ID")).body(null);
        }
        AccessDoorDTO result = accessDoorService.save(accessDoorDTO);
        return ResponseEntity.created(new URI("/api/access-doors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /access-doors : Updates an existing accessDoor.
     *
     * @param accessDoorDTO the accessDoorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accessDoorDTO,
     * or with status 400 (Bad Request) if the accessDoorDTO is not valid,
     * or with status 500 (Internal Server Error) if the accessDoorDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/access-doors")
    @Timed
    public ResponseEntity<AccessDoorDTO> updateAccessDoor(@Valid @RequestBody AccessDoorDTO accessDoorDTO) throws URISyntaxException {
        log.debug("REST request to update AccessDoor : {}", accessDoorDTO);
        if (accessDoorDTO.getId() == null) {
            return createAccessDoor(accessDoorDTO);
        }
        AccessDoorDTO result = accessDoorService.save(accessDoorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accessDoorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /access-doors : get all the accessDoors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accessDoors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/access-doors")
    @Timed
    public ResponseEntity<List<AccessDoorDTO>> getAllAccessDoors(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AccessDoors");
        Page<AccessDoorDTO> page = accessDoorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/access-doors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /access-doors/:id : get the "id" accessDoor.
     *
     * @param id the id of the accessDoorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accessDoorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/access-doors/{id}")
    @Timed
    public ResponseEntity<AccessDoorDTO> getAccessDoor(@PathVariable Long id) {
        log.debug("REST request to get AccessDoor : {}", id);
        AccessDoorDTO accessDoorDTO = accessDoorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accessDoorDTO));
    }

    /**
     * DELETE  /access-doors/:id : delete the "id" accessDoor.
     *
     * @param id the id of the accessDoorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/access-doors/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccessDoor(@PathVariable Long id) {
        log.debug("REST request to delete AccessDoor : {}", id);
        accessDoorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
