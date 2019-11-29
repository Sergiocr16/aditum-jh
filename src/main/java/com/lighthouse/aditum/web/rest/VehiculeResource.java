package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.VehiculeService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.VehiculeDTO;
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
 * REST controller for managing Vehicule.
 */
@RestController
@RequestMapping("/api")
public class VehiculeResource {

    private final Logger log = LoggerFactory.getLogger(VehiculeResource.class);

    private static final String ENTITY_NAME = "vehicule";

    private final VehiculeService vehiculeService;

    public VehiculeResource(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    /**
     * POST  /vehicules : Create a new vehicule.
     *
     * @param vehiculeDTO the vehiculeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vehiculeDTO, or with status 400 (Bad Request) if the vehicule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vehicules")
    @Timed
    public ResponseEntity<VehiculeDTO> createVehicule(@Valid @RequestBody VehiculeDTO vehiculeDTO) throws URISyntaxException {
        log.debug("REST request to save Vehicule : {}", vehiculeDTO);
        if (vehiculeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new vehicule cannot already have an ID")).body(null);
        }
        VehiculeDTO result = vehiculeService.save(vehiculeDTO);
        return ResponseEntity.created(new URI("/api/vehicules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vehicules : Updates an existing vehicule.
     *
     * @param vehiculeDTO the vehiculeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vehiculeDTO,
     * or with status 400 (Bad Request) if the vehiculeDTO is not valid,
     * or with status 500 (Internal Server Error) if the vehiculeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vehicules")
    @Timed
    public ResponseEntity<VehiculeDTO> updateVehicule(@Valid @RequestBody VehiculeDTO vehiculeDTO) throws URISyntaxException {
        log.debug("REST request to update Vehicule : {}", vehiculeDTO);
        if (vehiculeDTO.getId() == null) {
            return createVehicule(vehiculeDTO);
        }
        VehiculeDTO result = vehiculeService.save(vehiculeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vehiculeDTO.getId().toString()))
            .body(result);
    }


    @GetMapping("/vehiculesEnabled/byHouse")
    @Timed
    public ResponseEntity<List<VehiculeDTO>> getEnabledVehiculesByHouse(@ApiParam Pageable pageable,Long houseId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<VehiculeDTO> page = vehiculeService.findEnabledByHouse(pageable,houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehiculesEnabled/byHouse");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/vehiculesDisabled/byHouse")
    @Timed
    public ResponseEntity<List<VehiculeDTO>> getDisabledVehiculesByHouse(@ApiParam Pageable pageable,Long houseId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<VehiculeDTO> page = vehiculeService.findDisabledByHouse(pageable,houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehiculesDisabled/byHouse");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/vehiculesEnabled")
    @Timed
    public ResponseEntity<List<VehiculeDTO>> getEnabledVehicules(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<VehiculeDTO> page = vehiculeService.findEnabled(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehiculesEnabled");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/allVehicules/{companyId}/{houseId}/{enabled}/{licencePlate}")
    @Timed
    public ResponseEntity<List<VehiculeDTO>> getEnabledVehicules(@ApiParam Pageable pageable, @PathVariable Long companyId,@PathVariable String houseId, @PathVariable int enabled, @PathVariable String licencePlate)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<VehiculeDTO> page = vehiculeService.findByFilter(pageable,companyId,houseId,enabled,licencePlate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehiculesEnabled");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/vehicules/macro/{macroId}/filter/{filter}")
    @Timed
    public ResponseEntity<List<VehiculeDTO>> getVehiculesByMacroFilter(@ApiParam Pageable pageable,@PathVariable Long macroId , @PathVariable String filter)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents by Macro with Filter");
        Page<VehiculeDTO> page = vehiculeService.getAllByMacroWithFilter(pageable,macroId,filter);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/vehiculesDisabled")
    @Timed
    public ResponseEntity<List<VehiculeDTO>> getDisabledVehicules(@ApiParam Pageable pageable, Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<VehiculeDTO> page = vehiculeService.findDisabled(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehiculesDisabled");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /vehicules : get all the vehicules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of vehicules in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/vehicules")
    @Timed
    public ResponseEntity<List<VehiculeDTO>> getAllVehicules(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Vehicules");
        Page<VehiculeDTO> page = vehiculeService.findAll(companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehicules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vehicules/:id : get the "id" vehicule.
     *
     * @param id the id of the vehiculeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vehiculeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/vehicules/{id}")
    @Timed
    public ResponseEntity<VehiculeDTO> getVehicule(@PathVariable Long id) {
        log.debug("REST request to get Vehicule : {}", id);
        VehiculeDTO vehiculeDTO = vehiculeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vehiculeDTO));
    }

    @GetMapping("/vehicules/findByCompanyAndPlate/{companyId}/{licensePlate}")
    @Timed
    public ResponseEntity<VehiculeDTO> getVehicule(@PathVariable(value = "licensePlate")  String  licensePlate,
                                                   @PathVariable(value = "companyId")  Long companyId) {
        log.debug("REST request to get Vehicule : {}", companyId);
        VehiculeDTO vehiculeDTO = vehiculeService.findOneByCompanyAndPlate(companyId,licensePlate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vehiculeDTO));
    }

    /**
     * DELETE  /vehicules/:id : delete the "id" vehicule.
     *
     * @param id the id of the vehiculeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vehicules/{id}")
    @Timed
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        log.debug("REST request to delete Vehicule : {}", id);
        vehiculeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
