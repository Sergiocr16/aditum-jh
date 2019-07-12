package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.MacroAdminAccount;
import com.lighthouse.aditum.repository.MacroAdminAccountRepository;
import com.lighthouse.aditum.service.dto.MacroAdminAccountDTO;
import com.lighthouse.aditum.service.mapper.MacroAdminAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MacroAdminAccount.
 */
@Service
@Transactional
public class MacroAdminAccountService {

    private final Logger log = LoggerFactory.getLogger(MacroAdminAccountService.class);

    private final MacroAdminAccountRepository macroAdminAccountRepository;

    private final MacroAdminAccountMapper macroAdminAccountMapper;

    public MacroAdminAccountService(MacroAdminAccountRepository macroAdminAccountRepository, MacroAdminAccountMapper macroAdminAccountMapper) {
        this.macroAdminAccountRepository = macroAdminAccountRepository;
        this.macroAdminAccountMapper = macroAdminAccountMapper;
    }

    /**
     * Save a macroAdminAccount.
     *
     * @param macroAdminAccountDTO the entity to save
     * @return the persisted entity
     */
    public MacroAdminAccountDTO save(MacroAdminAccountDTO macroAdminAccountDTO) {
        log.debug("Request to save MacroAdminAccount : {}", macroAdminAccountDTO);
        MacroAdminAccount macroAdminAccount = macroAdminAccountMapper.toEntity(macroAdminAccountDTO);
        macroAdminAccount = macroAdminAccountRepository.save(macroAdminAccount);
        return macroAdminAccountMapper.toDto(macroAdminAccount);
    }

    /**
     * Get all the macroAdminAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MacroAdminAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MacroAdminAccounts");
        return macroAdminAccountRepository.findAll(pageable)
            .map(macroAdminAccountMapper::toDto);
    }
    @Transactional(readOnly = true)
    public Page<MacroAdminAccountDTO> findAllByMacro(Pageable pageable,Long id) {
        log.debug("Request to get all MacroOfficerAccounts");
        return macroAdminAccountRepository.findByMacroCondominiumId(pageable,id)
            .map(macroAdminAccountMapper::toDto);
    }
    @Transactional(readOnly = true)
    public MacroAdminAccountDTO findOneByUserId(Long id) {
        log.debug("Request to get MacroOfficerAccount : {}", id);
        MacroAdminAccount macroOfficerAccount = macroAdminAccountRepository.findOneByUserId(id);
        return macroAdminAccountMapper.toDto(macroOfficerAccount);
    }
    /**
     * Get one macroAdminAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MacroAdminAccountDTO findOne(Long id) {
        log.debug("Request to get MacroAdminAccount : {}", id);
        MacroAdminAccount macroAdminAccount = macroAdminAccountRepository.findOne(id);
        return macroAdminAccountMapper.toDto(macroAdminAccount);
    }

    /**
     * Delete the macroAdminAccount by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MacroAdminAccount : {}", id);
        macroAdminAccountRepository.delete(id);
    }
}
