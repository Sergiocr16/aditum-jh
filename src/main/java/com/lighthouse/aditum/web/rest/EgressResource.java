package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.EgressCategoryService;
import com.lighthouse.aditum.service.EgressDocumentService;
import com.lighthouse.aditum.service.ProveedorService;
import com.lighthouse.aditum.service.dto.EgressReportDTO;
import com.lighthouse.aditum.service.EgressService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.EgressDTO;
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

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * REST controller for managing Egress.
 */
@RestController
@RequestMapping("/api")
public class EgressResource {

    private final Logger log = LoggerFactory.getLogger(EgressResource.class);

    private static final String ENTITY_NAME = "egress";

    private final EgressService egressService;

    private final EgressCategoryService egressCategoryService;

    private final EgressDocumentService egressDocumentService;

    private final ProveedorService proveedorService;

    public EgressResource(EgressService egressService, EgressCategoryService egressCategoryService, EgressDocumentService egressDocumentService,ProveedorService proveedorService) {
        this.egressService = egressService;
        this.egressCategoryService = egressCategoryService;
        this.egressDocumentService = egressDocumentService;
        this.proveedorService = proveedorService;
    }

    /**
     * POST  /egresses : Create a new egress.
     *
     * @param egressDTO the egressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new egressDTO, or with status 400 (Bad Request) if the egress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/egresses")
    @Timed
    public ResponseEntity<EgressDTO> createEgress(@Valid @RequestBody EgressDTO egressDTO) throws URISyntaxException {
        log.debug("REST request to save Egress : {}", egressDTO);
        if (egressDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new egress cannot already have an ID")).body(null);
        }
        EgressDTO result = egressService.save(egressDTO);
        return ResponseEntity.created(new URI("/api/egresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /egresses : Updates an existing egress.
     *
     * @param egressDTO the egressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated egressDTO,
     * or with status 400 (Bad Request) if the egressDTO is not valid,
     * or with status 500 (Internal Server Error) if the egressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/egresses")
    @Timed
    public ResponseEntity<EgressDTO> updateEgress(@Valid @RequestBody EgressDTO egressDTO) throws URISyntaxException {
        log.debug("REST request to update Egress : {}", egressDTO);
        if (egressDTO.getId() == null) {
            return createEgress(egressDTO);
        }
        EgressDTO result = egressService.save(egressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, egressDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /egresses : get all the egresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of egresses in body
     */
    @GetMapping("/egresses")
    @Timed
    public ResponseEntity<List<EgressDTO>> getAllEgresses(@ApiParam Pageable pageable, Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Egresses");
        Page<EgressDTO> page = egressService.findAll(pageable, companyId);
        page.getContent().forEach(egressDTO -> {
            egressDTO.setCategoryName(egressCategoryService.findOne(Long.parseLong(egressDTO.getCategory())).getCategory());

        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/egresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /egresses/:id : get the "id" egress.
     *
     * @param id the id of the egressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the egressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/egresses/{id}")
    @Timed
    public ResponseEntity<EgressDTO> getEgress(@PathVariable Long id) {
        log.debug("REST request to get Egress : {}", id);
        EgressDTO egressDTO = egressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(egressDTO));
    }

    @GetMapping("/egresses/between/{initial_time}/{final_time}/byCompany/{companyId}")
    @Timed
    public ResponseEntity<List<EgressDTO>> getBetweenDatesAndCompany(
        @PathVariable(value = "initial_time") String initial_time,
        @PathVariable(value = "final_time") String final_time,
        @PathVariable(value = "companyId") Long companyId,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<EgressDTO> page = egressService.findByDatesBetweenAndCompany(pageable, initial_time, final_time, companyId);
        page.getContent().forEach(egressDTO -> {
            egressDTO.setCategory(egressCategoryService.findOne(Long.parseLong(egressDTO.getCategory())).getCategory());

        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/egress");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/egresses/betweenCobro/{initial_time}/{final_time}/byCompany/{companyId}")
    @Timed
    public ResponseEntity<List<EgressDTO>> getBetweenCobroDatesAndCompany(
        @PathVariable(value = "initial_time") String initial_time,
        @PathVariable(value = "final_time") String final_time,
        @PathVariable(value = "companyId") Long companyId,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<EgressDTO> page = egressService.findByCobroDatesBetweenAndCompany(pageable, initial_time, final_time, companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/egress");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/egresses/between/{initial_time}/{final_time}/byCompany/{companyId}/andAccount/{accountId}")
    @Timed
    public ResponseEntity<List<EgressDTO>> getBetweenDatesAndCompanyAndAccount(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "companyId") Long companyId,
        @PathVariable(value = "accountId") String accountId,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<EgressDTO> page = egressService.findByDatesBetweenAndCompanyAndAccount(pageable, initial_time, final_time, companyId, accountId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/egress");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/egresses/reportEgressToPay/file/{final_time}/byCompany/{companyId}")
    @Timed
    public void getEgressToPayFile(@PathVariable(value = "companyId") Long companyId,
                                   @PathVariable(value = "final_time") String final_time,
                                   HttpServletResponse response) throws URISyntaxException, IOException {


        Locale local = new Locale("es", "ES");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(local);
        ZonedDateTime utcDateZoned = ZonedDateTime.now(ZoneId.of("Etc/UTC"));
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "ES"));
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        Page<EgressDTO> egressReportDTO = egressService.getEgressToPay(null, final_time, companyId);

        double totalEgressToPay = 0.0;
        egressReportDTO.getContent().forEach(egressDTO -> {
            egressDTO.setCategory(egressCategoryService.findOne(Long.parseLong(egressDTO.getCategory())).getCategory());
            egressDTO.setProveedor(proveedorService.findOne(Long.parseLong(egressDTO.getProveedor())).getEmpresa());
            egressDTO.setExpirationDateFormatted(spanish.format(egressDTO.getExpirationDate()));
            egressDTO.setDateFormatted(spanish.format(egressDTO.getDate()));


        });

        for (int i = 0; i < egressReportDTO.getContent().size(); i++) {
            egressReportDTO.getContent().get(i).setTotalFormatted(currencyFormatter.format(Double.parseDouble(egressReportDTO.getContent().get(i).getTotal())).substring(1));
            totalEgressToPay = totalEgressToPay + Double.parseDouble(egressReportDTO.getContent().get(i).getTotal());
        }
        ZonedDateTime zd_finalTime = ZonedDateTime.parse(final_time + "[America/Regina]");
        String finalTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_finalTime);

        File file = egressDocumentService.obtainFileToPrintEgressToPay(egressReportDTO, finalTimeFormatted, companyId,currencyFormatter.format(totalEgressToPay).substring(1));
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

    @GetMapping("/egresses/reportEgressToPay/{final_time}/byCompany/{companyId}")
    @Timed
    public ResponseEntity<List<EgressDTO>> getEgressToPay(

        @PathVariable(value = "companyId") Long companyId,
        @PathVariable(value = "final_time") String final_time,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");

        Page<EgressDTO> egressReportDTO = egressService.getEgressToPay(pageable, final_time, companyId);
        egressReportDTO.getContent().forEach(egressDTO -> {
            egressDTO.setCategory(egressCategoryService.findOne(Long.parseLong(egressDTO.getCategory())).getCategory());

        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(egressReportDTO, "/api/egress");
        return new ResponseEntity<>(egressReportDTO.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/egresses/egressReport/{initial_time}/{final_time}/{companyId}/{empresas}/{selectedCampos}")
    @Timed
    public ResponseEntity<EgressReportDTO> egressReport(
        @PathVariable(value = "initial_time") String initial_time,
        @PathVariable(value = "final_time") String final_time,
        @PathVariable(value = "companyId") Long companyId,
        @PathVariable(value = "empresas") String empresas,
        @PathVariable(value = "selectedCampos") String selectedCampos,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        EgressReportDTO egressReportDTO = egressService.egressReport(pageable, initial_time, final_time, companyId, empresas, selectedCampos);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(egressReportDTO));
    }

    @GetMapping("/egresses/file/{egressObject}")
    @Timed
    public void getEgressFile(@PathVariable String egressObject,
                              HttpServletResponse response) throws URISyntaxException, IOException {

        String[] parts = egressObject.split("}");
        EgressReportDTO egressReportDTO = egressService.egressReport(null, parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4]);
        Locale local = new Locale("es", "ES");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(local);
        ZonedDateTime utcDateZoned = ZonedDateTime.now(ZoneId.of("Etc/UTC"));

        ZonedDateTime zd_initialTime = ZonedDateTime.parse(parts[0] + "[America/Regina]");
        String initialTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_initialTime);
        ZonedDateTime zd_finalTime = ZonedDateTime.parse(parts[1] + "[America/Regina]");
        String finalTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_finalTime);

        File file = egressDocumentService.obtainFileToPrint(egressReportDTO, initialTimeFormatted, finalTimeFormatted, Long.parseLong(parts[2]));
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

    /**
     * DELETE  /egresses/:id : delete the "id" egress.
     *
     * @param id the id of the egressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/egresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteEgress(@PathVariable Long id) {
        log.debug("REST request to delete Egress : {}", id);
        egressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
