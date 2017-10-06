package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.RHAccount;
import com.lighthouse.aditum.repository.CompanyRepository;
import com.lighthouse.aditum.repository.RHAccountRepository;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.dto.RHAccountDTO;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.mapper.RHAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing RHAccount.
 */
@Service
@Transactional
public class RHAccountService {

    private final Logger log = LoggerFactory.getLogger(RHAccountService.class);

    private final RHAccountRepository rHAccountRepository;

    private final RHAccountMapper rHAccountMapper;


    private final CompanyMapper companyMapper;

    private final CompanyRepository companyRepository;

    public RHAccountService(CompanyMapper companyMapper,RHAccountRepository rHAccountRepository, RHAccountMapper rHAccountMapper,CompanyRepository companyRepository) {
        this.rHAccountRepository = rHAccountRepository;
        this.rHAccountMapper = rHAccountMapper;
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Save a rHAccount.
     *
     * @param rHAccountDTO the entity to save
     * @return the persisted entity
     */
    public RHAccountDTO save(RHAccountDTO rHAccountDTO) {
        log.debug("Request to save RHAccount : {}", rHAccountDTO);
        RHAccount rHAccount = rHAccountMapper.rHAccountDTOToRHAccount(rHAccountDTO);
        if (rHAccountDTO.getCompanies() != null) {
            Set<Company> companies = new HashSet<>();
            rHAccountDTO.getCompanies().forEach(
                company -> companies.add(companyRepository.findOneById(company.getId()))
            );
            rHAccount.setCompanies(companies);
        }

        rHAccount = rHAccountRepository.save(rHAccount);
        RHAccountDTO result = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);
        return result;
    }

    /**
     *  Get all the rHAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RHAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RHAccounts");
        Page<RHAccount> result = rHAccountRepository.findAll(pageable);
        return result.map(rHAccount -> rHAccountMapper.rHAccountToRHAccountDTO(rHAccount));
    }

    /**
     *  Get one rHAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RHAccountDTO findOne(Long id) {
        log.debug("Request to get RHAccount : {}", id);
        RHAccount rHAccount = rHAccountRepository.findOneWithEagerRelationships(id);
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);
        Set<CompanyDTO> companies = new HashSet<>();

        rHAccount.getCompanies().forEach(company -> companies.add(companyMapper.companyToCompanyDTO(company)));

        rHAccountDTO.setCompanies(companies);
        return rHAccountDTO;
    }

    /**
     *  Delete the  rHAccount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RHAccount : {}", id);
        rHAccountRepository.delete(id);
    }


    @Transactional(readOnly = true)
    public RHAccountDTO findOneByUserId(Long id) {
        log.debug("Request to get Resident : {}", id);
        RHAccount rHAccount = rHAccountRepository.findOneByUserId(id);
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);
        rHAccount.setCompanies(null);
        return rHAccountDTO;
    }
}
