package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Charge;
import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.repository.ChargeRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.ChargeMapper;
import com.lighthouse.aditum.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.lighthouse.aditum.service.util.RandomUtil.formatDateTime;
import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;
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
    private final UserService userService;
    private final AdminInfoService adminInfoService;
    private final BitacoraAccionesService bitacoraAccionesService;
    private final CompanyService companyService;

    @Autowired
    public ChargeService(CompanyService companyService, AdminInfoService adminInfoService, UserService userService, BitacoraAccionesService bitacoraAccionesService, @Lazy HouseService houseService, ResidentService residentService, @Lazy PaymentDocumentService paymentEmailSenderService, BancoService bancoService, @Lazy PaymentService paymentService, ChargeRepository chargeRepository, ChargeMapper chargeMapper, BalanceService balanceService, AdministrationConfigurationService administrationConfigurationService) {
        this.chargeRepository = chargeRepository;
        this.chargeMapper = chargeMapper;
        this.balanceService = balanceService;
        this.paymentService = paymentService;
        this.bancoService = bancoService;
        this.paymentEmailSenderService = paymentEmailSenderService;
        this.residentService = residentService;
        this.houseService = houseService;
        this.administrationConfigurationService = administrationConfigurationService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.userService = userService;
        this.adminInfoService = adminInfoService;
        this.companyService = companyService;
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
        charge.setPayedSubcharge(chargeDTO.isPayedSubcharge());
        charge.setDate(formatDateTime(charge.getDate()));
        charge = chargeRepository.save(charge);

        return this.formatCharge(chargeMapper.toDto(charge));
    }

    public ChargeDTO pay(ChargeDTO chargeDTO, Payment payment) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        charge.setPayment(chargeMapper.paymentFromId(payment.getId()));
        charge.setCompany(chargeMapper.companyFromId(payment.getCompanyId().longValue()));
        charge.setPaymentDate(payment.getDate());
        charge.setState(2);
        charge.setDate(formatDateTime(charge.getDate()));
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
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(this.houseService.findOne(chargeDTO.getHouseId()).getCompanyId());

        BalanceDTO balanceDTO = this.houseService.findOne(chargeDTO.getHouseId()).getBalance();
        if (Double.parseDouble(balanceDTO.getMaintenance()) > 0 ) {
            chargeDTO = this.createSubchargeInCharge(administrationConfigurationDTO, chargeDTO, false);
            charge = payIfBalanceIsPositive(chargeDTO);
            balanceDTO = this.houseService.findOne(chargeDTO.getHouseId()).getBalance();
        } else {
            if (administrationConfigurationDTO.isHasSubcharges()) {
                chargeDTO = this.createSubchargeInCharge(administrationConfigurationDTO, chargeDTO, false);
            }
            charge = chargeMapper.toEntity(chargeDTO);
            charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
            if (chargeDTO.getPaymentId() != null) {
                charge.setPayment(chargeMapper.paymentFromId(chargeDTO.getPaymentId()));
                charge.setCompany(chargeMapper.companyFromId(chargeDTO.getCompanyId()));
                charge.setPaymentDate(ZonedDateTime.now());
            }
            charge = chargeRepository.save(charge);
        }

        return chargeMapper.toDto(charge);
    }

    public ChargeDTO update(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
//        Charge oldCharge = chargeRepository.getOne(chargeDTO.getId());
//        if(newCharge.getAmmount().equals(oldCharge.getAmmount()) && newCharge.getDeleted()==0 && oldCharge.getType()==newCharge.getType()) {
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(this.houseService.findOne(chargeDTO.getHouseId()).getCompanyId());
        if (administrationConfigurationDTO.isHasSubcharges()) {
            chargeDTO = this.createSubchargeInCharge(administrationConfigurationDTO, chargeDTO, false);
        }
        charge = chargeMapper.toEntity(chargeDTO);
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        charge.setDate(formatDateTime(charge.getDate()));
        Charge savedCharge = chargeRepository.save(charge);

        if (chargeDTO.getDeleted() == 1 || Double.parseDouble(chargeDTO.getAmmount()) !=  Double.parseDouble(chargeDTO.getTemporalAmmount())) {
            LocalDateTime today = LocalDateTime.now();
            ZoneId id = ZoneId.of("America/Costa_Rica");  //Create timezone
            ZonedDateTime zonedDateTime = ZonedDateTime.of(today, id);
            BitacoraAccionesDTO bitacoraAccionesDTO = new BitacoraAccionesDTO();
            if (chargeDTO.getDeleted() == 1) {
                bitacoraAccionesDTO.setConcept("Eliminación de la cuota: " + chargeDTO.getConcept() + " de " + formatMoney(Integer.parseInt(chargeDTO.getAmmount())) + " colones");
            } else if (Integer.parseInt(chargeDTO.getAmmount()) != Integer.parseInt(chargeDTO.getTemporalAmmount())) {
                bitacoraAccionesDTO.setConcept("Modificación de la cuota: " + chargeDTO.getConcept() + " de " + formatMoney(Integer.parseInt(chargeDTO.getTemporalAmmount())) + " colones a " + formatMoney(Integer.parseInt(chargeDTO.getAmmount())) + " colones");
            }

            bitacoraAccionesDTO.setType(6);
            bitacoraAccionesDTO.setEjecutionDate(zonedDateTime);
            bitacoraAccionesDTO.setCategory("Cuotas");
            bitacoraAccionesDTO.setUrlState("");
            bitacoraAccionesDTO.setIdReference(charge.getId());
            bitacoraAccionesDTO.setIdResponsable(adminInfoService.findOneByUserId(userService.getUserWithAuthorities().getId()).getId());
            bitacoraAccionesDTO.setCompanyId(companyService.findOne(houseService.findOne(chargeDTO.getHouseId()).getCompanyId()).getId());
            bitacoraAccionesService.save(bitacoraAccionesDTO);

        }

        return chargeMapper.toDto(savedCharge);
    }


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
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeletedAndState(houseId, 0, 1))
            .map(chargeMapper::toDto);
        return formatCharges(chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndBetweenDate(Long houseId, ZonedDateTime initialTime, ZonedDateTime finalTime) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_finalTime, houseId, 0))
            .map(chargeMapper::toDto);
        return formatCharges(chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndBetweenDateResidentAccount(Long houseId, ZonedDateTime initialTime, ZonedDateTime finalTime, ZonedDateTime todayTime) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ;
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        ZonedDateTime zd_todayTime = todayTime.withHour(23).withMinute(59).withSecond(59);
        Page<ChargeDTO> chargeDTOS;
        if (zd_finalTime.isAfter(zd_todayTime)) {
            chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_todayTime, houseId, 0))
                .map(chargeMapper::toDto);
        } else {
            chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_finalTime, houseId, 0))
                .map(chargeMapper::toDto);
        }
        return formatCharges(chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndUnderDate(Long houseId, ZonedDateTime initialTime) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findAllUnderDateAndHouseId(initialTime, houseId))
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
    public Page<ChargeDTO> findPaidChargesBetweenDates(ZonedDateTime initialTime, ZonedDateTime finalTime, int type, Long companyId) {
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        log.debug("Request to get all Charges");
        return new PageImpl<>(chargeRepository.findPaidChargesBetweenDatesAndCompanyId(zd_initialTime, zd_finalTime, type, 2, companyId))
            .map(chargeMapper::toDto);
    }

    public List<ChargeDTO> findPaidChargesBetweenDatesList(ZonedDateTime initialTime, ZonedDateTime finalTime, int type, Long companyId) {
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        log.debug("Request to get all Charges");
        List<ChargeDTO> chargeDTOS = chargeRepository.findPaidChargesBetweenDatesAndCompanyId(zd_initialTime, zd_finalTime, type, 2, companyId).stream()
            .map(chargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        return this.formatCharges(new PageImpl<>(chargeDTOS)).getContent();
    }

    private Charge payIfBalanceIsPositive(ChargeDTO charge) {
        PaymentDTO payment = paymentService.findPaymentInAdvance(charge.getHouseId());
        charge = this.formatCharge(charge);
        ChargeDTO newCharge = charge;
        ZonedDateTime now = ZonedDateTime.now();
//        BalanceDTO balanceDTO = balanceService.findOneByHouse(newCharge.getHouseId());
        if (payment != null) {
            payment.setAccount(bancoService.findOne(Long.parseLong(payment.getAccount())).getBeneficiario() + ";" + payment.getAccount());
            if (charge.getTotal() <= Double.parseDouble(payment.getAmmountLeft())) {
                payment.setAmmountLeft(Double.parseDouble(payment.getAmmountLeft()) - charge.getTotal() + "");
            } else {
                newCharge.setAmmount(charge.getTotal() - Double.parseDouble(payment.getAmmountLeft()) + "");
                newCharge.setPaymentDate(now.plusMinutes(10));
                newCharge = this.create(newCharge);
                charge.setAmmount(payment.getAmmountLeft());
                payment.setAmmountLeft(0 + "");
                charge.setSplitedCharge(newCharge.getId().intValue());
                newCharge.setSplited(1);
            }
            charge.setPaymentId(payment.getId());
            charge.setPaymentDate(ZonedDateTime.now());
            charge.setState(2);
            charge.setCompanyId(payment.getCompanyId().longValue());
            charge.setPaymentDate(now);
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
        if (newCharge != charge) {
            return this.payIfBalanceIsPositive(newCharge);
        } else {
            return savedCharge;
        }
    }


    public List<ChargeDTO> findBeforeDateAndHouseAndTypeAndState(ZonedDateTime initialDate, Long houseId, int type, int state) {
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findBeforeDateAndHouseAndTypeAndStateAndDeleted(initialDate, houseId, type, state, 0))
            .map(chargeMapper::toDto);
        return this.formatCharges(chargeDTOS).getContent();
    }

    @Async
    public void createSubchargeInCharges(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO houseDTO) {
        List<ChargeDTO> chargesPerHouse = this.findAllByHouse(houseDTO.getId()).getContent();
        this.sendReminderEmail(administrationConfigurationDTO, houseDTO, chargesPerHouse);
        chargesPerHouse.forEach(chargeDTO -> {
            this.createSubchargeInCharge(administrationConfigurationDTO, chargeDTO, true);
        });
    }

    private ChargeDTO createSubchargeInCharge(AdministrationConfigurationDTO administrationConfigurationDTO, ChargeDTO chargeDTO, boolean save) {
        ZonedDateTime now = ZonedDateTime.now();
        if (chargeDTO.getSplited() == null) {
            if (chargeDTO.getSubcharge() == null || chargeDTO.getSubcharge().equals("0") || chargeDTO.getSubcharge().equals("0.0") ) {
                int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
                if (diffBetweenChargeDateAndNow >= administrationConfigurationDTO.getDaysTobeDefaulter()) {
                    if (administrationConfigurationDTO.isUsingSubchargePercentage()) {
                        double subCharge = Double.parseDouble(chargeDTO.getAmmount()) * (administrationConfigurationDTO.getSubchargePercentage() / 100);
                        subCharge = (int) subCharge;
                        chargeDTO.setSubcharge(subCharge + "");
                    } else {
                        chargeDTO.setSubcharge(administrationConfigurationDTO.getSubchargeAmmount() + "");
                    }
                }
                if (save) {
                    this.save(chargeDTO);
                }
            }
        }
        chargeDTO.setSubcharge(chargeDTO.getSubcharge() == null ? "0" : chargeDTO.getSubcharge());
        return chargeDTO;
    }

    private ChargeDTO createSubchargeInChargeAlways(AdministrationConfigurationDTO administrationConfigurationDTO, ChargeDTO chargeDTO) {
        if (administrationConfigurationDTO.isUsingSubchargePercentage()) {
            double subCharge = Double.parseDouble(chargeDTO.getAmmount()) * (administrationConfigurationDTO.getSubchargePercentage() / 100);
            chargeDTO.setSubcharge(subCharge + "");
        } else {
            chargeDTO.setSubcharge(administrationConfigurationDTO.getSubchargeAmmount() + "");
        }
        return chargeDTO;
    }

    @Async
    public void sendReminderEmail(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO houseDTO, List<ChargeDTO> chargesDTO) {
        List<ChargeDTO> chargesWillHasSubcharge = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();
        chargesDTO.forEach(chargeDTO -> {
            int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
            int diffDaysToSendEmail = administrationConfigurationDTO.getDaysTobeDefaulter() - administrationConfigurationDTO.getDaysToSendEmailBeforeBeDefaulter();
            if (diffBetweenChargeDateAndNow == diffDaysToSendEmail) {
                chargeDTO = this.createSubchargeInChargeAlways(administrationConfigurationDTO, chargeDTO);
                chargesWillHasSubcharge.add(formatCharge(chargeDTO));
            }
        });
        if (!chargesWillHasSubcharge.isEmpty()) {
            this.paymentEmailSenderService.sendReminderEmail(administrationConfigurationDTO, houseDTO, chargesWillHasSubcharge,"subchargeReminderEmail");
        }
    }

    @Async
    public void sendReminderEmailAndMorosos(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO houseDTO, List<ChargeDTO> chargesDTO) {
        List<ChargeDTO> chargesWillHasSubcharge = new ArrayList<>();
        List<ChargeDTO> chargesWithSubcharge = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();
        chargesDTO.forEach(chargeDTO -> {
            if (chargeDTO.getSubcharge() == null || chargeDTO.getSubcharge().equals("0") || chargeDTO.getSubcharge().equals("0.0") ) {
                int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
                int diffDaysToSendEmail = administrationConfigurationDTO.getDaysTobeDefaulter() - administrationConfigurationDTO.getDaysToSendEmailBeforeBeDefaulter();
                if (diffBetweenChargeDateAndNow < diffDaysToSendEmail) {
                    ChargeDTO chargeFormatted = chargeDTO;
                    chargeFormatted = formatCharge(this.createSubchargeInChargeAlways(administrationConfigurationDTO, chargeFormatted));
                    chargesWillHasSubcharge.add(chargeFormatted);
                }
            }
        });
        if (!chargesWillHasSubcharge.isEmpty()) {
            this.paymentEmailSenderService.sendReminderEmail(administrationConfigurationDTO, houseDTO, chargesWillHasSubcharge,"subchargeReminderEmail");
        }
        chargesDTO.forEach(chargeDTO -> {
            if (chargeDTO.getSubcharge() != null) {
                int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
                int diffDaysToSendEmail = administrationConfigurationDTO.getDaysTobeDefaulter() - administrationConfigurationDTO.getDaysToSendEmailBeforeBeDefaulter();
//                    chargeDTO = this.createSubchargeInChargeAlways(administrationConfigurationDTO, chargeDTO);
               if(Double.parseDouble(chargeDTO.getSubcharge())>0) {
                   chargesWithSubcharge.add(formatCharge(chargeDTO));
               }
            }
        });
        if (!chargesWithSubcharge.isEmpty()) {
            this.paymentEmailSenderService.sendReminderEmail(administrationConfigurationDTO, houseDTO, chargesWithSubcharge,"defaulterReminderEmail");
        }
    }

    private Page<ChargeDTO> formatCharges(Page<ChargeDTO> charges) {
        charges.forEach(chargeDTO -> {
            if (chargeDTO.getSubcharge() != null) {
                chargeDTO.setTotal(Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(chargeDTO.getSubcharge()));
            } else {
                chargeDTO.setSubcharge("0");
                chargeDTO.setTotal(Double.parseDouble(chargeDTO.getAmmount()));
            }
        });
        return charges;
    }

    private ChargeDTO formatCharge(ChargeDTO chargeDTO) {
        if (chargeDTO.getSubcharge() != null) {
            chargeDTO.setTotal(Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(chargeDTO.getSubcharge()));
        } else {
            chargeDTO.setSubcharge("0");
            chargeDTO.setTotal(Double.parseDouble(chargeDTO.getAmmount()));
        }
        return chargeDTO;
    }

    public ChargesToPayReportDTO findChargesToPay(ZonedDateTime finalDate, int type, Long companyId) {
        ZonedDateTime zd_finalTime = finalDate.withHour(23).withMinute(59).withSecond(59);
        log.debug("Request to get all Charges");
        ChargesToPayReportDTO chargesReport = new ChargesToPayReportDTO();
        List<HouseDTO> houses = this.houseService.findAll(companyId).getContent();
        List<DueHouseDTO> finalHouses = new ArrayList<>();
        houses.forEach(houseDTO -> {
            List<ChargeDTO> chargesPerHouse;
            if (type == 5) {
                chargesPerHouse = new PageImpl<>(chargeRepository.findByHouseBetweenDates(zd_finalTime, 1, 0, houseDTO.getId())).map(chargeMapper::toDto).getContent();
            } else {
                chargesPerHouse = new PageImpl<>(chargeRepository.findByHouseBetweenDatesAndType(zd_finalTime, 1, type, 0, houseDTO.getId())).map(chargeMapper::toDto).getContent();
            }
            if (chargesPerHouse.size() > 0) {
                DueHouseDTO dueHouse = new DueHouseDTO();
                chargesPerHouse.forEach(chargeDTO -> {
                    chargeDTO = formatCharge(chargeDTO);
                    dueHouse.setTotalDue(dueHouse.getTotalDue() + chargeDTO.getTotal());
                });
                dueHouse.setHouseDTO(houseDTO);
                dueHouse.setResponsable(this.residentService.findPrincipalContactByHouse(houseDTO.getId()));
                dueHouse.setDues(chargesPerHouse);
                chargesReport.setTotalDue(chargesReport.getTotalDue() + dueHouse.getTotalDue());
                chargesReport.setTotalDueHouses(chargesReport.getTotalDueHouses() + 1);
                finalHouses.add(dueHouse);
            }
        });
        chargesReport.setDueHouses(finalHouses);
        return chargesReport;
    }

}
