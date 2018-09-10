package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.domain.CommonArea;
import com.lighthouse.aditum.service.CommonAreaReservationsService;
import com.lighthouse.aditum.service.CommonAreaService;
import com.lighthouse.aditum.service.HouseService;
import com.lighthouse.aditum.service.ResidentService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * REST controller for managing CommonAreaReservations.
 */
@RestController
@RequestMapping("/api")
public class CommonAreaReservationsResource {

    private final Logger log = LoggerFactory.getLogger(CommonAreaReservationsResource.class);

    private static final String ENTITY_NAME = "commonAreaReservations";

    private final CommonAreaReservationsService commonAreaReservationsService;
    private final ResidentService residentService;
    private final HouseService houseService;
    private final CommonAreaService commonAreaService;
    public CommonAreaReservationsResource(CommonAreaReservationsService commonAreaReservationsService,ResidentService residentService,HouseService houseService,CommonAreaService commonAreaService) {
        this.commonAreaReservationsService = commonAreaReservationsService;
        this.residentService = residentService;
        this.houseService = houseService;
        this.commonAreaService = commonAreaService;
    }

    /**
     * POST  /common-area-reservations : Create a new commonAreaReservations.
     *
     * @param commonAreaReservationsDTO the commonAreaReservationsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commonAreaReservationsDTO, or with status 400 (Bad Request) if the commonAreaReservations has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/common-area-reservations")
    @Timed
    public ResponseEntity<CommonAreaReservationsDTO> createCommonAreaReservations(@Valid @RequestBody CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {
        log.debug("REST request to save CommonAreaReservations : {}", commonAreaReservationsDTO);
        if (commonAreaReservationsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new commonAreaReservations cannot already have an ID")).body(null);
        }
        CommonAreaReservationsDTO result = commonAreaReservationsService.save(commonAreaReservationsDTO);
        return ResponseEntity.created(new URI("/api/common-area-reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /common-area-reservations : Updates an existing commonAreaReservations.
     *
     * @param commonAreaReservationsDTO the commonAreaReservationsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commonAreaReservationsDTO,
     * or with status 400 (Bad Request) if the commonAreaReservationsDTO is not valid,
     * or with status 500 (Internal Server Error) if the commonAreaReservationsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/common-area-reservations")
    @Timed
    public ResponseEntity<CommonAreaReservationsDTO> updateCommonAreaReservations(@Valid @RequestBody CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {
        log.debug("REST request to update CommonAreaReservations : {}", commonAreaReservationsDTO);
        if (commonAreaReservationsDTO.getId() == null ) {
            return createCommonAreaReservations(commonAreaReservationsDTO);
        }
        CommonAreaReservationsDTO result = commonAreaReservationsService.save(commonAreaReservationsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commonAreaReservationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /common-area-reservations : get all the commonAreaReservations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commonAreaReservations in body
     */
    @GetMapping("/common-area-reservations")
    @Timed
    public ResponseEntity<List<CommonAreaReservationsDTO>> getAllCommonAreaReservations(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of CommonAreaReservations");
        Page<CommonAreaReservationsDTO> page = commonAreaReservationsService.findAll(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/common-area-reservations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/common-area-reservations/getPendingReservations/{companyId}")
    @Timed
    public ResponseEntity<List<CommonAreaReservationsDTO>> getPendingReservations(@ApiParam Pageable pageable, @PathVariable Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of CommonAreaReservations");
        Page<CommonAreaReservationsDTO> page = commonAreaReservationsService.getPendingReservations(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getPendingReservations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/common-area-reservations/getPendingAndAcceptedReservations/{companyId}")
    @Timed
    public ResponseEntity<List<CommonAreaReservationsDTO>> getPendingAndAcceptedReservations(@ApiParam Pageable pageable, @PathVariable Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of CommonAreaReservations");
        Page<CommonAreaReservationsDTO> page = commonAreaReservationsService.getPendingAndAcceptedReservations(pageable,companyId);
        page.getContent().forEach(commonAreaReservations -> {
            commonAreaReservations.setResident(residentService.findOne(commonAreaReservations.getResidentId()));
            commonAreaReservations.setCommonArea(commonAreaService.findOne(commonAreaReservations.getCommonAreaId()));
            commonAreaReservations.setHouse(houseService.findOne(commonAreaReservations.getHouseId()));

        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getPendingAndAcceptedReservations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/common-area-reservations/getReservationsByCommonArea/{commonAreaId}")
    @Timed
    public ResponseEntity<List<CommonAreaReservationsDTO>> getReservationsByCommonArea(@ApiParam Pageable pageable, @PathVariable Long commonAreaId)
        throws URISyntaxException {
        log.debug("REST request to get a page of CommonAreaReservations");
        Page<CommonAreaReservationsDTO> page = commonAreaReservationsService.getReservationsByCommonArea(pageable,commonAreaId);
        page.getContent().forEach(commonAreaReservations -> {
            commonAreaReservations.setResident(residentService.findOne(commonAreaReservations.getResidentId()));
            commonAreaReservations.setHouse(houseService.findOne(commonAreaReservations.getHouseId()));


        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getPendingReservations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /common-area-reservations/:id : get the "id" commonAreaReservations.
     *
     * @param id the id of the commonAreaReservationsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commonAreaReservationsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/common-area-reservations/{id}")
    @Timed
    public ResponseEntity<CommonAreaReservationsDTO> getCommonAreaReservations(@PathVariable Long id) {
        log.debug("REST request to get CommonAreaReservations : {}", id);
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsService.findOne(id);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commonAreaReservationsDTO));
    }

    @GetMapping("/common-area-reservations/isAvailableToReserve/{maximun_hours}/{reservation_date}/{initial_time}/{final_time}/{common_area_id}")
    @Timed
    public CommonAreaReservationsDTO isAvailableToReserve(
        @PathVariable (value = "maximun_hours")  int maximun_hours,
        @PathVariable (value = "reservation_date")  String reservation_date,
        @PathVariable (value = "initial_time")  String initial_time,
        @PathVariable(value = "final_time")  String  final_time,
        @PathVariable(value = "common_area_id")  Long common_area_id
        ){
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsService.isAvailableToReserve(maximun_hours,reservation_date,initial_time,final_time,common_area_id,null);

            return commonAreaReservationsDTO;
    }
    @GetMapping("/common-area-reservations/isAvailableToReserveNotNull/{maximun_hours}/{reservation_date}/{initial_time}/{final_time}/{common_area_id}/{reservation_id}")
    @Timed
    public CommonAreaReservationsDTO isAvailableToReserveNotNull(
        @PathVariable (value = "maximun_hours")  int maximun_hours,
        @PathVariable (value = "reservation_date")  String reservation_date,
        @PathVariable (value = "initial_time")  String initial_time,
        @PathVariable(value = "final_time")  String  final_time,
        @PathVariable(value = "common_area_id")  Long common_area_id,
        @PathVariable(value = "reservation_id")  Long reservation_id
        ){
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsService.isAvailableToReserve(maximun_hours,reservation_date,initial_time,final_time,common_area_id,reservation_id);

        return commonAreaReservationsDTO;
    }
    /**
     * DELETE  /common-area-reservations/:id : delete the "id" commonAreaReservations.
     *
     * @param id the id of the commonAreaReservationsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/common-area-reservations/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommonAreaReservations(@PathVariable Long id) {
        log.debug("REST request to delete CommonAreaReservations : {}", id);
        commonAreaReservationsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
