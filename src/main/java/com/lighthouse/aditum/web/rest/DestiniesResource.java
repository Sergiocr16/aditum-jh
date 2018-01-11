package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.DestiniesService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.DestiniesDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Destinies.
 */
@RestController
@RequestMapping("/api")
public class DestiniesResource {

    private final Logger log = LoggerFactory.getLogger(DestiniesResource.class);

    private static final String ENTITY_NAME = "destinies";
        
    private final DestiniesService destiniesService;

    public DestiniesResource(DestiniesService destiniesService) {
        this.destiniesService = destiniesService;
    }

    /**
     * POST  /destinies : Create a new destinies.
     *
     * @param destiniesDTO the destiniesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new destiniesDTO, or with status 400 (Bad Request) if the destinies has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/destinies")
    @Timed
    public ResponseEntity<DestiniesDTO> createDestinies(@Valid @RequestBody DestiniesDTO destiniesDTO) throws URISyntaxException {
        log.debug("REST request to save Destinies : {}", destiniesDTO);
        if (destiniesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new destinies cannot already have an ID")).body(null);
        }
        DestiniesDTO result = destiniesService.save(destiniesDTO);
        return ResponseEntity.created(new URI("/api/destinies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /destinies : Updates an existing destinies.
     *
     * @param destiniesDTO the destiniesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated destiniesDTO,
     * or with status 400 (Bad Request) if the destiniesDTO is not valid,
     * or with status 500 (Internal Server Error) if the destiniesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/destinies")
    @Timed
    public ResponseEntity<DestiniesDTO> updateDestinies(@Valid @RequestBody DestiniesDTO destiniesDTO) throws URISyntaxException {
        log.debug("REST request to update Destinies : {}", destiniesDTO);
        if (destiniesDTO.getId() == null) {
            return createDestinies(destiniesDTO);
        }
        DestiniesDTO result = destiniesService.save(destiniesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, destiniesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /destinies : get all the destinies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of destinies in body
     */
    @GetMapping("/destinies")
    @Timed
    public List<DestiniesDTO> getAllDestinies() {
        log.debug("REST request to get all Destinies");
        return destiniesService.findAll();
    }

    /**
     * GET  /destinies/:id : get the "id" destinies.
     *
     * @param id the id of the destiniesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the destiniesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/destinies/{id}")
    @Timed
    public ResponseEntity<DestiniesDTO> getDestinies(@PathVariable Long id) {
        log.debug("REST request to get Destinies : {}", id);
        DestiniesDTO destiniesDTO = destiniesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(destiniesDTO));
    }

    /**
     * DELETE  /destinies/:id : delete the "id" destinies.
     *
     * @param id the id of the destiniesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/destinies/{id}")
    @Timed
    public ResponseEntity<Void> deleteDestinies(@PathVariable Long id) {
        log.debug("REST request to delete Destinies : {}", id);
        destiniesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
