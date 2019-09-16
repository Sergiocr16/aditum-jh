package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.repository.CompanyRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final MacroCondominiumService macroCondominiumService;

    private final ResidentService residentService;

    private final VehiculeService vehiculeService;

    private final VisitantInvitationService visitantInvitationService;


    public CompanyService(MacroCondominiumService macroCondominiumService, @Lazy ResidentService residentService,@Lazy VehiculeService vehiculeService, VisitantInvitationService visitantInvitationService, CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.macroCondominiumService = macroCondominiumService;
        this.residentService = residentService;
        this.vehiculeService = vehiculeService;
        this.visitantInvitationService = visitantInvitationService;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save
     * @return the persisted entity
     */
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        if(companyDTO.getId()!=null) {
            Company company1 = companyRepository.findOne(companyDTO.getId());
            company.setRHAccounts(company1.getRHAccounts());
            company.setAdminInfos(company1.getAdminInfos());
        }
        company = companyRepository.save(company);
        CompanyDTO result = companyMapper.companyToCompanyDTO(company);
        return result;
    }

    /**
     *  Get all the companies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        Page<Company> result = companyRepository.findAll(pageable);
        return result.map(company -> companyMapper.companyToCompanyDTO(company));
    }

    /**
     *  Get one company by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CompanyDTO findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        Company company = companyRepository.findOne(id);
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);

        return companyDTO;
    }
    @Transactional(readOnly = true)
    public AuthorizedUserAccessDoorDTO findAuthorized(Long id, String identification) {
        log.debug("Request to get MacroCondominium : {}", id);
        ResidentDTO residentDTO = this.residentService.getOneByCompanyWithIdentification(id, identification);
        if (residentDTO != null) {
            return macroCondominiumService.mapResidentDTOToAuthorizedDTO(residentDTO);
        } else {
            List<VisitantInvitationDTO> visitantInvitationDTOS = this.visitantInvitationService.getByCompanyWithIdentification(id, identification);
            if (!visitantInvitationDTOS.isEmpty()) {
                return macroCondominiumService.mapVisitorInvitedToAuthorizedDTO(visitantInvitationDTOS);
            }
        }
        return null;
    }
    @Transactional(readOnly = true)
    public AuthorizedUserAccessDoorDTO findAuthorizedVehicules(Long id, String plate) {
        log.debug("Request to get MacroCondominium : {}", id);
        VehiculeDTO vehiculeDTO = this.vehiculeService.getOneByCompanyWithIdentification(id, plate);
        if (vehiculeDTO != null) {
            return macroCondominiumService.mapVehiculeDTOtoAuthorizedDTO(vehiculeDTO);
        } else {
            List<VisitantInvitationDTO> visitantInvitationDTOS = this.visitantInvitationService.getByCompanyWithPlate(id, plate);
            if (!visitantInvitationDTOS.isEmpty()) {
                return macroCondominiumService.mapVisitorInvitedToAuthorizedDTO(visitantInvitationDTOS);
            }
        }
        return null;
    }

    /**
     *  Delete the  company by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.delete(id);
    }
}
