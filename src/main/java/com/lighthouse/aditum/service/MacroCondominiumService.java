package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.MacroCondominium;
import com.lighthouse.aditum.repository.MacroCondominiumRepository;
import com.lighthouse.aditum.service.dto.MacroCondominiumDTO;
import com.lighthouse.aditum.service.mapper.MacroCondominiumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MacroCondominium.
 */
@Service
@Transactional
public class MacroCondominiumService {

    private final Logger log = LoggerFactory.getLogger(MacroCondominiumService.class);

    private final MacroCondominiumRepository macroCondominiumRepository;

    private final MacroCondominiumMapper macroCondominiumMapper;

    public MacroCondominiumService(MacroCondominiumRepository macroCondominiumRepository, MacroCondominiumMapper macroCondominiumMapper) {
        this.macroCondominiumRepository = macroCondominiumRepository;
        this.macroCondominiumMapper = macroCondominiumMapper;
    }

    /**
     * Save a macroCondominium.
     *
     * @param macroCondominiumDTO the entity to save
     * @return the persisted entity
     */
    public MacroCondominiumDTO save(MacroCondominiumDTO macroCondominiumDTO) {
        log.debug("Request to save MacroCondominium : {}", macroCondominiumDTO);
        MacroCondominium macroCondominium = macroCondominiumMapper.toEntity(macroCondominiumDTO);
        macroCondominium = macroCondominiumRepository.save(macroCondominium);
        return macroCondominiumMapper.toDto(macroCondominium);
    }

    /**
     * Get all the macroCondominiums.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MacroCondominiumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MacroCondominiums");
        return macroCondominiumRepository.findAll(pageable)
            .map(macroCondominiumMapper::toDto);
    }

    /**
     * Get one macroCondominium by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MacroCondominiumDTO findOne(Long id) {
        log.debug("Request to get MacroCondominium : {}", id);
        MacroCondominium macroCondominium = macroCondominiumRepository.findOneWithEagerRelationships(id);
        return macroCondominiumMapper.toDto(macroCondominium);
    }

    /**
     * Delete the macroCondominium by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MacroCondominium : {}", id);
        macroCondominiumRepository.delete(id);
    }
}
