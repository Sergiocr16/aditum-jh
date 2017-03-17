package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Officer;
import com.lighthouse.aditum.repository.OfficerRepository;
import com.lighthouse.aditum.service.dto.OfficerDTO;
import com.lighthouse.aditum.service.mapper.OfficerMapper;
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
 * Service Implementation for managing Officer.
 */
@Service
@Transactional
public class OfficerService {

    private final Logger log = LoggerFactory.getLogger(OfficerService.class);

    private final OfficerRepository officerRepository;

    private final OfficerMapper officerMapper;

    public OfficerService(OfficerRepository officerRepository, OfficerMapper officerMapper) {
        this.officerRepository = officerRepository;
        this.officerMapper = officerMapper;
    }

    /**
     * Save a officer.
     *
     * @param officerDTO the entity to save
     * @return the persisted entity
     */
    public OfficerDTO save(OfficerDTO officerDTO) {
        log.debug("Request to save Officer : {}", officerDTO);
        Officer officer = officerMapper.officerDTOToOfficer(officerDTO);
        officer = officerRepository.save(officer);
        OfficerDTO result = officerMapper.officerToOfficerDTO(officer);
        return result;
    }

    /**
     *  Get all the officers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OfficerDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Officers");
        Page<Officer> result = officerRepository.findByCompanyId(pageable,companyId);
        return result.map(officer -> officerMapper.officerToOfficerDTO(officer));
    }

    /**
     *  Get one officer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OfficerDTO findOne(Long id) {
        log.debug("Request to get Officer : {}", id);
        Officer officer = officerRepository.findOne(id);
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);
        return officerDTO;
    }

    @Transactional(readOnly = true)
    public OfficerDTO findOneByUserId(Long id) {
        log.debug("Request to get Officer : {}", id);
        Officer officer = officerRepository.findOneByUserId(id);
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);
        return officerDTO;
    }

    /**
     *  Delete the  officer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Officer : {}", id);
        officerRepository.delete(id);
    }
}
