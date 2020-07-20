package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.PaymentCharge;
import com.lighthouse.aditum.repository.PaymentChargeRepository;
import com.lighthouse.aditum.service.dto.PaymentChargeDTO;
import com.lighthouse.aditum.service.mapper.PaymentChargeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PaymentCharge.
 */
@Service
@Transactional
public class PaymentChargeService {

    private final Logger log = LoggerFactory.getLogger(PaymentChargeService.class);

    private final PaymentChargeRepository paymentChargeRepository;

    private final PaymentChargeMapper paymentChargeMapper;

    public PaymentChargeService(PaymentChargeRepository paymentChargeRepository, PaymentChargeMapper paymentChargeMapper) {
        this.paymentChargeRepository = paymentChargeRepository;
        this.paymentChargeMapper = paymentChargeMapper;
    }

    /**
     * Save a paymentCharge.
     *
     * @param paymentChargeDTO the entity to save
     * @return the persisted entity
     */
    public PaymentChargeDTO save(PaymentChargeDTO paymentChargeDTO) {
        log.debug("Request to save PaymentCharge : {}", paymentChargeDTO);
        PaymentCharge paymentCharge = paymentChargeMapper.toEntity(paymentChargeDTO);
        paymentCharge = paymentChargeRepository.save(paymentCharge);
        return paymentChargeMapper.toDto(paymentCharge);
    }

    /**
     * Get all the paymentCharges.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PaymentChargeDTO> findAll() {
        log.debug("Request to get all PaymentCharges");
        return paymentChargeRepository.findAll().stream()
            .map(paymentChargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Transactional(readOnly = true)
    public List<PaymentChargeDTO> findAllByPayment(Long paymentId) {
        log.debug("Request to get all PaymentCharges");
        return paymentChargeRepository.findAllByPaymentId(paymentId).stream()
            .map(paymentChargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    /**
     * Get one paymentCharge by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PaymentChargeDTO findOne(Long id) {
        log.debug("Request to get PaymentCharge : {}", id);
        PaymentCharge paymentCharge = paymentChargeRepository.findOne(id);
        return paymentChargeMapper.toDto(paymentCharge);
    }

    /**
     * Delete the paymentCharge by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentCharge : {}", id);
        paymentChargeRepository.delete(id);
    }
}
