package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.PaymentCharge;
import com.lighthouse.aditum.repository.PaymentChargeRepository;
import com.lighthouse.aditum.service.dto.CustomChargeTypeDTO;
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

    private final ChargeService chargeService;

    public PaymentChargeService(ChargeService chargeService,PaymentChargeRepository paymentChargeRepository, PaymentChargeMapper paymentChargeMapper) {
        this.paymentChargeRepository = paymentChargeRepository;
        this.paymentChargeMapper = paymentChargeMapper;
        this.chargeService = chargeService;
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
    public List<PaymentChargeDTO> findAllByPayment(List<CustomChargeTypeDTO> customChargeTypes,String currency,Long paymentId) {
        log.debug("Request to get all PaymentCharges");
        List<PaymentChargeDTO> paymentCharge = paymentChargeRepository.findAllByPaymentId(paymentId).stream()
            .map(paymentChargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        paymentCharge.forEach(paymentChargeDTO -> {
            if(paymentChargeDTO.getType()!=null){
                paymentChargeDTO.setCategory(this.chargeService.getCategory(paymentChargeDTO.getType(),customChargeTypes));
            }else{
                paymentChargeDTO.setCategory("Otros");
            }
            if(paymentChargeDTO.getOldStyle()==0){
                paymentChargeDTO.setAmmountFormatted(currency,paymentChargeDTO.getAmmount());
                paymentChargeDTO.setAbonadoFormatted(currency,paymentChargeDTO.getAbonado());
                paymentChargeDTO.setLeftToPayFormatted(currency,paymentChargeDTO.getLeftToPay());
            }else{
                paymentChargeDTO.setAbonadoFormatted(currency,paymentChargeDTO.getAbonado());
            }

//            if (charge.getType() == 6 && charge.getId() != null) {
//                charge.setWaterConsumption(findWCRecursive(charge));
//            }
        });
        return  paymentCharge;
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

    @Transactional(readOnly = true)
    public List<PaymentChargeDTO> findAllByOldCharge(Long id) {
        log.debug("Request to get PaymentCharge : {}", id);
        return paymentChargeRepository.findAllByOriginalCharge(id).stream()
            .map(paymentChargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<PaymentChargeDTO> findAllByConsecutive(String id) {
        log.debug("Request to get PaymentCharge : {}", id);
        return paymentChargeRepository.findAllByConsecutive(id).stream()
            .map(paymentChargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
