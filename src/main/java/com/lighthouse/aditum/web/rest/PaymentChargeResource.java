package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.PaymentChargeService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.PaymentChargeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PaymentCharge.
 */
@RestController
@RequestMapping("/api")
public class PaymentChargeResource {

    private final Logger log = LoggerFactory.getLogger(PaymentChargeResource.class);

    private static final String ENTITY_NAME = "paymentCharge";

    private final PaymentChargeService paymentChargeService;

    public PaymentChargeResource(PaymentChargeService paymentChargeService) {
        this.paymentChargeService = paymentChargeService;
    }

    /**
     * POST  /payment-charges : Create a new paymentCharge.
     *
     * @param paymentChargeDTO the paymentChargeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentChargeDTO, or with status 400 (Bad Request) if the paymentCharge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-charges")
    @Timed
    public ResponseEntity<PaymentChargeDTO> createPaymentCharge(@RequestBody PaymentChargeDTO paymentChargeDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentCharge : {}", paymentChargeDTO);
        if (paymentChargeDTO.getId() != null) {
//            throw new BadRequestAlertException("A new paymentCharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentChargeDTO result = paymentChargeService.save(paymentChargeDTO);
        return ResponseEntity.created(new URI("/api/payment-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-charges : Updates an existing paymentCharge.
     *
     * @param paymentChargeDTO the paymentChargeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentChargeDTO,
     * or with status 400 (Bad Request) if the paymentChargeDTO is not valid,
     * or with status 500 (Internal Server Error) if the paymentChargeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-charges")
    @Timed
    public ResponseEntity<PaymentChargeDTO> updatePaymentCharge(@RequestBody PaymentChargeDTO paymentChargeDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentCharge : {}", paymentChargeDTO);
        if (paymentChargeDTO.getId() == null) {
            return createPaymentCharge(paymentChargeDTO);
        }
        PaymentChargeDTO result = paymentChargeService.save(paymentChargeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentChargeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-charges : get all the paymentCharges.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of paymentCharges in body
     */
    @GetMapping("/payment-charges")
    @Timed
    public List<PaymentChargeDTO> getAllPaymentCharges() {
        log.debug("REST request to get all PaymentCharges");
        return paymentChargeService.findAll();
        }

    /**
     * GET  /payment-charges/:id : get the "id" paymentCharge.
     *
     * @param id the id of the paymentChargeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentChargeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payment-charges/{id}")
    @Timed
    public ResponseEntity<PaymentChargeDTO> getPaymentCharge(@PathVariable Long id) {
        log.debug("REST request to get PaymentCharge : {}", id);
        PaymentChargeDTO paymentChargeDTO = paymentChargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentChargeDTO));
    }

    /**
     * DELETE  /payment-charges/:id : delete the "id" paymentCharge.
     *
     * @param id the id of the paymentChargeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-charges/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentCharge(@PathVariable Long id) {
        log.debug("REST request to delete PaymentCharge : {}", id);
        paymentChargeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
