package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.HistoricalDefaulter;
import com.lighthouse.aditum.repository.HistoricalDefaulterRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.HistoricalDefaulterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

/**
 * Service Implementation for managing HistoricalDefaulter.
 */
@Service
@Transactional
public class HistoricalDefaulterService {

    private final Logger log = LoggerFactory.getLogger(HistoricalDefaulterService.class);

    private final HistoricalDefaulterRepository historicalDefaulterRepository;

    private final HistoricalDefaulterMapper historicalDefaulterMapper;

    private final ChargeService chargeService;

    private final HistoricalDefaulterChargeService historicalDefaulterChargeService;

    private final AdministrationConfigurationService administrationConfigurationService;

    private final CompanyConfigurationService companyConfigurationService;

    private final CustomChargeTypeService customChargeTypeService;

    private final HouseService houseService;

    private final HistoricalPositiveService historicalPositiveService;

    public HistoricalDefaulterService(HistoricalPositiveService historicalPositiveService, HouseService houseService,CustomChargeTypeService customChargeTypeService, CompanyConfigurationService companyConfigurationService, AdministrationConfigurationService administrationConfigurationService, HistoricalDefaulterChargeService historicalDefaulterChargeService, ChargeService chargeService, HistoricalDefaulterRepository historicalDefaulterRepository, HistoricalDefaulterMapper historicalDefaulterMapper) {
        this.historicalDefaulterRepository = historicalDefaulterRepository;
        this.historicalDefaulterMapper = historicalDefaulterMapper;
        this.chargeService = chargeService;
        this.historicalDefaulterChargeService = historicalDefaulterChargeService;
        this.companyConfigurationService = companyConfigurationService;
        this.administrationConfigurationService = administrationConfigurationService;
        this.customChargeTypeService = customChargeTypeService;
        this.houseService = houseService;
        this.historicalPositiveService = historicalPositiveService;
    }

    /**
     * Save a historicalDefaulter.
     *
     * @param historicalDefaulterDTO the entity to save
     * @return the persisted entity
     */
    public HistoricalDefaulterDTO save(HistoricalDefaulterDTO historicalDefaulterDTO) {
        log.debug("Request to save HistoricalDefaulter : {}", historicalDefaulterDTO);
        HistoricalDefaulter historicalDefaulter = historicalDefaulterMapper.toEntity(historicalDefaulterDTO);
        historicalDefaulter = historicalDefaulterRepository.save(historicalDefaulter);
        return historicalDefaulterMapper.toDto(historicalDefaulter);
    }

