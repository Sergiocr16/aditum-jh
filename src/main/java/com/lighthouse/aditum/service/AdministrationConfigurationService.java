package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.AdministrationConfiguration;
import com.lighthouse.aditum.repository.AdministrationConfigurationRepository;
import com.lighthouse.aditum.service.dto.AdministrationConfigurationDTO;
import com.lighthouse.aditum.service.mapper.AdministrationConfigurationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing AdministrationConfiguration.
 */
@Service
@Transactional
public class AdministrationConfigurationService {

    private final Logger log = LoggerFactory.getLogger(AdministrationConfigurationService.class);

    private final AdministrationConfigurationRepository administrationConfigurationRepository;

    private final AdministrationConfigurationMapper administrationConfigurationMapper;

    public AdministrationConfigurationService(AdministrationConfigurationRepository administrationConfigurationRepository, AdministrationConfigurationMapper administrationConfigurationMapper) {
        this.administrationConfigurationRepository = administrationConfigurationRepository;
        this.administrationConfigurationMapper = administrationConfigurationMapper;
    }

    /**
     * Save a administrationConfiguration.
     *
     * @param administrationConfigurationDTO the entity to save
     * @return the persisted entity
     */
    public AdministrationConfigurationDTO save(AdministrationConfigurationDTO administrationConfigurationDTO) {
        log.debug("Request to save AdministrationConfiguration : {}", administrationConfigurationDTO);
        AdministrationConfiguration administrationConfiguration = administrationConfigurationMapper.toEntity(administrationConfigurationDTO);
        administrationConfiguration.setFolioNumber(administrationConfigurationDTO.getFolioNumber());
        administrationConfiguration.setFolioSerie(administrationConfigurationDTO.getFolioSerie());
        administrationConfiguration = administrationConfigurationRepository.save(administrationConfiguration);
        AdministrationConfigurationDTO adminConfigDto = administrationConfigurationMapper.toDto(administrationConfiguration);
        adminConfigDto.setFolioSerie(administrationConfiguration.getFolioSerie());
        adminConfigDto.setFolioNumber(administrationConfiguration.getFolioNumber());
        return adminConfigDto;
    }

    /**
     *  Get all the administrationConfigurations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AdministrationConfigurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdministrationConfigurations");
        return administrationConfigurationRepository.findAll(pageable)
            .map(administrationConfigurationMapper::toDto);
    }

    /**
     *  Get one administrationConfiguration by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AdministrationConfigurationDTO findOneByCompanyId(Long id) {
        log.debug("Request to get AdministrationConfiguration : {}", id);
        AdministrationConfiguration administrationConfiguration = administrationConfigurationRepository.findByCompanyId(id).get(0);
        AdministrationConfigurationDTO adminConfDto = administrationConfigurationMapper.toDto(administrationConfiguration);
        adminConfDto.setFolioNumber(administrationConfiguration.getFolioNumber());
        adminConfDto.setFolioSerie(administrationConfiguration.getFolioSerie());
        return adminConfDto;
    }

    /**
     *  Delete the  administrationConfiguration by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AdministrationConfiguration : {}", id);
        administrationConfigurationRepository.delete(id);
    }
}
