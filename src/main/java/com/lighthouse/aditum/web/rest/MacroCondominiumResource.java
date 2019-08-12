package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.service.HouseService;
import com.lighthouse.aditum.service.MacroCondominiumService;
import com.lighthouse.aditum.service.dto.AuthorizedUserAccessDoorDTO;
import com.lighthouse.aditum.service.dto.HouseAccessDoorDTO;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.MacroCondominiumDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MacroCondominium.
 */
@RestController
@RequestMapping("/api")
public class MacroCondominiumResource {

    private final Logger log = LoggerFactory.getLogger(MacroCondominiumResource.class);

    private static final String ENTITY_NAME = "macroCondominium";

    private final MacroCondominiumService macroCondominiumService;
    private final HouseService houseService;


    public MacroCondominiumResource(HouseService houseService,MacroCondominiumService macroCondominiumService) {
        this.macroCondominiumService = macroCondominiumService;
        this.houseService = houseService;

    }

    /**
     * POST  /macro-condominiums : Create a new macroCondominium.
     *
     * @param macroCondominiumDTO the macroCondominiumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new macroCondominiumDTO, or with status 400 (Bad Request) if the macroCondominium has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/macro-condominiums")
    @Timed
    public ResponseEntity<MacroCondominiumDTO> createMacroCondominium(@Valid @RequestBody MacroCondominiumDTO macroCondominiumDTO) throws URISyntaxException {
        log.debug("REST request to save MacroCondominium : {}", macroCondominiumDTO);
        if (macroCondominiumDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new macroCondominium cannot already have an ID")).body(null);
        }
        MacroCondominiumDTO result = macroCondominiumService.save(macroCondominiumDTO);
        return ResponseEntity.created(new URI("/api/macro-condominiums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /macro-condominiums : Updates an existing macroCondominium.
     *
     * @param macroCondominiumDTO the macroCondominiumDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated macroCondominiumDTO,
     * or with status 400 (Bad Request) if the macroCondominiumDTO is not valid,
     * or with status 500 (Internal Server Error) if the macroCondominiumDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/macro-condominiums")
    @Timed
    public ResponseEntity<MacroCondominiumDTO> updateMacroCondominium(@Valid @RequestBody MacroCondominiumDTO macroCondominiumDTO) throws URISyntaxException {
        log.debug("REST request to update MacroCondominium : {}", macroCondominiumDTO);
        if (macroCondominiumDTO.getId() == null) {
            return createMacroCondominium(macroCondominiumDTO);
        }
        MacroCondominiumDTO result = macroCondominiumService.save(macroCondominiumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, macroCondominiumDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /macro-condominiums : get all the macroCondominiums.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of macroCondominiums in body
     */
    @GetMapping("/macro-condominiums")
    @Timed
    public ResponseEntity<List<MacroCondominiumDTO>> getAllMacroCondominiums(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of MacroCondominiums");
        Page<MacroCondominiumDTO> page = macroCondominiumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/macro-condominiums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /macro-condominiums/:id : get the "id" macroCondominium.
     *
     * @param id the id of the macroCondominiumDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the macroCondominiumDTO, or with status 404 (Not Found)
     */
    @GetMapping("/macro-condominiums/{id}")
    @Timed
    public ResponseEntity<MacroCondominiumDTO> getMacroCondominium(@PathVariable Long id) {
        log.debug("REST request to get MacroCondominium : {}", id);
        MacroCondominiumDTO macroCondominiumDTO = macroCondominiumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(macroCondominiumDTO));
    }

    @GetMapping("/macro-condominiums/{id}/findAuthorized/{identification}")
    @Timed
    public ResponseEntity<AuthorizedUserAccessDoorDTO> getMacroCondominiumAthorized(@PathVariable Long id, @PathVariable String identification) {
        log.debug("REST request to get MacroCondominium : {}", id);
        AuthorizedUserAccessDoorDTO authorizedUserAccessDoorDTO = macroCondominiumService.findAuthorized(id,identification);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(authorizedUserAccessDoorDTO));
    }

    @GetMapping("/macro-condominiums/{id}/findAuthorizedByPlate/{plate}")
    @Timed
    public ResponseEntity<AuthorizedUserAccessDoorDTO> getMacroCondominiumAthorizedVehicule(@PathVariable Long id, @PathVariable String plate) {
        log.debug("REST request to get MacroCondominium : {}", id);
        AuthorizedUserAccessDoorDTO authorizedUserAccessDoorDTO = macroCondominiumService.findAuthorizedVehicules(id,plate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(authorizedUserAccessDoorDTO));
    }

    @GetMapping("/macro-condominiums/find-micros/{id}")
    @Timed
    public ResponseEntity<MacroCondominiumDTO> getMicroCondominiumsInMacro(@PathVariable Long id) {
        log.debug("REST request to get MacroCondominium : {}", id);
        MacroCondominiumDTO macroCondominiumDTO = macroCondominiumService.findMicrosInOne(id);
        macroCondominiumDTO.getCompanies().forEach(company -> {
            List<HouseAccessDoorDTO> houseAccessDoorDTOS = new ArrayList<>();
            houseService.findAll(company.getId()).getContent().forEach(houseDTO -> {
                HouseAccessDoorDTO houseClean= new HouseAccessDoorDTO();
                houseClean.setId(houseDTO.getId());
                houseClean.setHousenumber(houseDTO.getHousenumber());
                houseClean.setEmergencyKey(houseDTO.getEmergencyKey());
                houseClean.setSecurityKey(houseDTO.getSecurityKey());
                houseAccessDoorDTOS.add(houseClean);
            });
            company.setHouses(houseAccessDoorDTOS);
        });
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(macroCondominiumDTO));
    }

    /**
     * DELETE  /macro-condominiums/:id : delete the "id" macroCondominium.
     *
     * @param id the id of the macroCondominiumDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/macro-condominiums/{id}")
    @Timed
    public ResponseEntity<Void> deleteMacroCondominium(@PathVariable Long id) {
        log.debug("REST request to delete MacroCondominium : {}", id);
        macroCondominiumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
