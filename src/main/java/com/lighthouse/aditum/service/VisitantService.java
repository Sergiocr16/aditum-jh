package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Visitant;
import com.lighthouse.aditum.repository.VisitantRepository;
import com.lighthouse.aditum.service.dto.VisitantDTO;
import com.lighthouse.aditum.service.mapper.VisitantMapper;
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
 * Service Implementation for managing Visitant.
 */
@Service
@Transactional
public class VisitantService {

    private final Logger log = LoggerFactory.getLogger(VisitantService.class);

    private final VisitantRepository visitantRepository;

    private final VisitantMapper visitantMapper;

    public VisitantService(VisitantRepository visitantRepository, VisitantMapper visitantMapper) {
        this.visitantRepository = visitantRepository;
        this.visitantMapper = visitantMapper;
    }

    /**
     * Save a visitant.
     *
     * @param visitantDTO the entity to save
     * @return the persisted entity
     */
    public VisitantDTO save(VisitantDTO visitantDTO) {
        log.debug("Request to save Visitant : {}", visitantDTO);
        Visitant visitant = visitantMapper.visitantDTOToVisitant(visitantDTO);
        visitant = visitantRepository.save(visitant);
        VisitantDTO result = visitantMapper.visitantToVisitantDTO(visitant);
        return result;
    }

    /**
     *  Get all the visitants.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VisitantDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Visitants");
        Page<Visitant> result = visitantRepository.findByCompanyId(pageable,companyId);
        return result.map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }

    /**
     *  Get one visitant by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public VisitantDTO findOne(Long id) {
        log.debug("Request to get Visitant : {}", id);
        Visitant visitant = visitantRepository.findOne(id);
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
        return visitantDTO;
    }

    @Transactional(readOnly = true)
    public VisitantDTO findInvitedVisitorByHouse(String identificationNumber,Long houseId,Long companyId) {
        log.debug("Request to find if there is already a registered visitor with this identification number : {}", identificationNumber);
        Visitant visitant = visitantRepository.findByIdentificationnumberAndHouseIdAndCompanyId(identificationNumber,houseId,companyId);
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
        return visitantDTO;
    }

    /**
     *  Delete the  visitant by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Visitant : {}", id);
        visitantRepository.delete(id);
    }
}
