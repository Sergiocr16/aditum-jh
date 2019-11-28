package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Subsidiary;
import com.lighthouse.aditum.repository.SubsidiaryRepository;
import com.lighthouse.aditum.service.dto.SubsidiaryDTO;
import com.lighthouse.aditum.service.mapper.SubsidiaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing Subsidiary.
 */
@Service
@Transactional
public class SubsidiaryService {

    private final Logger log = LoggerFactory.getLogger(SubsidiaryService.class);

    private final SubsidiaryRepository subsidiaryRepository;

    public final SubsidiaryMapper subsidiaryMapper;

    public SubsidiaryService(SubsidiaryRepository subsidiaryRepository, SubsidiaryMapper subsidiaryMapper) {
        this.subsidiaryRepository = subsidiaryRepository;
        this.subsidiaryMapper = subsidiaryMapper;
    }

    /**
     * Save a subsidiary.
     *
     * @param subsidiaryDTO the entity to save
     * @return the persisted entity
     */
    public SubsidiaryDTO save(SubsidiaryDTO subsidiaryDTO) {
        log.debug("Request to save Subsidiary : {}", subsidiaryDTO);
        Subsidiary subsidiary = subsidiaryMapper.toEntity(subsidiaryDTO);
        subsidiary = subsidiaryRepository.save(subsidiary);
        return subsidiaryMapper.toDto(subsidiary);
    }

    /**
     * Get all the subsidiaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SubsidiaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Subsidiaries");
        return subsidiaryRepository.findAll(pageable)
            .map(subsidiaryMapper::toDto);
    }

    /**
     * Get all the subsidiaries.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SubsidiaryDTO> findAllByHouse(Long houseId) {
        log.debug("Request to get all Subsidiaries");
        List<SubsidiaryDTO> dtos = new ArrayList<>();
        subsidiaryRepository.findByHouseId(houseId).forEach(subsidiary -> {
            dtos.add(subsidiaryMapper.toDto(subsidiary));
        });
        return dtos;
    }

    /**
     * Get one subsidiary by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SubsidiaryDTO findOne(Long id) {
        log.debug("Request to get Subsidiary : {}", id);
        Subsidiary subsidiary = subsidiaryRepository.findOne(id);
        return subsidiaryMapper.toDto(subsidiary);
    }

    /**
     * Delete the subsidiary by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Subsidiary : {}", id);
        subsidiaryRepository.delete(id);
    }
}
