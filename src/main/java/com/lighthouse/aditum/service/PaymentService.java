package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.repository.PaymentRepository;
import com.lighthouse.aditum.service.dto.ChargeDTO;
import com.lighthouse.aditum.service.dto.CreatePaymentDTO;
import com.lighthouse.aditum.service.dto.PaymentDTO;
import com.lighthouse.aditum.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


/**
 * Service Implementation for managing Payment.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final ChargeService chargeService;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, ChargeService chargeService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.chargeService = chargeService;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save
     * @return the persisted entity
     */
    public PaymentDTO save(CreatePaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(createPaymentDTOtoPaymentDTO(paymentDTO));
        payment.setHouse(paymentMapper.houseFromId(paymentDTO.getHouseId()));
        payment = paymentRepository.save(payment);
        for (int i = 0; i < paymentDTO.getCharges().size(); i++) {
            this.payCharge(paymentDTO.getCharges().get(i),payment);
        }
        return paymentMapper.toDto(payment);
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByDatesBetweenAndCompany(Pageable pageable,String initialTime,String finalTime,int companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<Payment> result = paymentRepository.findByDatesBetweenAndCompany(pageable,zd_initialTime,zd_finalTime,companyId);
//        Collections.reverse(result);
        return result.map(payment -> paymentMapper.toDto(payment));
    }
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByDatesBetweenAndCompanyAndAccount(Pageable pageable,String initialTime,String finalTime,int companyId,String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<Payment> result = paymentRepository.findByDatesBetweenAndCompanyAndAccount(pageable,zd_initialTime,zd_finalTime,companyId,accountId);
//        Collections.reverse(result);
        return result.map(payment -> paymentMapper.toDto(payment));
    }
    /**
     *  Get all the payments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable)
            .map(paymentMapper::toDto);
    }

    /**
     *  Get one payment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PaymentDTO findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        Payment payment = paymentRepository.findOne(id);
        return paymentMapper.toDto(payment);
    }

    /**
     *  Delete the  payment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.delete(id);
    }

    private PaymentDTO createPaymentDTOtoPaymentDTO(CreatePaymentDTO cPaymentDTO){
       PaymentDTO paymentDTO = new PaymentDTO();
       paymentDTO.setAccount(cPaymentDTO.getAccount());
       paymentDTO.setAmmount(cPaymentDTO.getAmmount());
       paymentDTO.setComments(cPaymentDTO.getComments());
       paymentDTO.setCompanyId(cPaymentDTO.getCompanyId());
       paymentDTO.setConcept(cPaymentDTO.getComments());
       paymentDTO.setDate(cPaymentDTO.getDate());
       paymentDTO.setHouseId(cPaymentDTO.getHouseId());
       paymentDTO.setPaymentMethod(cPaymentDTO.getPaymentMethod());
       paymentDTO.setReceiptNumber(cPaymentDTO.getReceiptNumber());
       paymentDTO.setTransaction(cPaymentDTO.getTransaction());
       return paymentDTO;
    }

    private void payCharge(ChargeDTO charge,Payment payment){
        if(Integer.parseInt(charge.getLeft())>0){
            ChargeDTO newCharge = charge;
            newCharge.setAmmount(charge.getLeft());
            chargeService.pay(charge,payment);
            chargeService.create(newCharge);
        }else{
            chargeService.pay(charge,payment);
        }
    }
}
