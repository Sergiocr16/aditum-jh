package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Charge;
import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.repository.ChargeRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.ChargeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;


/**
 * Service Implementation for managing Charge.
 */
@Service
@Transactional
public class ChargeService {

    private final Logger log = LoggerFactory.getLogger(ChargeService.class);

    private final ChargeRepository chargeRepository;
    private final BalanceService balanceService;
    private final ChargeMapper chargeMapper;
    private final PaymentService paymentService;
    private final BancoService bancoService;
    private final PaymentDocumentService paymentEmailSenderService;
    private final ResidentService residentService;
    private final HouseService houseService;
    private final AdministrationConfigurationService administrationConfigurationService;


    @Autowired
    public ChargeService(@Lazy HouseService houseService, ResidentService residentService, @Lazy PaymentDocumentService paymentEmailSenderService, BancoService bancoService, @Lazy PaymentService paymentService, ChargeRepository chargeRepository, ChargeMapper chargeMapper, BalanceService balanceService, AdministrationConfigurationService administrationConfigurationService) {
        this.chargeRepository = chargeRepository;
        this.chargeMapper = chargeMapper;
        this.balanceService = balanceService;
        this.paymentService = paymentService;
        this.bancoService = bancoService;
        this.paymentEmailSenderService = paymentEmailSenderService;
        this.residentService = residentService;
        this.houseService = houseService;
        this.administrationConfigurationService = administrationConfigurationService;
    }

    /**
     * Save a charge.
     *
     * @param chargeDTO the entity to save
     * @return the persisted entity
     */
    public ChargeDTO create(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        charge.setCompany(chargeMapper.companyFromId(chargeDTO.getCompanyId()));
        charge.setId(null);
        charge.setState(1);
        charge = chargeRepository.save(charge);
        return chargeMapper.toDto(charge);
    }

