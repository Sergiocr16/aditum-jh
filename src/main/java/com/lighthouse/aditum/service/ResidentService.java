package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Resident;
import com.lighthouse.aditum.repository.ResidentRepository;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import com.lighthouse.aditum.service.dto.HouseAccessDoorDTO;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.ResidentMapper;
import com.lighthouse.aditum.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static com.lighthouse.aditum.service.util.RandomUtil.createBitacoraAcciones;
/**
 * Service Implementation for managing Resident.
 */
@Service
@Transactional
public class ResidentService {

    private final Logger log = LoggerFactory.getLogger(ResidentService.class);

    private final ResidentRepository residentRepository;

    private final ResidentMapper residentMapper;

    private final HouseService houseService;

    private final BitacoraAccionesService bitacoraAccionesService;

    private final MacroCondominiumService macroCondominiumService;

    private final CompanyService companyService;


    public ResidentService(CompanyService companyService, MacroCondominiumService macroCondominiumService, BitacoraAccionesService bitacoraAccionesService, ResidentRepository residentRepository, ResidentMapper residentMapper, @Lazy HouseService houseService) {
        this.residentRepository = residentRepository;
        this.residentMapper = residentMapper;
        this.houseService = houseService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.macroCondominiumService = macroCondominiumService;
        this.companyService = companyService;
    }

    /**
     * Save a resident.
     *
     * @param residentDTO the entity to save
     * @return the persisted entity
     */
    public ResidentDTO save(ResidentDTO residentDTO) {
        log.debug("Request to save Resident : {}", residentDTO);
        ResidentDTO residentTemporal = new ResidentDTO();
        if(residentDTO.getId()!=null){
            residentTemporal = this.findOne(residentDTO.getId());
        }
        Resident resident = residentMapper.toEntity(residentDTO);
        resident.setDeleted(0);
        if (residentDTO.getPrincipalContact() == 1) {
            Page<ResidentDTO> residentsEnabled = this.findEnabledByHouseId(null, residentDTO.getHouseId());
            Page<ResidentDTO> residentsDisabled = this.findDisabled(null, residentDTO.getHouseId());
            List<ResidentDTO> allHouseResidents = new ArrayList<>();
            residentsEnabled.getContent().forEach(residentDTO1 -> {
                allHouseResidents.add(residentDTO1);
            });
            residentsDisabled.getContent().forEach(residentDTO2 -> {
                allHouseResidents.add(residentDTO2);
            });
            ResidentDTO currentPrincipal = null;
            for (int i = 0; i < allHouseResidents.size(); i++) {
                if (allHouseResidents.get(i).getPrincipalContact() == 1) {
                    currentPrincipal = allHouseResidents.get(i);
                }
            }
            if (currentPrincipal != null) {
                currentPrincipal.setPrincipalContact(0);
                residentRepository.save(this.residentMapper.toEntity(currentPrincipal));
            }
        }
        resident = residentRepository.save(resident);

        String concepto = "";
        if (residentDTO.getId() == null) {
            concepto = "Registro de un nuevo usuario: " + residentDTO.getName() + " " + residentDTO.getLastname();
        }else if (residentDTO.getEnabled() == 0 && residentTemporal.getEnabled() == 1) {
            concepto = "Se deshabilitó el usuario: " + residentDTO.getName() + " " + residentDTO.getLastname();
        }else if (residentDTO.getEnabled() == 1 && residentTemporal.getEnabled() == 0) {
            concepto = "Se habilitó el usuario: " + residentDTO.getName() + " " + residentDTO.getLastname();
        }else if (residentDTO.getId() != null && residentDTO.getDeleted() == 0) {
            concepto = "Modificación de los datos del usuario: " + residentDTO.getName() + " " + residentDTO.getLastname();
        }
        bitacoraAccionesService.save(createBitacoraAcciones(concepto,8, "resident-detail","Usuarios",resident.getId(),resident.getCompany().getId(),resident.getHouse().getId()));

        ResidentDTO result = residentMapper.toDto(resident);
        return result;
    }

