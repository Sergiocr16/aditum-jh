package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.BlockReservationService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.BlockReservationDTO;
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
 * REST controller for managing BlockReservation.
 */
@RestController
@RequestMapping("/api")
public class BlockReservationResource {

    private final Logger log = LoggerFactory.getLogger(BlockReservationResource.class);

    private static final String ENTITY_NAME = "blockReservation";

    private final BlockReservationService blockReservationService;

    public BlockReservationResource(BlockReservationService blockReservationService) {
        this.blockReservationService = blockReservationService;
    }

    /**
     * POST  /block-reservations : Create a new blockReservation.
     *
     * @param blockReservationDTO the blockReservationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blockReservationDTO, or with status 400 (Bad Request) if the blockReservation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/block-reservations")
    @Timed
    public ResponseEntity<BlockReservationDTO> createBlockReservation(@RequestBody BlockReservationDTO blockReservationDTO) throws URISyntaxException {
        log.debug("REST request to save BlockReservation : {}", blockReservationDTO);
        if (blockReservationDTO.getId() != null) {
//            throw new BadRequestAlertException("A new blockReservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlockReservationDTO result = blockReservationService.save(blockReservationDTO);
        return ResponseEntity.created(new URI("/api/block-reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /block-reservations : Updates an existing blockReservation.
     *
     * @param blockReservationDTO the blockReservationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blockReservationDTO,
     * or with status 400 (Bad Request) if the blockReservationDTO is not valid,
     * or with status 500 (Internal Server Error) if the blockReservationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/block-reservations")
    @Timed
    public ResponseEntity<BlockReservationDTO> updateBlockReservation(@RequestBody BlockReservationDTO blockReservationDTO) throws URISyntaxException {
        log.debug("REST request to update BlockReservation : {}", blockReservationDTO);
        if (blockReservationDTO.getId() == null) {
            return createBlockReservation(blockReservationDTO);
        }
        BlockReservationDTO result = blockReservationService.save(blockReservationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, blockReservationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /block-reservations : get all the blockReservations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of blockReservations in body
     */
    @GetMapping("/block-reservations")
    @Timed
    public List<BlockReservationDTO> getAllBlockReservations() {
        log.debug("REST request to get all BlockReservations");
        return blockReservationService.findAll();
        }

    /**
     * GET  /block-reservations/:id : get the "id" blockReservation.
     *
     * @param id the id of the blockReservationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blockReservationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/block-reservations/{id}")
    @Timed
    public ResponseEntity<BlockReservationDTO> getBlockReservation(@PathVariable Long id) {
        log.debug("REST request to get BlockReservation : {}", id);
        BlockReservationDTO blockReservationDTO = blockReservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blockReservationDTO));
    }

    @GetMapping("/block-reservations/by-House-id/{houseId}")
    @Timed
    public ResponseEntity<BlockReservationDTO> getBlockReservationByHouseId(@PathVariable Long houseId) {
        log.debug("REST request to get BlockReservation : {}", houseId);
        BlockReservationDTO blockReservationDTO = blockReservationService.findOneByHouseId(houseId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blockReservationDTO));
    }


    /**
     * DELETE  /block-reservations/:id : delete the "id" blockReservation.
     *
     * @param id the id of the blockReservationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/block-reservations/{id}")
    @Timed
    public ResponseEntity<Void> deleteBlockReservation(@PathVariable Long id) {
        log.debug("REST request to delete BlockReservation : {}", id);
        blockReservationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
