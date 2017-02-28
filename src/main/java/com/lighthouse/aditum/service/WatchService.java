package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Watch;
import com.lighthouse.aditum.repository.WatchRepository;
import com.lighthouse.aditum.service.dto.WatchDTO;
import com.lighthouse.aditum.service.mapper.WatchMapper;
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
 * Service Implementation for managing Watch.
 */
@Service
@Transactional
public class WatchService {

    private final Logger log = LoggerFactory.getLogger(WatchService.class);
    
    private final WatchRepository watchRepository;

    private final WatchMapper watchMapper;

    public WatchService(WatchRepository watchRepository, WatchMapper watchMapper) {
        this.watchRepository = watchRepository;
        this.watchMapper = watchMapper;
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

    /**
     *  Get all the watches.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WatchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Watches");
        Page<Watch> result = watchRepository.findAll(pageable);
        return result.map(watch -> watchMapper.watchToWatchDTO(watch));
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
