package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.AdministrationConfiguration;
import com.lighthouse.aditum.repository.AdministrationConfigurationRepository;
import com.lighthouse.aditum.service.dto.AdministrationConfigurationDTO;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import com.lighthouse.aditum.service.mapper.AdministrationConfigurationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * Service Implementation for managing AdministrationConfiguration.
 */
@Service
@Transactional
public class AdministrationConfigurationService {

    private final Logger log = LoggerFactory.getLogger(AdministrationConfigurationService.class);

    private final AdministrationConfigurationRepository administrationConfigurationRepository;

    private final AdministrationConfigurationMapper administrationConfigurationMapper;

    private final BitacoraAccionesService bitacoraAccionesService;

    private final AdminInfoService adminInfoService;

    private final UserService userService;


    public AdministrationConfigurationService(UserService userService,AdminInfoService adminInfoService,BitacoraAccionesService bitacoraAccionesService,AdministrationConfigurationRepository administrationConfigurationRepository, AdministrationConfigurationMapper administrationConfigurationMapper) {
        this.administrationConfigurationRepository = administrationConfigurationRepository;
        this.administrationConfigurationMapper = administrationConfigurationMapper;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.adminInfoService = adminInfoService;
        this.userService = userService;
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
        administrationConfiguration.setInitialConfiguration(administrationConfigurationDTO.getInitialConfiguration());
        administrationConfiguration = administrationConfigurationRepository.save(administrationConfiguration);

        if(administrationConfigurationDTO.getSaveInBitacora()==1){

            LocalDateTime today = LocalDateTime.now();
            ZoneId id = ZoneId.of("America/Costa_Rica");  //Create timezone
            ZonedDateTime zonedDateTime = ZonedDateTime.of(today, id);
            BitacoraAccionesDTO bitacoraAccionesDTO = new BitacoraAccionesDTO();


            bitacoraAccionesDTO.setType(3);
            bitacoraAccionesDTO.setConcept("Edición de panel de configuración general");
            bitacoraAccionesDTO.setUrlState("administration-configuration-detail");


            bitacoraAccionesDTO.setEjecutionDate(zonedDateTime);
            bitacoraAccionesDTO.setCategory("Configuración general");

            bitacoraAccionesDTO.setIdReference(administrationConfiguration.getId());
            bitacoraAccionesDTO.setIdResponsable(adminInfoService.findOneByUserId(userService.getUserWithAuthorities().getId()).getId());
            bitacoraAccionesDTO.setCompanyId(administrationConfiguration.getCompany().getId());
            bitacoraAccionesService.save(bitacoraAccionesDTO);
        }




        AdministrationConfigurationDTO adminConfigDto = administrationConfigurationMapper.toDto(administrationConfiguration);
        adminConfigDto.setFolioSerie(administrationConfiguration.getFolioSerie());
        adminConfigDto.setFolioNumber(administrationConfiguration.getFolioNumber());
        adminConfigDto.setInitialConfiguration(administrationConfiguration.getInitialConfiguration());
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
        adminConfDto.setInitialConfiguration(administrationConfiguration.getInitialConfiguration());
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