    /**
     * Get all the residents.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ResidentDTO> findAll(Long companyId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByCompanyIdAndDeleted(companyId, 0);
        return new PageImpl<>(result).map(resident -> {
            ResidentDTO residentDTO = residentMapper.toDto(resident);
            return formatResidentAccessDoor(residentDTO);
            }
        );
    }

    @Transactional(readOnly = true)
    public Integer enableQuantityByCompany(Long companyId) {
        log.debug("Request to get Vehicule : {}", companyId);
        return residentRepository.countByEnabledAndCompanyIdAndDeleted(1, companyId, 0);
    }

    @Transactional(readOnly = true)
    public Integer disableQuantityByCompany(Long companyId) {
        log.debug("Request to get Vehicule : {}", companyId);
        return residentRepository.countByEnabledAndCompanyIdAndDeleted(0, companyId, 0);
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findEnabledByHouseId(Pageable pageable, Long houseId) {
        log.debug("Request to get all Residents");
        Page<Resident> result = residentRepository.findByEnabledAndHouseIdAndDeleted(pageable, 1, houseId, 0);
        return result.map(resident -> residentMapper.toDto(resident));
    }

    @Transactional(readOnly = true)
    public ResidentDTO findByCompanyIdAndIdentifiactionNumber(Long companyId, String identificationNumber) {
        log.debug("Request to get all Residents");
        Resident resident = residentRepository.findByCompanyIdAndIdentificationnumberAndDeleted(companyId, identificationNumber, 0);

        ResidentDTO residentDTO = residentMapper.toDto(resident);
        return residentDTO;
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findDisabledByHouseId(Pageable pageable, Long houseId) {
        log.debug("Request to get all Residents");
        Page<Resident> result = residentRepository.findByEnabledAndHouseIdAndDeleted(pageable, 0, houseId, 0);
        return result.map(resident -> residentMapper.toDto(resident));
    }
    @Transactional(readOnly = true)
    public Page<ResidentDTO> findPrincipalContactByCompanyId(Pageable pageable, Long companyId) {
        log.debug("Request to get all Residents");
        List<Resident> result = residentRepository.findByPrincipalContactAndCompanyIdAndDeleted(1, companyId, 0);
        String a = "a";
        return new PageImpl<>(result).map(resident -> residentMapper.toDto(resident));
    }
    /**
     * Get one resident by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ResidentDTO findOne(Long id) {
        log.debug("Request to get Resident : {}", id);
        Resident resident = residentRepository.findOne(id);
        ResidentDTO residentDTO = residentMapper.toDto(resident);
        return formatResidentAccessDoor(residentDTO);
    }

    @Transactional(readOnly = true)
    public ResidentDTO findOneByUserId(Long id) {
        log.debug("Request to get Resident : {}", id);
        Resident resident = residentRepository.findOneByUserId(id);
        ResidentDTO residentDTO = residentMapper.toDto(resident);
        return formatResidentAccessDoor(residentDTO);
    }

    /**
     * Delete the  resident by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Resident : {}", id);
        Resident resident = residentMapper.toEntity(this.findOne(id));
        resident.setDeleted(1);

        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        bitacoraAccionesService.save(createBitacoraAcciones("Eliminación del usuario: " + resident.getName() + " " + resident.getLastname(),8, "resident-detail","Usuarios",resident.getId(),resident.getCompany().getId(),resident.getHouse().getId()));

        residentRepository.save(resident);
    }
    @Transactional(readOnly = true)
    public Page<ResidentDTO> getAllByMacroWithFilter(Pageable pageable, Long macroId,  String filter) {
        log.debug("Request to get all Residents");
        Page<Resident> result;
        List<Long> companiesId = new ArrayList<>();
        macroCondominiumService.findOne(macroId).getCompanies().forEach(companyDTO -> {
            companiesId.add(companyDTO.getId());
        });
        result = residentRepository.findByEnabledAndDeletedAndNameContainsAndCompanyIdInOrEnabledAndDeletedAndLastnameContainsAndCompanyIdInOrEnabledAndDeletedAndSecondlastnameContainsAndCompanyIdInOrEnabledAndDeletedAndIdentificationnumberContainsAndCompanyIdIn(
            pageable,1,0,filter,companiesId,
            1,0,filter,companiesId,
            1,0,filter,companiesId,
            1,0,filter,companiesId);
        return result.map(resident -> {
            ResidentDTO residentDTO = residentMapper.toDto(resident);
            return formatResidentAccessDoor(residentDTO);
        });
    }

    @Transactional(readOnly = true)
    public ResidentDTO getOneByMacroWithIdentification(Long macroId,  String identificationnumber) {
        log.debug("Request to get all Residents");
        Resident result;
        List<Long> companiesId = new ArrayList<>();
        macroCondominiumService.findOne(macroId).getCompanies().forEach(companyDTO -> {
            companiesId.add(companyDTO.getId());
        });
        result = residentRepository.findByEnabledAndDeletedAndIdentificationnumberAndCompanyIdIn(1,0,identificationnumber,companiesId);
        if(result!=null){
            return formatResidentAccessDoor(residentMapper.toDto(result));
        }
        return null;
    }
    @Transactional(readOnly = true)
    public ResidentDTO getOneByCompanyWithIdentification(Long companyId,  String identificationnumber) {
        log.debug("Request to get all Residents");
        Resident result;
        result = residentRepository.findByEnabledAndDeletedAndIdentificationnumberAndCompanyId(1,0,identificationnumber,companyId);
        if(result!=null){
            return formatResidentAccessDoor(residentMapper.toDto(result));
        }
        return null;
    }
    @Transactional(readOnly = true)
    public Page<ResidentDTO> getAllInFilter(Pageable pageable, Long companyId, int enabled, String houseId, String owner, String name) {
        log.debug("Request to get all Residents");
        Page<Resident> result;
        if (!name.equals(" ")) {
            if (!houseId.equals("empty")) {
                if (!owner.equals("empty")) {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndNameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndSecondlastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndIdentificationnumberContains(
                        pageable, enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), name,
                        enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), name,
                        enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), name,
                        enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), name
                    );
                } else {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndHouseIdAndNameContainsOrEnabledAndCompanyIdAndDeletedAndHouseIdAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndHouseIdAndSecondlastnameContainsOrEnabledAndCompanyIdAndDeletedAndHouseIdAndIdentificationnumberContains(
                        pageable, enabled, companyId, 0, Long.parseLong(houseId), name,
                        enabled, companyId, 0, Long.parseLong(houseId), name,
                        enabled, companyId, 0, Long.parseLong(houseId), name,
                        enabled, companyId, 0, Long.parseLong(houseId), name
                        );
                }
            } else {
                if (!owner.equals("empty")) {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndNameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndSecondlastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndIdentificationnumberContains(
                        pageable, enabled, companyId, 0, Integer.parseInt(owner), name,
                        enabled, companyId, 0, Integer.parseInt(owner), name,
                        enabled, companyId, 0, Integer.parseInt(owner), name,
                        enabled, companyId, 0, Integer.parseInt(owner), name
                    );
                } else {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndNameContainsOrEnabledAndCompanyIdAndDeletedAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndSecondlastnameContainsOrEnabledAndCompanyIdAndDeletedAndIdentificationnumberContains(
                        pageable, enabled, companyId, 0, name,
                        enabled, companyId, 0, name,
                        enabled, companyId, 0,
                        name, enabled, companyId, 0, name);
                }
            }
        } else {
            if (!houseId.equals("empty")) {
                if (!owner.equals("empty")) {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseId(pageable, enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId));
                } else {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndHouseId(pageable, enabled, companyId, 0, Long.parseLong(houseId));
                }
            } else {
                if (!owner.equals("empty")) {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndIsOwner(pageable, enabled, companyId, 0, Integer.parseInt(owner));
                } else {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeleted(pageable, enabled, companyId, 0);
                }
            }
        }
        return result.map(resident -> {
            ResidentDTO residentDTO = residentMapper.toDto(resident);
            return formatResidentAccessDoor(residentDTO);
        });
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findDisabled(Pageable pageable, Long companyId) {
        log.debug("Request to get all Residents");
        Page<Resident> result = residentRepository.findByEnabledAndCompanyIdAndDeleted(pageable, 0, companyId, 0);
        return result.map(resident -> {
            ResidentDTO residentDTO = residentMapper.toDto(resident);
            return formatResidentAccessDoor(residentDTO);
        });
    }

    @Transactional(readOnly = true)
    public ResidentDTO findPrincipalContactByHouse(Long houseId) {
        Page<ResidentDTO> residents = this.findEnabledByHouseId(null, houseId);
        ResidentDTO principal = null;
        for (int i = 0; i < residents.getContent().size(); i++) {
            if (residents.getContent().get(i).getPrincipalContact() == 1) {
                principal = residents.getContent().get(i);
            }
        }
        return principal;
    }

    private ResidentDTO formatResidentAccessDoor(ResidentDTO residentDTO){
        HouseAccessDoorDTO houseClean= new HouseAccessDoorDTO();
        HouseDTO houseDTO = houseService.findOne(residentDTO.getHouseId());
        houseClean.setId(houseDTO.getId());
        houseClean.setHousenumber(houseDTO.getHousenumber());
        houseClean.setEmergencyKey(houseDTO.getEmergencyKey());
        houseClean.setSecurityKey(houseDTO.getSecurityKey());
        residentDTO.setHouseClean(houseClean);
        residentDTO.setHouse(null);
        residentDTO.setCompanyName(this.companyService.findOne(residentDTO.getCompanyId()).getName());
        return residentDTO;
    }

}
