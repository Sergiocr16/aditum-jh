package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AdministrationConfigurationService;
import com.lighthouse.aditum.service.ChargeService;
import com.lighthouse.aditum.service.CompanyConfigurationService;
import com.lighthouse.aditum.service.WaterConsumptionService;
import com.lighthouse.aditum.service.dto.AdministrationConfigurationDTO;
import com.lighthouse.aditum.service.dto.CompanyConfigurationDTO;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.WaterConsumptionDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WaterConsumption.
 */
@RestController
@RequestMapping("/api")
public class WaterConsumptionResource {

    private final Logger log = LoggerFactory.getLogger(WaterConsumptionResource.class);

    private static final String ENTITY_NAME = "waterConsumption";

    private final WaterConsumptionService waterConsumptionService;

    private final ChargeService chargeService;

    private final AdministrationConfigurationService administrationConfigurationService;

    private final CompanyConfigurationService companyConfigurationService;


    public WaterConsumptionResource(CompanyConfigurationService companyConfigurationService,AdministrationConfigurationService administrationConfigurationService, WaterConsumptionService waterConsumptionService, @Lazy ChargeService chargeService) {
        this.waterConsumptionService = waterConsumptionService;
        this.chargeService = chargeService;
        this.administrationConfigurationService = administrationConfigurationService;
        this.companyConfigurationService = companyConfigurationService;
    }

    /**
     * POST  /water-consumptions : Create a new waterConsumption.
     *
     * @param waterConsumptionDTO the waterConsumptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new waterConsumptionDTO, or with status 400 (Bad Request) if the waterConsumption has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/water-consumptions")
    @Timed
    public ResponseEntity<WaterConsumptionDTO> createWaterConsumption(@RequestBody WaterConsumptionDTO waterConsumptionDTO) throws URISyntaxException {
        log.debug("REST request to save WaterConsumption : {}", waterConsumptionDTO);
        if (waterConsumptionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new watch cannot already have an ID")).body(null);
        }
        WaterConsumptionDTO result = waterConsumptionService.save(waterConsumptionDTO);
        return ResponseEntity.created(new URI("/api/water-consumptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /water-consumptions : Updates an existing waterConsumption.
     *
     * @param waterConsumptionDTO the waterConsumptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated waterConsumptionDTO,
     * or with status 400 (Bad Request) if the waterConsumptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the waterConsumptionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/water-consumptions")
    @Timed
    public ResponseEntity<WaterConsumptionDTO> updateWaterConsumption(@RequestBody WaterConsumptionDTO waterConsumptionDTO) throws URISyntaxException {
        log.debug("REST request to update WaterConsumption : {}", waterConsumptionDTO);
        if (waterConsumptionDTO.getId() == null) {
            return createWaterConsumption(waterConsumptionDTO);
        }
        WaterConsumptionDTO result = waterConsumptionService.save(waterConsumptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, waterConsumptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /water-consumptions : get all the waterConsumptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of waterConsumptions in body
     */
    @GetMapping("/water-consumptions")
    @Timed
    public List<WaterConsumptionDTO> getAllWaterConsumptions() {
        log.debug("REST request to get all WaterConsumptions");
        return waterConsumptionService.findAll();
    }


    @GetMapping("/water-consumptions/bill/{companyId}/{wCid}/{date}/{sendEmail}/{autoCalculated}/{concept}")
    @Timed
    public void billWaterConsumption(@PathVariable Long companyId, @PathVariable Long wCid, @PathVariable ZonedDateTime date, @PathVariable Boolean sendEmail, @PathVariable Boolean autoCalculated, @PathVariable String concept) throws URISyntaxException {
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(companyId);
        CompanyConfigurationDTO companyConfigurationDTO = this.companyConfigurationService.findOne(companyId);
        chargeService.createWaterCharge(companyConfigurationDTO,this.waterConsumptionService.findOne(wCid),date,administrationConfigurationDTO,sendEmail,autoCalculated, concept);
    }


    @GetMapping("/water-consumptions/bill-all/{companyId}/{date}/{chargeDate}/{sendEmail}/{autoCalculated}/{concept}")
    @Timed
    public List<WaterConsumptionDTO> billAllWaterConsumption(@PathVariable Long companyId, @PathVariable ZonedDateTime date, @PathVariable ZonedDateTime chargeDate, @PathVariable Boolean sendEmail, @PathVariable Boolean autoCalculated, @PathVariable String concept)throws URISyntaxException {
        log.debug("REST request to get all WaterConsumptions");
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(companyId);
        return waterConsumptionService.createAllCharges(companyId, date, chargeDate, administrationConfigurationDTO, sendEmail, autoCalculated, concept);
    }

    @GetMapping("/water-consumptions/{companyId}/{date}")
    @Timed
    public List<WaterConsumptionDTO> getAllWaterConsumptions(@PathVariable Long companyId, @PathVariable ZonedDateTime date) {
        log.debug("REST request to get all WaterConsumptions");
        return waterConsumptionService.findAllByDate(companyId, date);
    }

    /**
     * GET  /water-consumptions/:id : get the "id" waterConsumption.
     *
     * @param id the id of the waterConsumptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the waterConsumptionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/water-consumptions/{id}")
    @Timed
    public ResponseEntity<WaterConsumptionDTO> getWaterConsumption(@PathVariable Long id) {
        log.debug("REST request to get WaterConsumption : {}", id);
        WaterConsumptionDTO waterConsumptionDTO = waterConsumptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(waterConsumptionDTO));
    }

    /**
     * DELETE  /water-consumptions/:id : delete the "id" waterConsumption.
     *
     * @param id the id of the waterConsumptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/water-consumptions/{id}")
    @Timed
    public ResponseEntity<Void> deleteWaterConsumption(@PathVariable Long id) {
        log.debug("REST request to delete WaterConsumption : {}", id);
        waterConsumptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
