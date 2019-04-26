package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.BitacoraAcciones;
import com.lighthouse.aditum.repository.BitacoraAccionesRepository;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import com.lighthouse.aditum.service.mapper.BitacoraAccionesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing BitacoraAcciones.
 */
@Service
@Transactional
public class BitacoraAccionesService {

    private final Logger log = LoggerFactory.getLogger(BitacoraAccionesService.class);

    private final BitacoraAccionesRepository bitacoraAccionesRepository;

    private final BitacoraAccionesMapper bitacoraAccionesMapper;

    private final AdminInfoService adminInfoService;

    private final UserService userService;

    public BitacoraAccionesService(UserService userService, AdminInfoService adminInfoService,BitacoraAccionesRepository bitacoraAccionesRepository, BitacoraAccionesMapper bitacoraAccionesMapper) {
        this.bitacoraAccionesRepository = bitacoraAccionesRepository;
        this.bitacoraAccionesMapper = bitacoraAccionesMapper;
        this.adminInfoService = adminInfoService;
        this.userService = userService;

    }

    /**
     * Save a bitacoraAcciones.
     *
     * @param bitacoraAccionesDTO the entity to save
     * @return the persisted entity
     */
    public BitacoraAccionesDTO save(BitacoraAccionesDTO bitacoraAccionesDTO) {
        log.debug("Request to save BitacoraAcciones : {}", bitacoraAccionesDTO);
        if (adminInfoService.findOneByUserId(userService.getUserWithAuthorities().getId()) != null) {
            bitacoraAccionesDTO.setIdResponsable(adminInfoService.findOneByUserId(userService.getUserWithAuthorities().getId()).getId());
        }
        BitacoraAcciones bitacoraAcciones = bitacoraAccionesMapper.toEntity(bitacoraAccionesDTO);
        bitacoraAcciones.setUrlState(bitacoraAccionesDTO.getUrlState());
        bitacoraAcciones = bitacoraAccionesRepository.save(bitacoraAcciones);
        return bitacoraAccionesMapper.toDto(bitacoraAcciones);
    }

    /**
     * Get all the bitacoraAcciones.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BitacoraAccionesDTO> findAll(Pageable pageable,Long companyId,int type) {
        log.debug("Request to get all BitacoraAcciones");
        if(type==0){
            Page<BitacoraAcciones> acciones = bitacoraAccionesRepository.findByCompanyId(pageable,companyId);
            Page<BitacoraAccionesDTO> accionesDTO = bitacoraAccionesRepository.findByCompanyId(pageable,companyId)
                .map(bitacoraAccionesMapper::toDto);
            for (int i = 0; i < accionesDTO.getContent().size(); i++) {
                if(accionesDTO.getContent().get(i).getIdResponsable()!=null){
                    accionesDTO.getContent().get(i).setResponsable(adminInfoService.findOne(accionesDTO.getContent().get(i).getIdResponsable()));
                }
                accionesDTO.getContent().get(i).setUrlState(acciones.getContent().get(i).getUrlState());

            }
            return accionesDTO;
        }else{
            Page<BitacoraAcciones> acciones = bitacoraAccionesRepository.findByCompanyIdAndType(pageable,companyId,type);
            Page<BitacoraAccionesDTO> accionesDTO = bitacoraAccionesRepository.findByCompanyIdAndType(pageable,companyId,type)
                .map(bitacoraAccionesMapper::toDto);
            for (int i = 0; i < accionesDTO.getContent().size(); i++) {
                accionesDTO.getContent().get(i).setResponsable(adminInfoService.findOne(accionesDTO.getContent().get(i).getIdResponsable()));
                accionesDTO.getContent().get(i).setUrlState(acciones.getContent().get(i).getUrlState());
            }
            return accionesDTO;
        }



    }

    /**
     * Get one bitacoraAcciones by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BitacoraAccionesDTO findOne(Long id) {
        log.debug("Request to get BitacoraAcciones : {}", id);
        BitacoraAcciones bitacoraAcciones = bitacoraAccionesRepository.findOne(id);
        return bitacoraAccionesMapper.toDto(bitacoraAcciones);
    }

    /**
     * Delete the bitacoraAcciones by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BitacoraAcciones : {}", id);
        bitacoraAccionesRepository.delete(id);
    }
}
