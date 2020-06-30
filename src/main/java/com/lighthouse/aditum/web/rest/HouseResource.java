package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.domain.AdminInfo;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.User;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import com.lighthouse.aditum.service.*;
import com.lighthouse.aditum.service.dto.HouseAccessDoorDTO;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.HouseDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing House.
 */
@RestController
@RequestMapping("/api")
public class HouseResource {

    private final Logger log = LoggerFactory.getLogger(HouseResource.class);

    private static final String ENTITY_NAME = "house";

    private final HouseService houseService;

    private final MailService mailService;

    private final AdminInfoService adminInfoService;

    private final UserService userService;

    private final HouseMapper houseMapper;

    private final CompanyService companyService;

    public HouseResource(HouseService houseService,MailService mailService, AdminInfoService adminInfoService ,UserService userService,HouseMapper houseMapper,CompanyService companyService) {

        this.houseService = houseService;
        this.mailService = mailService;
        this.adminInfoService = adminInfoService;
        this.userService = userService;
        this.houseMapper=houseMapper;
        this.companyService = companyService;
    }

    /**
     * POST  /houses : Create a new house.
     *
     * @param houseDTO the houseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new houseDTO, or with status 400 (Bad Request) if the house has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/houses")
    @Timed
    public ResponseEntity<HouseDTO> createHouse(@Valid @RequestBody HouseDTO houseDTO) throws URISyntaxException {
        log.debug("REST request to save House : {}", houseDTO);
        if (houseDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new house cannot already have an ID")).body(null);
        }
        HouseDTO result = houseService.save(houseDTO);
        return ResponseEntity.created(new URI("/api/houses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /houses : Updates an existing house.
     *
     * @param houseDTO the houseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated houseDTO,
     * or with status 400 (Bad Request) if the houseDTO is not valid,
     * or with status 500 (Internal Server Error) if the houseDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/houses")
    @Timed
    public ResponseEntity<HouseDTO> updateHouse(@Valid @RequestBody HouseDTO houseDTO) throws URISyntaxException {
        log.debug("REST request to update House : {}", houseDTO);
        if (houseDTO.getId() == null) {
            return createHouse(houseDTO);
        }
        HouseDTO result = houseService.save(houseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, houseDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/houses/report/absence")
    @Timed
    public ResponseEntity<HouseDTO> reportHouseAbsence(@Valid @RequestBody HouseDTO houseDTO) throws URISyntaxException {
        log.debug("REST request to update House : {}", houseDTO);
        if (houseDTO.getId() == null) {
            return createHouse(houseDTO);
        }
        HouseDTO result = houseService.save(houseDTO);
        String companyName =  this.companyService.findOne(houseDTO.getCompanyId()).getName();
        this.adminInfoService.findAllByCompany(null,houseDTO.getCompanyId()).forEach(adminInfoDTO -> {
            Optional<User> user = this.userService.getUserWithAuthorities(adminInfoDTO.getUserId());
            House house = houseMapper.houseDTOToHouse(houseDTO);
            try {
                mailService.sendAbsenceEmail(house,user.get(),companyName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, houseDTO.getId().toString()))
            .body(result);
    }


    /**
     * GET  /houses : get all the houses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of houses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/houses")
    @Timed
    public ResponseEntity<List<HouseDTO>> getAllHouses(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Houses");
        Page<HouseDTO> page = houseService.findAll(companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/houses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/houses/clean/{companyId}")
    @Timed
    public ResponseEntity<List<HouseAccessDoorDTO>> getAllHousesClean(@PathVariable Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Houses");
        List<HouseAccessDoorDTO> houseAccessDoorDTOS = new ArrayList<>();
        Page<HouseDTO> allHouses = houseService.findAll(companyId);
        allHouses.getContent().forEach(houseDTO -> {
            HouseAccessDoorDTO houseClean= new HouseAccessDoorDTO();
            houseClean.setId(houseDTO.getId());
            houseClean.setHousenumber(houseDTO.getHousenumber());
            houseClean.setEmergencyKey(houseDTO.getEmergencyKey());
            houseClean.setSecurityKey(houseDTO.getSecurityKey());
            houseAccessDoorDTOS.add(houseClean);
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(allHouses, "/api/houses");
        return new ResponseEntity<>(houseAccessDoorDTOS, headers, HttpStatus.OK);
    }

    @GetMapping("/allHouses/{companyId}/{desocupated}/{houseNumber}")
    @Timed
    public ResponseEntity<List<HouseDTO>> getAllHousesFilter(@ApiParam Pageable pageable,@PathVariable Long companyId, @PathVariable String desocupated, @PathVariable String houseNumber)
        throws URISyntaxException {
        log.debug("REST request to get a page of Houses");
        Page<HouseDTO> page = houseService.findAllFilter(pageable,companyId,desocupated,houseNumber);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/houses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/houses-balance")
    @Timed
    public ResponseEntity<List<HouseDTO>> getAllHousesWidthBalance(Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Houses");
        Page<HouseDTO> page = houseService.findWithBalance(companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/houses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/houses-maintenance")
    @Timed
    public ResponseEntity<List<HouseDTO>> getAllHousesWithMaintenance(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Houses");
        Page<HouseDTO> page = houseService.findAllWithMaintenance(companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/houses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /houses/:id : get the "id" house.
     *
     * @param id the id of the houseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the houseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/houses/{id}")
    @Timed
    public ResponseEntity<HouseDTO> getHouse(@PathVariable Long id) {
        log.debug("REST request to get House : {}", id);
        HouseDTO houseDTO = houseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(houseDTO));
    }

    @GetMapping("/house/clean/{id}")
    @Timed
    public ResponseEntity<HouseAccessDoorDTO> getHouseClean(@PathVariable Long id) {
        log.debug("REST request to get House : {}", id);
        HouseDTO houseDTO = houseService.findOneClean(id);
        HouseAccessDoorDTO houseClean= new HouseAccessDoorDTO();
        houseClean.setId(houseDTO.getId());
        houseClean.setHousenumber(houseDTO.getHousenumber());
        houseClean.setEmergencyKey(houseDTO.getEmergencyKey());
        houseClean.setSecurityKey(houseDTO.getSecurityKey());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(houseClean));
    }

    @GetMapping("/house/defaulter/{id}")
    @Timed
    public ResponseEntity<HouseDTO> isHouseDefaulter(@PathVariable Long id) {
        log.debug("REST request to get House : {}", id);
        HouseDTO houseClean= new HouseDTO();
        houseClean.setDue(this.houseService.isHouseMorosa(id)?"1":"0");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(houseClean));
    }



    @GetMapping("/houses/housesByLoginCode/{loginCode}")

    @Timed
    public ResponseEntity<HouseDTO> getHouseByLoginCode(@PathVariable String loginCode) {
        log.debug("REST request to get House : {}", loginCode);
        HouseDTO houseDTO = houseService.findByLoginCodde(loginCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(houseDTO));
    }

    @GetMapping("/houses/validate/{houseNumber}/{extension}/{companyId}")
    @Timed
    public ResponseEntity<HouseDTO> validateHouse(
        @PathVariable (value = "houseNumber")  String houseNumber,
        @PathVariable(value = "extension")  String  extension,
        @PathVariable(value = "companyId")  Long  companyId
    ) {
        HouseDTO houseDTO = houseService.validateHouse(houseNumber,extension,companyId);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(houseDTO));
    }

    @GetMapping("/houses/validateUpdate/{houseId}/{houseNumber}/{extension}/{companyId}")
    @Timed
    public ResponseEntity<HouseDTO> validateHuseUpdate(
        @PathVariable (value = "houseId")  Long houseId,
        @PathVariable (value = "houseNumber")  String houseNumber,
        @PathVariable(value = "extension")  String  extension,
        @PathVariable(value = "companyId")  Long  companyId
    ) {
        HouseDTO houseDTO = houseService.validateUpdatedHouse(houseId,houseNumber,extension,companyId);
        String a = "a";
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(houseDTO));
    }

    /**
     * DELETE  /houses/:id : delete the "id" house.
     *
     * @param id the id of the houseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/houses/{id}")
    @Timed
    public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
        log.debug("REST request to delete House : {}", id);
        houseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }




}
