package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Emergency;
import com.lighthouse.aditum.repository.EmergencyRepository;
import com.lighthouse.aditum.service.dto.EmergencyDTO;
import com.lighthouse.aditum.service.mapper.EmergencyMapper;
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
 * Service Implementation for managing Emergency.
 */
@Service
@Transactional
public class EmergencyService {

    private final Logger log = LoggerFactory.getLogger(EmergencyService.class);

    private final EmergencyRepository emergencyRepository;

    private final EmergencyMapper emergencyMapper;

    public EmergencyService(EmergencyRepository emergencyRepository, EmergencyMapper emergencyMapper) {
        this.emergencyRepository = emergencyRepository;
        this.emergencyMapper = emergencyMapper;
    }

    /**
     * Save a emergency.
     *
     * @param emergencyDTO the entity to save
     * @return the persisted entity
     */
    public EmergencyDTO save(EmergencyDTO emergencyDTO) {
        log.debug("Request to save Emergency : {}", emergencyDTO);
        Emergency emergency = emergencyMapper.emergencyDTOToEmergency(emergencyDTO);
        emergency = emergencyRepository.save(emergency);
        EmergencyDTO result = emergencyMapper.emergencyToEmergencyDTO(emergency);
        return result;
    }

    /**
     *  Get all the emergencies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmergencyDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Emergencies");
        Page<Emergency> result = emergencyRepository.findByCompanyId(pageable,companyId);
        return result.map(emergency -> emergencyMapper.emergencyToEmergencyDTO(emergency));
    }

    /**
     *  Get one emergency by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EmergencyDTO findOne(Long id) {
        log.debug("Request to get Emergency : {}", id);
        Emergency emergency = emergencyRepository.findOne(id);
        EmergencyDTO emergencyDTO = emergencyMapper.emergencyToEmergencyDTO(emergency);
        return emergencyDTO;
    }

    /**
     *  Delete the  emergency by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Emergency : {}", id);
        emergencyRepository.delete(id);
    }
}
