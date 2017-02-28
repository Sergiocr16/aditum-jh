package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.AccessDoor;
import com.lighthouse.aditum.repository.AccessDoorRepository;
import com.lighthouse.aditum.service.dto.AccessDoorDTO;
import com.lighthouse.aditum.service.mapper.AccessDoorMapper;
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
 * Service Implementation for managing AccessDoor.
 */
@Service
@Transactional
public class AccessDoorService {

    private final Logger log = LoggerFactory.getLogger(AccessDoorService.class);
    
    private final AccessDoorRepository accessDoorRepository;

    private final AccessDoorMapper accessDoorMapper;

    public AccessDoorService(AccessDoorRepository accessDoorRepository, AccessDoorMapper accessDoorMapper) {
        this.accessDoorRepository = accessDoorRepository;
        this.accessDoorMapper = accessDoorMapper;
    }

    /**
     * Save a accessDoor.
     *
     * @param accessDoorDTO the entity to save
     * @return the persisted entity
     */
    public AccessDoorDTO save(AccessDoorDTO accessDoorDTO) {
        log.debug("Request to save AccessDoor : {}", accessDoorDTO);
        AccessDoor accessDoor = accessDoorMapper.accessDoorDTOToAccessDoor(accessDoorDTO);
        accessDoor = accessDoorRepository.save(accessDoor);
        AccessDoorDTO result = accessDoorMapper.accessDoorToAccessDoorDTO(accessDoor);
        return result;
    }

    /**
     *  Get all the accessDoors.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccessDoorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccessDoors");
        Page<AccessDoor> result = accessDoorRepository.findAll(pageable);
        return result.map(accessDoor -> accessDoorMapper.accessDoorToAccessDoorDTO(accessDoor));
    }

    /**
     *  Get one accessDoor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AccessDoorDTO findOne(Long id) {
        log.debug("Request to get AccessDoor : {}", id);
        AccessDoor accessDoor = accessDoorRepository.findOne(id);
        AccessDoorDTO accessDoorDTO = accessDoorMapper.accessDoorToAccessDoorDTO(accessDoor);
        return accessDoorDTO;
    }

    /**
     *  Delete the  accessDoor by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AccessDoor : {}", id);
        accessDoorRepository.delete(id);
    }
}
