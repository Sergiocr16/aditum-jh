package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.WaterConsumption;
import com.lighthouse.aditum.repository.WaterConsumptionRepository;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.WaterConsumptionDTO;
import com.lighthouse.aditum.service.mapper.WaterConsumptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final HouseService houseService;

    public WaterConsumptionService(HouseService houseService, WaterConsumptionRepository waterConsumptionRepository, WaterConsumptionMapper waterConsumptionMapper) {
        this.waterConsumptionRepository = waterConsumptionRepository;
        this.waterConsumptionMapper = waterConsumptionMapper;
        this.houseService = houseService;
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
        waterConsumption = waterConsumptionRepository.save(waterConsumption);
        WaterConsumptionDTO wC =  waterConsumptionMapper.toDto(waterConsumption);
        wC.setHousenumber(this.houseService.findOne(wC.getHouseId()).getHousenumber());
        return wC;
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
        for (HouseDTO houseDTO : houseDTOS){
            WaterConsumption waterConsumption = this.waterConsumptionRepository.findByHouseIdAndRecordDate(houseDTO.getId(),date);
            if(waterConsumption!=null){
                WaterConsumptionDTO waterConsumptionDTO = this.waterConsumptionMapper.toDto(waterConsumption);
                waterConsumptionDTO.setHousenumber(houseDTO.getHousenumber());
                waterConsumptionDTOS.add(waterConsumptionDTO);
            }else{
                WaterConsumptionDTO waterConsumptionDTO = new WaterConsumptionDTO();
                waterConsumptionDTO.setHousenumber(houseDTO.getHousenumber());
                waterConsumptionDTO.setConsumption("0");
                waterConsumptionDTO.setStatus(0);
                waterConsumptionDTO.setRecordDate(date);
                waterConsumptionDTO.setHouseId(houseDTO.getId());
                waterConsumptionDTOS.add(waterConsumptionDTO);
            }
        }
        log.debug("Request to get all WaterConsumptions");
        return waterConsumptionDTOS;
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
        return waterConsumptionMapper.toDto(waterConsumption);
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
