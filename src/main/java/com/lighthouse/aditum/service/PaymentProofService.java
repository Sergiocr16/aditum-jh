package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.PaymentProof;
import com.lighthouse.aditum.repository.PaymentProofRepository;
import com.lighthouse.aditum.service.dto.PaymentProofDTO;
import com.lighthouse.aditum.service.mapper.PaymentProofMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing PaymentProof.
 */
@Service
@Transactional
public class PaymentProofService {

    private final Logger log = LoggerFactory.getLogger(PaymentProofService.class);

    private final PaymentProofRepository paymentProofRepository;

    private final PaymentProofMapper paymentProofMapper;

    public PaymentProofService(PaymentProofRepository paymentProofRepository, PaymentProofMapper paymentProofMapper) {
        this.paymentProofRepository = paymentProofRepository;
        this.paymentProofMapper = paymentProofMapper;
    }

    /**
     * Save a paymentProof.
     *
     * @param paymentProofDTO the entity to save
     * @return the persisted entity
     */
    public PaymentProofDTO save(PaymentProofDTO paymentProofDTO) {
        log.debug("Request to save PaymentProof : {}", paymentProofDTO);
        PaymentProof paymentProof = paymentProofMapper.toEntity(paymentProofDTO);
        paymentProof = paymentProofRepository.save(paymentProof);
        return paymentProofMapper.toDto(paymentProof);
    }

    /**
     * Get all the paymentProofs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentProofDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentProofs");
        return paymentProofRepository.findAll(pageable)
            .map(paymentProofMapper::toDto);
    }

    /**
     * Get one paymentProof by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PaymentProofDTO findOne(Long id) {
        log.debug("Request to get PaymentProof : {}", id);
        PaymentProof paymentProof = paymentProofRepository.findOne(id);
        return paymentProofMapper.toDto(paymentProof);
    }

    /**
     * Delete the paymentProof by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentProof : {}", id);
        paymentProofRepository.delete(id);
    }
}
