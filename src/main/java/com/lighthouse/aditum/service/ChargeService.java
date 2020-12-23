package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.repository.ChargeRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.ChargeMapper;
import com.lighthouse.aditum.service.util.RandomUtil;
import com.lighthouse.aditum.web.rest.HouseResource;
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

import javax.validation.constraints.NotNull;
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
import static java.lang.Math.max;
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
    private final CustomChargeTypeService customChargeTypeService;
    private final PaymentChargeService paymentChargeService;
    private final HistoricalDefaulterService historicalDefaulterService;


    @Autowired
    public ChargeService(@Lazy HistoricalDefaulterService historicalDefaulterService,@Lazy PaymentChargeService paymentChargeService,CustomChargeTypeService customChargeTypeService, @Lazy ChargesToPayDocumentService chargesToPayDocumentService, PushNotificationService pNotification, @Lazy WaterConsumptionService waterConsumptionService, CompanyConfigurationService companyConfigurationService, CompanyService companyService, AdminInfoService adminInfoService, UserService userService, BitacoraAccionesService bitacoraAccionesService, @Lazy HouseService houseService, ResidentService residentService, @Lazy PaymentDocumentService paymentEmailSenderService, BancoService bancoService, @Lazy PaymentService paymentService, ChargeRepository chargeRepository, ChargeMapper chargeMapper, BalanceService balanceService, AdministrationConfigurationService administrationConfigurationService) {
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
        this.customChargeTypeService = customChargeTypeService;
        this.paymentChargeService = paymentChargeService;
        this.historicalDefaulterService = historicalDefaulterService;
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
        HouseDTO house = this.houseService.findOneClean(chargeDTO.getHouseId());
        charge.setCompany(this.chargeMapper.companyFromId(chargeDTO.getCompanyId()));
        if (chargeDTO.getConsecutive() != null) {
            charge.setConsecutive(chargeDTO.getConsecutive());
        } else {
            charge.setConsecutive(this.obtainConsecutive(chargeDTO.getCompanyId()));
        }
        charge = chargeRepository.save(charge);
        String currency = companyConfigurationService.getByCompanyId(null, house.getCompanyId()).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(chargeDTO.getCompanyId());
        return this.formatCharge(currency, chargeMapper.toDto(charge), customChargeTypes);
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

    public ChargeDTO sendEmailCharge(CompanyConfigurationDTO companyConfigDTO, AdministrationConfigurationDTO administrationConfigurationDTO, Long companyId, Long chargeId, String emailTo) throws URISyntaxException, IOException, DocumentException {
        ChargeDTO charge = findOne(chargeId);
        HouseDTO house = this.houseService.findOneClean(charge.getHouseId());
        AdministrationConfigurationDTO adminConfig = this.administrationConfigurationService.findOneByCompanyId(house.getCompanyId());
        String[] parts = emailTo.split(",");
        for (int i = 0; i < parts.length; i++) {
            ResidentDTO residentDTO = residentService.findOne(Long.parseLong(parts[i]));
            this.paymentEmailSenderService.sendChargeManualEmail(administrationConfigurationDTO, house, charge, residentDTO);
        }
        return null;
    }


    public ChargeDTO createWaterCharge(CompanyConfigurationDTO companyConfigDTO, WaterConsumptionDTO wC, ZonedDateTime date, AdministrationConfigurationDTO administrationConfigurationDTO, Boolean sendEmail, Boolean autocalculated, String concept) throws URISyntaxException, IOException, DocumentException {
        HouseDTO house = this.houseService.findOneClean(wC.getHouseId());
        AdministrationConfigurationDTO adminConfig = this.administrationConfigurationService.findOneByCompanyId(house.getCompanyId());
        ChargeDTO wcCharge = new ChargeDTO();
        String ammount = "";
        ammount = wC.getMonth();
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
        ChargeDTO charge = null;
        if(Double.parseDouble(wcCharge.getAmmount()) > 0){
             charge = this.create(wcCharge);
            wC.setChargeId(charge.getId());
        }
        wC.setStatus(1);
        wC.setMonth(ammount);
        wcCharge.setTotal(companyConfigDTO.getCurrency(), Double.parseDouble(wcCharge.getAmmount()));
        if (Double.parseDouble(wcCharge.getAmmount()) > 0) {
            this.waterConsumptionService.save(wC);
            ZonedDateTime lastHourToday = ZonedDateTime.now().withHour(23).withMinute(59).withSecond(59);
            if (charge.getDate().isBefore(lastHourToday) && sendEmail) {
                this.paymentEmailSenderService.sendChargeEmail(administrationConfigurationDTO, house, wcCharge);
            }
            this.pNotification.sendNotificationsToOwnersByHouse(wcCharge.getHouseId(),
                this.pNotification.createPushNotification(wcCharge.getConcept() + " - " + house.getHousenumber(),
                    "Se ha creado una nueva cuota de agua en su filial por un monto de " + formatMoney(companyConfigDTO.getCurrency(), Double.parseDouble(wcCharge.getAmmount())) + "."));
            return charge;
        }
        return null;
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
        if (charge.getSplitedCharge() != null) {
            if (charge.getId() == charge.getSplitedCharge().longValue()) {
                charge.setSplitedCharge(null);
            }
        }
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
        charge.setPayment(chargeMapper.paymentFromId(chargeDTO.getPaymentId()));
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        charge.setCompany(this.chargeMapper.companyFromId(chargeDTO.getCompanyId()));
        charge = chargeRepository.save(charge);
        if(chargeDTO.getType() == 6){
            WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
            if(wc!=null){
                wc.setChargeId(charge.getId());
            }
            this.waterConsumptionService.save(wc);
        }
        return chargeMapper.toDto(charge);
    }

    public ChargeDTO saveFormatOld(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = null;
        charge = chargeMapper.toEntity(chargeDTO);
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
        Long companyID = administrationConfigurationDTO.getCompanyId();
        Charge charge = null;
        if (chargeDTO.getId() == null) {
            chargeDTO.setAbonado(0);
            chargeDTO.setLeftToPay(Double.parseDouble(chargeDTO.getAmmount()));
        }
        BalanceDTO balanceDTO = this.balanceService.findOneByHouse(chargeDTO.getHouseId());
//        if (Double.parseDouble(balanceDTO.getMaintenance()) > 0) {
//            chargeDTO = this.createSubchargeInCharge(administrationConfigurationDTO, chargeDTO, false);
//            chargeDTO.setConsecutive(this.obtainConsecutive(companyID));
//            charge = payIfBalanceIsPositive(chargeDTO);
//        } else {
            charge = chargeMapper.toEntity(chargeDTO);
            charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
            if (chargeDTO.getPaymentId() != null) {
                charge.setPayment(chargeMapper.paymentFromId(chargeDTO.getPaymentId()));
                charge.setCompany(chargeMapper.companyFromId(companyID));
                charge.setPaymentDate(ZonedDateTime.now());
            }
            if (charge.getSplited() != null) {
                charge.setConsecutive(this.chargeRepository.findBySplitedCharge(charge.getId().intValue()).getConsecutive());
            } else {
                if (charge.getConsecutive() == null) {
                    charge.setConsecutive(this.obtainConsecutive(companyID));
                }
            }
            charge.setCompany(this.chargeMapper.companyFromId(companyID));
            charge = chargeRepository.save(charge);
//        }
        ChargeDTO cReady = chargeMapper.toDto(charge);
        cReady.setConsecutive(charge.getConsecutive());
        String currency = companyConfigurationService.getByCompanyId(null, companyID).getContent().get(0).getCurrency();
        this.historicalDefaulterService.formatHistoricalReportByHouse(chargeDTO.getHouseId(),charge.getDate(),currency,companyID.intValue());
        return cReady;
    }

    public ChargeDTO updateClean(ChargeDTO chargeDTO) {
        Charge charge = chargeMapper.toEntity(chargeDTO);
        return chargeMapper.toDto(chargeRepository.save(charge));
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
        charge.setCompany(chargeMapper.companyFromId(chargeDTO.getCompanyId()));
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
            } else if (Double.parseDouble(chargeDTO.getAmmount()) != Double.parseDouble(chargeDTO.getTemporalAmmount())) {
                bitacoraAccionesDTO.setConcept("Modificación de la cuota: " + chargeDTO.getConcept() + " de " + formatMoney(currency, Double.parseDouble(chargeDTO.getTemporalAmmount())) + " a " + formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())));
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
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(houseDTO.getCompanyId());
        return paymentEmailSenderService.obtainFileBillCharge(administrationConfigurationDTO, houseDTO, chargeDTO);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseToFormat(Long houseId) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeleted(houseId, 0))
            .map(chargeMapper::toDto);
        return chargeDTOS;
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseCToFormat(Long houseId, String concept) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeletedAndConcept(houseId, 0, concept))
            .map(chargeMapper::toDto);
        return chargeDTOS;
    }


    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouse(Long houseId) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeletedAndState(houseId, 0, 1))
            .map(chargeMapper::toDto);
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        return formatCharges(currency, chargeDTOS, customChargeTypes);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouse(Long houseId,List<CustomChargeTypeDTO> customChargeTypes) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeletedAndState(houseId, 0, 1))
            .map(chargeMapper::toDto);
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        return formatCharges(currency, chargeDTOS, customChargeTypes);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findWaterChargeAllByHouse(Long houseId) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByHouseIdAndDeletedAndStateAndType(houseId, 0, 1, 6))
            .map(chargeMapper::toDto);
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        return formatCharges(currency, chargeDTOS, customChargeTypes);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndBetweenDate(String currency, Long houseId, ZonedDateTime initialTime, ZonedDateTime finalTime, List<CustomChargeTypeDTO> customChargeTypeDTOS) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_finalTime, houseId, 0))
            .map(chargeMapper::toDto);
        return formatCharges(currency, chargeDTOS, customChargeTypeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllNotPayedByHouseAndBetweenDate(String currency, Long houseId, ZonedDateTime initialTime, ZonedDateTime finalTime, List<CustomChargeTypeDTO> customChargeTypeDTOS) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseIdAndState(zd_initialTime, zd_finalTime, houseId, 0,1))
            .map(chargeMapper::toDto);
        return formatCharges(currency, chargeDTOS, customChargeTypeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndBetweenDateCollection(String currency, Long houseId, ZonedDateTime initialTime, ZonedDateTime finalTime) {
        log.debug("Request to get all Charges");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findAllBetweenDatesAndHouseId(zd_initialTime, zd_finalTime, houseId, 0))
            .map(chargeMapper::toDto);
        return chargeDTOS;
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
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();

        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        return formatCharges(currency, chargeDTOS, customChargeTypes);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByHouseAndUnderDate(Long houseId, ZonedDateTime initialTime) {
        log.debug("Request to get all Charges");
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findAllUnderDateAndHouseId(initialTime, houseId, 0))
            .map(chargeMapper::toDto);
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();

        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        return formatCharges(currency, chargeDTOS, customChargeTypes);
    }

    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAllByPayment(List<CustomChargeTypeDTO> customChargeTypes, String currency, Long paymentId) {
        log.debug("Request to get all Charges");
        Page<Charge> charges = new PageImpl<>(chargeRepository.findByPaymentIdAndDeletedAndState(paymentId, 0, 2));
        Page<ChargeDTO> chargesDTO = charges.map(chargeMapper::toDto);
        for (int i = 0; i < chargesDTO.getContent().size(); i++) {
            ChargeDTO charge = chargesDTO.getContent().get(i);
            charge.setPaymentDate(charges.getContent().get(i).getPaymentDate());
            charge.setCategory(this.getCategory(charge.getType(), customChargeTypes));
            if (charge.getType() == 6 && charge.getId() != null) {
                charge.setWaterConsumption(findWCRecursive(charge));
            }
        }
        return chargesDTO;
    }


    @Transactional(readOnly = true)
    public ChargeDTO removeChargeFromPayment(String currency,PaymentChargeDTO charge, Long companyId) {
        ChargeDTO oC = this.findByConsecutiveAndCompanyId(Integer.parseInt(charge.getConsecutive()),companyId);
        oC.setLeftToPay(currency, oC.getLeftToPay()+Double.parseDouble(charge.getAbonado()));
        oC.setAbonado(oC.getAbonado()-Double.parseDouble(charge.getAbonado()));
        if(oC.getLeftToPay()>0){
            oC.setState(1);
        }
        return this.updateClean(oC);
    }

    @Transactional(readOnly = true)
    public Charge findByConsecutiveToRemoveFromPayment(int consecutive, Long companyId, Long houseId) {
        return this.chargeRepository.findByConsecutiveAndDeletedAndStateAndCompanyIdAndHouseId(consecutive, 0, 1, companyId, houseId);
    }

    @Transactional(readOnly = true)
    public ChargeDTO findByConsecutiveAndCompanyId(int consecutive, Long companyId) {
        ChargeDTO chargeDTO = chargeMapper.toDto(this.chargeRepository.findByConsecutiveAndDeletedAndCompanyId(consecutive, 0, companyId));
        return chargeDTO;
    }

    private WaterConsumptionDTO findWCRecursive(ChargeDTO charge) {
        WaterConsumptionDTO wc = null;
        if (charge.getSplitedCharge() != null) {
            ChargeDTO c = this.findOneWithoutFormat(charge.getSplitedCharge().longValue());
            wc = this.waterConsumptionService.findOneByChargeId(charge.getId());
            if (wc != null && c == null) {
                return wc;
            } else {
                if (c == null) {
                    if (wc != null) {
                        return wc;
                    }
                } else {
                    return findWCRecursive(c);
                }
            }
        } else {
            wc = this.waterConsumptionService.findOneByChargeId(charge.getId());
            if (wc != null) {
                return wc;
            } else {
                return null;
            }
        }
        return null;
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

        String currency = companyConfigurationService.getByCompanyId(null, chargeDTO.getHouse().getCompanyId()).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(chargeDTO.getHouse().getCompanyId());
        return formatCharge(currency, chargeDTO, customChargeTypes);
    }

    @Transactional(readOnly = true)
    public ChargeDTO findOneWithoutFormat(Long id) {
        log.debug("Request to get Charge : {}", id);
        Charge charge = chargeRepository.findOne(id);
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);
        if (charge == null) {
            return null;
        }
        if (charge.getDeleted() == 1) {
            return null;
        }
        return chargeDTO;
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
        return chargeDTOS;
    }

    public List<ChargeDTO> findPaidChargesBetweenDatesListAndOther(ZonedDateTime initialTime, ZonedDateTime finalTime, int type, Long companyId) {
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        log.debug("Request to get all Charges");
        List<ChargeDTO> chargeDTOS = chargeRepository.findPaidChargesBetweenDatesAndCompanyIdBeingOther(zd_initialTime, zd_finalTime, type, 2, companyId).stream()
            .map(chargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        return chargeDTOS;
    }

    private Charge payIfBalanceIsPositive(ChargeDTO charge) {
        PaymentDTO payment = paymentService.findPaymentInAdvance(charge.getHouseId());
        Long companyId = this.houseService.findOne(charge.getHouseId()).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        ZonedDateTime now = ZonedDateTime.now();
        double paying = 0;
        if (payment != null) {
            payment.setAccount(bancoService.findOne(Long.parseLong(payment.getAccount())).getBeneficiario() + ";" + payment.getAccount());
            if (charge.getLeftToPay() <= Double.parseDouble(payment.getAmmountLeft())) {
                payment.setAmmountLeft(Double.parseDouble(payment.getAmmountLeft()) - charge.getLeftToPay() + "");
                charge.setLeftToPay(currency, 0);
                charge.setAbonado(currency, Double.parseDouble(charge.getAmmount()));
                paying = Double.parseDouble(charge.getAmmount());
                charge.setState(2);
            } else {
                charge.setLeftToPay(currency, charge.getLeftToPay() - Double.parseDouble(payment.getAmmountLeft()));
                paying = Double.parseDouble(payment.getAmmountLeft());
                charge.setAbonado(charge.getAbonado() + Double.parseDouble(payment.getAmmountLeft()));
                payment.setAmmountLeft("0");
            }
            charge.setPaymentDate(now);
            charge.setPaymentId(payment.getId());
            payment = paymentService.update(payment);
            charge.setCompanyId(companyId);
            Charge toSave = this.chargeMapper.toEntity(charge);
            toSave.setHouse(chargeMapper.houseFromId(charge.getHouseId()));;
            Charge savedCharge = chargeRepository.save(toSave);
            PaymentChargeDTO paymentCharge = new PaymentChargeDTO(null, savedCharge.getType(), savedCharge.getDate(), savedCharge.getConcept(), savedCharge.getAmmount(), savedCharge.getId(), savedCharge.getConsecutive() + "", paying+ "", savedCharge.getLeftToPay() + "", 0, payment.getId());
            paymentCharge.setOriginalCharge(savedCharge.getId());
            this.paymentChargeService.save(paymentCharge);
            ChargeDTO savedChargeDTO = this.chargeMapper.toDto(savedCharge);
            savedChargeDTO.setPaymentAmmount(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(savedChargeDTO.getDate()));
//            if (payment != null) {
//                payment.setCharges(new ArrayList<>());
////            payment.getCharges().add(savedChargeDTO);
//                Page<ResidentDTO> residents = residentService.findEnabledByHouseId(null, payment.getHouseId());
//                List<ResidentDTO> emailTo = new ArrayList<>();
//                for (int i = 0; i < residents.getContent().size(); i++) {
//                    if (residents.getContent().get(i).getPrincipalContact() == 1) {
//                        emailTo.add(residents.getContent().get(i));
//                    }
//                }
//                if (emailTo.size() > 0) {
//                    payment.setEmailTo(emailTo);
////                this.paymentEmailSenderService.sendPaymentEmail(payment, true);
//                }
//            }
            PaymentDTO payment2 = paymentService.findPaymentInAdvance(charge.getHouseId());
            if (payment2 != null) {
                if (savedChargeDTO.getLeftToPay() > 0) {
                    return this.payIfBalanceIsPositive(savedChargeDTO);
                }
            }
            this.historicalDefaulterService.formatHistoricalReportByHouse(charge.getHouseId(),charge.getDate(),currency,companyId.intValue());
            return savedCharge;
        }
       return null;
    }

    public String getCategory(int type, List<CustomChargeTypeDTO> customTypes) {
        if (type < 7) {
            switch (type) {
                case 1:
                    return "MANTENIMIENTO";
                case 2:
                    return "EXTRAORDINARIA";
                case 3:
                    return "ÁREAS COMUNES";
                case 5:
                    return "MULTA";
                case 6:
                    return "CUOTA AGUA";
                case 10:
                    return "OTROS";
            }
        } else {
            for (int i = 0; i < customTypes.size(); i++) {
                CustomChargeTypeDTO cc = customTypes.get(i);
                if (type == cc.getType()) {
                    return cc.getDescription();
                }
            }
        }
        return null;
    }


    public List<ChargeDTO> findBeforeDateAndHouseAndTypeAndState(List<CustomChargeTypeDTO> customChargeTypeDTOS, String currency, ZonedDateTime initialDate, Long houseId, int type, int state) {
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findBeforeDateAndHouseAndTypeAndStateAndDeleted(initialDate, houseId, type, state, 0))
            .map(chargeMapper::toDto);
        return this.formatCharges(currency, chargeDTOS, customChargeTypeDTOS).getContent();
    }

    public List<ChargeDTO> findBeforeDateAndHouseAndTypeMoreAndState(List<CustomChargeTypeDTO> customChargeTypeDTOS, String currency, ZonedDateTime initialDate, Long houseId, int state) {
        Page<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findBeforeDateAndHouseAndTypeGreaterThanAndStateAndDeleted(initialDate, houseId, 7, state, 0))
            .map(chargeMapper::toDto);
        return this.formatCharges(currency, chargeDTOS, customChargeTypeDTOS).getContent();
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
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(administrationConfigurationDTO.getCompanyId());

        chargesDTO.forEach(chargeDTO -> {
            int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
            int diffDaysToSendEmail = administrationConfigurationDTO.getDaysTobeDefaulter() - administrationConfigurationDTO.getDaysToSendEmailBeforeBeDefaulter();
            if (diffBetweenChargeDateAndNow == diffDaysToSendEmail) {
                chargeDTO = this.createSubchargeInChargeAlways(administrationConfigurationDTO, chargeDTO);
                chargesWillHasSubcharge.add(formatCharge(currency, chargeDTO, customChargeTypes));
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
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(administrationConfigurationDTO.getCompanyId());
        chargesDTO.forEach(chargeDTO -> {
            if (chargeDTO.getSubcharge() == null || chargeDTO.getSubcharge().equals("0") || chargeDTO.getSubcharge().equals("0.0")) {
                int diffBetweenChargeDateAndNow = toIntExact(ChronoUnit.DAYS.between(chargeDTO.getDate().toLocalDate(), now.toLocalDate()));
                int diffDaysToSendEmail = administrationConfigurationDTO.getDaysTobeDefaulter() - administrationConfigurationDTO.getDaysToSendEmailBeforeBeDefaulter();
                if (diffBetweenChargeDateAndNow < diffDaysToSendEmail) {
                    ChargeDTO chargeFormatted = chargeDTO;
                    chargeFormatted = formatCharge(currency, this.createSubchargeInChargeAlways(administrationConfigurationDTO, chargeFormatted), customChargeTypes);
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
                    chargesWithSubcharge.add(formatCharge(currency, chargeDTO, customChargeTypes));
                }
            }
        });
        if (!chargesWithSubcharge.isEmpty()) {
            this.paymentEmailSenderService.sendReminderEmail(administrationConfigurationDTO, houseDTO, chargesWithSubcharge, "defaulterReminderEmail");
        }
    }

    private Page<ChargeDTO> formatChargesOld(String currency, Page<ChargeDTO> charges, List<CustomChargeTypeDTO> customChargeTypes) {
        List<ChargeDTO> chargesList = new ArrayList<>();
        charges.forEach(chargeDTO -> {
            int exist = 0;
            for (ChargeDTO nC : chargesList) {
                if (nC.getConsecutive() == null) {
                    String a = "";
                }
                if (chargeDTO.getConsecutive() == null) {
                    String v = "";
                }
                if (chargeDTO.getDeleted() == 0 && nC.getDeleted() == 0) {
                    if (nC.getConsecutive().equals(chargeDTO.getConsecutive())) {
                        exist++;
                    }
                }
            }
            if (exist == 0) {
                ChargeDTO c = formatChargeOld(currency, chargeDTO, customChargeTypes);
                chargesList.add(c);
            }
        });
        return new PageImpl<>(chargesList);
    }

    private Page<ChargeDTO> formatCharges(String currency, Page<ChargeDTO> charges, List<CustomChargeTypeDTO> customChargeTypes) {
        List<ChargeDTO> chargesList = new ArrayList<>();
        charges.forEach(chargeDTO -> {
            ChargeDTO c = formatCharge(currency, chargeDTO, customChargeTypes);
            chargesList.add(c);
        });
        return new PageImpl<>(chargesList);
    }

    private Page<ChargeDTO> formatChargesHistorical(ZonedDateTime finalTime, int daysTobeDefaulter, String currency, Page<ChargeDTO> charges, List<CustomChargeTypeDTO> customChargeTypes) {
        List<ChargeDTO> chargesList = new ArrayList<>();
        charges.forEach(chargeDTO -> {
            int exist = 0;
            for (ChargeDTO nC : chargesList) {
                if (nC.getConsecutive().equals(chargeDTO.getConsecutive())) {
                    exist++;
                }
            }
            if (exist == 0) {
                ChargeDTO c = formatChargeHistorical(finalTime, daysTobeDefaulter, currency, chargeDTO, customChargeTypes);
                chargesList.add(c);
            }
        });
        return new PageImpl<>(chargesList);
    }

    private Page<ChargeDTO> formatChargesHistoricalPositive(String currency, Page<ChargeDTO> charges) {
        List<ChargeDTO> chargesList = new ArrayList<>();
        charges.forEach(chargeDTO -> {
            int exist = 0;
            for (ChargeDTO nC : chargesList) {
                if (nC.getConsecutive().equals(chargeDTO.getConsecutive())) {
                    exist++;
                }
            }
            if (exist == 0) {
                ChargeDTO c = formatChargeHistoricalPositive(currency, chargeDTO);
                chargesList.add(c);
            }
        });
        return new PageImpl<>(chargesList);
    }

    public ChargeDTO formatCharge(String currency, ChargeDTO chargeDTO, List<CustomChargeTypeDTO> customChargeTypes) {
        chargeDTO.setTotal(currency, Double.parseDouble(chargeDTO.getAmmount()));
        chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
        chargeDTO.setLeftToPay(currency, chargeDTO.getLeftToPay());
        chargeDTO.setAbonado(currency, chargeDTO.getAbonado());
        chargeDTO.setAmmountFormatted(currency, Double.parseDouble(chargeDTO.getAmmount()));
        if (chargeDTO.getType() == 6 && chargeDTO.getId() != null) {
            WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
            if (wc != null) {
                chargeDTO.setWaterConsumption(wc);
            }
        }
        chargeDTO.setCategory(this.getCategory(chargeDTO.getType(), customChargeTypes));
        return chargeDTO;
    }

    public ChargeDTO formatChargeOld(String currency, ChargeDTO chargeDTO, List<CustomChargeTypeDTO> customChargeTypes) {
        if (chargeDTO.getConsecutive() != null) {
            List<Charge> charges = this.chargeRepository.findByConsecutiveAndDeletedAndCompanyIdAndHouseId(chargeDTO.getConsecutive(), 0, chargeDTO.getCompanyId(), chargeDTO.getHouseId());
            double abonado = 0;
            double leftToPay = 0;
            double total = 0;
            for (Charge c : charges) {
                total += Double.parseDouble(c.getAmmount());
                if (c.getState() == 2) {
                    abonado += Double.parseDouble(c.getAmmount());
                } else {
                    leftToPay += Double.parseDouble(c.getAmmount());
                }
            }
            chargeDTO.setTotal(currency, total);
            chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
            chargeDTO.setLeftToPay(currency, leftToPay);
            chargeDTO.setAbonado(currency, abonado);
            chargeDTO.setAmmountFormatted(currency, Double.parseDouble(chargeDTO.getAmmount()));

        } else {
            chargeDTO.setTotal(currency, Double.parseDouble(chargeDTO.getAmmount()));
        }
        chargeDTO.setCategory(this.getCategory(chargeDTO.getType(), customChargeTypes));
        return chargeDTO;
    }

    private ChargeDTO formatChargeHistoricalPositive(String currency, ChargeDTO chargeDTO) {
        if (chargeDTO.getConsecutive() != null) {
            List<Charge> charges = this.chargeRepository.findByConsecutiveAndDeletedAndCompanyIdAndHouseId(chargeDTO.getConsecutive(), 0, chargeDTO.getCompanyId(), chargeDTO.getHouseId());
            double abonado = 0;
            double leftToPay = 0;
            double morosidad = 0;
            double total = 0;
            int defaultersDay = 0;
            for (Charge charge : charges) {
                ZonedDateTime fechaCobro = charge.getDate();
                ZonedDateTime fechaPago = charge.getPaymentDate();
                if (fechaPago != null) {
                    total += Double.parseDouble(charge.getAmmount());
                    if (charge.getState() == 2) {
                        if (fechaPago.isBefore(fechaCobro)) {
                            abonado += Double.parseDouble(charge.getAmmount());
                        }
                    } else {
                        leftToPay += Double.parseDouble(charge.getAmmount());
                    }
                }
            }
            chargeDTO.setTotal(currency, total);
            chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
            chargeDTO.setLeftToPay(currency, leftToPay);
            chargeDTO.setAbonado(currency, abonado);
            chargeDTO.setPaymentAmmount(morosidad + "");
            chargeDTO.setDefaulterDays(defaultersDay);
            if (chargeDTO.getType() == 6 && chargeDTO.getId() != null) {
                WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
                if (wc != null) {
                    chargeDTO.setWaterConsumption(wc);
                }
            }
        } else {
            chargeDTO.setTotal(currency, Double.parseDouble(chargeDTO.getAmmount()));
        }
        return chargeDTO;
    }

    private ChargeDTO formatChargeHistorical(ZonedDateTime finalTime, int daysTobeDefaulter, String currency, ChargeDTO chargeDTO, List<CustomChargeTypeDTO> customChargeTypes) {
        if (chargeDTO.getConsecutive() != null) {
            List<Charge> charges = this.chargeRepository.findByConsecutiveAndDeletedAndCompanyIdAndHouseId(chargeDTO.getConsecutive(), 0, chargeDTO.getCompanyId(), chargeDTO.getHouseId());
            double abonado = 0;
            double leftToPay = 0;
            double morosidad = 0;
            double total = 0;
            int defaultersDay = 0;
            for (Charge charge : charges) {
                ZonedDateTime fechaCobro = charge.getDate();
                ZonedDateTime fechaPago = charge.getPaymentDate();
                if (charge.getState() == 1) {
                    charge.setPaymentDate(null);
                    chargeDTO.setPaymentDate(null);
                    fechaPago = null;
                }
                if (fechaPago == null) {
                    if (fechaCobro.isBefore(ZonedDateTime.now())) {
                        int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), ZonedDateTime.now().toLocalDate()));
                        defaultersDay = (Math.abs(diffBetweenCobroYPago - daysTobeDefaulter));
                        morosidad += Double.parseDouble(charge.getAmmount());
                    }
                } else {
                    int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), fechaPago.toLocalDate()));
