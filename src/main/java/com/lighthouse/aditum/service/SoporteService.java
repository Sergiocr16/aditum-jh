package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Soporte;
import com.lighthouse.aditum.repository.SoporteRepository;
import com.lighthouse.aditum.service.dto.SoporteDTO;
import com.lighthouse.aditum.service.mapper.SoporteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Soporte.
 */
@Service
@Transactional
public class SoporteService {

    private final Logger log = LoggerFactory.getLogger(SoporteService.class);

    private final SoporteRepository soporteRepository;

    private final SoporteMapper soporteMapper;

    public SoporteService(SoporteRepository soporteRepository, SoporteMapper soporteMapper) {
        this.soporteRepository = soporteRepository;
        this.soporteMapper = soporteMapper;
    }

    /**
     * Save a soporte.
     *
     * @param soporteDTO the entity to save
     * @return the persisted entity
     */
    public SoporteDTO save(SoporteDTO soporteDTO) {
        log.debug("Request to save Soporte : {}", soporteDTO);
        Soporte soporte = soporteMapper.toEntity(soporteDTO);
        soporte = soporteRepository.save(soporte);
        return soporteMapper.toDto(soporte);
    }

    /**
     * Get all the soportes.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SoporteDTO> findAll() {
        log.debug("Request to get all Soportes");
        return soporteRepository.findAll().stream()
            .map(soporteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one soporte by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SoporteDTO findOne(Long id) {
        log.debug("Request to get Soporte : {}", id);
        Soporte soporte = soporteRepository.findOne(id);
        return soporteMapper.toDto(soporte);
    }

    /**
     * Delete the soporte by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Soporte : {}", id);
        soporteRepository.delete(id);
    }
}
