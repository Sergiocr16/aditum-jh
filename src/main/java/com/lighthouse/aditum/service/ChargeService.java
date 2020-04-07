package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Charge;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.repository.ChargeRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.ChargeMapper;
import com.lighthouse.aditum.service.util.RandomUtil;
import com.lowagie.text.DocumentException;
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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
    private final ChargesToPayDocumentService chargesToPayDocumentService;

    private final PaymentDocumentService paymentEmailSenderService;
    private final ResidentService residentService;
    private final HouseService houseService;
    private final AdministrationConfigurationService administrationConfigurationService;
    private final UserService userService;
    private final AdminInfoService adminInfoService;
    private final BitacoraAccionesService bitacoraAccionesService;
    private final CompanyService companyService;
    private final CompanyConfigurationService companyConfigurationService;
    private final WaterConsumptionService waterConsumptionService;
    private final PushNotificationService pNotification;


    @Autowired
    public ChargeService(@Lazy ChargesToPayDocumentService chargesToPayDocumentService, PushNotificationService pNotification, @Lazy WaterConsumptionService waterConsumptionService, CompanyConfigurationService companyConfigurationService, CompanyService companyService, AdminInfoService adminInfoService, UserService userService, BitacoraAccionesService bitacoraAccionesService, @Lazy HouseService houseService, ResidentService residentService, @Lazy PaymentDocumentService paymentEmailSenderService, BancoService bancoService, @Lazy PaymentService paymentService, ChargeRepository chargeRepository, ChargeMapper chargeMapper, BalanceService balanceService, AdministrationConfigurationService administrationConfigurationService) {
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
        this.companyConfigurationService = companyConfigurationService;
        this.waterConsumptionService = waterConsumptionService;
        this.pNotification = pNotification;
        this.chargesToPayDocumentService = chargesToPayDocumentService;
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
        HouseDTO house = this.houseService.findOne(chargeDTO.getHouseId());
        charge.setCompany(this.chargeMapper.companyFromId(chargeDTO.getCompanyId()));
        if (chargeDTO.getConsecutive() != null) {
            charge.setConsecutive(chargeDTO.getConsecutive());
        } else {
            charge.setConsecutive(this.obtainConsecutive(chargeDTO.getCompanyId()));
        }
        charge = chargeRepository.save(charge);
        String currency = companyConfigurationService.getByCompanyId(null, house.getCompanyId()).getContent().get(0).getCurrency();
        return this.formatCharge(currency, chargeMapper.toDto(charge));
    }


    public int obtainConsecutive(Long companyId) {
        Charge charge = this.chargeRepository.findFirstByCompanyIdAndSplitedIsNullOrderByConsecutiveDesc(companyId);
        if (charge != null) {
            if (charge.getConsecutive() == null) {
                return 1;
            }
            return charge.getConsecutive() + 1;
        } else {
            return 1;
        }
    }


    public ChargeDTO createWaterCharge(CompanyConfigurationDTO companyConfigDTO, WaterConsumptionDTO wC, ZonedDateTime date, AdministrationConfigurationDTO administrationConfigurationDTO, Boolean sendEmail, Boolean autocalculated, String concept) throws URISyntaxException, IOException, DocumentException {
        HouseDTO house = this.houseService.findOne(wC.getHouseId());
        AdministrationConfigurationDTO adminConfig = this.administrationConfigurationService.findOneByCompanyId(house.getCompanyId());
        ChargeDTO wcCharge = new ChargeDTO();
        String ammount = "";
        if (autocalculated) {
            ammount = Double.parseDouble(wC.getConsumption()) * Double.parseDouble(adminConfig.getWaterPrice()) + "";
        } else {
            ammount = wC.getMonth();
        }
        wcCharge.setAmmount(ammount);
        wcCharge.setCompanyId(house.getCompanyId());
        wcCharge.setDate(date);
        wcCharge.setType(6);
        wcCharge.setHouseId(house.getId());
        wcCharge.setDeleted(0);
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("MMMM", locale);
        String monthName = spanish.format(date);
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
        wcCharge.setConcept(concept);
        wcCharge.setConsecutive(this.obtainConsecutive(companyConfigDTO.getCompanyId()));
        ChargeDTO charge = this.create(wcCharge);
        wC.setStatus(1);
        wC.setMonth(ammount);
        wC.setChargeId(charge.getId());
        this.waterConsumptionService.save(wC);
        ZonedDateTime lastHourToday = ZonedDateTime.now().withHour(23).withMinute(59).withSecond(59);
        if (charge.getDate().isBefore(lastHourToday) && sendEmail) {
            this.paymentEmailSenderService.sendChargeEmail(administrationConfigurationDTO, house, charge);
        }
        this.pNotification.sendNotificationsToOwnersByHouse(wcCharge.getHouseId(),
            this.pNotification.createPushNotification(wcCharge.getConcept() + " - " + house.getHousenumber(),
                "Se ha creado una nueva cuota de agua en su filial por un monto de " + formatMoney(companyConfigDTO.getCurrency(), Double.parseDouble(wcCharge.getAmmount())) + "."));
        return charge;
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

    public ChargeDTO saveFormat(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = null;
        charge = chargeMapper.toEntity(chargeDTO);
        charge.setConsecutive(this.obtainConsecutive(chargeDTO.getCompanyId()));
        charge.setPayment(chargeMapper.paymentFromId(chargeDTO.getPaymentId()));
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        charge.setCompany(this.chargeMapper.companyFromId(chargeDTO.getCompanyId()));
        charge = chargeRepository.save(charge);
        return chargeMapper.toDto(charge);
    }

    public ChargeDTO saveFormatSplitted(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = null;
        charge = chargeMapper.toEntity(chargeDTO);
        charge.setConsecutive(this.chargeRepository.findBySplitedCharge(charge.getId().intValue()).getConsecutive());
        charge.setPayment(chargeMapper.paymentFromId(chargeDTO.getPaymentId()));
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        charge.setCompany(this.chargeMapper.companyFromId(chargeDTO.getCompanyId()));
        charge = chargeRepository.save(charge);
        return chargeMapper.toDto(charge);
    }

    public ChargeDTO save(AdministrationConfigurationDTO administrationConfigurationDTO, ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = null;
        BalanceDTO balanceDTO = this.houseService.findOne(chargeDTO.getHouseId()).getBalance();
        if (Double.parseDouble(balanceDTO.getMaintenance()) > 0) {
            chargeDTO = this.createSubchargeInCharge(administrationConfigurationDTO, chargeDTO, false);
            charge = payIfBalanceIsPositive(chargeDTO);
            charge.setConsecutive(this.obtainConsecutive(chargeDTO.getCompanyId()));
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
            if (charge.getSplited() != null) {
                charge.setConsecutive(this.chargeRepository.findBySplitedCharge(charge.getId().intValue()).getConsecutive());
            } else {
                if (charge.getConsecutive() == null) {
                    charge.setConsecutive(this.obtainConsecutive(chargeDTO.getCompanyId()));
                }
            }
            charge.setCompany(this.chargeMapper.companyFromId(chargeDTO.getCompanyId()));
            charge = chargeRepository.save(charge);
        }
        ChargeDTO cReady = chargeMapper.toDto(charge);
        cReady.setConsecutive(charge.getConsecutive());
        return cReady;
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

        if (chargeDTO.getDeleted() == 1 || Double.parseDouble(chargeDTO.getAmmount()) != Double.parseDouble(chargeDTO.getTemporalAmmount())) {
            LocalDateTime today = LocalDateTime.now();
            ZoneId id = ZoneId.of("America/Costa_Rica");  //Create timezone
            ZonedDateTime zonedDateTime = ZonedDateTime.of(today, id);
            BitacoraAccionesDTO bitacoraAccionesDTO = new BitacoraAccionesDTO();
            String currency = companyConfigurationService.getByCompanyId(null, this.houseService.findOne(chargeDTO.getHouseId()).getCompanyId()).getContent().get(0).getCurrency();
            if (chargeDTO.getDeleted() == 1) {
                bitacoraAccionesDTO.setConcept("Eliminación de la cuota: " + chargeDTO.getConcept() + " de " + formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
            } else if (Integer.parseInt(chargeDTO.getAmmount()) != Integer.parseInt(chargeDTO.getTemporalAmmount())) {
                bitacoraAccionesDTO.setConcept("Modificación de la cuota: " + chargeDTO.getConcept() + " de " + formatMoney(currency, Double.parseDouble(chargeDTO.getTemporalAmmount())) + " a " + formatMoney(currency, Integer.parseInt(chargeDTO.getAmmount())));
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

    public File obtainFileToPrint(Long chargeId) throws IOException, DocumentException {
        ChargeDTO chargeDTO = this.findOne(chargeId);
        HouseDTO houseDTO = this.houseService.findOne(chargeDTO.getHouseId());
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(chargeDTO.getCompanyId());
        return paymentEmailSenderService.obtainFileBillCharge(administrationConfigurationDTO, houseDTO, chargeDTO);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseToFormat(Long houseId) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeleted(houseId, 0))
            .map(chargeMapper::toDto);
        String currency = companyConfigurationService.getByCompanyId(null, this.houseService.findOne(houseId).getCompanyId()).getContent().get(0).getCurrency();
        return formatCharges(currency, chargeDTOS);
    }


    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouse(Long houseId) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeletedAndState(houseId, 0, 1))
            .map(chargeMapper::toDto);
        String currency = companyConfigurationService.getByCompanyId(null, this.houseService.findOne(houseId).getCompanyId()).getContent().get(0).getCurrency();
        return formatCharges(currency, chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndBetweenDate(String currency, Long houseId, ZonedDateTime initialTime, ZonedDateTime finalTime) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_finalTime, houseId, 0))
            .map(chargeMapper::toDto);
        return formatCharges(currency, chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndBetweenDateResidentAccount(Long houseId, ZonedDateTime initialTime, ZonedDateTime finalTime, ZonedDateTime todayTime) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
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
        String currency = companyConfigurationService.getByCompanyId(null, this.houseService.findOne(houseId).getCompanyId()).getContent().get(0).getCurrency();

        return formatCharges(currency, chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndUnderDate(Long houseId, ZonedDateTime initialTime) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findAllUnderDateAndHouseId(initialTime, houseId))
            .map(chargeMapper::toDto);
        String currency = companyConfigurationService.getByCompanyId(null, this.houseService.findOne(houseId).getCompanyId()).getContent().get(0).getCurrency();

        return formatCharges(currency, chargeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByPayment(String currency, Long paymentId) {
        log.debug("Request to get all Charges");
        Page<Charge> charges = new PageImpl<>(chargeRepository.findByPaymentIdAndDeletedAndState(paymentId, 0, 2));
        Page<ChargeDTO> chargesDTO = charges.map(chargeMapper::toDto);
        for (int i = 0; i < chargesDTO.getContent().size(); i++) {
            ChargeDTO charge = chargesDTO.getContent().get(i);
            charge.setPaymentDate(charges.getContent().get(i).getPaymentDate());
            if (charge.getType() == 6) {
                charge.setWaterConsumption(this.waterConsumptionService.findOneByChargeId(charge.getId()).getConsumption());
            }
        }
        return chargesDTO;
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
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);
        if (charge.getDeleted() == 1) {
            return null;
        }
        String currency = companyConfigurationService.getByCompanyId(null, chargeDTO.getCompanyId()).getContent().get(0).getCurrency();
        return formatCharge(currency, chargeDTO);
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
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        return this.formatCharges(currency, new PageImpl<>(chargeDTOS)).getContent();
    }

    private Charge payIfBalanceIsPositive(ChargeDTO charge) {
        PaymentDTO payment = paymentService.findPaymentInAdvance(charge.getHouseId());
        String currency = companyConfigurationService.getByCompanyId(null, this.houseService.findOne(charge.getHouseId()).getCompanyId()).getContent().get(0).getCurrency();
        charge = this.formatCharge(currency, charge);
        ChargeDTO newCharge = charge;
        ZonedDateTime now = ZonedDateTime.now();
//        BalanceDTO balanceDTO = balanceService.findOneByHouse(newCharge.getHouseId());
        if (payment != null) {
            payment.setAccount(bancoService.findOne(Long.parseLong(payment.getAccount())).getBeneficiario() + ";" + payment.getAccount());
            if (charge.getTotal() <= Double.parseDouble(payment.getAmmountLeft())) {
                payment.setAmmountLeft(Double.parseDouble(payment.getAmmountLeft()) - charge.getTotal() + "");
            } else {
                newCharge.setAmmount(charge.getTotal() - Double.parseDouble(payment.getAmmountLeft()) + "");
                newCharge.setPaymentDate(payment.getDate().plusMinutes(10));
                newCharge.setConsecutive(charge.getConsecutive());
                newCharge = this.create(newCharge);
                charge.setAmmount(payment.getAmmountLeft());
                payment.setAmmountLeft(0 + "");
                charge.setSplitedCharge(newCharge.getId().intValue());
                newCharge.setSplited(1);
            }
            charge.setPaymentId(payment.getId());
            charge.setPaymentDate(payment.getDate());
            charge.setState(2);
            charge.setCompanyId(payment.getCompanyId().longValue());
            charge.setPaymentDate(now);
            payment = paymentService.update(payment);
        }
        Charge chargeEntity = chargeMapper.toEntity(charge);
        chargeEntity.setHouse(chargeMapper.houseFromId(charge.getHouseId()));
        chargeEntity.setCompany(chargeMapper.companyFromId(charge.getCompanyId()));
        chargeEntity.setConsecutive(newCharge.getConsecutive());
        if (charge.getPaymentId() != null) {
            chargeEntity.setPayment(chargeMapper.paymentFromId(charge.getPaymentId()));
            chargeEntity.setCompany(chargeMapper.companyFromId(charge.getCompanyId()));
            PaymentDTO payment1 = paymentService.findOne(charge.getPaymentId());
            chargeEntity.setPaymentDate(payment1.getDate());
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
//                this.paymentEmailSenderService.sendPaymentEmail(payment, true);
            }
        }
        if (newCharge != charge) {
            return this.payIfBalanceIsPositive(newCharge);
        } else {
            return savedCharge;
        }
    }


    public List<ChargeDTO> findBeforeDateAndHouseAndTypeAndState(String currency, ZonedDateTime initialDate, Long houseId, int type, int state) {
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findBeforeDateAndHouseAndTypeAndStateAndDeleted(initialDate, houseId, type, state, 0))
            .map(chargeMapper::toDto);
        return this.formatCharges(currency, chargeDTOS).getContent();
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
            if (chargeDTO.getSubcharge() == null || chargeDTO.getSubcharge().equals("0") || chargeDTO.getSubcharge().equals("0.0")) {
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
                    this.save(administrationConfigurationDTO, chargeDTO);
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
        String currency = companyConfigurationService.getByCompanyId(null, administrationConfigurationDTO.getCompanyId()).getContent().get(0).getCurrency();

        chargesDTO.forEach(chargeDTO -> {
            int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
            int diffDaysToSendEmail = administrationConfigurationDTO.getDaysTobeDefaulter() - administrationConfigurationDTO.getDaysToSendEmailBeforeBeDefaulter();
            if (diffBetweenChargeDateAndNow == diffDaysToSendEmail) {
                chargeDTO = this.createSubchargeInChargeAlways(administrationConfigurationDTO, chargeDTO);
                chargesWillHasSubcharge.add(formatCharge(currency, chargeDTO));
            }
        });
        if (!chargesWillHasSubcharge.isEmpty()) {
            this.paymentEmailSenderService.sendReminderEmail(administrationConfigurationDTO, houseDTO, chargesWillHasSubcharge, "subchargeReminderEmail");
        }
    }

    @Async
    public void sendReminderEmailAndMorosos(AdministrationConfigurationDTO administrationConfigurationDTO, HouseDTO houseDTO, List<ChargeDTO> chargesDTO) {
        List<ChargeDTO> chargesWillHasSubcharge = new ArrayList<>();
        List<ChargeDTO> chargesWithSubcharge = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();
        String currency = companyConfigurationService.getByCompanyId(null, administrationConfigurationDTO.getCompanyId()).getContent().get(0).getCurrency();

        chargesDTO.forEach(chargeDTO -> {
            if (chargeDTO.getSubcharge() == null || chargeDTO.getSubcharge().equals("0") || chargeDTO.getSubcharge().equals("0.0")) {
                int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
                int diffDaysToSendEmail = administrationConfigurationDTO.getDaysTobeDefaulter() - administrationConfigurationDTO.getDaysToSendEmailBeforeBeDefaulter();
                if (diffBetweenChargeDateAndNow < diffDaysToSendEmail) {
                    ChargeDTO chargeFormatted = chargeDTO;
                    chargeFormatted = formatCharge(currency, this.createSubchargeInChargeAlways(administrationConfigurationDTO, chargeFormatted));
                    chargesWillHasSubcharge.add(chargeFormatted);
                }
            }
        });
        if (!chargesWillHasSubcharge.isEmpty()) {
            this.paymentEmailSenderService.sendReminderEmail(administrationConfigurationDTO, houseDTO, chargesWillHasSubcharge, "subchargeReminderEmail");
        }
        chargesDTO.forEach(chargeDTO -> {
            if (chargeDTO.getSubcharge() != null) {
                int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
                int diffDaysToSendEmail = administrationConfigurationDTO.getDaysTobeDefaulter() - administrationConfigurationDTO.getDaysToSendEmailBeforeBeDefaulter();
//                    chargeDTO = this.createSubchargeInChargeAlways(administrationConfigurationDTO, chargeDTO);
                if (Double.parseDouble(chargeDTO.getSubcharge()) > 0) {
                    chargesWithSubcharge.add(formatCharge(currency, chargeDTO));
                }
            }
        });
        if (!chargesWithSubcharge.isEmpty()) {
            this.paymentEmailSenderService.sendReminderEmail(administrationConfigurationDTO, houseDTO, chargesWithSubcharge, "defaulterReminderEmail");
        }
    }

    private Page<ChargeDTO> formatCharges(String currency, Page<ChargeDTO> charges) {
        List<ChargeDTO> chargesList = new ArrayList<>();
        charges.forEach(chargeDTO -> {
            chargesList.add(formatCharge(currency, chargeDTO));
        });
        String a = "";
        return new PageImpl<>(chargesList);
    }

    private ChargeDTO formatCharge(String currency, ChargeDTO chargeDTO) {
        if (chargeDTO.getSubcharge() != null) {
            chargeDTO.setTotal(currency, Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(chargeDTO.getSubcharge()));
        } else {
            chargeDTO.setSubcharge("0");
            chargeDTO.setTotal(currency, Double.parseDouble(chargeDTO.getAmmount()));
        }
        if (chargeDTO.getConsecutive() != null) {
            chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
        }

        if (chargeDTO.getSplited() != null) {
            ChargeDTO c = formatSplittedCharge(currency, chargeDTO);
            c.setConsecutive(chargeDTO.getConsecutive());
            if (chargeDTO.getConsecutive() != null) {
                c.setBillNumber(c.formatBillNumber(chargeDTO.getConsecutive()));
            }
            c.setLeftToPay(currency, c.getTotal() - c.getAbonado());
            c.setId(chargeDTO.getId());
            return c;
        }
        chargeDTO.setAbonado(currency, 0);
        chargeDTO.setLeftToPay(currency, chargeDTO.getTotal());
        if (chargeDTO.getType() == 6 && chargeDTO.getId() != null) {
            WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
            if (wc != null) {
                chargeDTO.setWaterConsumption(wc.getConsumption());
            }
        }
        return chargeDTO;
    }

    public ChargeDTO formatSplittedCharge(String currency, ChargeDTO chargeDTO) {
        double totalCharge = 0;
        double abonado = 0;
        if (chargeDTO.getAbonado() != 0) {
            abonado = chargeDTO.getAbonado();
        }
        if (chargeDTO.getSplited() != null) {
            Charge abonada = this.chargeRepository.findBySplitedCharge(chargeDTO.getId().intValue());
            if (abonada != null) {
                totalCharge = Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(abonada.getAmmount());
                abonado = abonado + Double.parseDouble(abonada.getAmmount());
                chargeDTO.setTotal(currency, totalCharge);
                chargeDTO.setAbonado(currency, abonado);
                if (abonada.getSplited() != null) {
                    ChargeDTO abonadaDTO = this.chargeMapper.toDto(abonada);
                    abonadaDTO.setAmmount(totalCharge + "");
                    abonadaDTO.setAbonado(currency, abonado);
                    return formatSplittedCharge(currency, abonadaDTO);
                }
            }
        }
        return chargeDTO;
    }

    public ChargesToPayReportDTO findChargesToPay(ZonedDateTime finalDate, int type, Long companyId) {
        ZonedDateTime zd_finalTime = finalDate.withHour(23).withMinute(59).withSecond(59);
        log.debug("Request to get all Charges");
        ChargesToPayReportDTO chargesReport = new ChargesToPayReportDTO();
        List<HouseDTO> houses = this.houseService.findAll(companyId).getContent();
        List<DueHouseDTO> finalHouses = new ArrayList<>();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        houses.forEach(houseDTO -> {
            List<ChargeDTO> chargesPerHouse;
            if (type == 10) {
                chargesPerHouse = new PageImpl<>(chargeRepository.findByHouseBetweenDates(zd_finalTime, 1, 0, houseDTO.getId())).map(chargeMapper::toDto).getContent();
            } else {
                chargesPerHouse = new PageImpl<>(chargeRepository.findByHouseBetweenDatesAndType(zd_finalTime, 1, type, 0, houseDTO.getId())).map(chargeMapper::toDto).getContent();
            }
            if (chargesPerHouse.size() > 0) {
                DueHouseDTO dueHouse = new DueHouseDTO();
                chargesPerHouse.forEach(chargeDTO -> {
                    chargeDTO = formatCharge(currency, chargeDTO);
                    dueHouse.setTotalDue(currency, dueHouse.getTotalDue() + chargeDTO.getLeftToPay());
                });
                dueHouse.setHouseDTO(houseDTO);
                dueHouse.setResponsable(null);
                dueHouse.setDues(chargesPerHouse);
                chargesReport.setTotalDue(currency, chargesReport.getTotalDue() + dueHouse.getTotalDue());
                chargesReport.setTotalDueHouses(chargesReport.getTotalDueHouses() + 1);
                finalHouses.add(dueHouse);
            }
        });
        chargesReport.setDueHouses(finalHouses);
        return chargesReport;
    }


    public File obtainBillingReportToPrint(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId, String houseId, String category) {
        BillingReportDTO reportDTO = this.findBillingReport(initialDate, finalDate, companyId, houseId, category);
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialDate + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalDate + "[America/Regina]").replace("00:00:00", "23:59:59"));
        return chargesToPayDocumentService.obtainBillingReportToPrint(reportDTO, Long.valueOf(companyId + ""), zd_initialTime, zd_finalTime);
    }

    public BillingReportDTO findBillingReport(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId, String houseId, String category) {
        ZonedDateTime zd_initialTime = initialDate.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalDate.withHour(23).withMinute(59).withSecond(59);
        double totalMaint = 0.0;
        double totalExtra = 0;
        double totalAreas = 0;
        double totalMultas = 0;
        double totalWaterCharge = 0;

        BillingReportDTO billingReportDTO = new BillingReportDTO();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<ChargeDTO> charges;
        if (category.equals("empty")) {
            if (houseId.equals("empty")) {
                charges = new PageImpl<>(chargeRepository.findBillingReport(zd_initialTime, zd_finalTime, companyId, 0)).map(chargeMapper::toDto).getContent();
            } else {
                charges = new PageImpl<>(chargeRepository.findBillingReportAndHouse(zd_initialTime, zd_finalTime, companyId, Long.parseLong(houseId), 0)).map(chargeMapper::toDto).getContent();
            }

        } else {
            int categoria = Integer.parseInt(category);
            if (houseId.equals("empty")) {
                charges = new PageImpl<>(chargeRepository.findBillingReportByType(zd_initialTime, zd_finalTime, categoria, companyId, 0)).map(chargeMapper::toDto).getContent();
            } else {
                charges = new PageImpl<>(chargeRepository.findBillingReportByTypeAndHouse(zd_initialTime, zd_finalTime, categoria, companyId, Long.parseLong(houseId), 0)).map(chargeMapper::toDto).getContent();
            }


        }


        for (int i = 0; i < charges.size(); i++) {
            ChargeDTO chargeDTO;
            chargeDTO = formatCharge(currency, charges.get(i));
            chargeDTO.setResponsable(this.residentService.findPrincipalContactByHouse(chargeDTO.getHouseId()));
            chargeDTO.setHouse(this.houseService.findOne(chargeDTO.getHouseId()));


            switch (chargeDTO.getType()) {
                case 1:
                    totalMaint = totalMaint + Double.parseDouble(chargeDTO.getAmmount());
                    break;
                case 2:
                    totalExtra = totalExtra + Double.parseDouble(chargeDTO.getAmmount());
                    break;
                case 3:
                    totalAreas = totalAreas + Double.parseDouble(chargeDTO.getAmmount());
                    break;
                case 5:
                    totalMultas = totalMultas + Double.parseDouble(chargeDTO.getAmmount());
                    break;
                case 6:
                    totalWaterCharge = totalWaterCharge + Double.parseDouble(chargeDTO.getAmmount());
                    break;
                default:
            }

        }
        billingReportDTO.setTotal(totalMaint + totalAreas + totalExtra + totalMultas + totalWaterCharge);
        billingReportDTO.setTotalMaintenance(totalMaint);
        billingReportDTO.setTotalExtraordinary(totalExtra);
        billingReportDTO.setTotalCommonArea(totalAreas);
        billingReportDTO.setTotalMulta(totalMultas);
        billingReportDTO.setTotalWaterCharge(totalWaterCharge);
        billingReportDTO.setCharges(charges);
        return billingReportDTO;
    }


}