//                    if (daysTobeDefaulter < diffBetweenCobroYPago) {
                    defaultersDay = Math.abs(diffBetweenCobroYPago - daysTobeDefaulter);
                    if (fechaPago.isAfter(finalTime)) {
                        morosidad += Double.parseDouble(charge.getAmmount());
                    }
//                    }
                }
                total += Double.parseDouble(charge.getAmmount());
                if (charge.getState() == 2) {
                    abonado += Double.parseDouble(charge.getAmmount());
                } else {
                    leftToPay += Double.parseDouble(charge.getAmmount());
                }
            }
            chargeDTO.setTotal(currency, total);
            chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
            chargeDTO.setLeftToPay(currency, leftToPay);
            chargeDTO.setAbonado(currency, abonado);
            chargeDTO.setPaymentAmmount(morosidad + "");
            chargeDTO.setDefaulterDays(defaultersDay);
            chargeDTO.setCategory(this.getCategory(chargeDTO.getType(), customChargeTypes));
            if (chargeDTO.getType() == 6 && chargeDTO.getId() != null) {
                WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
                if (wc != null) {
                    chargeDTO.setWaterConsumption(wc);
                }
            }
        } else {
            chargeDTO.setTotal(currency, Double.parseDouble(chargeDTO.getAmmount()));
        }
        return chargeDTO;
    }

