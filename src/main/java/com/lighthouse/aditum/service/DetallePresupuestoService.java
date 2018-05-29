package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.DetallePresupuesto;
import com.lighthouse.aditum.repository.DetallePresupuestoRepository;
import com.lighthouse.aditum.service.dto.DetallePresupuestoDTO;
import com.lighthouse.aditum.service.mapper.DetallePresupuestoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing DetallePresupuesto.
 */
@Service
@Transactional
public class DetallePresupuestoService {

    private final Logger log = LoggerFactory.getLogger(DetallePresupuestoService.class);

    private final DetallePresupuestoRepository detallePresupuestoRepository;

    private final DetallePresupuestoMapper detallePresupuestoMapper;

    public DetallePresupuestoService(DetallePresupuestoRepository detallePresupuestoRepository, DetallePresupuestoMapper detallePresupuestoMapper) {
        this.detallePresupuestoRepository = detallePresupuestoRepository;
        this.detallePresupuestoMapper = detallePresupuestoMapper;
    }

    /**
     * Save a detallePresupuesto.
     *
     * @param detallePresupuestoDTO the entity to save
     * @return the persisted entity
     */
    public DetallePresupuestoDTO save(DetallePresupuestoDTO detallePresupuestoDTO) {
        log.debug("Request to save DetallePresupuesto : {}", detallePresupuestoDTO);
        DetallePresupuesto detallePresupuesto = detallePresupuestoMapper.toEntity(detallePresupuestoDTO);
        detallePresupuesto = detallePresupuestoRepository.save(detallePresupuesto);
        return detallePresupuestoMapper.toDto(detallePresupuesto);
    }

    /**
     *  Get all the detallePresupuestos.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<DetallePresupuestoDTO> findAll() {
        log.debug("Request to get all DetallePresupuestos");
        return detallePresupuestoRepository.findAll().stream()
            .map(detallePresupuestoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one detallePresupuesto by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DetallePresupuestoDTO findOne(Long id) {
        log.debug("Request to get DetallePresupuesto : {}", id);
        DetallePresupuesto detallePresupuesto = detallePresupuestoRepository.findOne(id);
        return detallePresupuestoMapper.toDto(detallePresupuesto);
    }

    /**
     *  Delete the  detallePresupuesto by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DetallePresupuesto : {}", id);
        detallePresupuestoRepository.delete(id);
    }
}
