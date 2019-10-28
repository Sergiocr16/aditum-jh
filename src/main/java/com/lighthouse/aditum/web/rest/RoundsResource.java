package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.RoundsService;
import com.lighthouse.aditum.service.SoporteService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.service.dto.RoundDTO;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.SoporteDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing Soporte.
 */
@RestController
@RequestMapping("/api")
public class RoundsResource {

    private final Logger log = LoggerFactory.getLogger(SoporteResource.class);

    private static final String ENTITY_NAME = "rounds";

    private final RoundsService roundsService;

    public RoundsResource(RoundsService roundsService) {
        this.roundsService = roundsService;
    }


    /**
     * GET  /soportes : get all the soportes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of soportes in body
     */
    @GetMapping("/rounds/{companyId}/dates/{initialDate}/{finalDate}")
    @Timed
    public List<RoundDTO> getAllByCompanyIdAndDates(@PathVariable(value = "companyId") Long companyId, @PathVariable(value = "initialDate") Date initialDate, @PathVariable(value = "finalDate") Date finalDate) throws ExecutionException, InterruptedException {
        log.debug("REST request to get all Soportes");
        List<RoundDTO> r = roundsService.getAllByCompanyAndDates(companyId, initialDate, finalDate);
        return r;
    }

    @GetMapping("/rounds/get/{uid}")
    @Timed
    public RoundDTO getOne(@PathVariable(value = "uid") String uid) throws ExecutionException, InterruptedException {
        log.debug("REST request to get all Soportes");
        RoundDTO r = roundsService.getOne(uid);
        return r;
    }

    /**
     * GET  /soportes/:id : get the "id" soporte.
     *
     * @param id the id of the soporteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the soporteDTO, or with status 404 (Not Found)
     */
//    @GetMapping("/rounds/{id}")
//    @Timed
//    public ResponseEntity<SoporteDTO> getSoporte(@PathVariable Long id) {
//        log.debug("REST request to get Soporte : {}", id);
//        SoporteDTO soporteDTO = soporteService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(soporteDTO));
//    }


}
