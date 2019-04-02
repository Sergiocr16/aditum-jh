package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.BancoDocumentService;
import com.lighthouse.aditum.service.BancoService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.BancoDTO;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * REST controller for managing Banco.
 */
@RestController
@RequestMapping("/api")
public class BancoResource {

    private final Logger log = LoggerFactory.getLogger(BancoResource.class);

    private static final String ENTITY_NAME = "banco";
    private final BancoDocumentService bancoDocumentService;
    private final BancoService bancoService;


    public BancoResource(BancoDocumentService bancoDocumentService,BancoService bancoService) {
        this.bancoService = bancoService;
        this.bancoDocumentService = bancoDocumentService;
    }

    /**
     * POST  /bancos : Create a new banco.
     *
     * @param bancoDTO the bancoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bancoDTO, or with status 400 (Bad Request) if the banco has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bancos")
    @Timed
    public ResponseEntity<BancoDTO> createBanco(@Valid @RequestBody BancoDTO bancoDTO) throws URISyntaxException {
        log.debug("REST request to save Banco : {}", bancoDTO);
        if (bancoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new banco cannot already have an ID")).body(null);
        }
        BancoDTO result = bancoService.save(bancoDTO);
        return ResponseEntity.created(new URI("/api/bancos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bancos : Updates an existing banco.
     *
     * @param bancoDTO the bancoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bancoDTO,
     * or with status 400 (Bad Request) if the bancoDTO is not valid,
     * or with status 500 (Internal Server Error) if the bancoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bancos")
    @Timed
    public ResponseEntity<BancoDTO> updateBanco(@Valid @RequestBody BancoDTO bancoDTO) throws URISyntaxException {
        log.debug("REST request to update Banco : {}", bancoDTO);
        if (bancoDTO.getId() == null) {
            return createBanco(bancoDTO);
        }
        BancoDTO result = bancoService.save(bancoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bancoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bancos : get all the bancos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bancos in body
     */
    @GetMapping("/bancos")
    @Timed
    public ResponseEntity<List<BancoDTO>> getAllBancos(@ApiParam Pageable pageable,Long companyId)
         throws URISyntaxException {
            log.debug("REST request to get a page of Bancos");
            Page<BancoDTO> page = bancoService.findAll(pageable,companyId);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bancos");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }

    /**
     * GET  /bancos/:id : get the "id" banco.
     *
     * @param id the id of the bancoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bancoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bancos/{id}")
    @Timed
    public ResponseEntity<BancoDTO> getBanco(@PathVariable Long id) {
        log.debug("REST request to get Banco : {}", id);
        BancoDTO bancoDTO = bancoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bancoDTO));
    }
    @GetMapping("/bancos/accountStatus/{first_month_day}/{final_capital_date}/{initial_time}/{final_time}/{accountId}")
    @Timed
    public ResponseEntity<BancoDTO> getAccountStatus(
        @PathVariable("first_month_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime first_month_day,
        @PathVariable("final_capital_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_capital_date,
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "accountId") Long accountId
    ) {
        BancoDTO bancoDTO = bancoService.getAccountStatus(first_month_day,final_capital_date,initial_time,final_time,accountId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bancoDTO));
    }
    @GetMapping("/bancos/accountStatus/file/{first_month_day}/{final_capital_date}/{initial_time}/{final_time}/{accountId}")
    @Timed
    public void getAccountStatusFile(
        @PathVariable("first_month_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime first_month_day,
        @PathVariable("final_capital_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_capital_date,
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "accountId") Long accountId,
        HttpServletResponse response
    )  throws URISyntaxException, IOException {
        Locale local = new Locale("es", "ES");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(local);
        ZonedDateTime utcDateZoned = ZonedDateTime.now(ZoneId.of("Etc/UTC"));
        BancoDTO bancoDTO = bancoService.getAccountStatus(first_month_day,final_capital_date,initial_time,final_time,accountId);

        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initial_time + "[America/Regina]");
        String initialTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_initialTime);
        ZonedDateTime zd_finalTime = ZonedDateTime.parse(final_time + "[America/Regina]");
        String finalTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_finalTime);

        File file = bancoDocumentService.obtainFileToPrint(bancoDTO, initialTimeFormatted, finalTimeFormatted, bancoDTO.getCompanyId());
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
     * DELETE  /bancos/:id : delete the "id" banco.
     *
     * @param id the id of the bancoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bancos/{id}")
    @Timed
    public ResponseEntity<Void> deleteBanco(@PathVariable Long id) {
        log.debug("REST request to delete Banco : {}", id);
        bancoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
