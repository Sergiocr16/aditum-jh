package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.PetService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.PetDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pet.
 */
@RestController
@RequestMapping("/api")
public class PetResource {

    private final Logger log = LoggerFactory.getLogger(PetResource.class);

    private static final String ENTITY_NAME = "pet";

    private final PetService petService;

    public PetResource(PetService petService) {
        this.petService = petService;
    }

    /**
     * POST  /pets : Create a new pet.
     *
     * @param petDTO the petDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new petDTO, or with status 400 (Bad Request) if the pet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pets")
    @Timed
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO petDTO) throws URISyntaxException {
        log.debug("REST request to save Pet : {}", petDTO);
        if (petDTO.getId() != null) {
//            throw new BadRequestAlertException("A new pet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PetDTO result = petService.save(petDTO);
        return ResponseEntity.created(new URI("/api/pets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pets : Updates an existing pet.
     *
     * @param petDTO the petDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated petDTO,
     * or with status 400 (Bad Request) if the petDTO is not valid,
     * or with status 500 (Internal Server Error) if the petDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pets")
    @Timed
    public ResponseEntity<PetDTO> updatePet(@RequestBody PetDTO petDTO) throws URISyntaxException {
        log.debug("REST request to update Pet : {}", petDTO);
        if (petDTO.getId() == null) {
            return createPet(petDTO);
        }
        PetDTO result = petService.save(petDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, petDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pets : get all the pets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pets in body
     */
    @GetMapping("/pets")
    @Timed
    public ResponseEntity<List<PetDTO>> getAllPets(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Pets");
        Page<PetDTO> page = petService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/pets/by-company/{companyId}/{name}")
    @Timed
    public ResponseEntity<List<PetDTO>> getAllPetsByCompany(Pageable pageable,@PathVariable Long companyId,@PathVariable String name) throws URISyntaxException {
        log.debug("REST request to get a page of Pets");
        Page<PetDTO> page = petService.findAllByCompany(pageable,companyId,name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/pets/by-house/{houseId}/{name}")
    @Timed
    public ResponseEntity<List<PetDTO>> getAllPetsByHouse(Pageable pageable,@PathVariable Long houseId,@PathVariable String name) throws URISyntaxException {
        log.debug("REST request to get a page of Pets");
        Page<PetDTO> page = petService.findAllByHouseId(pageable,houseId,name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pets/:id : get the "id" pet.
     *
     * @param id the id of the petDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the petDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pets/{id}")
    @Timed
    public ResponseEntity<PetDTO> getPet(@PathVariable Long id) {
        log.debug("REST request to get Pet : {}", id);
        PetDTO petDTO = petService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(petDTO));
    }

    /**
     * DELETE  /pets/:id : delete the "id" pet.
     *
     * @param id the id of the petDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pets/{id}")
    @Timed
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        log.debug("REST request to delete Pet : {}", id);
        petService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
