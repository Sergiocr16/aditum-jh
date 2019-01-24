package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.WatchService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.WatchDTO;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Watch.
 */
@RestController
@RequestMapping("/api")
public class WatchResource {

    private final Logger log = LoggerFactory.getLogger(WatchResource.class);

    private static final String ENTITY_NAME = "watch";

    private final WatchService watchService;

    public WatchResource(WatchService watchService) {
        this.watchService = watchService;
    }

    /**
     * POST  /watches : Create a new watch.
     *
     * @param watchDTO the watchDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new watchDTO, or with status 400 (Bad Request) if the watch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/watches")
    @Timed
    public ResponseEntity<WatchDTO> createWatch(@Valid @RequestBody WatchDTO watchDTO) throws URISyntaxException {
        log.debug("REST request to save Watch : {}", watchDTO);
        if (watchDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new watch cannot already have an ID")).body(null);
        }
        WatchDTO previousWatch = watchService.findLastWatch(watchDTO.getCompanyId());
        ZonedDateTime currentTime = ZonedDateTime.now();
        watchDTO.setInitialtime(currentTime);
        watchDTO.setFinaltime(null);
        if(previousWatch!=null) {
            previousWatch.setFinaltime(currentTime);
            watchService.save(previousWatch);
        }
        WatchDTO result = watchService.save(watchDTO);
        return ResponseEntity.created(new URI("/api/watches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /watches : Updates an existing watch.
     *
     * @param watchDTO the watchDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated watchDTO,
     * or with status 400 (Bad Request) if the watchDTO is not valid,
     * or with status 500 (Internal Server Error) if the watchDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/watches")
    @Timed
    public ResponseEntity<WatchDTO> updateWatch(@Valid @RequestBody WatchDTO watchDTO) throws URISyntaxException {
        log.debug("REST request to update Watch : {}", watchDTO);
        if (watchDTO.getId() == null) {
            return createWatch(watchDTO);
        }
        WatchDTO result = watchService.save(watchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, watchDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /watches : get all the watches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of watches in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/watches")
    @Timed
    public ResponseEntity<List<WatchDTO>> getAllWatches(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Watches");
        Page<WatchDTO> page = watchService.findAll(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/watches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/watches/between/{initial_time}/{final_time}/{companyId}")
    @Timed
    public ResponseEntity<List<WatchDTO>> getBetweenDate(
        @ApiParam Pageable pageable,
        @PathVariable (value = "initial_time")  String initial_time,
        @PathVariable(value = "final_time")  String  final_time,
        @PathVariable(value = "companyId")  Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<WatchDTO> page = watchService.findBetweenDates(pageable,initial_time,final_time,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/watches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /watches/:id : get the "id" watch.
     *
     * @param id the id of the watchDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the watchDTO, or with status 404 (Not Found)
     */
    @GetMapping("/watches/{id}")
    @Timed
    public ResponseEntity<WatchDTO> getWatch(@PathVariable Long id) {
        log.debug("REST request to get Watch : {}", id);
        WatchDTO watchDTO = watchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(watchDTO));
    }

    @GetMapping("/watches/current/{companyId}")
    @Timed
    public ResponseEntity<WatchDTO> getCurrentWatch(@PathVariable Long companyId) {
        log.debug("REST request to get current Watch : {}", companyId);
        WatchDTO watchDTO = watchService.findLastWatch(companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(watchDTO));
    }

    /**
     * DELETE  /watches/:id : delete the "id" watch.
     *
     * @param id the id of the watchDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/watches/{id}")
    @Timed
    public ResponseEntity<Void> deleteWatch(@PathVariable Long id) {
        log.debug("REST request to delete Watch : {}", id);
        watchService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
