package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CustomChargeType;
import com.lighthouse.aditum.repository.CustomChargeTypeRepository;
import com.lighthouse.aditum.service.dto.CustomChargeTypeDTO;
import com.lighthouse.aditum.service.mapper.CustomChargeTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CustomChargeType.
 */
@Service
@Transactional
public class CustomChargeTypeService {

    private final Logger log = LoggerFactory.getLogger(CustomChargeTypeService.class);

    private final CustomChargeTypeRepository customChargeTypeRepository;

    private final CustomChargeTypeMapper customChargeTypeMapper;

    public CustomChargeTypeService(CustomChargeTypeRepository customChargeTypeRepository, CustomChargeTypeMapper customChargeTypeMapper) {
        this.customChargeTypeRepository = customChargeTypeRepository;
        this.customChargeTypeMapper = customChargeTypeMapper;
    }

    /**
     * Save a customChargeType.
     *
     * @param customChargeTypeDTO the entity to save
     * @return the persisted entity
     */
    public CustomChargeTypeDTO save(CustomChargeTypeDTO customChargeTypeDTO) {
        log.debug("Request to save CustomChargeType : {}", customChargeTypeDTO);
        CustomChargeType customChargeType = customChargeTypeMapper.toEntity(customChargeTypeDTO);
        if(customChargeType.getId()==null){
            Random rand = new Random();
            customChargeType.setType(rand.nextInt(10000));
        }
        customChargeType = customChargeTypeRepository.save(customChargeType);
        return customChargeTypeMapper.toDto(customChargeType);
    }

    /**
     * Get all the customChargeTypes.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CustomChargeTypeDTO> findAll() {
        log.debug("Request to get all CustomChargeTypes");
        return customChargeTypeRepository.findAll().stream()
            .map(customChargeTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<CustomChargeTypeDTO> findAllByCompany(Long companyId) {
        log.debug("Request to get all CustomChargeTypes");
        return customChargeTypeRepository.findAllByCompanyId(companyId).stream()
            .map(customChargeTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one customChargeType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CustomChargeTypeDTO findOne(Long id) {
        log.debug("Request to get CustomChargeType : {}", id);
        CustomChargeType customChargeType = customChargeTypeRepository.findOne(id);
        return customChargeTypeMapper.toDto(customChargeType);
    }

    /**
     * Delete the customChargeType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomChargeType : {}", id);
        customChargeTypeRepository.delete(id);
    }
}
