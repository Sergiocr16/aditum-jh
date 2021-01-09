package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.HistoricalDefaulter;
import com.lighthouse.aditum.repository.HistoricalDefaulterRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.HistoricalDefaulterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
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

    private final PaymentService paymentService;

    private final PaymentChargeService paymentChargeService;

    public HistoricalDefaulterService(@Lazy PaymentChargeService paymentChargeService, @Lazy PaymentService paymentService, HistoricalPositiveService historicalPositiveService, HouseService houseService, CustomChargeTypeService customChargeTypeService, CompanyConfigurationService companyConfigurationService, AdministrationConfigurationService administrationConfigurationService, HistoricalDefaulterChargeService historicalDefaulterChargeService, ChargeService chargeService, HistoricalDefaulterRepository historicalDefaulterRepository, HistoricalDefaulterMapper historicalDefaulterMapper) {
        this.historicalDefaulterRepository = historicalDefaulterRepository;
        this.historicalDefaulterMapper = historicalDefaulterMapper;
        this.chargeService = chargeService;
        this.historicalDefaulterChargeService = historicalDefaulterChargeService;
        this.companyConfigurationService = companyConfigurationService;
        this.administrationConfigurationService = administrationConfigurationService;
        this.customChargeTypeService = customChargeTypeService;
        this.houseService = houseService;
        this.historicalPositiveService = historicalPositiveService;
        this.paymentService = paymentService;
        this.paymentChargeService = paymentChargeService;
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
        ZonedDateTime lastHour = date.withHour(23);
        ZonedDateTime firstHour = date.withHour(0).withDayOfMonth(1);
        return historicalDefaulterRepository.findAllByCompanyIdAndDate(id, firstHour, lastHour).stream()
            .map(historicalDefaulterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public HistoricalDefaulterDTO findAllByHouseIdAndDate(Long id, ZonedDateTime date) {
        log.debug("Request to get all HistoricalDefaulters");
        ZonedDateTime lastHour = date.withHour(23);
        ZonedDateTime firstHour = date.withHour(0).withDayOfMonth(1).withMinute(0).withSecond(0).withNano(0);
        List<HistoricalDefaulter> historicalDefaulters = historicalDefaulterRepository.findAllByHouseIdAndDate(id, firstHour, lastHour);
        if (historicalDefaulters.size() > 0) {
            HistoricalDefaulter h = historicalDefaulters.get(historicalDefaulters.size() - 1);
            for (int i = 0; i < historicalDefaulters.size() - 1; i++) {
                this.delete(historicalDefaulters.get(i).getId());
            }
            return historicalDefaulterMapper.toDto(h);
        } else {
            return null;
        }
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
    public HistoricalDefaulterDTO findOneMonth(Long id, ZonedDateTime month) {
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

    //    Borra historicos pasados
    public void deleteHistorical(Long houseId, ZonedDateTime month) {
        HistoricalDefaulterDTO hd = this.findAllByHouseIdAndDate(houseId, month);
        if (hd != null) {
            List<HistoricalDefaulterChargeDTO> historicalCharge = this.historicalDefaulterChargeService.findAllbyHistoricalDefaulter(hd.getId());
            for (int i = 0; i < historicalCharge.size(); i++) {
                this.historicalDefaulterChargeService.delete(historicalCharge.get(i).getId());
            }
            this.delete(hd.getId());
        }
        HistoricalPositiveDTO hp = this.historicalPositiveService.findAllByHouseIdAndDate(houseId, month);
        if (hp != null) {
            this.historicalPositiveService.delete(hp.getId());
        }
    }

    //    Formatea los nuevos historicos por filial
    @Async
    public void formatResetHouse(Long houseId, ZonedDateTime month, List<CustomChargeTypeDTO> custom) {

        log.debug("Request to get all HistoricalDefaulters");
        month = month.withMinute(1).withSecond(0).withNano(0).withHour(0).withDayOfMonth(1).withNano(0);
        ZonedDateTime monthToSave = month;
        this.deleteHistorical(houseId, month);
        HouseDTO h = this.houseService.findOne(houseId);
        if (Double.parseDouble(h.getBalance().getTotalFavor()) > 0) {
            HistoricalPositiveDTO nhp = new HistoricalPositiveDTO(null, h.getBalance().getTotalFavor(), h.getHousenumber(), monthToSave, h.getCompanyId(), houseId);
            this.historicalPositiveService.save(nhp);
        }
        if (Double.parseDouble(h.getBalance().getTotal()) < 0) {
            HistoricalDefaulterDTO nhd = new HistoricalDefaulterDTO(null, h.getBalance().getTotal(), monthToSave, "", h.getHousenumber(), h.getCompanyId(), houseId);
            HistoricalDefaulterDTO historicalDefaulterSaved = this.save(nhd);
            List<ChargeDTO> charges = this.chargeService.findAllByHouse(houseId, custom).getContent();
            ZonedDateTime lastDay = month.with(TemporalAdjusters.lastDayOfMonth()).withMinute(59).withHour(23).withSecond(59);
            String categories = "";
            for (int k = 0; k < charges.size(); k++) {
                ChargeDTO c = charges.get(k);
                if (c.getDate().isBefore(lastDay)) {
                    HistoricalDefaulterChargeDTO hc = new HistoricalDefaulterChargeDTO(
                        null,
                        c.getType(),
                        c.getDate(),
                        c.getConcept(),
                        c.getConsecutive() + "",
                        c.getAmmount() + "",
                        c.getLeftToPay() + "",
                        c.getAbonado() + "",
                        c.getLeftToPay() + "",
                        historicalDefaulterSaved.getId(),
                        c.getId() + ""
                    );
                    categories = categories + c.getType() + ",";
                    this.historicalDefaulterChargeService.save(hc);
                }
            }
            ZonedDateTime dateN = ZonedDateTime.now().withHour(0).withMinute(1).withSecond(0).withDayOfMonth(1).withMonth(month.getMonthValue()).withYear(month.getYear());
            historicalDefaulterSaved.setDate(dateN);
            historicalDefaulterSaved.setCategories(categories);
            double total = Double.parseDouble(historicalDefaulterSaved.getTotal()) > 0 ? Double.parseDouble(historicalDefaulterSaved.getTotal()) : -Double.parseDouble(historicalDefaulterSaved.getTotal());
            historicalDefaulterSaved.setTotal(total + "");
            this.save(historicalDefaulterSaved);
        }
    }

    //    Guarga los historicos positivos del pasado
    private void setHistoricalPositivePast(Long houseId, ZonedDateTime month, List<CustomChargeTypeDTO> custom, String currency) {
        ZonedDateTime lastDayMonth = month.withHour(23).withMinute(59).withSecond(59).with(TemporalAdjusters.lastDayOfMonth());
        ZonedDateTime firstDayMonth = month.withDayOfMonth(1).withHour(0).withSecond(1);
        List<PaymentDTO> inAdvanced = this.paymentService.findFavorUntilDatesAndHouseId(lastDayMonth, houseId);
        double total = 0;
        for (int i = 0; i < inAdvanced.size(); i++) {
            total = total + Double.parseDouble(inAdvanced.get(i).getAmmountLeft());
        }
        if (total == 0) {
            HistoricalPositiveDTO hp = this.historicalPositiveService.findAllByHouseIdAndDate(houseId, month);
            if (hp != null) {
                this.historicalPositiveService.delete(hp.getId());
            }
        }
        if (total > 0) {
            HistoricalPositiveDTO hp = this.historicalPositiveService.findAllByHouseIdAndDate(houseId, month);
            if (hp != null) {
                this.historicalPositiveService.delete(hp.getId());
            }
            HouseDTO h = this.houseService.findOneClean(houseId);
            HistoricalPositiveDTO nhp = new HistoricalPositiveDTO(null, total + "", h.getHousenumber(), firstDayMonth, h.getCompanyId(), houseId);
            this.historicalPositiveService.save(nhp);
        }
    }

    private boolean existOnHistorialDefaulterPast(int consecutive, List<HistoricalDefaulterChargeDTO> historicalCharges) {
        int count = 0;
        for (int i = 0; i < historicalCharges.size(); i++) {
            if (Double.parseDouble(historicalCharges.get(i).getConsecutive()) == consecutive) {
                count++;
            }
        }
        return count > 0;
    }

    //    Guarga los historicos negativos del pasado
    private void setHistoricalDefaulterPastOnCharge(Long houseId, ZonedDateTime month, List<CustomChargeTypeDTO> custom, String currency) {
        ZonedDateTime lastDayMonth = month.withHour(23).withMinute(59).withSecond(59).with(TemporalAdjusters.lastDayOfMonth());
        ZonedDateTime firstDayMonth = month.withDayOfMonth(1).withHour(0).withSecond(1);
        ZonedDateTime hdDate = ZonedDateTime.now().withYear(month.getYear()).withMonth(month.getMonthValue()).withMinute(1).withSecond(0).withNano(0).withHour(0).withDayOfMonth(1).withNano(0);
        HistoricalDefaulterDTO hd = null;
        HistoricalDefaulterDTO hdOld = this.findAllByHouseIdAndDate(houseId, month);
        if (hdOld != null) {
            hd = hdOld;
            hd.setDate(hdDate);
        } else {
            hd = new HistoricalDefaulterDTO();
            HouseDTO h = this.houseService.findOneClean(houseId);
            hd.setHouseId(h.getId());
            hd.setCompanyId(h.getCompanyId());
            hd.setHousenumber(h.getHousenumber());
            hd.setTotal("0");
            hd.setDate(hdDate);
        }
        List<ChargeDTO> charges = this.chargeService.findAllNotPayedByHouseAndUntilDateNew(currency, houseId, firstDayMonth, lastDayMonth, custom).getContent();
        if (hd.getId() != null) {
            List<HistoricalDefaulterChargeDTO> historicalCharges = this.historicalDefaulterChargeService.findAllbyHistoricalDefaulter(hd.getId());
            double totalDefault = 0;
            for (int i = 0; i < charges.size(); i++) {
                ChargeDTO c = charges.get(i);
                for (int j = 0; j < historicalCharges.size(); j++) {
                    HistoricalDefaulterChargeDTO hc = historicalCharges.get(j);
                    if (Long.parseLong(hc.getConsecutive()) == c.getConsecutive()) {
                        double hcLeftToPay = Double.parseDouble(hc.getLeftToPay());
                        if (hcLeftToPay != c.getLeftToPay()) {
                            if (c.getLeftToPay() == 0) {
                                hd.setTotal((Double.parseDouble(hd.getTotal()) - hcLeftToPay + ""));
                                if (hc.getId() != null) {
                                    this.historicalDefaulterChargeService.delete(hc.getId());
                                }
                            }
                            if (c.getLeftToPay() < hcLeftToPay && c.getLeftToPay() != 0) {
                                hd.setTotal((Double.parseDouble(hd.getTotal()) - hcLeftToPay + ""));
                                hc.setLeftToPay(c.getLeftToPay() + "");
                                hc.setAbonado(c.getAbonado() + "");
                                this.historicalDefaulterChargeService.save(hc);
                                double difference = hcLeftToPay - c.getLeftToPay();
                                hd.setTotal((Double.parseDouble(hd.getTotal()) - difference) + "");
                            }
                            if (c.getLeftToPay() > hcLeftToPay && c.getLeftToPay() != 0) {
                                hd.setTotal((Double.parseDouble(hd.getTotal()) - hcLeftToPay + ""));
                                hc.setLeftToPay(c.getLeftToPay() + "");
                                hc.setAbonado(c.getAbonado() + "");
                                this.historicalDefaulterChargeService.save(hc);
                                double difference = hcLeftToPay + c.getLeftToPay();
                                hd.setTotal((Double.parseDouble(hd.getTotal()) + difference) + "");
                            }
                        }
                        totalDefault = totalDefault + Double.parseDouble(hc.getLeftToPay());
                    }
                }
                if (!this.existOnHistorialDefaulterPast(c.getConsecutive(), historicalCharges)) {
                    HistoricalDefaulterChargeDTO newHc = new HistoricalDefaulterChargeDTO(
                        null,
                        c.getType(),
                        c.getDate(),
                        c.getConcept(),
                        c.getConsecutive() + "",
                        c.getAmmount() + "",
                        c.getLeftToPay() + "",
                        c.getAbonado() + "",
                        c.getLeftToPay() + "",
                        hd.getId(),
                        c.getId() + ""
                    );
                    this.historicalDefaulterChargeService.save(newHc);
                    totalDefault = totalDefault + Double.parseDouble(newHc.getLeftToPay());
                }
                ;
            }
            if (totalDefault != 0) {
                double total = totalDefault > 0 ? totalDefault : -totalDefault;
                hd.setTotal(total + "");
            }
            if (Double.parseDouble(hd.getTotal()) == 0) {
                this.delete(hd.getId());
            } else {
                hd.setTotal(Double.parseDouble(hd.getTotal()) > 0 ? hd.getTotal() : -Double.parseDouble(hd.getTotal()) + "");
                this.save(hd);
            }
        } else {
            HistoricalDefaulterDTO nhd = this.save(hd);
            String categories = "";
            for (int k = 0; k < charges.size(); k++) {
                ChargeDTO c = charges.get(k);
                if (c.getDate().isBefore(lastDayMonth)) {
                    HistoricalDefaulterChargeDTO hc = new HistoricalDefaulterChargeDTO(
                        null,
                        c.getType(),
                        c.getDate(),
                        c.getConcept(),
                        c.getConsecutive() + "",
                        c.getAmmount() + "",
                        c.getLeftToPay() + "",
                        c.getAbonado() + "",
                        c.getLeftToPay() + "",
                        nhd.getId(),
                        c.getId() + ""
                    );
                    categories = categories + c.getType() + ",";
                    this.historicalDefaulterChargeService.save(hc);
                }
                nhd.setTotal((Double.parseDouble(nhd.getTotal()) - c.getLeftToPay() + ""));
            }
            nhd.setTotal(Double.parseDouble(nhd.getTotal()) > 0 ? nhd.getTotal() : -Double.parseDouble(nhd.getTotal()) + "");
            this.save(nhd);
        }
    }

    //    Guarga los historicos negativos del pasado
    private void setHistoricalDefaulterPastOnPayment(Long houseId, ZonedDateTime month, List<CustomChargeTypeDTO> custom, String currency) {
        ZonedDateTime lastDayMonth = month.withHour(23).withMinute(59).withSecond(59).with(TemporalAdjusters.lastDayOfMonth());
        ZonedDateTime firstDayMonth = month.withDayOfMonth(1).withHour(0).withSecond(1);
        ZonedDateTime hdDate = ZonedDateTime.now().withYear(month.getYear()).withMonth(month.getMonthValue()).withMinute(1).withSecond(0).withNano(0).withHour(0).withDayOfMonth(1).withNano(0);
        HistoricalDefaulterDTO hd = null;
        HistoricalDefaulterDTO hdOld = this.findAllByHouseIdAndDate(houseId, month);
        if (hdOld != null) {
            List<PaymentDTO> payments = this.paymentService.findByHouseFilteredByDate(null, houseId, firstDayMonth, lastDayMonth).getContent();
            if (hd.getId() != null) {
                List<HistoricalDefaulterChargeDTO> historicalCharges = this.historicalDefaulterChargeService.findAllbyHistoricalDefaulter(hd.getId());
                double totalDefault = 0;
                for (int i = 0; i < payments.size(); i++) {
                    PaymentDTO p = this.paymentService.findOneComplete(payments.get(i).getId());
                    for (int x = 0; x < p.getCharges().size(); x++) {
                        PaymentChargeDTO pc = p.getCharges().get(x);
                        for (int j = 0; j < historicalCharges.size(); j++) {
                            HistoricalDefaulterChargeDTO hc = historicalCharges.get(j);
                            if (Double.parseDouble(pc.getConsecutive()) == Double.parseDouble(hc.getConsecutive())) {
                                hc.setAmmount(pc.getAmmount() + "");
                                hc.setLeftToPay(pc.getLeftToPay() + "");
                                hc.setAbonado(pc.getAbonado() + "");
                                hc.setLeftToPay(pc.getLeftToPay() + "");
                                if (Double.parseDouble(hc.getLeftToPay()) <= 0) {
                                    this.historicalDefaulterChargeService.delete(hc.getId());
                                } else {
                                    this.historicalDefaulterChargeService.save(hc);
                                }
                            }
                        }
                    }
                }
                List<HistoricalDefaulterChargeDTO> historicalChargesNew = this.historicalDefaulterChargeService.findAllbyHistoricalDefaulter(hd.getId());
                for (int i = 0; i < historicalChargesNew.size(); i++) {
                    totalDefault = totalDefault + Double.parseDouble(historicalChargesNew.get(i).getLeftToPay());
                }
                if (totalDefault != 0) {
                    double total = totalDefault > 0 ? totalDefault : -totalDefault;
                    hd.setTotal(total + "");
                }
                if (Double.parseDouble(hd.getTotal()) == 0) {
                    this.delete(hd.getId());
                } else {
                    hd.setTotal(Double.parseDouble(hd.getTotal()) > 0 ? hd.getTotal() : -Double.parseDouble(hd.getTotal()) + "");
                    this.save(hd);
                }
            }
        }
    }

    public void formatResetHousePast(Long houseId, ZonedDateTime month, List<CustomChargeTypeDTO> custom, String currency,int type) {
        log.debug("Request to get all HistoricalDefaulters");
        this.setHistoricalPositivePast(houseId, month, custom, currency);
        if(type==1){
            this.setHistoricalDefaulterPastOnCharge(houseId, month, custom, currency);
        }else{
            this.setHistoricalDefaulterPastOnPayment(houseId, month, custom, currency);
        }
    }

    @Async
    public void formatHistoricalReportByHouse(Long houseId, ZonedDateTime month, String currency, int companyId, int type) {
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<CustomChargeTypeDTO> custom = customChargeTypeService.findAllByCompany((long) companyId);
                int currentMonth = ZonedDateTime.now().getMonthValue();
                int currentYear = ZonedDateTime.now().getYear();
                int selectedMonth = month.getMonthValue();
                int selectedYear = month.getYear();
                if (selectedMonth == currentMonth && currentYear == selectedYear) {
                    formatResetHouse(houseId, month, custom);
                }
                if (selectedMonth <= currentMonth && currentYear == selectedYear) {
                    for (int i = selectedMonth; i <= currentMonth; i++) {
                        ZonedDateTime date = month.withMonth(i);
                        if (i == currentMonth) {
                            formatResetHouse(houseId, date, custom);
                        } else {
                            formatResetHousePast(houseId, date, custom, currency, type);
                        }
                    }
                }
                if (selectedMonth > currentMonth && currentYear != selectedYear) {
                    for (int i = selectedMonth; i <= 12; i++) {
                        ZonedDateTime date = month.withMonth(i).withYear(selectedYear);
                        formatResetHousePast(houseId, date, custom, currency, type);
                    }
                    for (int i = 1; i <= currentMonth; i++) {
                        ZonedDateTime date = month.withMonth(i).withYear(currentYear);
                        if (i == currentMonth) {
                            formatResetHouse(houseId, date, custom);
                        } else {
                            formatResetHousePast(houseId, date, custom, currency, type);
                        }
                    }
                }
            }
        }.start();
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
            HistoricalDefaulterDTO house = this.findAllByHouseIdAndDate(houseId, date);
            if (house != null) {
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
        Collections.sort(defaulterHouses, Comparator.comparing(HistoricalDefaulterDTO::getHousenumber));
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
                            c.getTotal() + "",
                            c.getPaymentAmmount(),
                            c.getAbonado() + "",
                            c.getPaymentAmmount(),
                            historicalDefaulterSaved.getId(),
                            c.getId() + ""
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
