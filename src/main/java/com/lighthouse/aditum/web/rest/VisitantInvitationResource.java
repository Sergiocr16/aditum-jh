package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.VisitantInvitationService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.VisitantInvitationDTO;
import com.lighthouse.aditum.web.websocket.RealTimeCompanyInfoService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VisitantInvitation.
 */
@RestController
@RequestMapping("/api")
public class VisitantInvitationResource {

    private final Logger log = LoggerFactory.getLogger(VisitantInvitationResource.class);

    private static final String ENTITY_NAME = "visitantInvitation";

    private final VisitantInvitationService visitantInvitationService;


    public VisitantInvitationResource(VisitantInvitationService visitantInvitationService) {
        this.visitantInvitationService = visitantInvitationService;
    }

    /**
     * POST  /visitant-invitations : Create a new visitantInvitation.
     *
     * @param visitantInvitationDTO the visitantInvitationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new visitantInvitationDTO, or with status 400 (Bad Request) if the visitantInvitation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/visitant-invitations")
    @Timed
    public ResponseEntity<VisitantInvitationDTO> createVisitantInvitation(@Valid @RequestBody VisitantInvitationDTO visitantInvitationDTO) throws URISyntaxException {
        log.debug("REST request to save VisitantInvitation : {}", visitantInvitationDTO);
        if (visitantInvitationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new visitantInvitation cannot already have an ID")).body(null);

        }
        VisitantInvitationDTO result = visitantInvitationService.save(visitantInvitationDTO);
        return ResponseEntity.created(new URI("/api/visitant-invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @GetMapping("/visitant-invitations/invited/findOneByHouse/{identificationNumber}/{houseId}/{companyId}/{hasSchedule}")
    @Timed
    public ResponseEntity<VisitantInvitationDTO> getVisitantByHouse( @PathVariable (value = "identificationNumber")  String identificationNumber,
                                                           @PathVariable(value = "houseId")  Long  houseId,
                                                           @PathVariable(value = "companyId")  Long companyId,
                                                                     @PathVariable(value = "hasSchedule")  int hasSchedule) {
        log.debug("REST invited visitant in specific house with this idenfiticacion : {}", identificationNumber);
        String a = "a";
        VisitantInvitationDTO visitantDTO = visitantInvitationService.findInvitedVisitorByHouse(identificationNumber,houseId,companyId,hasSchedule);
        String b = "an";
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitantDTO));
    }

    @GetMapping("/visitant-invitations/invited/findByHouse/{companyId}/{houseId}/{timeFormat}")
    @Timed
    public ResponseEntity<List<VisitantInvitationDTO>> getInvitedVisitorsByHouse(@PathVariable(value = "houseId")  Long  houseId,
                                                                       @PathVariable(value = "companyId")  Long companyId,
                                                                       @PathVariable(value = "timeFormat")  int timeFormat)
        throws URISyntaxException {
        log.debug("REST request to get a page of Visitants");
        Page<VisitantInvitationDTO> page = visitantInvitationService.findInvitedVisitorsByHouse(companyId, houseId,timeFormat);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/visitant-invitations/invited/forAdmins/{companyId}/{timeFormat}")
    @Timed
    public ResponseEntity<List<VisitantInvitationDTO>> getInvitedVisitorsForAdmins(@PathVariable(value = "companyId")  Long companyId,
                                                                                 @PathVariable(value = "timeFormat")  int timeFormat)
        throws URISyntaxException {
        log.debug("REST request to get a page of Visitants");
        Page<VisitantInvitationDTO> page = visitantInvitationService.findInvitedVisitorsForAdmins(companyId,timeFormat);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * PUT  /visitant-invitations : Updates an existing visitantInvitation.
     *
     * @param visitantInvitationDTO the visitantInvitationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated visitantInvitationDTO,
     * or with status 400 (Bad Request) if the visitantInvitationDTO is not valid,
     * or with status 500 (Internal Server Error) if the visitantInvitationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/visitant-invitations")
    @Timed
    public ResponseEntity<VisitantInvitationDTO> updateVisitantInvitation(@Valid @RequestBody VisitantInvitationDTO visitantInvitationDTO) throws URISyntaxException {
        log.debug("REST request to update VisitantInvitation : {}", visitantInvitationDTO);
        if (visitantInvitationDTO.getId() == null) {
            return createVisitantInvitation(visitantInvitationDTO);
        }
        VisitantInvitationDTO result = visitantInvitationService.save(visitantInvitationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, visitantInvitationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /visitant-invitations : get all the visitantInvitations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of visitantInvitations in body
     */
    @GetMapping("/visitant-invitations")
    @Timed
    public ResponseEntity<List<VisitantInvitationDTO>> getAllVisitantInvitations(Pageable pageable)throws URISyntaxException  {
        log.debug("REST request to get a page of VisitantInvitations");
        Page<VisitantInvitationDTO> page = visitantInvitationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitant-invitations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /visitant-invitations/:id : get the "id" visitantInvitation.
     *
     * @param id the id of the visitantInvitationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the visitantInvitationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/visitant-invitations/{id}")
    @Timed
    public ResponseEntity<VisitantInvitationDTO> getVisitantInvitation(@PathVariable Long id) {
        log.debug("REST request to get VisitantInvitation : {}", id);
        VisitantInvitationDTO visitantInvitationDTO = visitantInvitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitantInvitationDTO));
    }

    @GetMapping("/visitant-invitations/active/company/{companyId}")
    @Timed
    public  ResponseEntity<List<VisitantInvitationDTO>> getActiveInvitedByCompany(@PathVariable Long companyId) {
        log.debug("REST request to get active VisitantInvitation per company : {}", companyId);
        List<VisitantInvitationDTO> visitantInvitationDTO = visitantInvitationService.getActiveInvitedByCompany(companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitantInvitationDTO));
    }

    @GetMapping("/visitant-invitations/access/active/company/{companyId}/{houseId}/{name}")
    @Timed
    public ResponseEntity<List<VisitantInvitationDTO>> getActiveInvitedByCompanyPage(@PathVariable Long companyId,
                                                                                      @ApiParam Pageable pageable,
         @PathVariable String houseId
        , @PathVariable String name) throws URISyntaxException {
        log.debug("REST request to get active VisitantInvitation per company : {}", companyId);
        Page<VisitantInvitationDTO> page = visitantInvitationService.getActiveInvitedByCompanyPage(pageable,companyId,houseId,name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/residents");
        return  new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/visitant-invitations/active/house/{houseId}")
    @Timed
    public  ResponseEntity<List<VisitantInvitationDTO>> getActiveInvitedByHouse(@PathVariable Long houseId) {
        log.debug("REST request to get active VisitantInvitation per house: {}", houseId);
        List<VisitantInvitationDTO> visitantInvitationDTO = visitantInvitationService.getActiveInvitedByHouse(houseId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitantInvitationDTO));
    }

    /**
     * DELETE  /visitant-invitations/:id : delete the "id" visitantInvitation.
     *
     * @param id the id of the visitantInvitationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/visitant-invitations/{id}")
    @Timed
    public ResponseEntity<Void> deleteVisitantInvitation(@PathVariable Long id) {
        log.debug("REST request to delete VisitantInvitation : {}", id);
        visitantInvitationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
