package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.Officer;
import com.lighthouse.aditum.domain.OfficerAccount;
import com.lighthouse.aditum.domain.RHAccount;
import com.lighthouse.aditum.repository.CompanyRepository;
import com.lighthouse.aditum.repository.OfficerRepository;
import com.lighthouse.aditum.repository.RHAccountRepository;
import com.lighthouse.aditum.service.dto.OfficerDTO;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.mapper.OfficerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Officer.
 */
@Service
@Transactional
public class OfficerService {

    private final Logger log = LoggerFactory.getLogger(OfficerService.class);

    private final OfficerRepository officerRepository;

    private final OfficerMapper officerMapper;

    private final CompanyMapper companyMapper;

    private final RHAccountRepository rhAccountRepository;

    private final CompanyRepository companyRepository;

    public OfficerService(CompanyMapper companyMapper,OfficerRepository officerRepository, OfficerMapper officerMapper,RHAccountRepository rhAccountRepository, CompanyRepository companyRepository) {
        this.companyMapper = companyMapper;
        this.officerRepository = officerRepository;
        this.officerMapper = officerMapper;
        this.rhAccountRepository = rhAccountRepository;
        this.companyRepository = companyRepository;
    }

    /**
     * Save a officer.
     *
     * @param officerDTO the entity to save
     * @return the persisted entity
     */
    public OfficerDTO save(OfficerDTO officerDTO) {
        log.debug("Request to save Officer : {}", officerDTO);
        Officer officer = officerMapper.toEntity(officerDTO);
        officer.setEnable(officerDTO.isEnable());
        officer.setDeleted(0);
        officer = officerRepository.save(officer);
        OfficerDTO result = officerMapper.toDto(officer);
        return result;
    }

    /**
     *  Get all the officers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OfficerDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Officers");
        Page<Officer> result = officerRepository.findByCompanyIdAndDeleted(pageable,companyId,0);
        return result.map(officer -> officerMapper.toDto(officer));
    }

    /**
     *  Get one officer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OfficerDTO findOne(Long id) {
        log.debug("Request to get Officer : {}", id);
        Officer officer = officerRepository.findOne(id);
        OfficerDTO officerDTO = officerMapper.toDto(officer);
        officerDTO.setEnable(officer.isEnable());
        officer.setPhonenumber(officerDTO.getPhonenumber());
        officer.setDirection(officerDTO.getDirection());
        officerDTO.setCompany(companyMapper.companyToCompanyDTO(companyRepository.findOneById(officerDTO.getCompanyId())));
        return officerDTO;
    }

    @Transactional(readOnly = true)
    public OfficerDTO findOneByCompanyAndIdentification(Long id,String identificationID) {
        log.debug("Request to get Vehicule : {}", id);
        Officer officer = officerRepository.findByCompanyIdAndIdentificationnumberAndDeleted(id,identificationID,0);
        OfficerDTO officerDTO = officerMapper.toDto(officer);
        return officerDTO;
    }

    @Transactional(readOnly = true)
    public Integer countByCompanyId(Long companyId) {
        log.debug("Request to get Officer : {}", companyId);
        return officerRepository.countByCompanyIdAndDeleted(companyId,0);
    }
//    @Transactional(readOnly = true)
//    public Page<OfficerDTO> findEnabled(Pageable pageable,Long companyId) {
//        List<Officer> result = officerRepository.findByEnabledAndCompanyId(1,companyId);
//        return new PageImpl<>(result).map(officer-> officer.image(null)).map(officer -> officerMapper.officerToOfficerDTO(officer));
//
//    }
//
//    @Transactional(readOnly = true)
//    public Page<OfficerDTO> findDisabled(Pageable pageable,Long companyId) {
//        List<Officer> result = officerRepository.findByEnabledAndCompanyId(0,companyId);
//        return new PageImpl<>(result).map(officer->officerMapper.officerToOfficerDTO(officer));
//
//    }

    @Transactional(readOnly = true)
    public Page<OfficerDTO> findEnabled(Pageable pageable,Long companyId) {
        List<Officer> result = officerRepository.findByEnableAndCompanyIdAndDeleted(true,companyId,0);
        return new PageImpl<>(result).map(officer-> officer.image(null)).map(officer -> officerMapper.toDto(officer));

    }

    @Transactional(readOnly = true)
    public Page<OfficerDTO> findDisabled(Pageable pageable,Long companyId) {
        List<Officer> result = officerRepository.findByEnableAndCompanyIdAndDeleted(false,companyId,0);
        return new PageImpl<>(result).map(officer->officerMapper.toDto(officer));

    }

    @Transactional(readOnly = true)
    public Page<OfficerDTO> findDisabledByEnterprise(Pageable pageable,Long rhAccountId) {
        RHAccount rhAccount = rhAccountRepository.findOne(rhAccountId);
        List<Company> companies = new ArrayList<>(rhAccount.getCompanies());
        List<Officer> allOfficers = new ArrayList<>();
        List<OfficerDTO> allOfficersDTO = new ArrayList<>();
        companies.forEach(company -> officerRepository.findByEnableAndCompanyIdAndDeleted(false,company.getId(),0).forEach(officer ->   {  allOfficers.add(officer);}));
        String a = "";
        allOfficers.forEach(officer -> {allOfficersDTO.add(officerMapper.toDto(officer));});
        allOfficersDTO.forEach(officerDTO -> { officerDTO.setCompany(companyMapper.companyToCompanyDTO(companyRepository.findOneById(officerDTO.getCompanyId())));});
        return new PageImpl<>(allOfficersDTO);
    }

    @Transactional(readOnly = true)
    public Page<OfficerDTO> findEnabledByEnterprise(Pageable pageable,Long rhAccountId) {
        RHAccount rhAccount = rhAccountRepository.findOne(rhAccountId);
        List<Company> companies = new ArrayList<>(rhAccount.getCompanies());
        List<Officer> allOfficers = new ArrayList<>();
        List<OfficerDTO> allOfficersDTO = new ArrayList<>();
        companies.forEach(company -> officerRepository.findByEnableAndCompanyIdAndDeleted(true,company.getId(),0).forEach(officer ->   {  allOfficers.add(officer);}));
        allOfficers.forEach(officer -> {allOfficersDTO.add(officerMapper.toDto(officer));});
        allOfficersDTO.forEach(officerDTO -> { officerDTO.setCompany(companyMapper.companyToCompanyDTO(companyRepository.findOneById(officerDTO.getCompanyId())));});
        return new PageImpl<>(allOfficersDTO);


    }

    /**
     *  Delete the  officer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Officer : {}", id);
        Officer officer = officerMapper.toEntity(this.findOne(id));
        officer.setDeleted(1);
        officerRepository.save(officer);
    }
}
