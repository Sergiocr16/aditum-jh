package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Presupuesto;
import com.lighthouse.aditum.repository.PresupuestoRepository;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import com.lighthouse.aditum.service.dto.PresupuestoDTO;
import com.lighthouse.aditum.service.mapper.PresupuestoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lighthouse.aditum.service.util.RandomUtil.createBitacoraAcciones;

/**
 * Service Implementation for managing Presupuesto.
 */
@Service
@Transactional
public class PresupuestoService {

    private final Logger log = LoggerFactory.getLogger(PresupuestoService.class);

    private final PresupuestoRepository presupuestoRepository;

    private final PresupuestoMapper presupuestoMapper;


    private final BitacoraAccionesService bitacoraAccionesService;

    private final AdminInfoService adminInfoService;

    private final UserService userService;

    public PresupuestoService(UserService userService,AdminInfoService adminInfoService,BitacoraAccionesService bitacoraAccionesService,PresupuestoRepository presupuestoRepository, PresupuestoMapper presupuestoMapper) {
        this.presupuestoRepository = presupuestoRepository;
        this.presupuestoMapper = presupuestoMapper;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.adminInfoService = adminInfoService;
        this.userService = userService;
    }

    /**
     * Save a presupuesto.
     *
     * @param presupuestoDTO the entity to save
     * @return the persisted entity
     */
    public PresupuestoDTO save(PresupuestoDTO presupuestoDTO) {
        log.debug("Request to save Presupuesto : {}", presupuestoDTO);
        Presupuesto presupuesto = presupuestoMapper.toEntity(presupuestoDTO);
        presupuesto = presupuestoRepository.save(presupuesto);




        String concepto = "";
        if(presupuestoDTO.getId()==null){
            concepto = "Registro de nuevo presupuesto: " + presupuestoDTO.getAnno();
        }else if(presupuestoDTO.getId()!=null && presupuestoDTO.getDeleted()==0){
            concepto = "Edición del presupuesto: " + presupuestoDTO.getAnno();
        }else{
            concepto = "Eliminación del presupuesto: " + presupuestoDTO.getAnno();
        }

        bitacoraAccionesService.save(createBitacoraAcciones(concepto,4, "presupuesto-detail","Presupuestos",presupuesto.getId(),presupuesto.getCompany().getId(),null));

        return presupuestoMapper.toDto(presupuesto);
    }

    /**
     *  Get all the presupuestos.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PresupuestoDTO> findAll(Long companyId) {
        log.debug("Request to get all Presupuestos");
        return presupuestoRepository.findByCompanyIdAndDeleted(companyId,0).stream()
            .map(presupuestoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the presupuestos.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PresupuestoDTO> findByBudgetsDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId) {
        log.debug("Request to get all Presupuestos");
        return presupuestoRepository.findByBudgetsDatesBetweenAndCompany(initialDate,finalDate,companyId,0).stream()
            .map(presupuestoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    /**
     *  Get one presupuesto by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PresupuestoDTO findOne(Long id) {
        log.debug("Request to get Presupuesto : {}", id);
        Presupuesto presupuesto = presupuestoRepository.findOne(id);
        return presupuestoMapper.toDto(presupuesto);
    }

    /**
     *  Delete the  presupuesto by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Presupuesto : {}", id);
        presupuestoRepository.delete(id);
    }
}
