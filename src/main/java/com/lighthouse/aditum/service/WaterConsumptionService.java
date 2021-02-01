package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.WaterConsumption;
import com.lighthouse.aditum.repository.WaterConsumptionRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.ChargeMapper;
import com.lighthouse.aditum.service.mapper.WaterConsumptionMapper;
import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing WaterConsumption.
 */
@Service
@Transactional
public class WaterConsumptionService {

    private final Logger log = LoggerFactory.getLogger(WaterConsumptionService.class);

    private final WaterConsumptionRepository waterConsumptionRepository;

    private final WaterConsumptionMapper waterConsumptionMapper;

    private final ChargeMapper chargeMapper;


    private final HouseService houseService;

    private final ChargeService chargeService;

    private final CompanyConfigurationService companyConfigurationService;


    public WaterConsumptionService(ChargeMapper chargeMapper, CompanyConfigurationService companyConfigurationService, ChargeService chargeService, HouseService houseService, WaterConsumptionRepository waterConsumptionRepository, WaterConsumptionMapper waterConsumptionMapper) {
        this.waterConsumptionRepository = waterConsumptionRepository;
        this.waterConsumptionMapper = waterConsumptionMapper;
        this.houseService = houseService;
        this.chargeService = chargeService;
        this.companyConfigurationService = companyConfigurationService;
        this.chargeMapper = chargeMapper;
    }

    /**
     * Save a waterConsumption.
     *
     * @param waterConsumptionDTO the entity to save
     * @return the persisted entity
     */
    public WaterConsumptionDTO save(WaterConsumptionDTO waterConsumptionDTO) {
        log.debug("Request to save WaterConsumption : {}", waterConsumptionDTO);
        WaterConsumption waterConsumption = waterConsumptionMapper.toEntity(waterConsumptionDTO);
        WaterConsumption waterConsumptionOld = this.waterConsumptionRepository.findFirstByHouseIdAndRecordDate(waterConsumptionDTO.getHouseId(), waterConsumptionDTO.getRecordDate());
        WaterConsumptionDTO wC = null;
        if (waterConsumptionOld != null) {
            waterConsumptionOld.setConsumption(waterConsumptionDTO.getConsumption());
            waterConsumptionOld.setMonth(waterConsumptionDTO.getMonth());
            waterConsumptionOld.setStatus(waterConsumptionDTO.getStatus());
            waterConsumptionOld.setMedicionAnterior(waterConsumptionDTO.getMedicionAnterior());
            waterConsumptionOld.setMedicionActual(waterConsumptionDTO.getMedicionActual());
            if (waterConsumptionDTO.getChargeId() != null) {
                waterConsumptionOld.setCharge(this.chargeMapper.fromId(waterConsumptionDTO.getChargeId()));
            }
            waterConsumption = waterConsumptionRepository.save(waterConsumptionOld);
            wC = waterConsumptionMapper.toDto(waterConsumption);
        } else {
            if (waterConsumptionDTO.getChargeId() != null) {
                waterConsumptionOld.setCharge(this.chargeMapper.fromId(waterConsumptionDTO.getChargeId()));
            }
            waterConsumption.setMonth(waterConsumptionDTO.getMonth());
            waterConsumption = waterConsumptionRepository.save(waterConsumption);
            wC = waterConsumptionMapper.toDto(waterConsumption);
        }
        wC.setHousenumber(this.houseService.findOneClean(wC.getHouseId()).getHousenumber());
        wC.setMonth(waterConsumptionDTO.getMonth());
        return wC;
    }

    public void update(WaterConsumptionDTO waterConsumptionDTO) {
        WaterConsumption waterConsumption = waterConsumptionMapper.toEntity(waterConsumptionDTO);
        waterConsumptionRepository.save(waterConsumption);
    }


