package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.repository.PaymentRepository;
import com.lighthouse.aditum.service.dto.ChargeDTO;
import com.lighthouse.aditum.service.dto.CreatePaymentDTO;
import com.lighthouse.aditum.service.dto.PaymentDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


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

    private final BancoService bancoService;

    private final PaymentDocumentService paymentEmailSenderService;

    private final ResidentService residentService;

    public PaymentService(ResidentService residentService, PaymentDocumentService paymentEmailSenderService, PaymentRepository paymentRepository, PaymentMapper paymentMapper, ChargeService chargeService, BancoService bancoService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.chargeService = chargeService;
        this.bancoService = bancoService;
        this.paymentEmailSenderService = paymentEmailSenderService;
        this.residentService = residentService;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save
     * @return the persisted entity
     */

    public PaymentDTO update(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment.setHouse(paymentMapper.houseFromId(paymentDTO.getHouseId()));
        payment.setAccount(paymentDTO.getAccount().split(";")[1]);
        payment.setAmmountLeft(paymentDTO.getAmmountLeft());
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }
    public PaymentDTO save(CreatePaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(createPaymentDTOtoPaymentDTO(paymentDTO));
        if(payment.getTransaction().equals("2")){
            payment.setAmmountLeft(paymentDTO.getAmmount());
        }
        payment.setHouse(paymentMapper.houseFromId(paymentDTO.getHouseId()));
        payment.setAccount(paymentDTO.getAccount().split(";")[1]);
        payment = paymentRepository.save(payment);
        bancoService.increaseSaldo(Long.valueOf(paymentDTO.getAccount().split(";")[1]).longValue(),paymentDTO.getAmmount());
        List<ChargeDTO> paymentCharges = this.filterCharges(paymentDTO);
        for (int i = 0; i < paymentCharges.size(); i++) {
            this.payCharge(paymentCharges.get(i),payment);
        }
        PaymentDTO paymentDTo = paymentMapper.toDto(payment);
        paymentDTo.setCharges(paymentCharges);
        paymentDTo.setEmailTo(paymentDTO.getEmailTo());
        if(paymentDTo.getEmailTo().size()>0) {
            this.paymentEmailSenderService.sendPaymentEmail(paymentDTo, false);
        }
        return paymentMapper.toDto(payment);
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByDatesBetweenAndCompany(Pageable pageable,String initialTime,String finalTime,int companyId) {
        log.debug("Request to get all Payments in last month by house");
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
    @Transactional(readOnly = true)
    public List<PaymentDTO> findByDatesBetweenAndCompanyAndAccount(String initialTime,String finalTime,int companyId,String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return paymentRepository.findByDatesBetweenAndCompanyAndAccount(zd_initialTime,zd_finalTime,companyId,accountId).stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    @Transactional(readOnly = true)
    public List<PaymentDTO> findAdelantosByDatesBetweenAndCompany(String initialTime,String finalTime,int companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return paymentRepository.findAdelantosByDatesBetweenAndCompany(zd_initialTime,zd_finalTime,companyId,"2").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByHouseFilteredByDate(Pageable pageable,Long houseId,String initialTime,String finalTime) {
        log.debug("Request to get all Payments");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<Payment> payments = paymentRepository.findByDatesBetweenAndHouseId(pageable,zd_initialTime,zd_finalTime,houseId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }
        return paymentsDTO;
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByHouseUnderDate(Pageable pageable,Long houseId,String initialTime) {
        log.debug("Request to get all Payments");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        Page<Payment> payments = paymentRepository.findUnderDateAndHouseId(pageable,zd_initialTime,houseId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }
        return paymentsDTO;
    }
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByHouse(Pageable pageable,Long houseId) {
        log.debug("Request to get all Payments");
        Page<Payment> payments = paymentRepository.findByHouseId(pageable,houseId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }

        return paymentsDTO;
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

    public PaymentDTO createPaymentDTOtoPaymentDTO(CreatePaymentDTO cPaymentDTO){
       PaymentDTO paymentDTO = new PaymentDTO();
       paymentDTO.setAccount(cPaymentDTO.getAccount());
       paymentDTO.setAmmount(cPaymentDTO.getAmmount());
       paymentDTO.setComments(cPaymentDTO.getComments());
       paymentDTO.setCompanyId(cPaymentDTO.getCompanyId());
       paymentDTO.setConcept(cPaymentDTO.getConcept());
       paymentDTO.setDate(cPaymentDTO.getDate());
       paymentDTO.setHouseId(cPaymentDTO.getHouseId());
       paymentDTO.setPaymentMethod(cPaymentDTO.getPaymentMethod());
       paymentDTO.setReceiptNumber(cPaymentDTO.getReceiptNumber());
       paymentDTO.setTransaction(cPaymentDTO.getTransaction());
       return paymentDTO;
    }

    private List<ChargeDTO> filterCharges(CreatePaymentDTO payment){
        List<ChargeDTO> listaCargos = payment.getCharges();
        List<ChargeDTO> cargosFiltrados = new ArrayList<>();
        for (int i = 0; i < listaCargos.size(); i++) {
            if(Integer.parseInt(listaCargos.get(i).getPaymentAmmount())!=0){
                cargosFiltrados.add(listaCargos.get(i));
            }
        }
        return cargosFiltrados;
    }
    public PaymentDTO findPaymentInAdvance(Long houseId){
        List<Payment> payments = paymentRepository.findPaymentsInAdvance(null,"2","0",houseId).getContent();
        Payment paymentToUse = null;
        if(payments.size()>0) {
            paymentToUse = payments.get(0);
            for (int i = 0; i < payments.size(); i++) {
                Payment payment = payments.get(i);
                if (payment.getDate().isBefore(paymentToUse.getDate())){
                    paymentToUse = payment;
                }
            }
        }

        if(paymentToUse!=null) {
            PaymentDTO paymentDTO = paymentMapper.toDto(paymentToUse);
            paymentDTO.setAmmountLeft(paymentToUse.getAmmountLeft());
            return paymentDTO;
        }else {
            return null;
        }
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


    public File obtainFileToPrint(Long paymentId){
        PaymentDTO paymentDTO = this.findOne(paymentId);
        paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
        paymentDTO.getCharges().forEach(chargeDTO -> {
            chargeDTO.setPaymentAmmount(chargeDTO.getAmmount());
        });
        if(paymentDTO.getCharges().size()==0){
            paymentDTO.setCharges(new ArrayList<>());
        }
        paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
        paymentDTO.setAmmountLeft("0");
        Page<ResidentDTO> residents = residentService.findEnabledByHouseId(null,paymentDTO.getHouseId());
        List<ResidentDTO> emailTo = new ArrayList<>();
        for (int i = 0; i < residents.getContent().size(); i++) {
            if (residents.getContent().get(i).getPrincipalContact()==1){
                emailTo.add(residents.getContent().get(i));
            }
        }

        paymentDTO.setEmailTo(emailTo);

        return paymentEmailSenderService.obtainFileToPrint(paymentDTO,false);
    }
    @Async
    public void sendPaymentEmail(Long paymentId){
        PaymentDTO paymentDTO = this.findOne(paymentId);
        paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
        paymentDTO.getCharges().forEach(chargeDTO -> {
            chargeDTO.setPaymentAmmount(chargeDTO.getAmmount());
        });
        if(paymentDTO.getCharges().size()==0){
            paymentDTO.setCharges(new ArrayList<>());
        }
        paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
        paymentDTO.setAmmountLeft("0");
        Page<ResidentDTO> residents = residentService.findEnabledByHouseId(null,paymentDTO.getHouseId());
        List<ResidentDTO> emailTo = new ArrayList<>();
        for (int i = 0; i < residents.getContent().size(); i++) {
            if (residents.getContent().get(i).getPrincipalContact()==1){
                emailTo.add(residents.getContent().get(i));
            }
        }

        paymentDTO.setEmailTo(emailTo);
        paymentEmailSenderService.sendPaymentEmail(paymentDTO,false);
    }


}
