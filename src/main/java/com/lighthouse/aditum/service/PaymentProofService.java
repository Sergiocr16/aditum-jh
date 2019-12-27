package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.PaymentProof;
import com.lighthouse.aditum.repository.PaymentProofRepository;
import com.lighthouse.aditum.service.dto.PaymentProofDTO;
import com.lighthouse.aditum.service.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing PaymentProof.
 */
@Service
@Transactional
public class PaymentProofService {

    private final Logger log = LoggerFactory.getLogger(PaymentProofService.class);

    private final PaymentProofRepository paymentProofRepository;

    private final PaymentProofMapper paymentProofMapper;

    private final PaymentMapper paymentMapper;

    private final PresupuestoMapper companyMapper;

    private final HouseService houseService;

    private final paymentProofMailService paymentProofMailService;


    public PaymentProofService(PaymentMapper paymentMapper, PresupuestoMapper companyMapper, paymentProofMailService paymentProofMailService, PaymentProofRepository paymentProofRepository, PaymentProofMapper paymentProofMapper, HouseService houseService) {
        this.paymentProofRepository = paymentProofRepository;
        this.paymentProofMapper = paymentProofMapper;
        this.houseService = houseService;
        this.paymentProofMailService = paymentProofMailService;
        this.paymentMapper = paymentMapper;
        this.companyMapper = companyMapper;
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
        paymentProof.setHouse(paymentMapper.houseFromId(paymentProofDTO.getHouseId()));
        paymentProofDTO.setHouse(houseService.findOne(paymentProofDTO.getHouseId()));
        paymentProof.setCompany(companyMapper.companyFromId(paymentProofDTO.getCompanyId()));
        paymentProof = paymentProofRepository.save(paymentProof);
        if (paymentProofDTO.getId() == null) {
            this.paymentProofMailService.sendEmail(paymentProofDTO);
        }
        return paymentProofMapper.toDto(paymentProof);
    }

    /**
     * Get all the paymentProofs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentProofDTO> findAll(Pageable pageable, Long companyId, int status) {
        log.debug("Request to get all PaymentProofs");
        Page<PaymentProofDTO> paymentProofDTOS = paymentProofRepository.findByCompanyIdAndStatus(pageable, companyId, status)
            .map(paymentProofMapper::toDto);

        for (int i = 0; i < paymentProofDTOS.getContent().size(); i++) {
            paymentProofDTOS.getContent().get(i).setHousenumber(houseService.findOne(paymentProofDTOS.getContent().get(i).getHouseId()).getHousenumber());
        }

        return paymentProofDTOS;
    }

    /**
     * Get all the paymentProofs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentProofDTO> getPaymentProofsByHouse(Pageable pageable, Long houseId, int status) {
        log.debug("Request to get all PaymentProofs");
        return paymentProofRepository.findByHouseIdAndStatus(pageable, houseId, status)
            .map(paymentProofMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PaymentProofDTO> getPaymentProofsByHouseWithoutPayment(Pageable pageable, Long houseId) {
        log.debug("Request to get all PaymentProofs");
        return paymentProofRepository.findByHouseIdAndPaymentId(pageable, houseId, null)
            .map(paymentProofMapper::toDto);
    }
    @Transactional(readOnly = true)
    public List<PaymentProofDTO> getPaymentProofsByPaymentId(Long paymentId) {
        log.debug("Request to get all PaymentProofs");
        return paymentProofRepository.findByPaymentId(null,paymentId)
            .map(paymentProofMapper::toDto).getContent();
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
        PaymentProofDTO paymentProofDTO = paymentProofMapper.toDto(paymentProof);
        paymentProofDTO.setHouse(houseService.findOne(paymentProofDTO.getHouseId()));
        return paymentProofDTO;
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
