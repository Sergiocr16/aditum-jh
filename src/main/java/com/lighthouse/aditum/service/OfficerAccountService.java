package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.OfficerAccount;
import com.lighthouse.aditum.repository.OfficerAccountRepository;
import com.lighthouse.aditum.service.dto.OfficerAccountDTO;
import com.lighthouse.aditum.service.mapper.OfficerAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing OfficerAccount.
 */
@Service
@Transactional
public class OfficerAccountService {

    private final Logger log = LoggerFactory.getLogger(OfficerAccountService.class);

    private final OfficerAccountRepository officerAccountRepository;

    private final OfficerAccountMapper officerAccountMapper;

    public OfficerAccountService(OfficerAccountRepository officerAccountRepository, OfficerAccountMapper officerAccountMapper) {
        this.officerAccountRepository = officerAccountRepository;
        this.officerAccountMapper = officerAccountMapper;
    }

    /**
     * Save a officerAccount.
     *
     * @param officerAccountDTO the entity to save
     * @return the persisted entity
     */
    public OfficerAccountDTO save(OfficerAccountDTO officerAccountDTO) {
        log.debug("Request to save OfficerAccount : {}", officerAccountDTO);
        OfficerAccount officerAccount = officerAccountMapper.officerAccountDTOToOfficerAccount(officerAccountDTO);
        officerAccount = officerAccountRepository.save(officerAccount);
        OfficerAccountDTO result = officerAccountMapper.officerAccountToOfficerAccountDTO(officerAccount);
        return result;
    }

    @Transactional(readOnly = true)
    public OfficerAccountDTO findOneByUserId(Long id) {
        log.debug("Request to get Resident : {}", id);
        OfficerAccount officer = officerAccountRepository.findOneByUserId(id);
        OfficerAccountDTO officerAccountDTO = officerAccountMapper.officerAccountToOfficerAccountDTO(officer);
        return officerAccountDTO;
    }
    /**
     *  Get all the officerAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OfficerAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OfficerAccounts");
        Page<OfficerAccount> result = officerAccountRepository.findAll(pageable);
        return result.map(officerAccount -> officerAccountMapper.officerAccountToOfficerAccountDTO(officerAccount));
    }

    /**
     *  Get one officerAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OfficerAccountDTO findOne(Long id) {
        log.debug("Request to get OfficerAccount : {}", id);
        OfficerAccount officerAccount = officerAccountRepository.findOne(id);
        OfficerAccountDTO officerAccountDTO = officerAccountMapper.officerAccountToOfficerAccountDTO(officerAccount);
        return officerAccountDTO;
    }

    /**
     *  Delete the  officerAccount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OfficerAccount : {}", id);
        officerAccountRepository.delete(id);
    }
}
