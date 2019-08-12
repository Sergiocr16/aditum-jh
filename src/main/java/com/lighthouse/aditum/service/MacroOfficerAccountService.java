package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.MacroOfficerAccount;
import com.lighthouse.aditum.repository.MacroOfficerAccountRepository;
import com.lighthouse.aditum.service.dto.MacroOfficerAccountDTO;
import com.lighthouse.aditum.service.mapper.MacroOfficerAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MacroOfficerAccount.
 */
@Service
@Transactional
public class MacroOfficerAccountService {

    private final Logger log = LoggerFactory.getLogger(MacroOfficerAccountService.class);

    private final MacroOfficerAccountRepository macroOfficerAccountRepository;

    private final MacroOfficerAccountMapper macroOfficerAccountMapper;

    public MacroOfficerAccountService(MacroOfficerAccountRepository macroOfficerAccountRepository, MacroOfficerAccountMapper macroOfficerAccountMapper) {
        this.macroOfficerAccountRepository = macroOfficerAccountRepository;
        this.macroOfficerAccountMapper = macroOfficerAccountMapper;
    }

    /**
     * Save a macroOfficerAccount.
     *
     * @param macroOfficerAccountDTO the entity to save
     * @return the persisted entity
     */
    public MacroOfficerAccountDTO save(MacroOfficerAccountDTO macroOfficerAccountDTO) {
        log.debug("Request to save MacroOfficerAccount : {}", macroOfficerAccountDTO);
        MacroOfficerAccount macroOfficerAccount = macroOfficerAccountMapper.toEntity(macroOfficerAccountDTO);
        macroOfficerAccount = macroOfficerAccountRepository.save(macroOfficerAccount);
        return macroOfficerAccountMapper.toDto(macroOfficerAccount);
    }
    @Transactional(readOnly = true)
    public MacroOfficerAccountDTO findOneByUserId(Long id) {
        log.debug("Request to get MacroOfficerAccount : {}", id);
        MacroOfficerAccount macroOfficerAccount = macroOfficerAccountRepository.findOneByUserId(id);
        return macroOfficerAccountMapper.toDto(macroOfficerAccount);
    }
    /**
     * Get all the macroOfficerAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MacroOfficerAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MacroOfficerAccounts");
        return macroOfficerAccountRepository.findAll(pageable)
            .map(macroOfficerAccountMapper::toDto);
    }
    @Transactional(readOnly = true)
    public Page<MacroOfficerAccountDTO> findAllByMacro(Pageable pageable,Long id) {
        log.debug("Request to get all MacroOfficerAccounts");
        return macroOfficerAccountRepository.findByMacroCondominiumId(pageable,id)
            .map(macroOfficerAccountMapper::toDto);
    }

    /**
     * Get one macroOfficerAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MacroOfficerAccountDTO findOne(Long id) {
        log.debug("Request to get MacroOfficerAccount : {}", id);
        MacroOfficerAccount macroOfficerAccount = macroOfficerAccountRepository.findOne(id);
        return macroOfficerAccountMapper.toDto(macroOfficerAccount);
    }

    /**
     * Delete the macroOfficerAccount by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MacroOfficerAccount : {}", id);
        macroOfficerAccountRepository.delete(id);
    }
}