    public ChargeDTO pay(ChargeDTO chargeDTO, Payment payment) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        charge.setPayment(chargeMapper.paymentFromId(payment.getId()));
        charge.setCompany(chargeMapper.companyFromId(payment.getCompanyId().longValue()));
        charge.setPaymentDate(payment.getDate());
        charge.setState(2);
        charge = chargeRepository.save(charge);
//        BalanceDTO balanceDTO = balanceService.findOneByHouse(chargeDTO.getHouseId());
//        switch (chargeDTO.getType()) {
//            case 1:
//                int newMaintBalance = 0;
//                newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(chargeDTO.getPaymentAmmount());
//                balanceDTO.setMaintenance(newMaintBalance + "");
//                break;
//            case 2:
//                int newExtraBalance = 0;
//                newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + Integer.parseInt(chargeDTO.getPaymentAmmount());
//                balanceDTO.setExtraordinary(newExtraBalance + "");
//                break;
//            case 3:
//                int newCommonBalance = 0;
//                newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + Integer.parseInt(chargeDTO.getPaymentAmmount());
//                balanceDTO.setCommonAreas(newCommonBalance + "");
//                break;
//        }
//        balanceService.save(balanceDTO);
        return chargeMapper.toDto(charge);
    }


    public ChargeDTO save(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = null;
        BalanceDTO balanceDTO = this.houseService.findOne(chargeDTO.getHouseId()).getBalance();
        if (Double.parseDouble(balanceDTO.getMaintenance()) > 0 && chargeDTO.getType() == 1) {
            charge = payIfBalanceIsPositive(chargeDTO);
            balanceDTO = this.houseService.findOne(chargeDTO.getHouseId()).getBalance();
        } else {
            charge = chargeMapper.toEntity(chargeDTO);
            charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
            if (chargeDTO.getPaymentId() != null) {
                charge.setPayment(chargeMapper.paymentFromId(chargeDTO.getPaymentId()));
                charge.setCompany(chargeMapper.companyFromId(chargeDTO.getCompanyId()));
                charge.setPaymentDate(ZonedDateTime.now());
            }

            charge = chargeRepository.save(charge);
//
//            switch (chargeDTO.getType()) {
//                case 1:
//                    int newMaintBalance = 0;
//                    if (chargeDTO.getDeleted() == 1) {
//                        if (Integer.parseInt(balanceDTO.getMaintenance()) >= 0) {
//                            newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(chargeDTO.getAmmount());
//                        } else {
//                            newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(chargeDTO.getAmmount());
//
//                        }
//                    } else {
//                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(chargeDTO.getAmmount());
//                    }
//                    balanceDTO.setMaintenance(newMaintBalance + "");
//                    break;
//                case 2:
//                    int newExtraBalance = 0;
//                    if (chargeDTO.getDeleted() == 1) {
//                        if (Integer.parseInt(balanceDTO.getExtraordinary()) >= 0) {
//                            newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) - Integer.parseInt(chargeDTO.getAmmount());
//                        } else {
//                            newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + Integer.parseInt(chargeDTO.getAmmount());
//                        }
//                    } else {
//                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) - Integer.parseInt(chargeDTO.getAmmount());
//                    }
//                    balanceDTO.setExtraordinary(newExtraBalance + "");
//                    break;
//                case 3:
//                    int newCommonBalance = 0;
//                    if (chargeDTO.getDeleted() == 1) {
//                        if (Integer.parseInt(balanceDTO.getCommonAreas()) >= 0) {
//                            newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) - Integer.parseInt(chargeDTO.getAmmount());
//                        } else {
//                            newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + Integer.parseInt(chargeDTO.getAmmount());
//
//                        }
//                    } else {
//                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) - Integer.parseInt(chargeDTO.getAmmount());
//                    }
//                    balanceDTO.setCommonAreas(newCommonBalance + "");
//                    break;
//            }
//            balanceService.save(balanceDTO);
        }

        return chargeMapper.toDto(charge);
    }

    public ChargeDTO update(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
//        Charge oldCharge = chargeRepository.getOne(chargeDTO.getId());
//        if(newCharge.getAmmount().equals(oldCharge.getAmmount()) && newCharge.getDeleted()==0 && oldCharge.getType()==newCharge.getType()) {
        Charge savedCharge = chargeRepository.save(charge);
//        }
//        }else {
//            int newAmmount = Integer.parseInt(newCharge.getAmmount());
//            int oldAmmount = Integer.parseInt(oldCharge.getAmmount());
//            int ammountModyfyingBalance = 0;
//            if(oldCharge.getType()!=newCharge.getType()){
//                ammountModyfyingBalance =  -newAmmount;
//            }else {
//                ammountModyfyingBalance = oldAmmount - newAmmount;
//            }
//            BalanceDTO balanceDTO = balanceService.findOneByHouse(chargeDTO.getHouseId());
//
//            switch (chargeDTO.getType()) {
//                case 1:
//                    balanceDTO = modifyIfTypeChanged(oldCharge,newCharge);
//                    int newMaintBalance = 0;
//                    if (chargeDTO.getDeleted() == 1) {
//                        if (Integer.parseInt(balanceDTO.getMaintenance()) >= 0) {
//                            newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(chargeDTO.getAmmount());
//                        } else {
//                            newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(chargeDTO.getAmmount());
//                        }
//                    } else {
//                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + ammountModyfyingBalance;
//
//                    }
//
//                    balanceDTO.setMaintenance(newMaintBalance + "");
//                    break;
//                case 2:
//                    int newExtraBalance = 0;
//                    balanceDTO = modifyIfTypeChanged(oldCharge,newCharge);
//
//                    if (chargeDTO.getDeleted() == 1) {
//                        if (Integer.parseInt(balanceDTO.getExtraordinary()) >= 0) {
//                            newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) - Integer.parseInt(chargeDTO.getAmmount());
//                        } else {
//                            newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + Integer.parseInt(chargeDTO.getAmmount());
//
//                        }
//                    } else {
//                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + ammountModyfyingBalance;
//                    }
//                    balanceDTO.setExtraordinary(newExtraBalance+ "");
//                    break;
//                case 3:
//                    int newCommonBalance = 0;
//                    balanceDTO = modifyIfTypeChanged(oldCharge,newCharge);
//
//                    if (chargeDTO.getDeleted() == 1) {
//                        if (Integer.parseInt(balanceDTO.getCommonAreas()) >= 0) {
//                            newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) - Integer.parseInt(chargeDTO.getAmmount());
//                        } else {
//                            newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + Integer.parseInt(chargeDTO.getAmmount());
//
//                        }
//                    } else {
//                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + ammountModyfyingBalance;
//                    }
//                    balanceDTO.setCommonAreas(newCommonBalance+ "");
//                    break;
//            }
//            chargeRepository.save(newCharge);
//            balanceService.save(balanceDTO);
//        }
        return chargeMapper.toDto(savedCharge);
    }

