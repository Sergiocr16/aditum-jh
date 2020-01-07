package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.DetallePresupuesto;
import com.lighthouse.aditum.repository.DetallePresupuestoRepository;
import com.lighthouse.aditum.service.dto.DetallePresupuestoDTO;
import com.lighthouse.aditum.service.dto.EgressCategoryDTO;
import com.lighthouse.aditum.service.mapper.DetallePresupuestoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private final EgressCategoryService egressCategoryService;

    public DetallePresupuestoService(DetallePresupuestoRepository detallePresupuestoRepository, DetallePresupuestoMapper detallePresupuestoMapper,EgressCategoryService egressCategoryService) {
        this.detallePresupuestoRepository = detallePresupuestoRepository;
        this.detallePresupuestoMapper = detallePresupuestoMapper;
        this.egressCategoryService = egressCategoryService;
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
//    @Transactional(readOnly = true)
//    public List<DetallePresupuestoDTO> getCategoriesByBudget(Long budgetId) {
//        log.debug("Request to get budget details");
//        return detallePresupuestoRepository.findByPresupuestoId(budgetId).stream()
//            .map(detallePresupuestoMapper::toDto)
//            .collect(Collectors.toCollection(LinkedList::new));
//    }

    public List<DetallePresupuestoDTO> getCategoriesByBudget(Pageable pageable, String budgetId) {
        log.debug("Request to get all Visitants in last month by house");
        List<DetallePresupuesto> result = detallePresupuestoRepository.findByPresupuestoId(budgetId);
        List<DetallePresupuestoDTO> detallesDTO = new ArrayList<>();
        result.forEach(detallePresupuesto -> {
            detallesDTO.add(detallePresupuestoMapper.toDto(detallePresupuesto));
        });

        detallesDTO.forEach(detallePresupuestoDTO -> {
        if(Integer.parseInt(detallePresupuestoDTO.getType())==2){
           EgressCategoryDTO egressCategoryDTO = egressCategoryService.findOne(Long.parseLong(detallePresupuestoDTO.getCategory()));
           detallePresupuestoDTO.setCategoryName(egressCategoryDTO.getCategory());
           detallePresupuestoDTO.setGroup(egressCategoryDTO.getGroup());
        }
         });
        return detallesDTO;
    }
    public List<DetallePresupuestoDTO> getIngressCategoriesByBudget(String budgetId) {
        log.debug("Request to get all Visitants in last month by house");
        List<DetallePresupuestoDTO> detallesDTO = detallePresupuestoRepository.findByPresupuestoIdAndType(budgetId,"1").stream()
            .map(detallePresupuestoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        detallesDTO.forEach(detallePresupuestoDTO -> {
            if(Integer.parseInt(detallePresupuestoDTO.getType())==2){
                EgressCategoryDTO egressCategoryDTO = egressCategoryService.findOne(Long.parseLong(detallePresupuestoDTO.getCategory()));
                detallePresupuestoDTO.setCategory(egressCategoryDTO.getCategory());
                detallePresupuestoDTO.setGroup(egressCategoryDTO.getGroup());
            }
        });
        return detallesDTO;
    }
    public List<DetallePresupuestoDTO> getEgressCategoriesByBudget(String budgetId) {
        log.debug("Request to get all Visitants in last month by house");
        List<DetallePresupuestoDTO> detallesDTO = detallePresupuestoRepository.findByPresupuestoIdAndType(budgetId,"2").stream()
            .map(detallePresupuestoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        return detallesDTO;
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
