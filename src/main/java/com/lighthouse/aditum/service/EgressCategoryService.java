package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.DetallePresupuesto;
import com.lighthouse.aditum.domain.EgressCategory;
import com.lighthouse.aditum.domain.Presupuesto;
import com.lighthouse.aditum.repository.EgressCategoryRepository;
import com.lighthouse.aditum.service.dto.DetallePresupuestoDTO;
import com.lighthouse.aditum.service.dto.EgressCategoryDTO;
import com.lighthouse.aditum.service.dto.PresupuestoDTO;
import com.lighthouse.aditum.service.mapper.EgressCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing EgressCategory.
 */
@Service
@Transactional
public class EgressCategoryService {

    private final Logger log = LoggerFactory.getLogger(EgressCategoryService.class);

    private final EgressCategoryRepository egressCategoryRepository;

    private final EgressCategoryMapper egressCategoryMapper;

    private final DetallePresupuestoService detallePresupuestoService;

    private final PresupuestoService presupuestoService;

    @Autowired
    public EgressCategoryService(EgressCategoryRepository egressCategoryRepository, EgressCategoryMapper egressCategoryMapper, @Lazy DetallePresupuestoService detallePresupuestoService, PresupuestoService presupuestoService) {
        this.egressCategoryRepository = egressCategoryRepository;
        this.egressCategoryMapper = egressCategoryMapper;
        this.detallePresupuestoService = detallePresupuestoService;
        this.presupuestoService = presupuestoService;
    }

    /**
     * Save a egressCategory.
     *
     * @param egressCategoryDTO the entity to save
     * @return the persisted entity
     */
    public EgressCategoryDTO save(EgressCategoryDTO egressCategoryDTO) {
        log.debug("Request to save EgressCategory : {}", egressCategoryDTO);
        EgressCategory egressCategory = egressCategoryMapper.toEntity(egressCategoryDTO);
        egressCategory = egressCategoryRepository.save(egressCategory);
        this.addInBudgets(egressCategory);
        return egressCategoryMapper.toDto(egressCategory);
    }

    public EgressCategoryDTO update(EgressCategoryDTO egressCategoryDTO) {
        log.debug("Request to save EgressCategory : {}", egressCategoryDTO);
        EgressCategory egressCategory = egressCategoryMapper.toEntity(egressCategoryDTO);
        egressCategory = egressCategoryRepository.save(egressCategory);
        return egressCategoryMapper.toDto(egressCategory);
    }

    private void addInBudgets(EgressCategory egressCategory){
    List<PresupuestoDTO> presupuestos = presupuestoService.findAll(egressCategory.getCompany().getId());
    presupuestos.forEach(presupuestoDTO -> {
        DetallePresupuestoDTO detallePresupuesto = new DetallePresupuestoDTO();
        detallePresupuesto.setCategory(egressCategory.getId()+"");
        detallePresupuesto.setValuePerMonth("0,0,0,0,0,0,0,0,0,0,0,0");
        detallePresupuesto.setType("2");
        detallePresupuesto.setPresupuestoId(presupuestoDTO.getId()+"");
        detallePresupuestoService.save(detallePresupuesto);
    });
    }

    /**
     *  Get all the egressCategories.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EgressCategoryDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all Egresses");
        List<EgressCategory> result = egressCategoryRepository.findByCompanyId(companyId);
        List<EgressCategoryDTO> resultMapped = new ArrayList<>();
        result.forEach(egressCategory -> {
               resultMapped.add(egressCategoryMapper.toDto(egressCategory));
            }
        );
        return  resultMapped;
    }
    @Transactional(readOnly = true)
    public Page<EgressCategoryDTO> findAll(Long companyId) {
        log.debug("Request to get all Egresses");
        return new PageImpl<>(egressCategoryRepository.findByCompanyId(companyId))
            .map(egressCategoryMapper::toDto);
    }
    /**
     *  Get one egressCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EgressCategoryDTO findOne(Long id) {
        log.debug("Request to get EgressCategory : {}", id);
        EgressCategory egressCategory = egressCategoryRepository.findOne(id);
        return egressCategoryMapper.toDto(egressCategory);
    }

    /**
     *  Delete the  egressCategory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EgressCategory : {}", id);
        egressCategoryRepository.delete(id);
    }
}
