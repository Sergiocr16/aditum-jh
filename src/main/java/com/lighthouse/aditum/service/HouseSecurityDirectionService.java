package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.HouseSecurityDirection;
import com.lighthouse.aditum.repository.HouseSecurityDirectionRepository;
import com.lighthouse.aditum.service.dto.HouseArDTO;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.HouseSecurityDirectionDTO;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import com.lighthouse.aditum.service.mapper.HouseSecurityDirectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing HouseSecurityDirection.
 */
@Service
@Transactional
public class HouseSecurityDirectionService {

    private final Logger log = LoggerFactory.getLogger(HouseSecurityDirectionService.class);

    private final HouseSecurityDirectionRepository houseSecurityDirectionRepository;

    private final HouseSecurityDirectionMapper houseSecurityDirectionMapper;

    private final HouseMapper houseMapper;

    private final HouseService houseService;

    public HouseSecurityDirectionService(HouseMapper houseMapper, HouseService houseService, HouseSecurityDirectionRepository houseSecurityDirectionRepository, HouseSecurityDirectionMapper houseSecurityDirectionMapper) {
        this.houseSecurityDirectionRepository = houseSecurityDirectionRepository;
        this.houseSecurityDirectionMapper = houseSecurityDirectionMapper;
        this.houseMapper = houseMapper;
        this.houseService = houseService;
    }

    /**
     * Save a houseSecurityDirection.
     *
     * @param houseSecurityDirectionDTO the entity to save
     * @return the persisted entity
     */
    public HouseSecurityDirectionDTO save(HouseSecurityDirectionDTO houseSecurityDirectionDTO) {
        log.debug("Request to save HouseSecurityDirection : {}", houseSecurityDirectionDTO);
        HouseSecurityDirection houseSecurityDirection = houseSecurityDirectionMapper.toEntity(houseSecurityDirectionDTO);
        houseSecurityDirection = houseSecurityDirectionRepository.save(houseSecurityDirection);
        return houseSecurityDirectionMapper.toDto(houseSecurityDirection);
    }

    public HouseSecurityDirectionDTO save(HouseArDTO houseArDTO) {
        log.debug("Request to save HouseSecurityDirection : {}", houseArDTO);
        HouseDTO houseDTO = this.houseArDTOtoHouseDTO(houseArDTO);
        houseDTO =  this.houseService.save(houseDTO);
        HouseSecurityDirection houseSecurityDirection = houseSecurityDirectionMapper.toEntity(houseArDTO.getUbication());
        houseSecurityDirection.setHouse(this.houseSecurityDirectionMapper.houseFromId(houseDTO.getId()));
        houseSecurityDirection.setCompany(this.houseMapper.companyFromId(houseDTO.getCompanyId()));
        houseSecurityDirection = houseSecurityDirectionRepository.save(houseSecurityDirection);
        return houseSecurityDirectionMapper.toDto(houseSecurityDirection);
    }

    private HouseDTO houseArDTOtoHouseDTO(HouseArDTO houseArDTO) {
        HouseDTO house = new HouseDTO();
        house.setCompanyId(houseArDTO.getCompanyId());
        house.setHasOwner(houseArDTO.getHasOwner());
        house.setId(houseArDTO.getId());
        house.setHousenumber(houseArDTO.getHousenumber());
        house.setExtension(houseArDTO.getExtension());
        house.setIsdesocupated(houseArDTO.getIsdesocupated());
        house.setSecurityKey(houseArDTO.getSecurityKey());
        house.setEmergencyKey(houseArDTO.getEmergencyKey());
        house.setLoginCode(houseArDTO.getLoginCode());
        house.setCodeStatus(houseArDTO.getCodeStatus());
        house.setDue(houseArDTO.getDue());
        house.setEmail(houseArDTO.getEmail());
        return house;
    }

    /**
     * Get all the houseSecurityDirections.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<HouseSecurityDirectionDTO> findAll() {
        log.debug("Request to get all HouseSecurityDirections");
        return houseSecurityDirectionRepository.findAll().stream()
            .map(houseSecurityDirectionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one houseSecurityDirection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HouseSecurityDirectionDTO findOne(Long id) {
        log.debug("Request to get HouseSecurityDirection : {}", id);
        HouseSecurityDirection houseSecurityDirection = houseSecurityDirectionRepository.findOne(id);
        return houseSecurityDirectionMapper.toDto(houseSecurityDirection);
    }

    /**
     * Delete the houseSecurityDirection by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HouseSecurityDirection : {}", id);
        houseSecurityDirectionRepository.delete(id);
    }
}