//    private ChargeDTO formatCharge(String currency, ChargeDTO chargeDTO) {
//        if (chargeDTO.getSubcharge() != null) {
//            chargeDTO.setTotal(currency, Double.parseDouble(chargeDTO.getAmmount()) + Double.parseDouble(chargeDTO.getSubcharge()));
//        } else {
//            chargeDTO.setSubcharge("0");
//            chargeDTO.setTotal(currency, Double.parseDouble(chargeDTO.getAmmount()));
//        }
//        if (chargeDTO.getConsecutive() != null) {
//            chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
//        }
//
//        if (chargeDTO.getSplited() != null) {
//            ChargeDTO c = formatSplittedCharge(currency, chargeDTO);
//            c.setConsecutive(chargeDTO.getConsecutive());
//            if (chargeDTO.getConsecutive() != null) {
//                c.setBillNumber(c.formatBillNumber(chargeDTO.getConsecutive()));
//            }
//            c.setLeftToPay(currency, c.getTotal() - c.getAbonado());
//            c.setId(chargeDTO.getId());
//            return c;
//        }
//        chargeDTO.setAbonado(currency, 0);
//        chargeDTO.setLeftToPay(currency, chargeDTO.getTotal());
//        if (chargeDTO.getType() == 6 && chargeDTO.getId() != null) {
//            WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
//            if (wc != null) {
//                chargeDTO.setWaterConsumption(wc.getConsumption());
//            }
//        }
//        return chargeDTO;
//    }

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

    public ChargesToPayReportDTO findChargesToPay(ZonedDateTime finalDate, int type, Long companyId, Long houseId) {
        ZonedDateTime zd_finalTime = finalDate.withHour(23).withMinute(59).withSecond(59);
        log.debug("Request to get all Charges");
        ChargesToPayReportDTO chargesReport = new ChargesToPayReportDTO();
        List<DueHouseDTO> finalHouses = new ArrayList<>();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);

        if (houseId == -1) {
            List<HouseDTO> houses = this.houseService.findAll(companyId).getContent();
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
                        chargeDTO = formatCharge(currency, chargeDTO, customChargeTypes);
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
        } else {
            HouseHistoricalReportDefaulterDTO houseDTO = this.houseService.findOneCleanReport(houseId);
            List<ChargeDTO> chargesPerHouse;
            if (type == 10) {
                chargesPerHouse = new PageImpl<>(chargeRepository.findByHouseBetweenDates(zd_finalTime, 1, 0, houseDTO.getId())).map(chargeMapper::toDto).getContent();
            } else {
                chargesPerHouse = new PageImpl<>(chargeRepository.findByHouseBetweenDatesAndType(zd_finalTime, 1, type, 0, houseDTO.getId())).map(chargeMapper::toDto).getContent();
            }
            if (chargesPerHouse.size() > 0) {
                DueHouseDTO dueHouse = new DueHouseDTO();
                chargesPerHouse.forEach(chargeDTO -> {
                    chargeDTO = formatCharge(currency, chargeDTO, customChargeTypes);
                    dueHouse.setTotalDue(currency, dueHouse.getTotalDue() + chargeDTO.getLeftToPay());
                });
                HouseDTO h = new HouseDTO();
                h.setId(houseDTO.getId());
                h.setHousenumber(houseDTO.getHousenumber());
                dueHouse.setHouseDTO(h);
                dueHouse.setResponsable(null);
                dueHouse.setDues(chargesPerHouse);
                chargesReport.setTotalDue(currency, chargesReport.getTotalDue() + dueHouse.getTotalDue());
                chargesReport.setTotalDueHouses(chargesReport.getTotalDueHouses() + 1);
                finalHouses.add(dueHouse);
            }
        }
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
        double totalOther = 0;
        List<ChargeDTO> finalList = new ArrayList<>();
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
            ChargeDTO chargeDTO = charges.get(i);
