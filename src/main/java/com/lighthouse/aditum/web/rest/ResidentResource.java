package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.ResidentService;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ResidentDTO;
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
 * REST controller for managing Resident.
 */
@RestController
@RequestMapping("/api")
public class ResidentResource {

    private final Logger log = LoggerFactory.getLogger(ResidentResource.class);

    private static final String ENTITY_NAME = "resident";

    private final ResidentService residentService;

    public ResidentResource(ResidentService residentService) {
        this.residentService = residentService;
    }

    /**
     * POST  /residents : Create a new resident.
     *
     * @param residentDTO the residentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new residentDTO, or with status 400 (Bad Request) if the resident has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/residents")
    @Timed
    public ResponseEntity<ResidentDTO> createResident(@Valid @RequestBody ResidentDTO residentDTO) throws URISyntaxException {
        log.debug("REST request to save Resident : {}", residentDTO);
        if (residentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new resident cannot already have an ID")).body(null);
        }
        ResidentDTO result = residentService.save(residentDTO);
        return ResponseEntity.created(new URI("/api/residents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /residents : Updates an existing resident.
     *
     * @param residentDTO the residentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated residentDTO,
     * or with status 400 (Bad Request) if the residentDTO is not valid,
     * or with status 500 (Internal Server Error) if the residentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/residents")
    @Timed
    public ResponseEntity<ResidentDTO> updateResident(@Valid @RequestBody ResidentDTO residentDTO) throws URISyntaxException {
        log.debug("REST request to update Resident : {}", residentDTO);
        if (residentDTO.getId() == null) {
            return createResident(residentDTO);
        }
        ResidentDTO result = residentService.save(residentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, residentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /residents : get all the residents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of residents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/residents")
    @Timed
    public ResponseEntity<List<ResidentDTO>> getAllResidents(@ApiParam Pageable pageable, Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<ResidentDTO> page = residentService.findAll(companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/residentsEnabled/byHouse")
    @Timed
    public ResponseEntity<List<ResidentDTO>> findResidentesEnabledByHouseId(@ApiParam Pageable pageable, Long houseId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<ResidentDTO> page = residentService.findEnabledByHouseId(pageable, houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/residentsDisabled/byHouse")
    @Timed
    public ResponseEntity<List<ResidentDTO>> findResidentesDisabledByHouseId(@ApiParam Pageable pageable, Long houseId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<ResidentDTO> page = residentService.findDisabledByHouseId(pageable, houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/allOwners/{companyId}/{houseId}/{name}")
    @Timed
    public ResponseEntity<List<ResidentDTO>> getOwners(@ApiParam Pageable pageable,
                                                       @PathVariable Long companyId,
                                                       @PathVariable String houseId,
                                                       @PathVariable String name
    )
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<ResidentDTO> page = residentService.findOwners(pageable, companyId, houseId, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/allTenants/{companyId}/{houseId}/{name}")
    @Timed
    public ResponseEntity<List<ResidentDTO>> getTenants(@ApiParam Pageable pageable,
                                                       @PathVariable Long companyId,
                                                       @PathVariable String houseId,
                                                       @PathVariable String name
    )
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<ResidentDTO> page = residentService.findTenants(pageable, companyId, houseId, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/allResidents/{companyId}/{enabled}/{houseId}/{owner}/{name}")
    @Timed
    public ResponseEntity<List<ResidentDTO>> getResidents(@ApiParam Pageable pageable,
                                                          @PathVariable Long companyId
        , @PathVariable int enabled
        , @PathVariable String houseId
        , @PathVariable String owner
        , @PathVariable String name)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<ResidentDTO> page = residentService.getAllInFilter(pageable, companyId, enabled, houseId, owner, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/residents/macro/{macroId}/filter/{filter}")
    @Timed
    public ResponseEntity<List<ResidentDTO>> getResidentsByMacroFilter(@ApiParam Pageable pageable, @PathVariable Long macroId, @PathVariable String filter)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents by Macro with Filter");
        Page<ResidentDTO> page = residentService.getAllByMacroWithFilter(pageable, macroId, filter);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/residentsDisabled/{companyId}")
    @Timed
    public ResponseEntity<List<ResidentDTO>> getDisabledResidents(@ApiParam Pageable pageable, @PathVariable Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<ResidentDTO> page = residentService.findDisabled(pageable, companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/residents/houses-has-owners/{housesIds}")
    @Timed
    public ResponseEntity<HouseDTO> findOwnersByHouse(@PathVariable String housesIds)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        HouseDTO result = residentService.isOwnerInHouses(housesIds);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(null, "/api/residents");
        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }
    /**
     * GET  /residents/:id : get the "id" resident.
     *
     * @param id the id of the residentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the residentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/residents/{id}")
    @Timed
    public ResponseEntity<ResidentDTO> getResident(@PathVariable Long id) {
        log.debug("REST request to get Resident : {}", id);
        ResidentDTO residentDTO = residentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(residentDTO));
    }

    @GetMapping("/residents/findByCompanyAndIdentification/{companyId}/{identificationNumber}")
    @Timed
    public ResponseEntity<ResidentDTO> getResidentByCompanyAndIdentification(@PathVariable(value = "companyId") Long companyId, @PathVariable(value = "identificationNumber") String identificationNumber) {
        log.debug("REST request to get Resident by Company and Identifiaction : {}", companyId, identificationNumber);
        ResidentDTO residentDTO = residentService.findByCompanyIdAndIdentifiactionNumber(companyId, identificationNumber);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(residentDTO));
    }

    @GetMapping("/residents/findByUserId/{id}")
    @Timed
    public ResponseEntity<ResidentDTO> getResidentByUserId(@PathVariable Long id) {
        log.debug("REST request to get Resident : {}", id);
        ResidentDTO residentDTO = residentService.findOneByUserId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(residentDTO));
    }

    /**
     * DELETE  /residents/:id : delete the "id" resident.
     *
     * @param id the id of the residentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/residents/{id}")
    @Timed
    public ResponseEntity<Void> deleteResident(@PathVariable Long id) {
        log.debug("REST request to delete Resident : {}", id);
        residentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
