package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ActivosFijos;
import com.lighthouse.aditum.repository.ActivosFijosRepository;
import com.lighthouse.aditum.service.dto.ActivosFijosDTO;
import com.lighthouse.aditum.service.mapper.ActivosFijosMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ActivosFijos.
 */
@Service
@Transactional
public class ActivosFijosService {

    private final Logger log = LoggerFactory.getLogger(ActivosFijosService.class);

    private final ActivosFijosRepository activosFijosRepository;

    private final ActivosFijosMapper activosFijosMapper;

    public ActivosFijosService(ActivosFijosRepository activosFijosRepository, ActivosFijosMapper activosFijosMapper) {
        this.activosFijosRepository = activosFijosRepository;
        this.activosFijosMapper = activosFijosMapper;
    }

    /**
     * Save a activosFijos.
     *
     * @param activosFijosDTO the entity to save
     * @return the persisted entity
     */
    public ActivosFijosDTO save(ActivosFijosDTO activosFijosDTO) {
        log.debug("Request to save ActivosFijos : {}", activosFijosDTO);
        ActivosFijos activosFijos = activosFijosMapper.toEntity(activosFijosDTO);
        activosFijos = activosFijosRepository.save(activosFijos);
        return activosFijosMapper.toDto(activosFijos);
    }

    /**
     * Get all the activosFijos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ActivosFijosDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all ActivosFijos");
        return activosFijosRepository.findByCompanyIdAndDeleted(pageable,companyId,1)
            .map(activosFijosMapper::toDto);
    }

    /**
     * Get one activosFijos by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ActivosFijosDTO findOne(Long id) {
        log.debug("Request to get ActivosFijos : {}", id);
        ActivosFijos activosFijos = activosFijosRepository.findOne(id);
        return activosFijosMapper.toDto(activosFijos);
    }

    /**
     * Delete the activosFijos by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ActivosFijos : {}", id);
        activosFijosRepository.delete(id);
    }
}
