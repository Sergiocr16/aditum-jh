package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Resident;
import com.lighthouse.aditum.repository.ResidentRepository;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.ResidentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private final HouseService houseService;

    public ResidentService(ResidentRepository residentRepository, ResidentMapper residentMapper, @Lazy HouseService houseService) {
        this.residentRepository = residentRepository;
        this.residentMapper = residentMapper;
        this.houseService = houseService;
    }

    /**
     * Save a resident.
     *
     * @param residentDTO the entity to save
     * @return the persisted entity
     */
    public ResidentDTO save(ResidentDTO residentDTO) {
        log.debug("Request to save Resident : {}", residentDTO);
        Resident resident = residentMapper.toEntity(residentDTO);
        resident.setDeleted(0);
        if (residentDTO.getPrincipalContact() == 1) {
            Page<ResidentDTO> residentsEnabled = this.findEnabledByHouseId(null, residentDTO.getHouseId());
            Page<ResidentDTO> residentsDisabled = this.findDisabled(null, residentDTO.getHouseId());
            List<ResidentDTO> allHouseResidents = new ArrayList<>();
            residentsEnabled.getContent().forEach(residentDTO1 -> {
                allHouseResidents.add(residentDTO1);
            });
            residentsDisabled.getContent().forEach(residentDTO2 -> {
                allHouseResidents.add(residentDTO2);
            });
            ResidentDTO currentPrincipal = null;
            for (int i = 0; i < allHouseResidents.size(); i++) {
                if (allHouseResidents.get(i).getPrincipalContact() == 1) {
                    currentPrincipal = allHouseResidents.get(i);
                }
            }
            if (currentPrincipal != null) {
                currentPrincipal.setPrincipalContact(0);
                residentRepository.save(this.residentMapper.toEntity(currentPrincipal));
            }
        }
        resident = residentRepository.save(resident);
        ResidentDTO result = residentMapper.toDto(resident);
        return result;
    }

    /**
     * Get all the residents.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ResidentDTO> findAll(Long companyId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByCompanyIdAndDeleted(companyId, 0);
        return new PageImpl<>(result).map(resident -> {
                ResidentDTO residentDTO = residentMapper.toDto(resident);
                residentDTO.setHouse(houseService.findOne(residentDTO.getHouseId()));
                return residentDTO;
            }
        );
    }

    @Transactional(readOnly = true)
    public Integer enableQuantityByCompany(Long companyId) {
        log.debug("Request to get Vehicule : {}", companyId);
        return residentRepository.countByEnabledAndCompanyIdAndDeleted(1, companyId, 0);
    }

    @Transactional(readOnly = true)
    public Integer disableQuantityByCompany(Long companyId) {
        log.debug("Request to get Vehicule : {}", companyId);
        return residentRepository.countByEnabledAndCompanyIdAndDeleted(0, companyId, 0);
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findEnabledByHouseId(Pageable pageable, Long houseId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByEnabledAndHouseIdAndDeleted(1, houseId, 0);
        return new PageImpl<>(result).map(resident -> residentMapper.toDto(resident));
    }

    @Transactional(readOnly = true)
    public ResidentDTO findByCompanyIdAndIdentifiactionNumber(Long companyId, String identificationNumber) {
        log.debug("Request to get all Residents");
        Resident resident = residentRepository.findByCompanyIdAndIdentificationnumberAndDeleted(companyId, identificationNumber, 0);

        ResidentDTO residentDTO = residentMapper.toDto(resident);
        return residentDTO;
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findDisabledByHouseId(Pageable pageable, Long houseId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByEnabledAndHouseIdAndDeleted(0, houseId, 0);
        return new PageImpl<>(result).map(resident -> residentMapper.toDto(resident));
    }
    @Transactional(readOnly = true)
    public Page<ResidentDTO> findPrincipalContactByCompanyId(Pageable pageable, Long companyId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByPrincipalContactAndCompanyIdAndDeleted(1, companyId, 0);
        String a = "a";
        return new PageImpl<>(result).map(resident -> residentMapper.toDto(resident));
    }
    /**
     * Get one resident by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ResidentDTO findOne(Long id) {
        log.debug("Request to get Resident : {}", id);
        Resident resident = residentRepository.findOne(id);
        ResidentDTO residentDTO = residentMapper.toDto(resident);
        return residentDTO;
    }

    @Transactional(readOnly = true)
    public ResidentDTO findOneByUserId(Long id) {
        log.debug("Request to get Resident : {}", id);
        Resident resident = residentRepository.findOneByUserId(id);
        ResidentDTO residentDTO = residentMapper.toDto(resident);
        residentDTO.setHouse(this.houseService.findOne(residentDTO.getHouseId()));
        return residentDTO;
    }

    /**
     * Delete the  resident by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Resident : {}", id);
        Resident resident = residentMapper.toEntity(this.findOne(id));
        resident.setDeleted(1);
        residentRepository.save(resident);
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findEnabled(Pageable pageable, Long companyId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByEnabledAndCompanyIdAndDeleted(1, companyId, 0);
        return new PageImpl<>(result).map(resident -> resident.image(null)).map(resident -> residentMapper.toDto(resident));
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findDisabled(Pageable pageable, Long companyId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByEnabledAndCompanyIdAndDeleted(0, companyId, 0);
        return new PageImpl<>(result).map(resident -> residentMapper.toDto(resident));

    }

    @Transactional(readOnly = true)
    public ResidentDTO findPrincipalContactByHouse(Long houseId) {
        Page<ResidentDTO> residents = this.findEnabledByHouseId(null, houseId);
        ResidentDTO principal = null;
        for (int i = 0; i < residents.getContent().size(); i++) {
            if (residents.getContent().get(i).getPrincipalContact() == 1) {
                principal = residents.getContent().get(i);
            }
        }
        return principal;
    }


}
