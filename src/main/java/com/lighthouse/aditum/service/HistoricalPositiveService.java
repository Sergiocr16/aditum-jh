package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.HistoricalPositive;
import com.lighthouse.aditum.repository.HistoricalPositiveRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.HistoricalPositiveMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Service Implementation for managing HistoricalPositive.
 */
@Service
@Transactional
public class HistoricalPositiveService {

    private final Logger log = LoggerFactory.getLogger(HistoricalPositiveService.class);

    private final HistoricalPositiveRepository historicalPositiveRepository;

    private final HistoricalPositiveMapper historicalPositiveMapper;

    private final ChargeService chargeService;

    private final AdministrationConfigurationService administrationConfigurationService;

    private final CompanyConfigurationService companyConfigurationService;

    public HistoricalPositiveService(AdministrationConfigurationService administrationConfigurationService,CompanyConfigurationService companyConfigurationService,ChargeService chargeService, HistoricalPositiveRepository historicalPositiveRepository, HistoricalPositiveMapper historicalPositiveMapper) {
        this.historicalPositiveRepository = historicalPositiveRepository;
        this.historicalPositiveMapper = historicalPositiveMapper;
        this.chargeService = chargeService;
        this.administrationConfigurationService = administrationConfigurationService;
        this.companyConfigurationService = companyConfigurationService;
    }

    /**
     * Save a historicalPositive.
     *
     * @param historicalPositiveDTO the entity to save
     * @return the persisted entity
     */
    public HistoricalPositiveDTO save(HistoricalPositiveDTO historicalPositiveDTO) {
        log.debug("Request to save HistoricalPositive : {}", historicalPositiveDTO);
         HistoricalPositive historicalPositive = historicalPositiveMapper.toEntity(historicalPositiveDTO);
        historicalPositive = historicalPositiveRepository.save(historicalPositive);
        return historicalPositiveMapper.toDto(historicalPositive);
    }

    /**
     * Get all the historicalPositives.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<HistoricalPositiveDTO> findAll() {
        log.debug("Request to get all HistoricalPositives");
        return historicalPositiveRepository.findAll().stream()
            .map(historicalPositiveMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<HistoricalPositiveDTO> findAllByCompanyIdAndDate(Long companyId,ZonedDateTime date) {
        log.debug("Request to get all HistoricalPositives");
        return historicalPositiveRepository.findAllByCompanyIdAndDate(companyId,date).stream()
            .map(historicalPositiveMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public HistoricalPositiveDTO findAllByHouseIdAndDate(Long houseId, ZonedDateTime date) {
        log.debug("Request to get HistoricalPositive : {}", houseId);
        ZonedDateTime lastHour = date.withHour(23);
        ZonedDateTime firstHour = date.withHour(0);
        HistoricalPositive historicalPositive = historicalPositiveRepository.findAllByHouseIdAndDate(houseId,firstHour,lastHour);
        return historicalPositiveMapper.toDto(historicalPositive);
    }
    /**
     * Get one historicalPositive by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HistoricalPositiveDTO findOne(Long id) {
        log.debug("Request to get HistoricalPositive : {}", id);
        HistoricalPositive historicalPositive = historicalPositiveRepository.findOne(id);
        return historicalPositiveMapper.toDto(historicalPositive);
    }

    /**
     * Delete the historicalPositive by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HistoricalPositive : {}", id);
        historicalPositiveRepository.delete(id);
    }

    public void formatHistoricalPositiveReportCompany(Long companyId) {
        ZonedDateTime firstDay = ZonedDateTime.now().withMinute(1).withSecond(0).withDayOfMonth(1).withMonth(1).withHour(0).withNano(0);
        ZonedDateTime zdt = ZonedDateTime.now(); //let's start with a ZonedDateTime if that's what you have
        LocalDate date = zdt.toLocalDate(); //but a LocalDate is enough
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        ZonedDateTime lastDay = endOfMonth.atStartOfDay(ZoneOffset.UTC).withMinute(59).withHour(23).withSecond(59).withMonth(1);
        for (int i = 1; i <= ZonedDateTime.now().getMonthValue(); i++) {
            ZonedDateTime firstDayMonth = firstDay.withMonth(i);
            ZonedDateTime lasDayMonth = lastDay.withMonth(i);
            HistoricalReportPositiveBalanceDTO report = this.chargeService.findHistoricalReportPositiveBalance(firstDayMonth, lasDayMonth, companyId, Long.parseLong("-1"));
            for (int j = 0; j < report.getDueHouses().size(); j++) {
                HouseHistoricalReportDefaulterDTO house = report.getDueHouses().get(j);
                if (house.getTotalDue() > 0) {
                    HistoricalPositiveDTO historicalPositive = new HistoricalPositiveDTO(
                        null,
                        house.getTotalDue() + "",
                        house.getHousenumber(),
                        firstDayMonth,
                        companyId,
                        house.getId()
                    );
                    this.save(historicalPositive);
                }
            }
        }
        log.debug("LISTOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }

    public HistoricalPositiveBalanceReportDTO findHistoricalReportPositiveBalance(ZonedDateTime initial_time, ZonedDateTime final_time, Long companyId, Long houseId) {
        ZonedDateTime date = initial_time.withMinute(1).withHour(0).withSecond(0).withNano(0);
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(companyId);
        String currency = companyConfigurationService.getByCompanyId(null, administrationConfigurationDTO.getCompanyId()).getContent().get(0).getCurrency();
        List<HistoricalPositiveDTO> positiveHouses = new ArrayList<>();
        HistoricalPositiveBalanceReportDTO report = new HistoricalPositiveBalanceReportDTO();
        List<HistoricalPositiveDTO> housesFinal = new ArrayList<>();
        if (houseId == -1) {
            List<HistoricalPositiveDTO> houses = this.findAllByCompanyIdAndDate(companyId, date);
            housesFinal = houses;
            for (int i = 0; i < housesFinal.size(); i++) {
                HistoricalPositiveDTO house = housesFinal.get(i);
                positiveHouses.add(house);
                report.setTotalDue(currency, report.getTotalDue() + Double.parseDouble(house.getTotal()));
                report.setTotalDueHouses(report.getTotalDueHouses() + 1);
            }
        } else {
            HistoricalPositiveDTO house = this.findAllByHouseIdAndDate(houseId,date);
            if(house!=null){
                positiveHouses.add(house);
                report.setTotalDue(currency, report.getTotalDue() + Double.parseDouble(house.getTotal()));
                report.setTotalDueHouses(report.getTotalDueHouses() + 1);
            }
        }
        report.setDueHouses(positiveHouses);
        return report;
    }

}
