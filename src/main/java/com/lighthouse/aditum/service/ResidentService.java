package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.Resident;
import com.lighthouse.aditum.repository.ResidentRepository;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import com.lighthouse.aditum.service.dto.HouseAccessDoorDTO;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.HouseMapper;
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
import java.util.*;
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

    private final HouseMapper houseMapper;


    public ResidentService(HouseMapper houseMapper, CompanyService companyService, MacroCondominiumService macroCondominiumService, BitacoraAccionesService bitacoraAccionesService, ResidentRepository residentRepository, ResidentMapper residentMapper, @Lazy HouseService houseService) {
        this.residentRepository = residentRepository;
        this.residentMapper = residentMapper;
        this.houseService = houseService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.macroCondominiumService = macroCondominiumService;
        this.companyService = companyService;
        this.houseMapper = houseMapper;
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
        if (residentDTO.getId() != null) {
            residentTemporal = this.findOne(residentDTO.getId());
        }
        Resident resident = residentMapper.toEntity(residentDTO);
        resident.setDeleted(0);
        if (residentDTO.getType() == 1) {
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
            resident.setPrincipalContact(1);
        }

        if (residentDTO.getHouses() != null) {
            Set<House> houses = new HashSet<>();
            residentDTO.getHouses().forEach(
                house -> houses.add(houseMapper.houseDTOToHouse(houseService.findOne(house.getId())))
            );
            resident.setHouses(houses);
        }


        if (residentDTO.getType() <= 2) {
                residentDTO.getHouses().forEach(house -> {
                    if (residentDTO.getType() == 1) {
                        if (residentDTO.getHouseId() != house.getId()) {
                            house.setHasOwner(true);
                        }else{
                            house.setHasOwner(false);
                        }
                    }
                    if (residentDTO.getType() == 2) {
                        house.setHasOwner(true);
                    }
                    this.houseService.save(house);
                });
            if(residentTemporal!=null){
                List<HouseDTO> removedHouses = new ArrayList<>();
                    residentTemporal.getHouses().forEach(house -> {
                        if(!residentDTO.getHouses().contains(house)){
                            removedHouses.add(house);
                        }
                    });
                  removedHouses.forEach(removedHouse->{
                      List<ResidentDTO> owners = this.findOwnerByHouse(removedHouse.getId()+"");
                      if(owners.size()>=1){
                           removedHouse.setHasOwner(false);
                          this.houseService.save(removedHouse);
                      }
                  });
//                    if (residentDTO.getType() == 1) {
//                        if (residentDTO.getHouseId() != house.getId()) {
//                            house.setHasOwner(true);
//                        }
//                    }
//                    if (residentDTO.getType() == 2) {
//                        house.setHasOwner(true);
//                    }
            }

        }

        resident = residentRepository.save(resident);

        String concepto = "";
        if (residentDTO.getId() == null) {
            concepto = "Registro de un nuevo usuario: " + residentDTO.getName() + " " + residentDTO.getLastname();
        } else if (residentDTO.getEnabled() == 0 && residentTemporal.getEnabled() == 1) {
            concepto = "Se deshabilitó el usuario: " + residentDTO.getName() + " " + residentDTO.getLastname();
        } else if (residentDTO.getEnabled() == 1 && residentTemporal.getEnabled() == 0) {
            concepto = "Se habilitó el usuario: " + residentDTO.getName() + " " + residentDTO.getLastname();
        } else if (residentDTO.getId() != null && residentDTO.getDeleted() == 0) {
            concepto = "Modificación de los datos del usuario: " + residentDTO.getName() + " " + residentDTO.getLastname();
        }
//        bitacoraAccionesService.save(createBitacoraAcciones(concepto, 8, "resident-detail", "Usuarios", resident.getId(), resident.getCompany().getId(), resident.getHouse().getId()));

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
        Set<HouseDTO> houses = new HashSet<>();
        resident.getHouses().forEach(house -> houses.add(houseMapper.houseToHouseDTO(house)));
        residentDTO.setHouses(houses);
        return formatResidentAccessDoor(residentDTO);
    }

    @Transactional(readOnly = true)
    public ResidentDTO findOneByUserId(Long id) {
        log.debug("Request to get Resident : {}", id);
        Resident resident = residentRepository.findOneByUserId(id);
        ResidentDTO residentDTO = residentMapper.toDto(resident);
        Set<HouseDTO> houses = new HashSet<>();
        resident.getHouses().forEach(house -> houses.add(houseMapper.houseToHouseDTO(house)));
        residentDTO.setHouses(houses);
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
//        if(resident.getType()<=2){
//            resident.getHouses().forEach(house -> {
//                bitacoraAccionesService.save(createBitacoraAcciones("Eliminación del propietario: " + resident.getName() + " " + resident.getLastname(), 8, "resident-detail", "Usuarios", resident.getId(), resident.getCompany().getId(), house.getId()));
//            });
//        }else{
//            bitacoraAccionesService.save(createBitacoraAcciones("Eliminación del usuario: " + resident.getName() + " " + resident.getLastname(), 8, "resident-detail", "Usuarios", resident.getId(), resident.getCompany().getId(), resident.getHouse().getId()));
//        }


        residentRepository.save(resident);
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> getAllByMacroWithFilter(Pageable pageable, Long macroId, String filter) {
        log.debug("Request to get all Residents");
        Page<Resident> result;
        List<Long> companiesId = new ArrayList<>();
        macroCondominiumService.findOne(macroId).getCompanies().forEach(companyDTO -> {
            companiesId.add(companyDTO.getId());
        });
        result = residentRepository.findByEnabledAndDeletedAndNameContainsAndCompanyIdInOrEnabledAndDeletedAndLastnameContainsAndCompanyIdInOrEnabledAndDeletedAndSecondlastnameContainsAndCompanyIdInOrEnabledAndDeletedAndIdentificationnumberContainsAndCompanyIdIn(
            pageable, 1, 0, filter, companiesId,
            1, 0, filter, companiesId,
            1, 0, filter, companiesId,
            1, 0, filter, companiesId);
        return result.map(resident -> {
            ResidentDTO residentDTO = residentMapper.toDto(resident);
            return formatResidentAccessDoor(residentDTO);
        });
    }

    @Transactional(readOnly = true)
    public ResidentDTO getOneByMacroWithIdentification(Long macroId, String identificationnumber) {
        log.debug("Request to get all Residents");
        Resident result;
        List<Long> companiesId = new ArrayList<>();
        macroCondominiumService.findOne(macroId).getCompanies().forEach(companyDTO -> {
            companiesId.add(companyDTO.getId());
        });
        result = residentRepository.findByEnabledAndDeletedAndIdentificationnumberAndCompanyIdIn(1, 0, identificationnumber, companiesId);
        if (result != null) {
            return formatResidentAccessDoor(residentMapper.toDto(result));
        }
        return null;
    }

    @Transactional(readOnly = true)
    public ResidentDTO getOneByCompanyWithIdentification(Long companyId, String identificationnumber) {
        log.debug("Request to get all Residents");
        Resident result;
        result = residentRepository.findByDeletedAndIdentificationnumberAndCompanyIdAndTypeNot(0, identificationnumber, companyId,2);
        if (result != null) {
            return formatResidentAccessDoor(residentMapper.toDto(result));
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<ResidentDTO> findOwnerByHouse(String houseId) {
        List<House> housesId = new ArrayList<>();
        this.houseService.findOne(Long.parseLong(houseId));
        housesId.add(houseMapper.houseDTOToHouse(this.houseService.findOne(Long.parseLong(houseId))));
        List<Resident> result = residentRepository.findByHouses(housesId);
        List<ResidentDTO> formattedResidents = new ArrayList<>();
         result.forEach(resident -> {
            ResidentDTO residentDTO = residentMapper.toDto(resident);
            Set<HouseDTO> houses = new HashSet<>();
            resident.getHouses().forEach(house -> houses.add(houseMapper.houseToHouseDTO(house)));
            residentDTO.setHouses(houses);
            formattedResidents.add(formatResidentAccessDoor(residentDTO));
        });
         return formattedResidents;
    }
    @Transactional(readOnly = true)
    public List<ResidentDTO> findOwnerByHouseLiving(String houseId) {
        List<Resident> result = residentRepository.findByHouseIdAndTypeIsLessThan(Long.parseLong(houseId),3);
        List<ResidentDTO> formattedResidents = new ArrayList<>();
        result.forEach(resident -> {
            ResidentDTO residentDTO = residentMapper.toDto(resident);
            Set<HouseDTO> houses = new HashSet<>();
            resident.getHouses().forEach(house -> houses.add(houseMapper.houseToHouseDTO(house)));
            residentDTO.setHouses(houses);
            formattedResidents.add(formatResidentAccessDoor(residentDTO));
        });
        return formattedResidents;
    }
    @Transactional(readOnly = true)
    public HouseDTO isOwnerInHouses(String housesId) {
        List<ResidentDTO> residentesQueYaViven = new ArrayList<>();
        String[] housesIds = housesId.split(",");
        for (int i = 0; i < housesIds.length; i++) {
            List<Resident> result = residentRepository.findByHouseIdAndTypeIsLessThan(Long.parseLong(housesIds[i]),3);
            if(result.size()>0){
                return this.houseService.findOne(Long.parseLong(housesIds[0]));
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> findOwners(Pageable pageable, Long companyId, String houseId, String name) {
        log.debug("Request to get all Residents");
        Page<Resident> result = null;
        List<House> housesConsult = new ArrayList<>();
        if (!houseId.equals("empty")) {
            HouseDTO house = this.houseService.findOne(Long.parseLong(houseId));
            housesConsult.add(houseMapper.houseDTOToHouse(house));
        }
        if (!name.equals(" ")) {
            if (!houseId.equals("empty")) {
                result = residentRepository.findByTypeIsLessThanAndCompanyIdAndHousesAndDeletedAndNameContainsOrTypeIsLessThanAndCompanyIdAndHousesAndDeletedAndLastnameContainsOrTypeIsLessThanAndCompanyIdAndHousesAndDeletedAndSecondlastnameContainsOrTypeIsLessThanAndCompanyIdAndHousesAndDeletedAndIdentificationnumberContains(pageable,
                    3, companyId, housesConsult, 0, name,
                    3, companyId, housesConsult, 0, name,
                    3, companyId, housesConsult, 0, name,
                    3, companyId, housesConsult, 0, name
                );
            } else {
                result = residentRepository.findByTypeLessThanAndCompanyIdAndDeletedAndNameContainsOrTypeIsLessThanAndCompanyIdAndDeletedAndLastnameContainsOrTypeIsLessThanAndCompanyIdAndDeletedAndSecondlastnameContainsOrTypeIsLessThanAndCompanyIdAndDeletedAndIdentificationnumberContains(pageable,
                    3, companyId, 0, name,
                    3, companyId, 0, name,
                    3, companyId, 0, name,
                    3, companyId, 0, name
                );
            }
        } else {
            if (!houseId.equals("empty")) {
                result = residentRepository.findByTypeLessThanAndCompanyIdAndHousesAndDeleted(pageable,
                    3, companyId, housesConsult, 0);

            } else {

                result = residentRepository.findByTypeLessThanAndCompanyIdAndDeleted(pageable, 3, companyId, 0);
            }
        }
        return result.map(resident -> {
            ResidentDTO residentDTO = residentMapper.toDto(resident);
            Set<HouseDTO> houses = new HashSet<>();
            resident.getHouses().forEach(house -> houses.add(houseMapper.houseToHouseDTO(house)));
            residentDTO.setHouses(houses);
            return formatResidentAccessDoor(residentDTO);
        });
    }

    @Transactional(readOnly = true)
    public Page<ResidentDTO> getAllInFilter(Pageable pageable, Long companyId, int enabled, String houseId, String owner, String name) {
        log.debug("Request to get all Residents");
        Page<Resident> result;
        if (!name.equals(" ")) {
            if (!houseId.equals("empty")) {
                if (!owner.equals("empty")) {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndNameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndSecondlastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndIdentificationnumberContainsAndTypeNot(
                        pageable, enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), name, 2,
                        enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), name, 2,
                        enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), name, 2,
                        enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), name, 2
                    );
                } else {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndHouseIdAndNameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndHouseIdAndLastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndHouseIdAndSecondlastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndHouseIdAndIdentificationnumberContainsAndTypeNot(
                        pageable, enabled, companyId, 0, Long.parseLong(houseId), name, 2,
                        enabled, companyId, 0, Long.parseLong(houseId), name, 2,
                        enabled, companyId, 0, Long.parseLong(houseId), name, 2,
                        enabled, companyId, 0, Long.parseLong(houseId), name, 2
                    );
                }
            } else {
                if (!owner.equals("empty")) {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndNameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndLastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndSecondlastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndIdentificationnumberContainsAndTypeNot(
                        pageable, enabled, companyId, 0, Integer.parseInt(owner), name, 2,
                        enabled, companyId, 0, Integer.parseInt(owner), name, 2,
                        enabled, companyId, 0, Integer.parseInt(owner), name, 2,
                        enabled, companyId, 0, Integer.parseInt(owner), name, 2
                    );
                } else {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndNameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndLastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndSecondlastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIdentificationnumberContainsAndTypeNot(
                        pageable, enabled, companyId, 0, name, 2,
                        enabled, companyId, 0, name, 2,
                        enabled, companyId, 0, name, 2,
                        enabled, companyId, 0, name, 2);
                }
            }
        } else {
            if (!houseId.equals("empty")) {
                if (!owner.equals("empty")) {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndTypeNot(pageable, enabled, companyId, 0, Integer.parseInt(owner), Long.parseLong(houseId), 2);
                } else {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndHouseIdAndTypeNot(pageable, enabled, companyId, 0, Long.parseLong(houseId), 2);
                }
            } else {
                if (!owner.equals("empty")) {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndTypeNot(pageable, enabled, companyId, 0, Integer.parseInt(owner), 2);
                } else {
                    result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndTypeNot(pageable, enabled, companyId, 0, 2);
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
        Page<Resident> result = residentRepository.findByEnabledAndCompanyIdAndDeletedAndTypeNot(pageable, 0, companyId, 0, 2);
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

    private ResidentDTO formatResidentAccessDoor(ResidentDTO residentDTO) {
        HouseAccessDoorDTO houseClean = new HouseAccessDoorDTO();
        if (residentDTO.getType() > 2) {
            HouseDTO houseDTO = houseService.findOne(residentDTO.getHouseId());
            houseClean.setId(houseDTO.getId());
            houseClean.setHousenumber(houseDTO.getHousenumber());
            houseClean.setEmergencyKey(houseDTO.getEmergencyKey());
            houseClean.setSecurityKey(houseDTO.getSecurityKey());
            residentDTO.setHouseClean(houseClean);
            residentDTO.setHouse(null);
        }
        residentDTO.setCompanyName(this.companyService.findOne(residentDTO.getCompanyId()).getName());
        return residentDTO;
    }

}