    /**
     * Get all the waterConsumptions.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WaterConsumptionDTO> findAll() {
        log.debug("Request to get all WaterConsumptions");
        return waterConsumptionRepository.findAll().stream()
            .map(waterConsumptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the waterConsumptions.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WaterConsumptionDTO> findAllByDate(Long companyId, ZonedDateTime date) {
        List<HouseDTO> houseDTOS = this.houseService.findAll(companyId).getContent();
        List<WaterConsumptionDTO> waterConsumptionDTOS = new ArrayList<>();
        for (HouseDTO houseDTO : houseDTOS) {
            WaterConsumption waterConsumption = this.waterConsumptionRepository.findFirstByHouseIdAndRecordDate(houseDTO.getId(), date);
            ZonedDateTime lastMonth = null;
            if (date.getMonthValue() == 1) {
                lastMonth = date.withMonth(12).withYear(date.getYear() - 1);
            } else {
                lastMonth = date.withMonth(date.getMonthValue() - 1);
            }
            WaterConsumption waterConsumptionLast = this.waterConsumptionRepository.findFirstByHouseIdAndRecordDate(houseDTO.getId(), lastMonth);
            if (waterConsumption != null) {
                WaterConsumptionDTO waterConsumptionDTO = this.waterConsumptionMapper.toDto(waterConsumption);
                waterConsumptionDTO.setHousenumber(houseDTO.getHousenumber());
                if (waterConsumption.getMonth() == null) {
                    waterConsumptionDTO.setMonth("0");
                }
                if (waterConsumptionLast != null && waterConsumptionDTO.getStatus() != 1) {
                    if (waterConsumption.getMedicionAnterior() == null) {
                        if (waterConsumptionLast.getMedicionActual().equals("NaN")) {
                            waterConsumptionDTO.setMedicionAnterior("0");
                        } else {
                            waterConsumptionDTO.setMedicionAnterior(waterConsumptionLast.getMedicionActual());
                        }
                    }
                }
                waterConsumptionDTOS.add(waterConsumptionDTO);
            } else {
                WaterConsumptionDTO waterConsumptionDTO = new WaterConsumptionDTO();
                waterConsumptionDTO.setHousenumber(houseDTO.getHousenumber());
                waterConsumptionDTO.setConsumption("0");
                waterConsumptionDTO.setMedicionActual("0");
                if(waterConsumptionDTO.getMedicionActual()==null){
                    waterConsumptionDTO.setMedicionAnterior("0");
                }
                waterConsumptionDTO.setStatus(0);
                waterConsumptionDTO.setMonth("0");
                waterConsumptionDTO.setRecordDate(date);
                waterConsumptionDTO.setHouseId(houseDTO.getId());
                if (waterConsumptionLast != null && waterConsumptionDTO.getStatus() != 1) {
                    waterConsumptionDTO.setMedicionAnterior(waterConsumptionLast.getMedicionActual());
                }
                waterConsumptionDTOS.add(waterConsumptionDTO);
            }
        }
        log.debug("Request to get all WaterConsumptions");
        return waterConsumptionDTOS;
    }

    public List<WaterConsumptionDTO> createAllCharges(Long companyId, ZonedDateTime date, ZonedDateTime chargeDate, AdministrationConfigurationDTO administrationConfigurationDTO, Boolean sendEmail, Boolean autocalculated, String concept) throws URISyntaxException, IOException, DocumentException {
        List<WaterConsumptionDTO> waterConsumptions = this.findAllByDate(companyId, date);
        CompanyConfigurationDTO companyConfigDTO = this.companyConfigurationService.findOne(companyId);
        for (WaterConsumptionDTO waterConsumptionDTO : waterConsumptions) {
            if (waterConsumptionDTO.getId() != null) {
                if (waterConsumptionDTO.getStatus() == 0 && !waterConsumptionDTO.getConsumption().equals("0")) {
                    this.chargeService.createWaterCharge(companyConfigDTO, waterConsumptionDTO, chargeDate, administrationConfigurationDTO, sendEmail, autocalculated, concept);
                }
            }
        }
        return waterConsumptions;
    }

    /**
     * Get one waterConsumption by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public WaterConsumptionDTO findOne(Long id) {
        log.debug("Request to get WaterConsumption : {}", id);
        WaterConsumption waterConsumption = waterConsumptionRepository.findOne(id);
        WaterConsumptionDTO wC = this.waterConsumptionMapper.toDto(waterConsumption);
        wC.setHousenumber(this.houseService.findOneClean(wC.getHouseId()).getHousenumber());
        wC.setMonth(waterConsumption.getMonth());
        if (wC.getMonth() == null) {
            wC.setMonth("0");
        }
        return wC;
    }


    @Transactional(readOnly = true)
    public WaterConsumptionDTO findOneByChargeId(Long id) {
        log.debug("Request to get WaterConsumption : {}", id);
        WaterConsumption waterConsumption = waterConsumptionRepository.findFirstByChargeId(id);
        WaterConsumptionDTO wC = this.waterConsumptionMapper.toDto(waterConsumption);
        return wC;
    }

    @Transactional(readOnly = true)
    public WaterConsumptionDTO findOneByChargeIdFormating(Long id) {
        log.debug("Request to get WaterConsumption : {}", id);
        WaterConsumption waterConsumption = waterConsumptionRepository.findFirstByChargeId(id);
        return this.waterConsumptionMapper.toDto(waterConsumption);
    }

    @Transactional(readOnly = true)
    public List<WaterConsumptionDTO> findByHouseId(Long houseId) {
        List<WaterConsumption> waterConsumptions = waterConsumptionRepository.findByHouseId(houseId);
        return waterConsumptions.stream()
            .map(waterConsumptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Delete the waterConsumption by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WaterConsumption : {}", id);
        waterConsumptionRepository.delete(id);
    }
}
