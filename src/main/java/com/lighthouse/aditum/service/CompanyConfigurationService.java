package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CompanyConfiguration;
import com.lighthouse.aditum.repository.CompanyConfigurationRepository;
import com.lighthouse.aditum.service.dto.CompanyConfigurationDTO;
import com.lighthouse.aditum.service.mapper.CompanyConfigurationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CompanyConfiguration.
 */
@Service
@Transactional
public class CompanyConfigurationService {

    private final Logger log = LoggerFactory.getLogger(CompanyConfigurationService.class);
    
    private final CompanyConfigurationRepository companyConfigurationRepository;

    private final CompanyConfigurationMapper companyConfigurationMapper;

    public CompanyConfigurationService(CompanyConfigurationRepository companyConfigurationRepository, CompanyConfigurationMapper companyConfigurationMapper) {
        this.companyConfigurationRepository = companyConfigurationRepository;
        this.companyConfigurationMapper = companyConfigurationMapper;
    }

    /**
     * Save a companyConfiguration.
     *
     * @param companyConfigurationDTO the entity to save
     * @return the persisted entity
     */
    public CompanyConfigurationDTO save(CompanyConfigurationDTO companyConfigurationDTO) {
        log.debug("Request to save CompanyConfiguration : {}", companyConfigurationDTO);
        CompanyConfiguration companyConfiguration = companyConfigurationMapper.companyConfigurationDTOToCompanyConfiguration(companyConfigurationDTO);
        companyConfiguration = companyConfigurationRepository.save(companyConfiguration);
        CompanyConfigurationDTO result = companyConfigurationMapper.companyConfigurationToCompanyConfigurationDTO(companyConfiguration);
        return result;
    }

    /**
     *  Get all the companyConfigurations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CompanyConfigurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyConfigurations");
        Page<CompanyConfiguration> result = companyConfigurationRepository.findAll(pageable);
        return result.map(companyConfiguration -> companyConfigurationMapper.companyConfigurationToCompanyConfigurationDTO(companyConfiguration));
    }

    /**
     *  Get one companyConfiguration by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CompanyConfigurationDTO findOne(Long id) {
        log.debug("Request to get CompanyConfiguration : {}", id);
        CompanyConfiguration companyConfiguration = companyConfigurationRepository.findOne(id);
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationMapper.companyConfigurationToCompanyConfigurationDTO(companyConfiguration);
        return companyConfigurationDTO;
    }

    /**
     *  Delete the  companyConfiguration by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyConfiguration : {}", id);
        companyConfigurationRepository.delete(id);
    }
}
