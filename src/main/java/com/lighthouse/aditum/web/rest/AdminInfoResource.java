package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import com.lighthouse.aditum.service.AdminInfoService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing AdminInfo.
 */
@RestController
@RequestMapping("/api")
public class AdminInfoResource {

    private final Logger log = LoggerFactory.getLogger(AdminInfoResource.class);

    private static final String ENTITY_NAME = "adminInfo";

    private final AdminInfoService adminInfoService;

    public AdminInfoResource(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    /**
     * POST  /admin-infos : Create a new adminInfo.
     *
     * @param adminInfoDTO the adminInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adminInfoDTO, or with status 400 (Bad Request) if the adminInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/admin-infos")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<AdminInfoDTO> createAdminInfo(@Valid @RequestBody AdminInfoDTO adminInfoDTO) throws URISyntaxException {
        log.debug("REST request to save AdminInfo : {}", adminInfoDTO);
        if (adminInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new adminInfo cannot already have an ID")).body(null);
        }
        AdminInfoDTO result = adminInfoService.save(adminInfoDTO);
        return ResponseEntity.created(new URI("/api/admin-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /admin-infos : Updates an existing adminInfo.
     *
     * @param adminInfoDTO the adminInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adminInfoDTO,
     * or with status 400 (Bad Request) if the adminInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the adminInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/admin-infos")
    @Timed
    public ResponseEntity<AdminInfoDTO> updateAdminInfo(@Valid @RequestBody AdminInfoDTO adminInfoDTO) throws URISyntaxException {
        log.debug("REST request to update AdminInfo : {}", adminInfoDTO);
        if (adminInfoDTO.getId() == null) {
            return createAdminInfo(adminInfoDTO);
        }
        AdminInfoDTO result = adminInfoService.save(adminInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adminInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /admin-infos : get all the adminInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of adminInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/admin-infos")
    @Timed
    public ResponseEntity<List<AdminInfoDTO>> getAllAdminInfos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AdminInfos");
        Page<AdminInfoDTO> page = adminInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/admin-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /admin-infos/:id : get the "id" adminInfo.
     *
     * @param id the id of the adminInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adminInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/admin-infos/{id}")
    @Timed
    public ResponseEntity<AdminInfoDTO> getAdminInfo(@PathVariable Long id) {
        log.debug("REST request to get AdminInfo : {}", id);
        AdminInfoDTO adminInfoDTO = adminInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(adminInfoDTO));
    }

    @GetMapping("/admin-infos/findByUserId/{id}")
    @Timed
    public ResponseEntity<AdminInfoDTO> getAdminInfoByUserId(@PathVariable Long id) {
        log.debug("REST request to get AdminInfo : {}", id);
        AdminInfoDTO adminInfoDTO = adminInfoService.findOneByUserId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(adminInfoDTO));
    }

    /**
     * DELETE  /admin-infos/:id : delete the "id" adminInfo.
     *
     * @param id the id of the adminInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/admin-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdminInfo(@PathVariable Long id) {
        log.debug("REST request to delete AdminInfo : {}", id);
        adminInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/getAdminsByCompanyId")
    @Timed
    public ResponseEntity<List<AdminInfoDTO>> getByCompanyId(@ApiParam Pageable pageable, Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<AdminInfoDTO> page = adminInfoService.findAllByCompany(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/getAdminsByCompanyId");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
