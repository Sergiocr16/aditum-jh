package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Vehicule;
import com.lighthouse.aditum.repository.VehiculeRepository;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import com.lighthouse.aditum.service.dto.VehiculeDTO;
import com.lighthouse.aditum.service.mapper.VehiculeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lighthouse.aditum.service.util.RandomUtil.createBitacoraAcciones;

/**
 * Service Implementation for managing Vehicule.
 */
@Service
@Transactional
public class VehiculeService {

    private final Logger log = LoggerFactory.getLogger(VehiculeService.class);

    private final VehiculeRepository vehiculeRepository;

    private final VehiculeMapper vehiculeMapper;

    private final HouseService houseService;

    private final BitacoraAccionesService bitacoraAccionesService;

    private final AdminInfoService adminInfoService;

    private final UserService userService;


    public VehiculeService(UserService userService, AdminInfoService adminInfoService, BitacoraAccionesService bitacoraAccionesService, VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper, @Lazy HouseService houseService) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeMapper = vehiculeMapper;
        this.houseService = houseService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.adminInfoService = adminInfoService;
        this.userService = userService;
    }

    /**
     * Save a vehicule.
     *
     * @param vehiculeDTO the entity to save
     * @return the persisted entity
     */
    public VehiculeDTO save(VehiculeDTO vehiculeDTO) {
        log.debug("Request to save Vehicule : {}", vehiculeDTO);
        VehiculeDTO vehiculeTemporal = new VehiculeDTO();
        if(vehiculeDTO.getId()!=null){
            vehiculeTemporal = this.findOne(vehiculeDTO.getId());
        }
        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO);
        vehicule = vehiculeRepository.save(vehicule);
        vehicule.setDeleted(0);

        String concepto = "";

            if (vehiculeDTO.getId() == null) {
                concepto = "Registro de un nuevo vehículo: " + vehiculeDTO.getBrand() + ", placa: " + vehiculeDTO.getLicenseplate();
            }else if (vehiculeDTO.getEnabled() == 0 && vehiculeTemporal.getEnabled() == 1) {
                concepto = "Se deshabilitó el vehículo: " + vehiculeDTO.getBrand() + ", placa: " + vehiculeDTO.getLicenseplate();
            }else if (vehiculeDTO.getEnabled() == 1 && vehiculeTemporal.getEnabled() == 0) {
                concepto = "Se habilitó el vehículo: " + vehiculeDTO.getBrand() + ", placa: " + vehiculeDTO.getLicenseplate();
            }else if (vehiculeDTO.getId() != null && vehiculeDTO.getDeleted() == 0) {
                concepto = "Modificación de los datos del vehículo: " + vehiculeDTO.getBrand() + ", placa: " + vehiculeDTO.getLicenseplate();
            }


        bitacoraAccionesService.save(createBitacoraAcciones(concepto,9, null,"Vehículos",vehicule.getId(),vehicule.getCompany().getId(),vehicule.getHouse().getId()));

        VehiculeDTO result = vehiculeMapper.toDto(vehicule);
        return result;
    }

    /**
     * Get all the vehicules.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findAll(Long companyId) {
        log.debug("Request to get all Vehicules");
        List<Vehicule> result = vehiculeRepository.findByCompanyIdAndDeleted(companyId, 0);

        return new PageImpl<>(result).map(vehicule -> {
            VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);
            vehiculeDTO.setHouse(houseService.findOne(vehiculeDTO.getHouseId()));
            return vehiculeDTO;
        });
    }

    /**
     * Get one vehicule by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public VehiculeDTO findOne(Long id) {
        log.debug("Request to get Vehicule : {}", id);
        Vehicule vehicule = vehiculeRepository.findOne(id);
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);
        return vehiculeDTO;
    }

    @Transactional(readOnly = true)
    public VehiculeDTO findOneByCompanyAndPlate(Long id, String licensePlate) {
        log.debug("Request to get Vehicule : {}", id);
        Vehicule vehicule = vehiculeRepository.findOneByCompanyIdAndLicenseplateAndDeleted(id, licensePlate, 0);
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);
        return vehiculeDTO;
    }

    @Transactional(readOnly = true)
    public Integer enableQuantityByCompany(Long companyId) {
        log.debug("Request to get Vehicule : {}", companyId);
        return vehiculeRepository.countByEnabledAndCompanyIdAndDeleted(1, companyId, 0);
    }

    @Transactional(readOnly = true)
    public Integer disableQuantityByCompany(Long companyId) {
        log.debug("Request to get Vehicule : {}", companyId);
        return vehiculeRepository.countByEnabledAndCompanyIdAndDeleted(0, companyId, 0);
    }

    /**
     * Delete the  vehicule by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Vehicule : {}", id);
        Vehicule vehicule = vehiculeMapper.toEntity(this.findOne(id));
        vehicule.setDeleted(1);

        bitacoraAccionesService.save(createBitacoraAcciones("Eliminación del vehículo: " + vehicule.getBrand() + ", placa: " + vehicule.getLicenseplate(),9, null,"Vehículos",vehicule.getId(),vehicule.getCompany().getId(),vehicule.getHouse().getId()));

        vehiculeRepository.save(vehicule);
    }


    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findEnabled(Pageable pageable, Long companyId) {
        log.debug("Request to get all Residents");
        List<Vehicule> result = vehiculeRepository.findByEnabledAndCompanyIdAndDeleted(1, companyId, 0);
        return new PageImpl<>(result).map(vehicule -> vehiculeMapper.toDto(vehicule));
    }

    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findByFilter(Pageable pageable, Long companyId, String houseId, int enabled, String licensePlate) {
        Page<Vehicule> result;
        if (!licensePlate.equals(" ")) {
            if (!houseId.equals("empty")) {
                result = vehiculeRepository.findByEnabledAndCompanyIdAndDeletedAndHouseIdAndLicenseplateContains(pageable, enabled, companyId, 0, Long.parseLong(houseId),licensePlate);
            } else {
                result = vehiculeRepository.findByEnabledAndCompanyIdAndDeletedAndLicenseplateContains(pageable, enabled, companyId, 0,licensePlate);
            }
        }else{
            if (!houseId.equals("empty")) {
                result = vehiculeRepository.findByEnabledAndCompanyIdAndDeletedAndHouseId(pageable, enabled, companyId, 0, Long.parseLong(houseId));
            } else {
                result = vehiculeRepository.findByEnabledAndCompanyIdAndDeleted(pageable, enabled, companyId, 0);
            }
        }
        return result.map(vehicule -> {
            VehiculeDTO vehiculeDto = vehiculeMapper.toDto(vehicule);
            vehiculeDto.setHouse(houseService.findOne(vehiculeDto.getHouseId()));
            return vehiculeDto;
        });
    }


    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findDisabled(Pageable pageable, Long companyId) {
        log.debug("Request to get all Residents");
        List<Vehicule> result = vehiculeRepository.findByEnabledAndCompanyIdAndDeleted(0, companyId, 0);
        return new PageImpl<>(result).map(vehicule -> vehiculeMapper.toDto(vehicule));

    }

    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findEnabledByHouse(Pageable pageable, Long houseId) {
        log.debug("Request to get all Residents");
        List<Vehicule> result = vehiculeRepository.findByEnabledAndHouseIdAndDeleted(1, houseId, 0);
        return new PageImpl<>(result).map(vehicule -> vehiculeMapper.toDto(vehicule));

    }

    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findDisabledByHouse(Pageable pageable, Long houseId) {
        log.debug("Request to get all Residents");
        List<Vehicule> result = vehiculeRepository.findByEnabledAndHouseIdAndDeleted(0, houseId, 0);
        return new PageImpl<>(result).map(vehicule -> vehiculeMapper.toDto(vehicule));

    }


}
