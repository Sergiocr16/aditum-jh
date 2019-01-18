package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Watch;
import com.lighthouse.aditum.repository.WatchRepository;
import com.lighthouse.aditum.service.dto.WatchDTO;
import com.lighthouse.aditum.service.mapper.WatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Service Implementation for managing Watch.
 */
@Service
@Transactional
public class WatchService {

    private final Logger log = LoggerFactory.getLogger(WatchService.class);

    private final WatchRepository watchRepository;

    private final WatchMapper watchMapper;

    private final OfficerService officerService;


    public WatchService(WatchRepository watchRepository, WatchMapper watchMapper, OfficerService officerService) {
        this.watchRepository = watchRepository;
        this.watchMapper = watchMapper;
        this.officerService = officerService;
    }

    /**
     * Save a watch.
     *
     * @param watchDTO the entity to save
     * @return the persisted entity
     */
    public WatchDTO save(WatchDTO watchDTO) {
        log.debug("Request to save Watch : {}", watchDTO);
        Watch watch = watchMapper.watchDTOToWatch(watchDTO);
        watch = watchRepository.save(watch);
        WatchDTO result = watchMapper.watchToWatchDTO(watch);
        return result;
    }

    @Transactional(readOnly = true)
    public WatchDTO findLastWatch(Long companyId) {
        log.debug("Request to get Watch : {}");
        Watch watch = null;
        if(!watchRepository.findTop1ByCompanyIdOrderByInitialtimeDesc(companyId).isEmpty()) {
             watch = watchRepository.findTop1ByCompanyIdOrderByInitialtimeDesc(companyId).get(0);
        }
        WatchDTO watchDTO = watchMapper.watchToWatchDTO(watch);
        return watchDTO;
    }
    /**
     *  Get all the watches.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WatchDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Watches");
        Page<Watch> result = watchRepository.findByCompanyId(pageable,companyId);
        return result.map(watch -> watchMapper.watchToWatchDTO(watch));
    }

    @Transactional(readOnly = true)
    public Page<WatchDTO> findBetweenDates(Pageable pageable,String initialTime,String finalTime,Long companyId) {
        log.debug("Request to get all Watches");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<Watch> result = watchRepository.findByDatesBetween(zd_initialTime,zd_finalTime,companyId,pageable);

        return (result).map(watch -> watchMapper.watchToWatchDTO(watch));
    }

    /**
     *  Get one watch by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WatchDTO findOne(Long id) {
        log.debug("Request to get Watch : {}", id);
        Watch watch = watchRepository.findOne(id);
        WatchDTO watchDTO = watchMapper.watchToWatchDTO(watch);
        String[] ids = watch.getResponsableofficer().split(";");
        watchDTO.setOfficers(new ArrayList<>());
        for (int i = 0; i < ids.length; i++) {
         watchDTO.getOfficers().add(this.officerService.findOne(Long.parseLong(ids[i])));
        }
        return watchDTO;
    }

    /**
     *  Delete the  watch by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Watch : {}", id);
        watchRepository.delete(id);
    }
}