//            chargeDTO = formatCharge(currency, charges.get(i));
            chargeDTO.setDownloading(false);
//            chargeDTO.setHouseNumber(this.houseService.getHouseNumberById(chargeDTO.getHouseId()));
            if (chargeDTO.getType() == 6) {
                WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
                if (wc != null) {
                    chargeDTO.setWaterConsumption(wc);
                }
            }
            if (finalList.stream().filter(o -> o.getConsecutive().equals(chargeDTO.getConsecutive()) && o.getHouseId() == chargeDTO.getHouseId()).count() == 0) {
                double total = charges.stream().filter(o -> o.getConsecutive().equals(chargeDTO.getConsecutive()) && o.getHouseId() == chargeDTO.getHouseId()).mapToDouble(o -> Double.parseDouble(o.getAmmount())).sum();
                chargeDTO.setAmmount(total + "");
                chargeDTO.setTotal(currency, total);
                chargeDTO.setBillNumber(chargeDTO.formatBillNumber(chargeDTO.getConsecutive()));
                if (chargeDTO.getType() < 8) {
                    switch (chargeDTO.getType()) {
                        case 1:
                            totalMaint = totalMaint + total;
                            break;
                        case 2:
                            totalExtra = totalExtra + total;
                            break;
                        case 3:
                            totalAreas = totalAreas + total;
                            break;
                        case 5:
                            totalMultas = totalMultas + total;
                            break;
                        case 6:
                            totalWaterCharge = totalWaterCharge + total;
                            break;
                        default:
                    }
                } else {
                    totalOther = totalOther + total;
                }
                finalList.add(chargeDTO);
            }
        }

        billingReportDTO.setTotal(totalMaint + totalAreas + totalExtra + totalMultas + totalWaterCharge + totalOther);
        billingReportDTO.setTotalMaintenance(totalMaint);
        billingReportDTO.setTotalExtraordinary(totalExtra);
        billingReportDTO.setTotalCommonArea(totalAreas);
        billingReportDTO.setTotalMulta(totalMultas);
        billingReportDTO.setTotalWaterCharge(totalWaterCharge);
        billingReportDTO.setTotalOtherCharge(totalOther);
        billingReportDTO.setCharges(finalList);
        return billingReportDTO;
    }

    public Page<ChargeDTO> findAccountStatusCharges(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId, String houseId, String category) {
        ZonedDateTime zd_initialTime = initialDate.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalDate.withHour(23).withMinute(59).withSecond(59);
        List<ChargeDTO> finalList = new ArrayList<>();
        BillingReportDTO billingReportDTO = new BillingReportDTO();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
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
            chargeDTO = formatCharge(currency, charges.get(i), customChargeTypes);
            chargeDTO.setDownloading(false);
            if (chargeDTO.getType() == 6) {
                WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeId(chargeDTO.getId());
                if (wc != null) {
                    chargeDTO.setWaterConsumption(wc);
                }
            }
            double total = charges.stream().filter(o -> o.getConsecutive().equals(chargeDTO.getConsecutive())).mapToDouble(o -> Double.parseDouble(o.getAmmount() != null ? o.getAmmount() : o.getTotal() + "")).sum();
            chargeDTO.setAmmount(total + "");
            if (finalList.stream().filter(o -> o.getConsecutive().equals(chargeDTO.getConsecutive())).count() == 0) {
                finalList.add(chargeDTO);
            }

        }

        return new PageImpl<>(finalList);
    }

    public HistoricalDefaultersReportDTO findHistoricalReportDefaulters(ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId, int chargeType, Long houseId) {
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(companyId);
        String currency = companyConfigurationService.getByCompanyId(null, administrationConfigurationDTO.getCompanyId()).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        List<HouseHistoricalReportDefaulterDTO> defaulterHouses = new ArrayList<>();
        HistoricalDefaultersReportDTO report = new HistoricalDefaultersReportDTO();
        if (houseId == -1) {
            List<HouseHistoricalReportDefaulterDTO> houses = this.houseService.findAllClean(companyId);
            for (int i = 0; i < houses.size(); i++) {
                HouseHistoricalReportDefaulterDTO house = houses.get(i);
                List<ChargeDTO> chargeDTOS = new ArrayList<>();
                if (chargeType == -1) {
                    chargeDTOS = chargeRepository.findAllBetweenMorosidadDatesAndHouseId(zd_finalTime, house.getId(), 0).stream()
                        .map(chargeMapper::toDto)
                        .collect(Collectors.toCollection(LinkedList::new));
                } else {
                    chargeDTOS = chargeRepository.findAllBetweenDatesMorosidadAndHouseIdAndType(zd_finalTime, house.getId(), 0, chargeType).stream()
                        .map(chargeMapper::toDto)
                        .collect(Collectors.toCollection(LinkedList::new));
                }
                int daysTobeDefaulter = administrationConfigurationDTO.getDaysTobeDefaulter();
                List<ChargeDTO> allCharges = this.formatChargesHistorical(zd_finalTime, daysTobeDefaulter, currency, new PageImpl<ChargeDTO>(chargeDTOS), customChargeTypes).getContent();
                List<ChargeDTO> defaulterCharges = new ArrayList<>();
                for (int c = 0; c < allCharges.size(); c++) {
                    ChargeDTO charge = allCharges.get(c);
                    ZonedDateTime fechaCobro = charge.getDate();
                    ZonedDateTime fechaPago = charge.getPaymentDate();
                    if (Double.parseDouble(charge.getPaymentAmmount()) > 0) {
                        if (fechaPago == null) {
                            int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), ZonedDateTime.now().toLocalDate()));
                            charge.setSubcharge((Math.abs(diffBetweenCobroYPago - daysTobeDefaulter)) + "");
                            defaulterCharges.add(charge);
                            house.setTotalDue(currency, house.getTotalDue() + Double.parseDouble(charge.getPaymentAmmount()));
                        } else {
                            int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), fechaPago.toLocalDate()));
                            if (fechaPago.isAfter(zd_finalTime)) {
                                charge.setSubcharge((Math.abs(diffBetweenCobroYPago - daysTobeDefaulter)) + "");
                                defaulterCharges.add(charge);
                                house.setTotalDue(currency, house.getTotalDue() + Double.parseDouble(charge.getPaymentAmmount()));
                            }
                        }
                    }
                }
                if (defaulterCharges.size() > 0) {
                    house.setCharges(defaulterCharges);
                    defaulterHouses.add(house);
                    report.setTotalDue(currency, report.getTotalDue() + house.getTotalDue());
                    report.setTotalDueHouses(report.getTotalDueHouses() + 1);
                }
            }
        } else {
            HouseHistoricalReportDefaulterDTO house = this.houseService.findOneCleanReport(houseId);
            List<ChargeDTO> chargeDTOS = new ArrayList<>();
            if (chargeType == -1) {
                chargeDTOS = chargeRepository.findAllBetweenMorosidadDatesAndHouseId(zd_finalTime, house.getId(), 0).stream()
                    .map(chargeMapper::toDto)
                    .collect(Collectors.toCollection(LinkedList::new));
            } else {
                chargeDTOS = chargeRepository.findAllBetweenDatesMorosidadAndHouseIdAndType(zd_finalTime, house.getId(), 0, chargeType).stream()
                    .map(chargeMapper::toDto)
                    .collect(Collectors.toCollection(LinkedList::new));
            }
            int daysTobeDefaulter = administrationConfigurationDTO.getDaysTobeDefaulter();
            List<ChargeDTO> allCharges = this.formatChargesHistorical(zd_finalTime, daysTobeDefaulter, currency, new PageImpl<ChargeDTO>(chargeDTOS), customChargeTypes).getContent();
            List<ChargeDTO> defaulterCharges = new ArrayList<>();
            for (int c = 0; c < allCharges.size(); c++) {
                ChargeDTO charge = allCharges.get(c);
                ZonedDateTime fechaCobro = charge.getDate();
                ZonedDateTime fechaPago = charge.getPaymentDate();
//                if (Double.parseDouble(charge.getPaymentAmmount()) > 0) {
                if (fechaPago == null) {
                    int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), ZonedDateTime.now().toLocalDate()));
                    charge.setSubcharge((Math.abs(diffBetweenCobroYPago - daysTobeDefaulter)) + "");
                    charge.setLeftToPay(currency, Double.parseDouble(charge.getPaymentAmmount()));
                    defaulterCharges.add(charge);
                    house.setTotalDue(currency, house.getTotalDue() + Double.parseDouble(charge.getPaymentAmmount()));
                } else {
                    int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), fechaPago.toLocalDate()));
                    if (fechaPago.isAfter(zd_finalTime)) {
                        charge.setSubcharge((Math.abs(diffBetweenCobroYPago - daysTobeDefaulter)) + "");
                        defaulterCharges.add(charge);
                        house.setTotalDue(currency, house.getTotalDue() + Double.parseDouble(charge.getPaymentAmmount()));
                    }
                }
