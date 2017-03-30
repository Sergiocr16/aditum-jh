package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Resident;
import com.lighthouse.aditum.repository.ResidentRepository;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.ResidentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Resident.
 */
@Service
@Transactional
public class ResidentService {

    private final Logger log = LoggerFactory.getLogger(ResidentService.class);

    private final ResidentRepository residentRepository;

    private final ResidentMapper residentMapper;

    public ResidentService(ResidentRepository residentRepository, ResidentMapper residentMapper) {
        this.residentRepository = residentRepository;
        this.residentMapper = residentMapper;
    }

    /**
     * Save a resident.
     *
     * @param residentDTO the entity to save
     * @return the persisted entity
     */
    public ResidentDTO save(ResidentDTO residentDTO) {
        log.debug("Request to save Resident : {}", residentDTO);
        Resident resident = residentMapper.residentDTOToResident(residentDTO);
        resident = residentRepository.save(resident);
        ResidentDTO result = residentMapper.residentToResidentDTO(resident);
        return result;
    }

    /**
     *  Get all the residents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ResidentDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Residents");
        Page<Resident> result = residentRepository.findByCompanyId(pageable,companyId);
        return result.map(resident -> residentMapper.residentToResidentDTO(resident));
    }
    @Transactional(readOnly = true)
    public Page<ResidentDTO> findByHouseId(Pageable pageable,Long houseId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByHouseId(pageable,houseId);
        return new PageImpl<>(result).map(resident -> residentMapper.residentToResidentDTO(resident));
    }
    /**
     *  Get one resident by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ResidentDTO findOne(Long id) {
        log.debug("Request to get Resident : {}", id);
        Resident resident = residentRepository.findOne(id);
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(resident);
        return residentDTO;
    }

    @Transactional(readOnly = true)
    public ResidentDTO findOneByUserId(Long id) {
        log.debug("Request to get Resident : {}", id);
        Resident resident = residentRepository.findOneByUserId(id);
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(resident);
        return residentDTO;
    }

    /**
     *  Delete the  resident by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Resident : {}", id);
        residentRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findEnabled(Pageable pageable,Long companyId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByEnabledAndCompanyId(1,companyId);
        return new PageImpl<>(result).map(resident -> residentMapper.residentToResidentDTO(resident));
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findDisabled(Pageable pageable,Long companyId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByEnabledAndCompanyId(0,companyId);
        return new PageImpl<>(result).map(resident -> residentMapper.residentToResidentDTO(resident));

    }
}
