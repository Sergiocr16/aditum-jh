package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.PaymentProofService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.PaymentProofDTO;
import io.github.jhipster.web.util.ResponseUtil;
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
 * REST controller for managing PaymentProof.
 */
@RestController
@RequestMapping("/api")
public class PaymentProofResource {

    private final Logger log = LoggerFactory.getLogger(PaymentProofResource.class);

    private static final String ENTITY_NAME = "paymentProof";

    private final PaymentProofService paymentProofService;

    public PaymentProofResource(PaymentProofService paymentProofService) {
        this.paymentProofService = paymentProofService;
    }

    /**
     * POST  /payment-proofs : Create a new paymentProof.
     *
     * @param paymentProofDTO the paymentProofDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentProofDTO, or with status 400 (Bad Request) if the paymentProof has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-proofs")
    @Timed
    public ResponseEntity<PaymentProofDTO> createPaymentProof(@Valid @RequestBody PaymentProofDTO paymentProofDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentProof : {}", paymentProofDTO);
        if (paymentProofDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new payment proof cannot already have an ID")).body(null);
        }
        PaymentProofDTO result = paymentProofService.save(paymentProofDTO);
        return ResponseEntity.created(new URI("/api/payment-proofs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-proofs : Updates an existing paymentProof.
     *
     * @param paymentProofDTO the paymentProofDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentProofDTO,
     * or with status 400 (Bad Request) if the paymentProofDTO is not valid,
     * or with status 500 (Internal Server Error) if the paymentProofDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-proofs")
    @Timed
    public ResponseEntity<PaymentProofDTO> updatePaymentProof(@Valid @RequestBody PaymentProofDTO paymentProofDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentProof : {}", paymentProofDTO);
        if (paymentProofDTO.getId() == null) {
            return createPaymentProof(paymentProofDTO);
        }
        PaymentProofDTO result = paymentProofService.save(paymentProofDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentProofDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-proofs : get all the paymentProofs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentProofs in body
     */
    @GetMapping("/payment-proofs")
    @Timed
    public ResponseEntity<List<PaymentProofDTO>> getAllPaymentProofs(Pageable pageable,Long companyId,int status) throws URISyntaxException{
        log.debug("REST request to get a page of PaymentProofs");
        Page<PaymentProofDTO> page = paymentProofService.findAll(pageable,companyId,status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-proofs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /payment-proofs : get all the paymentProofs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentProofs in body
     */
    @GetMapping("/payment-proofs/byHouse")
    @Timed
    public ResponseEntity<List<PaymentProofDTO>> getPendingPaymentProofsByHouse(Pageable pageable, Long houseId,int status) throws URISyntaxException{
        log.debug("REST request to get a page of PaymentProofs");
        Page<PaymentProofDTO> page = paymentProofService.getPaymentProofsByHouse(pageable,houseId,status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-proofs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /payment-proofs/:id : get the "id" paymentProof.
     *
     * @param id the id of the paymentProofDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentProofDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payment-proofs/{id}")
    @Timed
    public ResponseEntity<PaymentProofDTO> getPaymentProof(@PathVariable Long id) {
        log.debug("REST request to get PaymentProof : {}", id);
        PaymentProofDTO paymentProofDTO = paymentProofService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentProofDTO));
    }

    /**
     * DELETE  /payment-proofs/:id : delete the "id" paymentProof.
     *
     * @param id the id of the paymentProofDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-proofs/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentProof(@PathVariable Long id) {
        log.debug("REST request to delete PaymentProof : {}", id);
        paymentProofService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
