package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.SubsidiaryType;
import com.lighthouse.aditum.repository.SubsidiaryTypeRepository;
import com.lighthouse.aditum.service.dto.SubsidiaryTypeDTO;
import com.lighthouse.aditum.service.mapper.SubsidiaryTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing SubsidiaryType.
 */
@Service
@Transactional
public class SubsidiaryTypeService {

    private final Logger log = LoggerFactory.getLogger(SubsidiaryTypeService.class);

    private final SubsidiaryTypeRepository subsidiaryTypeRepository;

    private final SubsidiaryTypeMapper subsidiaryTypeMapper;

    public SubsidiaryTypeService(SubsidiaryTypeRepository subsidiaryTypeRepository, SubsidiaryTypeMapper subsidiaryTypeMapper) {
        this.subsidiaryTypeRepository = subsidiaryTypeRepository;
        this.subsidiaryTypeMapper = subsidiaryTypeMapper;
    }

    /**
     * Save a subsidiaryType.
     *
     * @param subsidiaryTypeDTO the entity to save
     * @return the persisted entity
     */
    public SubsidiaryTypeDTO save(SubsidiaryTypeDTO subsidiaryTypeDTO) {
        log.debug("Request to save SubsidiaryType : {}", subsidiaryTypeDTO);
        SubsidiaryType subsidiaryType = subsidiaryTypeMapper.toEntity(subsidiaryTypeDTO);
        subsidiaryType = subsidiaryTypeRepository.save(subsidiaryType);
        return subsidiaryTypeMapper.toDto(subsidiaryType);
    }

    /**
     * Get all the subsidiaryTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SubsidiaryTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubsidiaryTypes");
        return subsidiaryTypeRepository.findAll(pageable)
            .map(subsidiaryTypeMapper::toDto);
    }
    @Transactional(readOnly = true)
    public List<SubsidiaryTypeDTO> findAllNoPage(Long id) {
        log.debug("Request to get all SubsidiaryTypes");
        List<SubsidiaryTypeDTO> dtos = new ArrayList<>();
         subsidiaryTypeRepository.findByCompanyIdAndSubsidiaryType(id,1).forEach(
            subsidiaryType -> {
                dtos.add(subsidiaryTypeMapper.toDto(subsidiaryType));
            }
        );
         return dtos;
    }
    @Transactional(readOnly = true)
    public List<SubsidiaryTypeDTO> findAllSubNoPage(Long id) {
        log.debug("Request to get all SubsidiaryTypes");
        List<SubsidiaryTypeDTO> dtos = new ArrayList<>();
        subsidiaryTypeRepository.findByCompanyIdAndSubsidiaryTypeNot(id,1).forEach(
            subsidiaryType -> {
                dtos.add(subsidiaryTypeMapper.toDto(subsidiaryType));
            }
        );
        return dtos;
    }
    /**
     * Get one subsidiaryType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SubsidiaryTypeDTO findOne(Long id) {
        log.debug("Request to get SubsidiaryType : {}", id);
        SubsidiaryType subsidiaryType = subsidiaryTypeRepository.findOne(id);
        return subsidiaryTypeMapper.toDto(subsidiaryType);
    }

    /**
     * Delete the subsidiaryType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SubsidiaryType : {}", id);
        subsidiaryTypeRepository.delete(id);
    }
}
