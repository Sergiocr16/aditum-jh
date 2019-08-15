package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.MacroCondominium;
import com.lighthouse.aditum.domain.VisitantInvitation;
import com.lighthouse.aditum.repository.MacroCondominiumRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.MacroCondominiumMapper;
import org.hibernate.annotations.LazyToOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing MacroCondominium.
 */
@Service
@Transactional
public class MacroCondominiumService {

    private final Logger log = LoggerFactory.getLogger(MacroCondominiumService.class);

    private final MacroCondominiumRepository macroCondominiumRepository;

    private final MacroCondominiumMapper macroCondominiumMapper;

    private final ResidentService residentService;

    private final VehiculeService vehiculeService;

    private final VisitantInvitationService visitantInvitationService;

    private final CompanyService companyService;

    private final HouseService houseService;


    public MacroCondominiumService(@Lazy VehiculeService vehiculeService, @Lazy HouseService houseService, @Lazy CompanyService companyService, @Lazy ResidentService residentService, @Lazy VisitantInvitationService visitantInvitationService, MacroCondominiumRepository macroCondominiumRepository, MacroCondominiumMapper macroCondominiumMapper) {
        this.macroCondominiumRepository = macroCondominiumRepository;
        this.macroCondominiumMapper = macroCondominiumMapper;
        this.residentService = residentService;
        this.visitantInvitationService = visitantInvitationService;
        this.companyService = companyService;
        this.vehiculeService = vehiculeService;
        this.houseService = houseService;
    }

    /**
     * Save a macroCondominium.
     *
     * @param macroCondominiumDTO the entity to save
     * @return the persisted entity
     */
    public MacroCondominiumDTO save(MacroCondominiumDTO macroCondominiumDTO) {
        log.debug("Request to save MacroCondominium : {}", macroCondominiumDTO);
        MacroCondominium macroCondominium = macroCondominiumMapper.toEntity(macroCondominiumDTO);
        macroCondominium = macroCondominiumRepository.save(macroCondominium);
        return macroCondominiumMapper.toDto(macroCondominium);
    }