    /**
     * Get all the historicalDefaulters.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<HistoricalDefaulterDTO> findAll() {
        log.debug("Request to get all HistoricalDefaulters");
        return historicalDefaulterRepository.findAll().stream()
            .map(historicalDefaulterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<HistoricalDefaulterDTO> findAllByCompanyIdAndDate(Long id, ZonedDateTime date) {
        log.debug("Request to get all HistoricalDefaulters");
        return historicalDefaulterRepository.findAllByCompanyIdAndDate(id, date).stream()
            .map(historicalDefaulterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public HistoricalDefaulterDTO findAllByHouseIdAndDate(Long id, ZonedDateTime date) {
        log.debug("Request to get all HistoricalDefaulters");
        HistoricalDefaulter historicalDefaulter = historicalDefaulterRepository.findAllByHouseIdAndDate(id, date);
        return historicalDefaulterMapper.toDto(historicalDefaulter);
    }
    /**
     * Get one historicalDefaulter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HistoricalDefaulterDTO findOne(Long id) {
        log.debug("Request to get HistoricalDefaulter : {}", id);
        HistoricalDefaulter historicalDefaulter = historicalDefaulterRepository.findOne(id);
        return historicalDefaulterMapper.toDto(historicalDefaulter);
    }
    @Transactional(readOnly = true)
    public HistoricalDefaulterDTO findOneMonth(Long id,ZonedDateTime month) {
        log.debug("Request to get HistoricalDefaulter : {}", id);
        HistoricalDefaulter historicalDefaulter = historicalDefaulterRepository.findOne(id);
        return historicalDefaulterMapper.toDto(historicalDefaulter);
    }
    /**
     * Delete the historicalDefaulter by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HistoricalDefaulter : {}", id);
        historicalDefaulterRepository.delete(id);
    }

    public void formatResetHouse(Long houseId,ZonedDateTime month,List<CustomChargeTypeDTO> custom) {
        log.debug("Request to get all HistoricalDefaulters");
        month = month.withMinute(1).withSecond(0).withNano(0);
        HistoricalDefaulterDTO hd = this.findAllByHouseIdAndDate(houseId,month);
        if(hd!=null){
            List<HistoricalDefaulterChargeDTO> historicalCharge = this.historicalDefaulterChargeService.findAllbyHistoricalDefaulter(hd.getId());
            for (int i = 0; i < historicalCharge.size(); i++) {
                this.historicalDefaulterChargeService.delete(historicalCharge.get(i).getId());
            }
            this.delete(hd.getId());
        }
        HistoricalPositiveDTO hp = this.historicalPositiveService.findAllByHouseIdAndDate(houseId,month);
        if(hp!=null){
            this.historicalPositiveService.delete(hp.getId());
        }
        HouseDTO h = this.houseService.findOne(houseId);

        if(Double.parseDouble(h.getBalance().getTotal())>0){
            HistoricalPositiveDTO nhp = new HistoricalPositiveDTO(null, h.getBalance().getTotal(), h.getHousenumber(),month, h.getCompanyId(),houseId);
            this.historicalPositiveService.save(nhp);
        }else{
           HistoricalDefaulterDTO nhd = new HistoricalDefaulterDTO(null, h.getBalance().getTotal(), month, "", h.getHousenumber(), h.getCompanyId(),houseId);
            HistoricalDefaulterDTO historicalDefaulterSaved = this.save(nhd);
            List<ChargeDTO> charges = this.chargeService.findAllByHouse(houseId,custom).getContent();
            LocalDate date = month.toLocalDate(); //but a LocalDate is enough
            LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
            ZonedDateTime lastDay = endOfMonth.atStartOfDay(ZoneOffset.UTC).withMinute(59).withHour(23).withSecond(59).withMonth(1);
            String categories = "";
            for (int k = 0; k < charges.size(); k++) {
                ChargeDTO c = charges.get(k);
                if(c.getDate().isBefore(lastDay)){
                    HistoricalDefaulterChargeDTO hc = new HistoricalDefaulterChargeDTO(
                        null,
                        c.getType(),
                        c.getDate(),
                        c.getConcept(),
                        c.getConsecutive() + "",
                        c.getTotal()+"",
                        c.getPaymentAmmount(),
                        c.getAbonado() + "",
                        c.getPaymentAmmount(),
                        historicalDefaulterSaved.getId(),
                        c.getId()+""
                    );
                    categories = categories + c.getType() + ",";
                    this.historicalDefaulterChargeService.save(hc);
                }
            }
            historicalDefaulterSaved.setCategories(categories);
            this.save(historicalDefaulterSaved);
        }
    }
    public List<HistoricalDefaulterDTO> filterHouses(List<HistoricalDefaulterDTO> houses, int chargeType) {
        List<HistoricalDefaulterDTO> housesFinal = new ArrayList<>();
        for (int i = 0; i < houses.size(); i++) {
            HistoricalDefaulterDTO house = houses.get(i);
            String categories[] = house.getCategories().split(",");
            for (int j = 0; j < categories.length; j++) {
                if (categories[j].equals(chargeType + "")) {
                    housesFinal.add(house);
                }
            }
        }
        return housesFinal;
    }

    public HistoricalDefaulterReportDTO findHistoricalReportDefaulters(ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId, int chargeType, Long houseId) {
        ZonedDateTime date = initialTime.withMinute(1).withHour(0).withSecond(0).withNano(0);
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(companyId);
        String currency = companyConfigurationService.getByCompanyId(null, administrationConfigurationDTO.getCompanyId()).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        List<HistoricalDefaulterDTO> defaulterHouses = new ArrayList<>();
        HistoricalDefaulterReportDTO report = new HistoricalDefaulterReportDTO();
        List<HistoricalDefaulterDTO> housesFinal = new ArrayList<>();
        if (houseId == -1) {
            List<HistoricalDefaulterDTO> houses = this.findAllByCompanyIdAndDate(companyId, date);
            if (chargeType == -1) {
                housesFinal = houses;
            } else {
                housesFinal = this.filterHouses(houses, chargeType);
            }
            for (int i = 0; i < housesFinal.size(); i++) {
                HistoricalDefaulterDTO house = housesFinal.get(i);
                List<HistoricalDefaulterChargeDTO> chargeDTOS = this.historicalDefaulterChargeService.findAllbyHistoricalDefaulter(house.getId());
                List<HistoricalDefaulterChargeDTO> chargeDTOSFinal = new ArrayList<>();

                for (int j = 0; j < chargeDTOS.size(); j++) {
                    chargeDTOS.get(j).setCategory(this.chargeService.getCategory(chargeDTOS.get(j).getType(), customChargeTypes));
                    chargeDTOS.get(j).setBillNumber(Integer.parseInt(chargeDTOS.get(j).getConsecutive()));
                }
                house.setCharges(chargeDTOS);
                defaulterHouses.add(house);
                report.setTotalDue(currency, report.getTotalDue() + Double.parseDouble(house.getTotal()));
                report.setTotalDueHouses(report.getTotalDueHouses() + 1);
            }
        } else {
            HistoricalDefaulterDTO house = this.findAllByHouseIdAndDate(houseId,date);
            if(house!=null){
                List<HistoricalDefaulterChargeDTO> chargeDTOS = this.historicalDefaulterChargeService.findAllbyHistoricalDefaulter(house.getId());
                for (int j = 0; j < chargeDTOS.size(); j++) {
                    chargeDTOS.get(j).setCategory(this.chargeService.getCategory(chargeDTOS.get(j).getType(), customChargeTypes));
                    chargeDTOS.get(j).setBillNumber(Integer.parseInt(chargeDTOS.get(j).getConsecutive()));
                }
                house.setCharges(chargeDTOS);
                defaulterHouses.add(house);
                report.setTotalDue(currency, report.getTotalDue() + Double.parseDouble(house.getTotal()));
                report.setTotalDueHouses(report.getTotalDueHouses() + 1);
            }
        }
        report.setDueHouses(defaulterHouses);
        return report;
    }


    public void formatHistorialDefaulterReportCompany(Long companyId) {
        ZonedDateTime firstDay = ZonedDateTime.now().withMinute(0).withSecond(1).withDayOfMonth(1).withMonth(1).withHour(0).withNano(0);
        ZonedDateTime zdt = ZonedDateTime.now(); //let's start with a ZonedDateTime if that's what you have
        LocalDate date = zdt.toLocalDate(); //but a LocalDate is enough
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        ZonedDateTime lastDay = endOfMonth.atStartOfDay(ZoneOffset.UTC).withMinute(59).withHour(23).withSecond(59).withMonth(1);
        for (int i = 1; i <= ZonedDateTime.now().getMonthValue(); i++) {
            ZonedDateTime firstDayMonth = firstDay.withMonth(i);
            ZonedDateTime lasDayMonth = lastDay.withMonth(i);
            HistoricalDefaultersReportDTO report = this.chargeService.findHistoricalReportDefaulters(firstDayMonth, lasDayMonth, companyId, -1, Long.parseLong("-1"));
            for (int j = 0; j < report.getDueHouses().size(); j++) {
                HouseHistoricalReportDefaulterDTO house = report.getDueHouses().get(j);
                String categories = "";
                if (house.getTotalDue() > 0) {
                    HistoricalDefaulterDTO historicalDefaulter = new HistoricalDefaulterDTO(null, house.getTotalDue() + "", firstDayMonth.withMinute(1).withSecond(0), categories, house.getHousenumber(), companyId, house.getId());
                    HistoricalDefaulterDTO historicalDefaulterSaved = this.save(historicalDefaulter);
                    for (int k = 0; k < house.getCharges().size(); k++) {
                        ChargeDTO c = house.getCharges().get(k);
                        HistoricalDefaulterChargeDTO hc = new HistoricalDefaulterChargeDTO(
                            null,
                            c.getType(),
                            c.getDate(),
                            c.getConcept(),
                            c.getConsecutive() + "",
                            c.getTotal()+"",
                            c.getPaymentAmmount(),
                            c.getAbonado() + "",
                            c.getPaymentAmmount(),
                            historicalDefaulterSaved.getId(),
                            c.getId()+""
                        );
                        categories = categories + c.getType() + ",";
                        this.historicalDefaulterChargeService.save(hc);
                    }
                    historicalDefaulterSaved.setCategories(categories);
                    this.save(historicalDefaulterSaved);
                }
            }
        }
        log.debug("LISTOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }
}
