package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Regulation;
import com.lighthouse.aditum.repository.RegulationRepository;
import com.lighthouse.aditum.service.dto.RegulationDTO;
import com.lighthouse.aditum.service.mapper.RegulationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Regulation.
 */
@Service
@Transactional
public class RegulationService {

    private final Logger log = LoggerFactory.getLogger(RegulationService.class);

    private final RegulationRepository regulationRepository;

    private final RegulationMapper regulationMapper;

    public RegulationService(RegulationRepository regulationRepository, RegulationMapper regulationMapper) {
        this.regulationRepository = regulationRepository;
        this.regulationMapper = regulationMapper;
    }

    /**
     * Save a regulation.
     *
     * @param regulationDTO the entity to save
     * @return the persisted entity
     */
    public RegulationDTO save(RegulationDTO regulationDTO) {
        log.debug("Request to save Regulation : {}", regulationDTO);
        Regulation regulation = regulationMapper.toEntity(regulationDTO);
        regulation = regulationRepository.save(regulation);
        return regulationMapper.toDto(regulation);
    }

    /**
     * Get all the regulations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RegulationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Regulations");
        return regulationRepository.findAll(pageable)
            .map(regulationMapper::toDto);
    }

    /**
     * Get one regulation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RegulationDTO findOne(Long id) {
        log.debug("Request to get Regulation : {}", id);
        Regulation regulation = regulationRepository.findOne(id);
        return regulationMapper.toDto(regulation);
    }

    /**
     * Delete the regulation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Regulation : {}", id);
        regulationRepository.delete(id);
    }
}