//    private BalanceDTO modifyIfTypeChanged(Charge oldCharge, Charge newCharge){
//        BalanceDTO balanceDTO = balanceService.findOneByHouse(oldCharge.getHouse().getId());
//        if(oldCharge.getType()!=newCharge.getType()){
//            switch (oldCharge.getType()) {
//                case 1:
//                    int newMaintBalance= 0;
//                    if (Integer.parseInt(balanceDTO.getMaintenance()) >= 0) {
//                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(oldCharge.getAmmount());
//                    } else {
//                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(oldCharge.getAmmount());
//                    }
//                    balanceDTO.setMaintenance(newMaintBalance + "");
//                    break;
//                case 2:
//                    int newExtraBalance = 0;
//                    if (Integer.parseInt(balanceDTO.getExtraordinary()) >= 0) {
//                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) - Integer.parseInt(oldCharge.getAmmount());
//                    } else {
//                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + Integer.parseInt(oldCharge.getAmmount());
//                    }
//                    balanceDTO.setExtraordinary(newExtraBalance+ "");
//                    break;
//                case 3:
//                    int newCommonBalance = 0;
//                    if (Integer.parseInt(balanceDTO.getCommonAreas()) >= 0) {
//                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) - Integer.parseInt(oldCharge.getAmmount());
//                    } else {
//                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + Integer.parseInt(oldCharge.getAmmount());
//                    }
//                    balanceDTO.setCommonAreas(newCommonBalance+ "");
//                    break;
//            }
//        }
//        return balanceDTO;
//    }

    /**
     * Get all the charges.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Charges");
        return chargeRepository.findAll(pageable)
            .map(chargeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouse(Long houseId) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO>  chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeletedAndState(houseId, 0, 1))
            .map(chargeMapper::toDto);
        return formatCharges(chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndBetweenDate(Long houseId, String initialTime, String finalTime) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        List<Charge> a = chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_finalTime, houseId);

        Page<ChargeDTO>  chargeDTOS =  new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_finalTime, houseId))
            .map(chargeMapper::toDto);
        return formatCharges(chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndBetweenDateResidentAccount(Long houseId, String initialTime, String finalTime, String todayTime) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_todayTime = ZonedDateTime.parse((todayTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        Page<ChargeDTO>  chargeDTOS;
        if (zd_finalTime.isAfter(zd_todayTime)) {
            chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_todayTime, houseId))
                .map(chargeMapper::toDto);
        } else {
            chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_finalTime, houseId))
                .map(chargeMapper::toDto);
        }
        return formatCharges(chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndUnderDate(Long houseId, String initialTime) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        Page<ChargeDTO>  chargeDTOS = new PageImpl<>(chargeRepository.findAllUnderDateAndHouseId(zd_initialTime, houseId))
            .map(chargeMapper::toDto);
        return formatCharges(chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByPayment(Long paymentId) {
        log.debug("Request to get all Charges");
        Page<Charge> charges = new PageImpl<>(chargeRepository.findByPaymentIdAndDeletedAndState(paymentId, 0, 2));
        Page<ChargeDTO> chargesDTO = charges.map(chargeMapper::toDto);
        for (int i = 0; i < chargesDTO.getContent().size(); i++) {
            ChargeDTO charge = chargesDTO.getContent().get(i);
            charge.setPaymentDate(charges.getContent().get(i).getPaymentDate());
        }
        return formatCharges(chargesDTO);
    }


    /**
     * Get one charge by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ChargeDTO findOne(Long id) {
        log.debug("Request to get Charge : {}", id);
        Charge charge = chargeRepository.findOne(id);
        return chargeMapper.toDto(charge);
    }

    /**
     * Delete the  charge by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Charge : {}", id);
        chargeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findPaidChargesBetweenDates(String initialTime, String finalTime, int type, Long companyId) {
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        log.debug("Request to get all Charges");
        return new PageImpl<>(chargeRepository.findPaidChargesBetweenDatesAndCompanyId(zd_initialTime, zd_finalTime, type, 2, companyId))
            .map(chargeMapper::toDto);
    }

    public List<ChargeDTO> findPaidChargesBetweenDatesList(String initialTime, String finalTime, int type, Long companyId) {
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        log.debug("Request to get all Charges");
        List<ChargeDTO> chargeDTOS = chargeRepository.findPaidChargesBetweenDatesAndCompanyId(zd_initialTime, zd_finalTime, type, 2, companyId).stream()
            .map(chargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        return this.formatCharges(new PageImpl<>(chargeDTOS)).getContent();
    }

    private Charge payIfBalanceIsPositive(ChargeDTO charge) {
        PaymentDTO payment = paymentService.findPaymentInAdvance(charge.getHouseId());

        ChargeDTO newCharge = charge;
//        BalanceDTO balanceDTO = balanceService.findOneByHouse(newCharge.getHouseId());
        if (payment != null) {
            payment.setAccount(bancoService.findOne(Long.parseLong(payment.getAccount())).getBeneficiario() + ";" + payment.getAccount());
            if (Integer.parseInt(charge.getAmmount()) <= Integer.parseInt(payment.getAmmountLeft())) {
                payment.setAmmountLeft(Integer.parseInt(payment.getAmmountLeft()) - Integer.parseInt(charge.getAmmount()) + "");
            } else {
                newCharge.setAmmount(Integer.parseInt(charge.getAmmount()) - Integer.parseInt(payment.getAmmountLeft()) + "");
                newCharge = this.create(newCharge);
//                int newMaintBalance = 0;
//                if (Integer.parseInt(balanceDTO.getMaintenance()) >= 0) {
//                    newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(newCharge.getAmmount());
//                } else {
//                    newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(newCharge.getAmmount());
//                }
//                balanceDTO.setMaintenance(newMaintBalance+"");
//                balanceDTO = balanceService.save(balanceDTO);
                charge.setAmmount(payment.getAmmountLeft());
                payment.setAmmountLeft(0 + "");
            }
            charge.setPaymentId(payment.getId());
            charge.setPaymentDate(ZonedDateTime.now());
            charge.setState(2);
            charge.setCompanyId(payment.getCompanyId().longValue());
            charge.setPaymentDate(ZonedDateTime.now());
            payment = paymentService.update(payment);
        }
        Charge chargeEntity = chargeMapper.toEntity(charge);
        chargeEntity.setHouse(chargeMapper.houseFromId(charge.getHouseId()));
        if (charge.getPaymentId() != null) {
            chargeEntity.setPayment(chargeMapper.paymentFromId(charge.getPaymentId()));
            chargeEntity.setCompany(chargeMapper.companyFromId(charge.getCompanyId()));
            chargeEntity.setPaymentDate(ZonedDateTime.now());
        }

        Charge savedCharge = chargeRepository.save(chargeEntity);
//        int newMaintBalance = 0;
//        if (Integer.parseInt(balanceDTO.getMaintenance()) >= 0) {
//            newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(savedCharge.getAmmount());
//        } else {
//            newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(savedCharge.getAmmount());
//        }
//        balanceDTO.setMaintenance(newMaintBalance+"");
        ChargeDTO savedChargeDTO = this.chargeMapper.toDto(savedCharge);
        savedChargeDTO.setPaymentAmmount(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(savedChargeDTO.getDate()));
        if (payment != null) {
            payment.setCharges(new ArrayList<>());
            payment.getCharges().add(savedChargeDTO);
            Page<ResidentDTO> residents = residentService.findEnabledByHouseId(null, payment.getHouseId());
            List<ResidentDTO> emailTo = new ArrayList<>();
            for (int i = 0; i < residents.getContent().size(); i++) {
                if (residents.getContent().get(i).getPrincipalContact() == 1) {
                    emailTo.add(residents.getContent().get(i));
                }
            }
            if (emailTo.size() > 0) {
                payment.setEmailTo(emailTo);
                this.paymentEmailSenderService.sendPaymentEmail(payment, true);
            }
        }
//        balanceService.save(balanceDTO);
        if (newCharge != charge) {
            return this.payIfBalanceIsPositive(newCharge);
        } else {
            return savedCharge;
        }

    }


    public List<ChargeDTO> findBeforeDateAndHouseAndTypeAndState(ZonedDateTime initialDate, Long houseId, int type, int state) {
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findBeforeDateAndHouseAndTypeAndState(initialDate, houseId, type, state))
            .map(chargeMapper::toDto);
        return this.formatCharges(chargeDTOS).getContent();
    }


    public void createSubchargeInCharges(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO houseDTO) {
        List<ChargeDTO> chargesPerHouse = this.findAllByHouse(houseDTO.getId()).getContent();
        ZonedDateTime now = ZonedDateTime.now();
        chargesPerHouse.forEach(chargeDTO -> {
            if (chargeDTO.getSubcharge() == null || chargeDTO.getSubcharge().equals("0")) {
                int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
                if (diffBetweenChargeDateAndNow >= administrationConfigurationDTO.getDaysTobeDefaulter()) {
                    if (administrationConfigurationDTO.isUsingSubchargePercentage()) {
                        double subCharge = Double.parseDouble(chargeDTO.getAmmount()) * (administrationConfigurationDTO.getSubchargePercentage() / 100);
                        chargeDTO.setSubcharge(subCharge + "");
                    } else {
                        chargeDTO.setSubcharge(administrationConfigurationDTO.getSubchargeAmmount() + "");
                    }
                }
                this.save(chargeDTO);
            }

        });
    }

    private Page<ChargeDTO> formatCharges(Page<ChargeDTO> charges){
        charges.forEach(chargeDTO -> {
            if(chargeDTO.getSubcharge()!=null){
                chargeDTO.setTotal(Double.parseDouble(chargeDTO.getAmmount())+Double.parseDouble(chargeDTO.getSubcharge()));
            }else{
                chargeDTO.setSubcharge("0");
                chargeDTO.setTotal(Double.parseDouble(chargeDTO.getAmmount()));
            }
        });
        return charges;
    }
}
