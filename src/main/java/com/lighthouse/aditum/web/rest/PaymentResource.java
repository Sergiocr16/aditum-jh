package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.CompanyConfigurationService;
import com.lighthouse.aditum.service.HistoricalDefaulterService;
import com.lighthouse.aditum.service.IndicadoresEconomicosBccr;
import com.lighthouse.aditum.service.PaymentService;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.util.RandomUtil;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
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
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Payment.
 */
@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";

    private final PaymentService paymentService;

    private final IndicadoresEconomicosBccr indicadoresEconomicosBccr;

    private final HistoricalDefaulterService historicalDefaulterService;

    private final CompanyConfigurationService companyConfigurationService;

    public PaymentResource(CompanyConfigurationService companyConfigurationService, HistoricalDefaulterService historicalDefaulterService, PaymentService paymentService, IndicadoresEconomicosBccr indicadoresEconomicosBccr) {
        this.paymentService = paymentService;
        this.indicadoresEconomicosBccr = indicadoresEconomicosBccr;
        this.companyConfigurationService = companyConfigurationService;
        this.historicalDefaulterService = historicalDefaulterService;
    }

    /**
     * POST  /payments : Create a new payment.
     *
     * @param paymentDTO the paymentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentDTO, or with status 400 (Bad Request) if the payment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payments")
    @Timed
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody CreatePaymentDTO paymentDTO) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", paymentDTO);
        if (paymentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new payment cannot already have an ID")).body(null);
        }
        String currency = companyConfigurationService.getByCompanyId(null, Long.parseLong(paymentDTO.getCompanyId() + "")).getContent().get(0).getCurrency();
        PaymentDTO result = paymentService.save(paymentDTO, currency);
        this.historicalDefaulterService.formatHistoricalReportByHouse(paymentDTO.getHouseId(), paymentDTO.getDate(), currency, paymentDTO.getCompanyId());
        return ResponseEntity.created(new URI("/api/payments/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }


    /**
     * PUT  /payments : Updates an existing payment.
     *
     * @param paymentDTO the paymentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentDTO,
     * or with status 400 (Bad Request) if the paymentDTO is not valid,
     * or with status 500 (Internal Server Error) if the paymentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payments")
    @Timed
    public ResponseEntity<PaymentDTO> updatePayment(@Valid @RequestBody CreatePaymentDTO paymentDTO) throws URISyntaxException {
        log.debug("REST request to update Payment : {}", paymentDTO);
        if (paymentDTO.getId() == null) {
            return createPayment(paymentDTO);
        }
        String currency = companyConfigurationService.getByCompanyId(null, Long.parseLong(paymentDTO.getCompanyId() + "")).getContent().get(0).getCurrency();
        PaymentDTO result = paymentService.save(paymentDTO, currency);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentDTO.getId().toString())).body(result);
    }

    @GetMapping("/payments/between/{initial_time}/{final_time}/byHouseId/{houseId}")
    @Timed
    public ResponseEntity<List<PaymentDTO>> getByHouseFilteredByDates(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "houseId") String houseId,
        @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        String houseIdD = RandomUtil.decrypt(houseId);
        Page<PaymentDTO> page = paymentService.findByHouseFilteredByDate(pageable, Long.parseLong(houseIdD), initial_time, final_time);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payments/byHouseFilteredByDate");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/payments/complete/find/{id}")
    @Timed
    public ResponseEntity<PaymentDTO> getPaymentComplete(@PathVariable(value = "id") Long id) throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        PaymentDTO paymentDTO = paymentService.findOneComplete(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentDTO));
    }

    @GetMapping("/payments-byHouse/{houseId}")
    @Timed
    public ResponseEntity<List<PaymentDTO>> getByHouse(@ApiParam Pageable pageable, @PathVariable(value = "houseId") String houseId) throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Long houseIdD = Long.parseLong(RandomUtil.decrypt(houseId));
        Page<PaymentDTO> page = paymentService.findByHouse(pageable, houseIdD);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payments/byHouse");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/format-old-charges/{companyId}")
    @Timed
    public List<ChargeDTO> formatOldCharges(@PathVariable Long companyId) {
        log.debug("REST request to get a Watches between dates");
        List<ChargeDTO> c = paymentService.formatOldCharges(companyId);
        return new ArrayList<ChargeDTO>();
    }

    @GetMapping("/format-new-payments/{companyId}")
    @Timed
    public List<PaymentDTO> formatNewPayments(@PathVariable Long companyId) {
        log.debug("REST request to get a Watches between dates");
        List<PaymentDTO> page = paymentService.formatNewPayments(companyId);
        return page;
    }

    @GetMapping("/payments-water-byHouse/{houseId}")
    @Timed
    public List<PaymentDTO> getWaterPaymentsByHouse(@ApiParam Pageable pageable, @PathVariable(value = "houseId") Long houseId) throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        List<PaymentDTO> page = paymentService.findWaterPaymentsByHouse(pageable, houseId);
        return page;
    }

    @GetMapping("/payments/between/{initial_time}/{final_time}/byCompany/{companyId}")
    @Timed
    public ResponseEntity<List<PaymentDTO>> getBetweenDatesAndCompany(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "companyId") int companyId, @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<PaymentDTO> page = paymentService.findByDatesBetweenAndCompany(pageable, initial_time, final_time, companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/payments/report/between/{initial_time}/{final_time}/byCompany/{companyId}/{account}/{paymentMethod}/{houseId}/{category}")
    @Timed
    public ResponseEntity<IncomeReportDTO> getIncomeReportByBetweenDatesAndCompany(@PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time, @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
                                                                                   @PathVariable(value = "companyId") int companyId,
                                                                                   @PathVariable(value = "houseId") String houseId,
                                                                                   @PathVariable(value = "paymentMethod") String paymentMethod,
                                                                                   @PathVariable(value = "category") String category,
                                                                                   @PathVariable(value = "account") String account,
                                                                                   @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        IncomeReportDTO incomeReport = paymentService.findIncomeReportByCompanyAndDatesBetween(pageable, initial_time, final_time, companyId, houseId, paymentMethod, category, account);
        return new ResponseEntity<>(incomeReport, HttpStatus.OK);
    }

    @GetMapping("/payments/between/{initial_time}/{final_time}/byCompany/{companyId}/andAccount/{accountId}")
    @Timed
    public ResponseEntity<List<PaymentDTO>> getBetweenDatesAndCompanyAndAccount(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "companyId") int companyId, @PathVariable(value = "accountId") String accountId, @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a Watches between dates");
        Page<PaymentDTO> page = paymentService.findByDatesBetweenAndCompanyAndAccount(pageable, initial_time, final_time, companyId, accountId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payments : get all the payments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of payments in body
     */
    @GetMapping("/payments")
    @Timed
    public ResponseEntity<List<PaymentDTO>> getAllPayments(@ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Payments");
        Page<PaymentDTO> page = paymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payments/:id : get the "id" payment.
     *
     * @param id the id of the paymentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payments/{id}")
    @Timed
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        PaymentDTO paymentDTO = paymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentDTO));
    }

    /**
     * DELETE  /payments/:id : delete the "id" payment.
     *
     * @param id the id of the paymentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payments/{id}")
    @Timed
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.debug("REST request to delete Payment : {}", id);
        paymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/payments/file/{paymentId}")
    @Timed
    public void getFile(@PathVariable Long paymentId, HttpServletResponse response) throws URISyntaxException, IOException {
        File file = paymentService.obtainFileToPrint(paymentId);
        FileInputStream stream = new FileInputStream(file);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        IOUtils.copy(stream, response.getOutputStream());
        stream.close();
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                file.delete();

            }
        }.start();
    }

    @GetMapping("/payments/incomeReport/file/{initial_time}/{final_time}/{companyId}/{account}/{paymentMethod}/{houseId}/{category}")
    @Timed
    public void getIncomeReportFile(@PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
                                    @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
                                    @PathVariable(value = "companyId") int companyId,
                                    @PathVariable(value = "houseId") String houseId,
                                    @PathVariable(value = "paymentMethod") String paymentMethod,
                                    @PathVariable(value = "category") String category,
                                    @PathVariable(value = "account") String account,
                                    @ApiParam Pageable pageable, HttpServletResponse response) throws URISyntaxException, IOException {
        File file = paymentService.obtainIncomeReportToPrint(pageable, companyId, initial_time, final_time, houseId, paymentMethod, category, account);
        FileInputStream stream = new FileInputStream(file);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        IOUtils.copy(stream, response.getOutputStream());
        stream.close();
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                file.delete();

            }
        }.start();
    }

    @GetMapping("/payments/sendEmail/{paymentId}")
    @Timed
    public void sendByEmail(@PathVariable Long paymentId) {
        paymentService.sendPaymentEmail(paymentId);
    }


    @GetMapping("/exchange-rate/{fechaInicio}/{fechaFinal}")
    @Timed
    public ExchangeRateBccr obtenerTipoDeCambio(@PathVariable ZonedDateTime fechaInicio, @PathVariable ZonedDateTime fechaFinal) {

        try {
            return this.indicadoresEconomicosBccr.obtenerTipodeCambio(fechaInicio, fechaFinal);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

}

