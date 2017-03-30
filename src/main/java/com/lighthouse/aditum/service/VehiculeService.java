package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Vehicule;
import com.lighthouse.aditum.repository.VehiculeRepository;
import com.lighthouse.aditum.service.dto.VehiculeDTO;
import com.lighthouse.aditum.service.mapper.VehiculeMapper;
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
 * Service Implementation for managing Vehicule.
 */
@Service
@Transactional
public class VehiculeService {

    private final Logger log = LoggerFactory.getLogger(VehiculeService.class);

    private final VehiculeRepository vehiculeRepository;

    private final VehiculeMapper vehiculeMapper;

    public VehiculeService(VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeMapper = vehiculeMapper;
    }

    /**
     * Save a vehicule.
     *
     * @param vehiculeDTO the entity to save
     * @return the persisted entity
     */
    public VehiculeDTO save(VehiculeDTO vehiculeDTO) {
        log.debug("Request to save Vehicule : {}", vehiculeDTO);
        Vehicule vehicule = vehiculeMapper.vehiculeDTOToVehicule(vehiculeDTO);
        vehicule = vehiculeRepository.save(vehicule);
        VehiculeDTO result = vehiculeMapper.vehiculeToVehiculeDTO(vehicule);
        return result;
    }

    /**
     *  Get all the vehicules.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Vehicules");
        Page<Vehicule> result = vehiculeRepository.findByCompanyId(pageable,companyId);
        return result.map(vehicule -> vehiculeMapper.vehiculeToVehiculeDTO(vehicule));
    }

    /**
     *  Get one vehicule by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public VehiculeDTO findOne(Long id) {
        log.debug("Request to get Vehicule : {}", id);
        Vehicule vehicule = vehiculeRepository.findOne(id);
        VehiculeDTO vehiculeDTO = vehiculeMapper.vehiculeToVehiculeDTO(vehicule);
        return vehiculeDTO;
    }

    /**
     *  Delete the  vehicule by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Vehicule : {}", id);
        vehiculeRepository.delete(id);
    }


    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findEnabled(Pageable pageable,Long companyId) {
        log.debug("Request to get all Residents");
        List<Vehicule> result = vehiculeRepository.findByEnabledAndCompanyId(1,companyId);
        return new PageImpl<>(result).map(vehicule -> vehiculeMapper.vehiculeToVehiculeDTO(vehicule));
    }
    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findDisabled(Pageable pageable,Long companyId) {
        log.debug("Request to get all Residents");
        List<Vehicule> result = vehiculeRepository.findByEnabledAndCompanyId(0,companyId);
        return new PageImpl<>(result).map(vehicule -> vehiculeMapper.vehiculeToVehiculeDTO(vehicule));

    }
}
