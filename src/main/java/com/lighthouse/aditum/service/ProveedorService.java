package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Proveedor;
import com.lighthouse.aditum.repository.ProveedorRepository;
import com.lighthouse.aditum.service.dto.ProveedorDTO;
import com.lighthouse.aditum.service.mapper.ProveedorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Service Implementation for managing Proveedor.
 */
@Service
@Transactional
public class ProveedorService {

    private final Logger log = LoggerFactory.getLogger(ProveedorService.class);

    private final ProveedorRepository proveedorRepository;

    private final ProveedorMapper proveedorMapper;

    public ProveedorService(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    /**
     * Save a proveedor.
     *
     * @param proveedorDTO the entity to save
     * @return the persisted entity
     */
    public ProveedorDTO save(ProveedorDTO proveedorDTO) {
        log.debug("Request to save Proveedor : {}", proveedorDTO);
        Proveedor proveedor = proveedorMapper.toEntity(proveedorDTO);
        proveedor = proveedorRepository.save(proveedor);
        return proveedorMapper.toDto(proveedor);
    }

    /**
     *  Get all the proveedors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProveedorDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all provedoors");
        List<Proveedor> result = proveedorRepository.findByCompanyId(companyId);
        return new PageImpl<>(result).map(proveedor -> proveedorMapper.toDto(proveedor));

    }

    /**
     *  Get one proveedor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProveedorDTO findOne(Long id) {
        log.debug("Request to get Proveedor : {}", id);
        Proveedor proveedor = proveedorRepository.findOne(id);
        return proveedorMapper.toDto(proveedor);
    }

    /**
     *  Delete the  proveedor by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Proveedor : {}", id);
        proveedorRepository.delete(id);
    }
}