//                }
            }
            if (defaulterCharges.size() > 0) {
                house.setCharges(defaulterCharges);
                defaulterHouses.add(house);
                report.setTotalDue(currency, report.getTotalDue() + house.getTotalDue());
                report.setTotalDueHouses(report.getTotalDueHouses() + 1);
            }
        }
        report.setDueHouses(defaulterHouses);
        return report;
    }

    public HistoricalDefaultersReportDTO findHistoricalReportDefaultersAcumulative(ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId, int chargeType) {
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(companyId);
        List<HouseHistoricalReportDefaulterDTO> houses = this.houseService.findAllClean(companyId);
        String currency = companyConfigurationService.getByCompanyId(null, administrationConfigurationDTO.getCompanyId()).getContent().get(0).getCurrency();
        List<HouseHistoricalReportDefaulterDTO> defaulterHouses = new ArrayList<>();
        HistoricalDefaultersReportDTO report = new HistoricalDefaultersReportDTO();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        for (int i = 0; i < houses.size(); i++) {
            HouseHistoricalReportDefaulterDTO house = houses.get(i);
            List<ChargeDTO> chargeDTOS = new ArrayList<>();
            if (chargeType == -1) {
                chargeDTOS = chargeRepository.findAllBetweenMorosidadDatesAndHouseId(zd_finalTime, house.getId(), 0).stream()
                    .map(chargeMapper::toDto)
                    .collect(Collectors.toCollection(LinkedList::new));
            } else {
                chargeDTOS = chargeRepository.findAllBetweenDatesMorosidadAndHouseIdAndType(zd_finalTime, house.getId(), 0, chargeType).stream()
                    .map(chargeMapper::toDto)
                    .collect(Collectors.toCollection(LinkedList::new));
            }

            int daysTobeDefaulter = administrationConfigurationDTO.getDaysTobeDefaulter();
            List<ChargeDTO> allCharges = this.formatChargesHistorical(finalTime, daysTobeDefaulter, currency, new PageImpl<ChargeDTO>(chargeDTOS), customChargeTypes).getContent();
            List<ChargeDTO> defaulterCharges = new ArrayList<>();
            for (int c = 0; c < allCharges.size(); c++) {
                ChargeDTO charge = allCharges.get(c);
                ZonedDateTime fechaCobro = charge.getDate();
                ZonedDateTime fechaPago = charge.getPaymentDate();
                if (fechaPago == null) {
                    int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), ZonedDateTime.now().toLocalDate()));
                    charge.setSubcharge((Math.abs(diffBetweenCobroYPago - daysTobeDefaulter)) + "");
                    defaulterCharges.add(charge);
                    house.setTotalDue(currency, house.getTotalDue() + Double.parseDouble(charge.getPaymentAmmount()));
                } else {
                    int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), fechaPago.toLocalDate()));
                    if (fechaPago.isAfter(finalTime)) {
                        charge.setSubcharge((Math.abs(diffBetweenCobroYPago - daysTobeDefaulter)) + "");
                        defaulterCharges.add(charge);
                        house.setTotalDue(currency, house.getTotalDue() + Double.parseDouble(charge.getPaymentAmmount()));
                    }
                }
            }
            if (defaulterCharges.size() > 0) {
                house.setCharges(defaulterCharges);
                defaulterHouses.add(house);
                report.setTotalDue(currency, report.getTotalDue() + house.getTotalDue());
                report.setTotalDueHouses(report.getTotalDueHouses() + 1);
            }
        }
        report.setDueHouses(defaulterHouses);
        return report;
    }


    public HistoricalReportPositiveBalanceDTO findHistoricalReportPositiveBalance(ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId, Long houseId) {
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(companyId);
        String currency = companyConfigurationService.getByCompanyId(null, administrationConfigurationDTO.getCompanyId()).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        List<HouseHistoricalReportDefaulterDTO> positiveHouses = new ArrayList<>();
        HistoricalReportPositiveBalanceDTO report = new HistoricalReportPositiveBalanceDTO();
        List<ChargeDTO> chargeDTOS = new ArrayList<>();
        if (houseId != -1) {
//            chargeDTOS = chargeRepository.findAllUntilPaymentDatesAndHouseId(zd_finalTime, houseId, 0).stream()
//                .map(chargeMapper::toDto)
//                .collect(Collectors.toCollection(LinkedList::new));
//            List<ChargeDTO> allCharges = this.formatCharges(currency, new PageImpl<ChargeDTO>(chargeDTOS)).getContent();
            List<PaymentDTO> payments = this.paymentService.findAdelantosUntilDatesAndHouseId(zd_finalTime, houseId);
            HouseHistoricalReportDefaulterDTO house = this.houseService.findOneCleanReport(houseId);
            List<ChargeDTO> positiveCharges = new ArrayList<>();
//            for (int c = 0; c < allCharges.size(); c++) {
//                ChargeDTO charge = allCharges.get(c);
//                ZonedDateTime fechaCobro = charge.getDate();
//                ZonedDateTime fechaPago = charge.getPaymentDate();
//                int diasAdelanto = 0;
//                if (fechaPago != null) {
//                    if (fechaPago.isBefore(fechaCobro)) {
//                        if (fechaPago.isAfter(initialTime)) {
//                            int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), fechaPago.toLocalDate()));
//                            charge.setDefaulterDays((Math.abs(diffBetweenCobroYPago)));
//                            positiveCharges.add(charge);
//                            house.setTotalDue(currency, house.getTotalDue() + charge.getAbonado());
//                        }
//                    }
//                }
//            }
            for (int p = 0; p < payments.size(); p++) {
                PaymentDTO payment = this.paymentService.findOneCompleteOld(payments.get(p).getId(), currency,customChargeTypes);
                if (payment.getAmmountLeft() != null) {
                    if (Double.parseDouble(payment.getAmmountLeft()) > 0) {
                        ChargeDTO c = new ChargeDTO();
                        c.setConsecutive(null);
                        c.setConcept("Saldo a favor");
                        c.setTotal(currency, Double.parseDouble(payment.getAmmountLeft()));
                        positiveCharges.add(c);
                        c.setType(9);
                        c.setPaymentDate(payment.getDate());
                        c.setAbonado(currency, Double.parseDouble(payment.getAmmountLeft()));
                        house.setTotalDue(currency, house.getTotalDue() + c.getTotal());
                    }
                }
                for (ChargeDTO charge : payment.getChargesOld()) {
                    ZonedDateTime fechaCobro = charge.getDate();
                    ZonedDateTime fechaPago = charge.getPaymentDate();
                    if (fechaCobro.isAfter(finalTime)) {
                        int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), fechaPago.toLocalDate()));
                        charge.setDefaulterDays((Math.abs(diffBetweenCobroYPago)));
                        ChargeDTO c = new ChargeDTO();
                        c.setConsecutive(null);
                        c.setConcept("Saldo a favor");
                        c.setTotal(currency, Double.parseDouble(charge.getAmmount()));
                        positiveCharges.add(c);
                        c.setType(9);
                        c.setPaymentDate(payment.getDate());
                        c.setAbonado(currency, Double.parseDouble(charge.getAmmount()));
                        house.setTotalDue(currency, house.getTotalDue() + c.getTotal());
                    }
                }
            }
            if (positiveCharges.size() > 0) {
                house.setCharges(positiveCharges);
                positiveHouses.add(house);
                report.setTotalDue(currency, report.getTotalDue() + house.getTotalDue());
                report.setTotalDueHouses(report.getTotalDueHouses() + 1);
            }
        } else {
            List<HouseHistoricalReportDefaulterDTO> houses = this.houseService.findAllClean(companyId);
            for (int i = 0; i < houses.size(); i++) {
                HouseHistoricalReportDefaulterDTO house = houses.get(i);
                List<PaymentDTO> payments = this.paymentService.findAdelantosUntilDatesAndHouseId(zd_finalTime, house.getId());
                List<ChargeDTO> positiveCharges = new ArrayList<>();
                for (int p = 0; p < payments.size(); p++) {
                    PaymentDTO payment = this.paymentService.findOneCompleteOld(payments.get(p).getId(), currency,customChargeTypes);
                    if (payment.getAmmountLeft() != null) {
                        if (Double.parseDouble(payment.getAmmountLeft()) > 0) {
                            ChargeDTO c = new ChargeDTO();
                            c.setConsecutive(null);
                            c.setConcept("Saldo a favor");
                            c.setTotal(currency, Double.parseDouble(payment.getAmmountLeft()));
                            positiveCharges.add(c);
                            c.setType(9);
                            c.setPaymentDate(payment.getDate());
                            c.setAbonado(currency, Double.parseDouble(payment.getAmmountLeft()));
                            house.setTotalDue(currency, house.getTotalDue() + c.getTotal());
                        }
                    }
                    for (ChargeDTO charge : payment.getChargesOld()) {
                        ZonedDateTime fechaCobro = charge.getDate();
                        ZonedDateTime fechaPago = charge.getPaymentDate();
                        if (fechaCobro.isAfter(finalTime)) {
                            int diffBetweenCobroYPago = toIntExact(ChronoUnit.DAYS.between(fechaCobro.toLocalDate(), fechaPago.toLocalDate()));
                            charge.setDefaulterDays((Math.abs(diffBetweenCobroYPago)));
                            ChargeDTO c = new ChargeDTO();
                            c.setConsecutive(null);
                            c.setConcept("Saldo a favor");
                            c.setTotal(currency, Double.parseDouble(charge.getAmmount()));
                            positiveCharges.add(c);
                            c.setType(9);
                            c.setPaymentDate(payment.getDate());
                            c.setAbonado(currency, Double.parseDouble(charge.getAmmount()));
                            house.setTotalDue(currency, house.getTotalDue() + c.getTotal());
                        }
                    }
                }
                if (positiveCharges.size() > 0) {
                    house.setCharges(positiveCharges);
                    positiveHouses.add(house);
                    report.setTotalDue(currency, report.getTotalDue() + house.getTotalDue());
                    report.setTotalDueHouses(report.getTotalDueHouses() + 1);
                }
            }
        }
        report.setDueHouses(positiveHouses);
        return report;
    }

    @Transactional(readOnly = true)
    public List<ChargeDTO> findAllByCompany(Long companyId) {
        log.debug("Request to get all Charges");
        List<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByCompanyId(companyId)).map(chargeMapper::toDto).getContent();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        return formatChargesOld(currency, new PageImpl<>(chargeDTOS), customChargeTypes).getContent();
    }
    @Transactional(readOnly = true)
    public List<ChargeDTO> findAllByCompanyAll(Long companyId) {
        log.debug("Request to get all Charges");
        List<ChargeDTO> chargeDTOS = new PageImpl<>(chargeRepository.findByCompanyId(companyId)).map(chargeMapper::toDto).getContent();
        return chargeDTOS;
    }
}