    /**
     * Get all the macroCondominiums.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MacroCondominiumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MacroCondominiums");
        return macroCondominiumRepository.findAll(pageable)
            .map(macroCondominiumMapper::toDto);
    }

    /**
     * Get one macroCondominium by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MacroCondominiumDTO findOne(Long id) {
        log.debug("Request to get MacroCondominium : {}", id);
        MacroCondominium macroCondominium = macroCondominiumRepository.findOneWithEagerRelationships(id);
        return macroCondominiumMapper.toDto(macroCondominium);
    }

    @Transactional(readOnly = true)
    public AuthorizedUserAccessDoorDTO findAuthorized(Long id, String identification) {
        log.debug("Request to get MacroCondominium : {}", id);
        ResidentDTO residentDTO = this.residentService.getOneByMacroWithIdentification(id, identification);
        if (residentDTO != null) {
            return this.mapResidentDTOToAuthorizedDTO(residentDTO);
        } else {
            List<VisitantInvitationDTO> visitantInvitationDTOS = this.visitantInvitationService.getByMacroWithIdentification(id, identification);
            if (!visitantInvitationDTOS.isEmpty()) {
                return this.mapVisitorInvitedToAuthorizedDTO(visitantInvitationDTOS);
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public AuthorizedUserAccessDoorDTO findAuthorizedVehicules(Long id, String plate) {
        log.debug("Request to get MacroCondominium : {}", id);
        VehiculeDTO vehiculeDTO = this.vehiculeService.getOneByMacroWithIdentification(id, plate);
        if (vehiculeDTO != null) {
            return this.mapVehiculeDTOtoAuthorizedDTO(vehiculeDTO);
        } else {
            List<VisitantInvitationDTO> visitantInvitationDTOS = this.visitantInvitationService.getByMacroWithPlate(id, plate);
            if (!visitantInvitationDTOS.isEmpty()) {
                return this.mapVisitorInvitedToAuthorizedDTO(visitantInvitationDTOS);
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public MacroCondominiumDTO findMicrosInOne(Long id) {
        log.debug("Request to get MacroCondominium : {}", id);
        MacroCondominium macroCondominium = macroCondominiumRepository.findOneWithEagerRelationships(id);
        return macroCondominiumMapper.toDto(macroCondominium);
    }

    /**
     * Delete the macroCondominium by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MacroCondominium : {}", id);
        macroCondominiumRepository.delete(id);
    }

    public AuthorizedUserAccessDoorDTO mapResidentDTOToAuthorizedDTO(ResidentDTO residentDTO) {
        AuthorizedUserAccessDoorDTO authorized = new AuthorizedUserAccessDoorDTO();
        authorized.setCondominiumName(residentDTO.getCompanyName());
        authorized.setFullName(residentDTO.getName() + " " + residentDTO.getLastname() + " " + residentDTO.getSecondlastname());
        authorized.setHouseNumber(residentDTO.getHouseClean().getHousenumber());
        authorized.setImageUrl(residentDTO.getImage_url());
        authorized.setType(1);
        authorized.setAuthorizedType(findResidentType(residentDTO.getType()));
        return authorized;
    }

    public AuthorizedUserAccessDoorDTO mapVehiculeDTOtoAuthorizedDTO(VehiculeDTO vehiculeDTO) {
        AuthorizedUserAccessDoorDTO authorized = new AuthorizedUserAccessDoorDTO();
        authorized.setCondominiumName(vehiculeDTO.getCompanyName());
        authorized.setHouseNumber(vehiculeDTO.getHouseClean().getHousenumber());
        authorized.setType(2);
        authorized.setLicenseplate(vehiculeDTO.getLicenseplate());
        authorized.setVehiculeColor(vehiculeDTO.getColor());
        authorized.setVehiculeBrand(vehiculeDTO.getBrand());
        if (vehiculeDTO.getType().equals("Automóvil")) {
            authorized.setAuthorizedType("Automóvil autorizado");
        } else {
            authorized.setAuthorizedType("Motocicleta autorizada");
        }
        return authorized;
    }

    private String findResidentType(Integer type) {
        String typeS = "";
        switch (type) {
            case 1:
                typeS = "Residente propietario";
                break;
            case 2:
                typeS = "Residente inquilino";
                break;
            case 3:
                typeS = "Visitante autorizado";
                break;
        }
        return typeS;
    }

    public AuthorizedUserAccessDoorDTO mapVisitorInvitedToAuthorizedDTO(List<VisitantInvitationDTO> visitantInvitationDTOS) {
        AuthorizedUserAccessDoorDTO authorized = new AuthorizedUserAccessDoorDTO();
        VisitantInvitationDTO visitantInvited = visitantInvitationDTOS.get(0);
        authorized.setFullName(visitantInvited.getName() + " " + visitantInvited.getLastname() + " " + visitantInvited.getSecondlastname());
        authorized.setType(3);
        authorized.setName(visitantInvited.getName());
        authorized.setLastname(visitantInvited.getLastname());
        authorized.setSecondlastname(visitantInvited.getSecondlastname());
        authorized.setIdentificationnumber(visitantInvited.getIdentificationnumber());
        authorized.setHouseId(visitantInvited.getHouseId());
        authorized.setAuthorizedType("Visitante Invitado");
        if (visitantInvited.getProveedor() != null) {
            if (!visitantInvited.getProveedor().isEmpty()) {
                authorized.setProveedor(visitantInvited.getProveedor());
            }
        }
        if (visitantInvited.getLicenseplate() != null) {
            authorized.setLicenseplate(visitantInvited.getLicenseplate());
        }
        if (visitantInvitationDTOS.size() == 1) {
            if (visitantInvited.getHouseId() != null) {
                authorized.setHouseNumber(this.houseService.findOne(visitantInvited.getHouseId()).getHousenumber());
            } else {
                authorized.setDestiny(visitantInvited.getDestiny());
            }
            authorized.setCondominiumName(this.companyService.findOne(visitantInvited.getCompanyId()).getName());
        } else {
            List<CompanyDTO> companies = new ArrayList<>();
            visitantInvitationDTOS.forEach(visitantInvitationDTO -> {
                CompanyDTO company = new CompanyDTO();
                company.setId(visitantInvitationDTO.getCompanyId());
                company.setName(this.companyService.findOne(visitantInvitationDTO.getCompanyId()).getName());
                company.setHouses(new ArrayList<>());
                if (!companies.contains(company)) {
                    companies.add(company);
                }
            });
            for (int i = 0; i < visitantInvitationDTOS.size(); i++) {
                VisitantInvitationDTO visitor = visitantInvitationDTOS.get(i);
                for (int j = 0; j < companies.size(); j++) {
                    CompanyDTO company = companies.get(j);
                    HouseAccessDoorDTO house = new HouseAccessDoorDTO();
                    if (visitor.getCompanyId().equals(company.getId())) {
                        if (visitor.getHouseId() != null) {
                            house.setId(visitor.getHouseId());
                            house.setHousenumber(this.houseService.findOne(visitor.getHouseId()).getHousenumber());
                            if (!company.getHouses().contains(house)) {
                                company.getHouses().add(house);
                            }
                        } else {
                            house.setId(null);
                            house.setHousenumber(visitor.getDestiny());
                            if (!company.getHouses().contains(house)) {
                                company.getHouses().add(house);
                            }
                        }
                    }
                }
            }
            authorized.setCompaniesInvited(companies);
        }
        return authorized;
    }
}
