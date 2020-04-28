package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.HouseSecurityDirection;
import com.lighthouse.aditum.repository.HouseRepository;
import com.lighthouse.aditum.repository.HouseSecurityDirectionRepository;
import com.lighthouse.aditum.service.dto.HouseArDTO;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.HouseSecurityDirectionDTO;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import com.lighthouse.aditum.service.mapper.HouseSecurityDirectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    private final HouseRepository houseRepository;

    private final HouseSecurityDirectionMapper houseSecurityDirectionMapper;

    private final HouseMapper houseMapper;

    private final HouseService houseService;

    public HouseSecurityDirectionService(HouseRepository houseRepository,HouseMapper houseMapper, HouseService houseService, HouseSecurityDirectionRepository houseSecurityDirectionRepository, HouseSecurityDirectionMapper houseSecurityDirectionMapper) {
        this.houseSecurityDirectionRepository = houseSecurityDirectionRepository;
        this.houseSecurityDirectionMapper = houseSecurityDirectionMapper;
        this.houseMapper = houseMapper;
        this.houseService = houseService;
        this.houseRepository = houseRepository;
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

    private HouseArDTO HouseDTOtohouseArDTO(HouseDTO houseDTO) {
        HouseArDTO houseArDTO = new HouseArDTO();
        houseArDTO.setCompanyId(houseDTO.getCompanyId());
        houseArDTO.setHasOwner(houseDTO.getHasOwner());
        houseArDTO.setId(houseDTO.getId());
        houseArDTO.setHousenumber(houseDTO.getHousenumber());
        houseArDTO.setExtension(houseDTO.getExtension());
        houseArDTO.setIsdesocupated(houseDTO.getIsdesocupated());
        houseArDTO.setSecurityKey(houseDTO.getSecurityKey());
        houseArDTO.setEmergencyKey(houseDTO.getEmergencyKey());
        houseArDTO.setLoginCode(houseDTO.getLoginCode());
        houseArDTO.setCodeStatus(houseDTO.getCodeStatus());
        houseArDTO.setDue(houseDTO.getDue());
        houseArDTO.setEmail(houseDTO.getEmail());
        return houseArDTO;
    }

    /**
     * Get all the houseSecurityDirections.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HouseArDTO> findAll(Long companyId) {
//        log.debug("Request to get all Houses");
      List<House> result = houseRepository.findByCompanyId(companyId);
      List<House> houses = houseService.orderHouses(result);
        return new PageImpl<>(houses).map(house -> {
            HouseDTO house1 = houseMapper.houseToHouseDTO(house);
            HouseArDTO h = this.HouseDTOtohouseArDTO(house1);
            h.setUbication(this.findOneByHouseId(h.getId()));
            return h;
        });
    }

    @Transactional(readOnly = true)
    public Page<HouseArDTO> findAllFilter(Pageable pageable, Long companyId, String desocupated, String houseNumber) {
        log.debug("Request to get all Houses");
        Page<House> result;
        if (houseNumber.equals(" ")) {
            if (desocupated.equals("empty")) {
                result = houseRepository.findByCompanyId(pageable, companyId);
            } else {
                result = houseRepository.findByCompanyIdAndIsdesocupated(pageable, companyId, Integer.parseInt(desocupated));
            }
            result = new PageImpl<>(houseService.orderHouses(result.getContent()), pageable, result.getTotalElements());
        } else {
            if (desocupated.equals("empty")) {
                result = houseRepository.findByCompanyIdAndHousenumberContains(pageable, companyId, houseNumber);
            } else {
                result = houseRepository.findByCompanyIdAndIsdesocupatedAndHousenumberContains(pageable, companyId, Integer.parseInt(desocupated), houseNumber);
            }
            result = new PageImpl<>(houseService.orderHouses(result.getContent()), pageable, result.getTotalElements());
        }
        return result.map(house -> {
            HouseDTO house1 = houseMapper.houseToHouseDTO(house);
            HouseArDTO h = this.HouseDTOtohouseArDTO(house1);
            h.setUbication(this.findOneByHouseId(h.getId()));
            return h;
        });
    }

    /**
     * Get one houseSecurityDirection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HouseArDTO findOne(Long id) {
        log.debug("Request to get HouseSecurityDirection : {}", id);
        HouseDTO house = this.houseService.findOne(id);
        HouseArDTO h = this.HouseDTOtohouseArDTO(house);
        h.setUbication(this.findOneByHouseId(h.getId()));
        return h;
    }

    @Transactional(readOnly = true)
    public HouseSecurityDirectionDTO findOneByHouseId(Long id) {
        log.debug("Request to get HouseSecurityDirection : {}", id);
        HouseSecurityDirection houseSecurityDirection = houseSecurityDirectionRepository.findOneByHouseId(id);
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
