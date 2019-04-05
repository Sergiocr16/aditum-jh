package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.config.BugsnagConfig;
import com.lighthouse.aditum.service.VisitantDocumentService;
import com.lighthouse.aditum.service.VisitantService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.VisitantDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Visitant.
 */
@RestController
@RequestMapping("/api")
public class VisitantResource {

    private final Logger log = LoggerFactory.getLogger(VisitantResource.class);

    private static final String ENTITY_NAME = "visitant";

    private final VisitantService visitantService;

    private final VisitantDocumentService visitantDocumentService;

    private final BugsnagConfig bugsnagConfig;


    public VisitantResource(VisitantService visitantService,VisitantDocumentService visitantDocumentService,BugsnagConfig bugsnagConfig) {
        this.visitantService = visitantService;
        this.visitantDocumentService = visitantDocumentService;
        this.bugsnagConfig = bugsnagConfig;
    }

    /**
     * POST  /visitants : Create a new visitant.
     *
     * @param visitantDTO the visitantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new visitantDTO, or with status 400 (Bad Request) if the visitant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/visitants")
    @Timed
    public ResponseEntity<VisitantDTO> createVisitant(@Valid @RequestBody VisitantDTO visitantDTO) throws URISyntaxException {
        log.debug("REST request to save Visitant : {}", visitantDTO);
        if (visitantDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new visitant cannot already have an ID")).body(null);
        }
        VisitantDTO result = visitantService.save(visitantDTO);
        return ResponseEntity.created(new URI("/api/visitants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /visitants : Updates an existing visitant.
     *
     * @param visitantDTO the visitantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated visitantDTO,
     * or with status 400 (Bad Request) if the visitantDTO is not valid,
     * or with status 500 (Internal Server Error) if the visitantDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/visitants")
    @Timed
    public ResponseEntity<VisitantDTO> updateVisitant(@Valid @RequestBody VisitantDTO visitantDTO) throws URISyntaxException {
        log.debug("REST request to update Visitant : {}", visitantDTO);
        if (visitantDTO.getId() == null) {
            return createVisitant(visitantDTO);
        }
        VisitantDTO result = visitantService.save(visitantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, visitantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /visitants : get all the visitants.
     *

     * @return the ResponseEntity with status 200 (OK) and the list of visitants in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/visitants")
    @Timed
    public ResponseEntity<List<VisitantDTO>> getAllVisitants(Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Visitants");
        Page<VisitantDTO> page = visitantService.findAll(companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/visitants/invited/all/{companyId}")
    @Timed
    public ResponseEntity<List<VisitantDTO>> findAllInvited(@PathVariable(value = "companyId")  Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Visitants");
        Page<VisitantDTO> page = visitantService.findAllInvited(companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/visitants/invited/finByHouse/{companyId}/{houseId}")
    @Timed
    public ResponseEntity<List<VisitantDTO>> getInvitedVisitorsByHouse(@PathVariable(value = "houseId")  Long  houseId,
                                                                       @PathVariable(value = "companyId")  Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Visitants");
        Page<VisitantDTO> page = visitantService.findInvitedVisitorsByHouse(companyId, houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/visitants/finByHouse/lastMonth/{houseId}")
    @Timed
    public ResponseEntity<List<VisitantDTO>> getVisitorsByHouseInLastMonth(@PathVariable Long  houseId )
        throws URISyntaxException {
        log.debug("REST request to get a page of Visitants");
        Page<VisitantDTO> page = visitantService.findByHouseInLastMonth(houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/visitants/between/{initial_time}/{final_time}/byHouse/{houseId}")
    @Timed
    public ResponseEntity<List<VisitantDTO>> getBetweenDatesAndHouse(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "houseId")  Long houseId)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<VisitantDTO> page = visitantService.findByDatesBetweenAndHouse(initial_time,final_time,houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitant");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/visitants/filter/{initial_time}/{final_time}/byCompany/{companyId}/byHouse/{houseId}/byName/{name}")
    @Timed
    public ResponseEntity<List<VisitantDTO>> getByFilter(
        @ApiParam Pageable pageable,
        @PathVariable("companyId") Long companyId,
        @PathVariable("houseId")String houseId,
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable ("name") String name)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<VisitantDTO> page = visitantService.findByFilter(pageable,companyId,houseId,initial_time,final_time,name);
        HttpHeaders headers  = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitant");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/visitants/finByCompany/lastMonth/{companyId}")
    @Timed
    public ResponseEntity<List<VisitantDTO>> getVisitorsByCompanyInLastMonth(@PathVariable Long  companyId )
        throws URISyntaxException {
        log.debug("REST request to get a page of Visitants");
        Page<VisitantDTO> page = visitantService.findByCompanyInLastMonth(companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/visitants/file/{initial_time}/{final_time}/{companyId}/{houseId}")
    @Timed
    public void getVisitantReportFile(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "companyId") Long companyId,
        @PathVariable(value = "houseId") Long houseId,
        HttpServletResponse response
    )  throws URISyntaxException, IOException {
        Locale local = new Locale("es", "CR");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(local);
        Page<VisitantDTO> visitantDTOS;
        if(houseId==-1){
            visitantDTOS = visitantService.findByDatesBetweenAndCompany(initial_time,final_time,companyId);

        }else{
            visitantDTOS = visitantService.findByDatesBetweenAndHouse(initial_time,final_time,houseId);

        }


        ZonedDateTime zd_initialTime = initial_time;
        String initialTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_initialTime);
        ZonedDateTime zd_finalTime = final_time;
        String finalTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_finalTime);

        File file = visitantDocumentService.obtainFileToPrint(visitantDTOS, initialTimeFormatted, finalTimeFormatted, companyId,houseId);
        FileInputStream stream = new FileInputStream(file);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        IOUtils.copy(stream, response.getOutputStream());
        stream.close();
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(400000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                file.delete();

            }
        }.start();

    }
    @GetMapping("/visitants/between/{initial_time}/{final_time}/byCompany/{companyId}")
    @Timed
    public ResponseEntity<List<VisitantDTO>> getBetweenDatesAndCompany(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "companyId")  Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<VisitantDTO> page = visitantService.findByDatesBetweenAndCompany(initial_time,final_time,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitant");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /visitants/:id : get the "id" visitant.
     *
     * @param id the id of the visitantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the visitantDTO, or with status 404 (Not Found)
     */
    @GetMapping("/visitants/{id}")
    @Timed
    public ResponseEntity<VisitantDTO> getVisitant(@PathVariable Long id) {
        log.debug("REST request to get Visitant : {}", id);
        VisitantDTO visitantDTO = visitantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitantDTO));
    }


    @GetMapping("/visitants/invited/findOneByHouse/{identificationNumber}/{houseId}/{companyId}")
    @Timed
    public ResponseEntity<VisitantDTO> getVisitantByHouse( @PathVariable (value = "identificationNumber")  String identificationNumber,
                                                           @PathVariable(value = "houseId")  Long  houseId,
                                                           @PathVariable(value = "companyId")  Long companyId) {
        log.debug("REST invited visitant in specific house with this idenfiticacion : {}", identificationNumber);
        VisitantDTO visitantDTO = visitantService.findInvitedVisitorByHouse(identificationNumber,houseId,companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitantDTO));
    }

    /**
     * DELETE  /visitants/:id : delete the "id" visitant.
     *
     * @param id the id of the visitantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/visitants/{id}")
    @Timed
    public ResponseEntity<Void> deleteVisitant(@PathVariable Long id) {
        log.debug("REST request to delete Visitant : {}", id);
        visitantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
