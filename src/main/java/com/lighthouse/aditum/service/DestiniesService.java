package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Destinies;
import com.lighthouse.aditum.repository.DestiniesRepository;
import com.lighthouse.aditum.service.dto.DestiniesDTO;
import com.lighthouse.aditum.service.mapper.DestiniesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Destinies.
 */
@Service
@Transactional
public class DestiniesService {

    private final Logger log = LoggerFactory.getLogger(DestiniesService.class);
    
    private final DestiniesRepository destiniesRepository;

    private final DestiniesMapper destiniesMapper;

    public DestiniesService(DestiniesRepository destiniesRepository, DestiniesMapper destiniesMapper) {
        this.destiniesRepository = destiniesRepository;
        this.destiniesMapper = destiniesMapper;
    }

    /**
     * Save a destinies.
     *
     * @param destiniesDTO the entity to save
     * @return the persisted entity
     */
    public DestiniesDTO save(DestiniesDTO destiniesDTO) {
        log.debug("Request to save Destinies : {}", destiniesDTO);
        Destinies destinies = destiniesMapper.destiniesDTOToDestinies(destiniesDTO);
        destinies = destiniesRepository.save(destinies);
        DestiniesDTO result = destiniesMapper.destiniesToDestiniesDTO(destinies);
        return result;
    }

    /**
     *  Get all the destinies.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<DestiniesDTO> findAll() {
        log.debug("Request to get all Destinies");
        List<DestiniesDTO> result = destiniesRepository.findAll().stream()
            .map(destiniesMapper::destiniesToDestiniesDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one destinies by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DestiniesDTO findOne(Long id) {
        log.debug("Request to get Destinies : {}", id);
        Destinies destinies = destiniesRepository.findOne(id);
        DestiniesDTO destiniesDTO = destiniesMapper.destiniesToDestiniesDTO(destinies);
        return destiniesDTO;
    }

    /**
     *  Delete the  destinies by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Destinies : {}", id);
        destiniesRepository.delete(id);
    }
}
