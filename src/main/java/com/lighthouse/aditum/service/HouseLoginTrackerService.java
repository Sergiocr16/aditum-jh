package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.HouseLoginTracker;
import com.lighthouse.aditum.domain.Resident;
import com.lighthouse.aditum.repository.HouseLoginTrackerRepository;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.HouseLoginTrackerDTO;
import com.lighthouse.aditum.service.mapper.HouseLoginTrackerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing HouseLoginTracker.
 */
@Service
@Transactional
public class HouseLoginTrackerService {

    private final Logger log = LoggerFactory.getLogger(HouseLoginTrackerService.class);

    private final HouseLoginTrackerRepository houseLoginTrackerRepository;

    private final HouseLoginTrackerMapper houseLoginTrackerMapper;

    private final HouseService houseService;
    private final ResidentService residentService;

    public HouseLoginTrackerService(ResidentService residentService, HouseService houseService, HouseLoginTrackerRepository houseLoginTrackerRepository, HouseLoginTrackerMapper houseLoginTrackerMapper) {
        this.houseLoginTrackerRepository = houseLoginTrackerRepository;
        this.houseLoginTrackerMapper = houseLoginTrackerMapper;
        this.residentService = residentService;
        this.houseService = houseService;
    }

    /**
     * Save a houseLoginTracker.
     *
     * @param houseLoginTrackerDTO the entity to save
     * @return the persisted entity
     */
    public HouseLoginTrackerDTO save(HouseLoginTrackerDTO houseLoginTrackerDTO) {
        log.debug("Request to save HouseLoginTracker : {}", houseLoginTrackerDTO);
        HouseLoginTracker houseLoginTracker = houseLoginTrackerMapper.toEntity(houseLoginTrackerDTO);
        HouseLoginTrackerDTO houseLoginTrackerSaved = this.findOneByHouseId(houseLoginTrackerDTO.getHouseId());
        if(houseLoginTrackerSaved!=null){
            HouseLoginTracker houseLoginTrackerSavedDTO = houseLoginTrackerMapper.toEntity(houseLoginTrackerSaved);
            houseLoginTrackerSavedDTO.setLastTime(houseLoginTrackerDTO.getLastTime());
            houseLoginTrackerSavedDTO.setUser(houseLoginTrackerDTO.getUser());
            houseLoginTracker = this.houseLoginTrackerRepository.save(houseLoginTrackerSavedDTO);
        }else{
            houseLoginTracker = houseLoginTrackerRepository.save(houseLoginTracker);
        }
        return houseLoginTrackerMapper.toDto(houseLoginTracker);
    }


    @Transactional(readOnly = true)
    public HouseLoginTrackerDTO findOneByHouseId(Long houseId) {
        log.debug("Request to get HouseLoginTracker : {}", houseId);
        HouseLoginTracker houseLoginTracker = houseLoginTrackerRepository.findOneByHouseId(houseId);
        return houseLoginTrackerMapper.toDto(houseLoginTracker);
    }
    /**
     * Get all the houseLoginTrackers.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<HouseLoginTrackerDTO> findAll(Long companyId) {
        log.debug("Request to get all HouseLoginTrackers");

        List<HouseLoginTrackerDTO> finalList = new ArrayList<>();
        Page<HouseDTO> houses = houseService.findAll(companyId);

        for (int i = 0; i < houses.getContent().size(); i++) {
            List<HouseLoginTrackerDTO> login = houseLoginTrackerRepository.findByHouseId(houses.getContent().get(i).getId()).stream()
                .map(houseLoginTrackerMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));


            if(login.size()>0){
                login.get(0).setHouse(houses.getContent().get(i));
                login.get(0).setStatus(1);
                login.get(0).setResident(residentService.findOneByUserId(Long.parseLong(login.get(0).getUser())));
                finalList.add(login.get(0));

            }else{
                HouseLoginTrackerDTO loginToCreate = new HouseLoginTrackerDTO();
                loginToCreate.setHouse(houses.getContent().get(i));
                loginToCreate.setStatus(0);
                finalList.add(loginToCreate);
            }
        }
        String a = "a";
        return finalList;
    }

    /**
     * Get one houseLoginTracker by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HouseLoginTrackerDTO findOne(Long id) {
        log.debug("Request to get HouseLoginTracker : {}", id);
        HouseLoginTracker houseLoginTracker = houseLoginTrackerRepository.findOne(id);
        return houseLoginTrackerMapper.toDto(houseLoginTracker);
    }

    /**
     * Delete the houseLoginTracker by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HouseLoginTracker : {}", id);
        houseLoginTrackerRepository.delete(id);
    }
}
