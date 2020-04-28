package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.HouseSecurityDirectionService;
import com.lighthouse.aditum.service.dto.HouseArDTO;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.HouseSecurityDirectionDTO;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
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
 * REST controller for managing HouseSecurityDirection.
 */
@RestController
@RequestMapping("/api")
public class HouseSecurityDirectionResource {

    private final Logger log = LoggerFactory.getLogger(HouseSecurityDirectionResource.class);

    private static final String ENTITY_NAME = "houseSecurityDirection";

    private final HouseSecurityDirectionService houseSecurityDirectionService;

    public HouseSecurityDirectionResource(HouseSecurityDirectionService houseSecurityDirectionService) {
        this.houseSecurityDirectionService = houseSecurityDirectionService;
    }

    /**
     * POST  /house-security-directions : Create a new houseSecurityDirection.
     * <p>
     * //     * @param houseSecurityDirectionDTO the houseSecurityDirectionDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new houseSecurityDirectionDTO, or with status 400 (Bad Request) if the houseSecurityDirection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/house-security-directions")
    @Timed
    public ResponseEntity<HouseSecurityDirectionDTO> createHouseSecurityDirection(@RequestBody HouseArDTO houseArDTO) throws URISyntaxException {
        log.debug("REST request to save HouseSecurityDirection : {}", houseArDTO);
        if (houseArDTO.getId() != null) {
//            throw new BadRequestAlertException("A new houseSecurityDirection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HouseSecurityDirectionDTO result = houseSecurityDirectionService.save(houseArDTO);
        return ResponseEntity.created(new URI("/api/house-security-directions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /house-security-directions : Updates an existing houseSecurityDirection.
     * <p>
     * //     * @param houseSecurityDirectionDTO the houseSecurityDirectionDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated houseSecurityDirectionDTO,
     * or with status 400 (Bad Request) if the houseSecurityDirectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the houseSecurityDirectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/house-security-directions")
    @Timed
    public ResponseEntity<HouseSecurityDirectionDTO> updateHouseSecurityDirection(@RequestBody HouseArDTO houseArDTO) throws URISyntaxException {
        log.debug("REST request to update HouseSecurityDirection : {}", houseArDTO);
        if (houseArDTO.getId() == null) {
            return createHouseSecurityDirection(houseArDTO);
        }
        HouseSecurityDirectionDTO result = houseSecurityDirectionService.save(houseArDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, houseArDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /house-security-directions : get all the houseSecurityDirections.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of houseSecurityDirections in body
     */
    @GetMapping("/house-security-directions")
    @Timed
    public Page<HouseArDTO> getAllHouseSecurityDirections(Long companyId) {
        log.debug("REST request to get all HouseSecurityDirections");
        return houseSecurityDirectionService.findAll(companyId);
    }

    @GetMapping("/allHousesAR/{companyId}/{desocupated}/{houseNumber}")
    @Timed
    public ResponseEntity<List<HouseArDTO>> getAllHousesFilter(@ApiParam Pageable pageable, @PathVariable Long companyId, @PathVariable String desocupated, @PathVariable String houseNumber)
        throws URISyntaxException {
        log.debug("REST request to get a page of Houses");
        Page<HouseArDTO> page = houseSecurityDirectionService.findAllFilter(pageable, companyId, desocupated, houseNumber);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/houses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /house-security-directions/:id : get the "id" houseSecurityDirection.
     *
     * @param id the id of the houseSecurityDirectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the houseSecurityDirectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/house-security-directions/{id}")
    @Timed
    public ResponseEntity<HouseArDTO> getHouseSecurityDirection(@PathVariable Long id) {
        log.debug("REST request to get HouseSecurityDirection : {}", id);
        HouseArDTO houseArDTO = houseSecurityDirectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(houseArDTO));
    }

    /**
     * DELETE  /house-security-directions/:id : delete the "id" houseSecurityDirection.
     *
     * @param id the id of the houseSecurityDirectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/house-security-directions/{id}")
    @Timed
    public ResponseEntity<Void> deleteHouseSecurityDirection(@PathVariable Long id) {
        log.debug("REST request to delete HouseSecurityDirection : {}", id);
        houseSecurityDirectionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
