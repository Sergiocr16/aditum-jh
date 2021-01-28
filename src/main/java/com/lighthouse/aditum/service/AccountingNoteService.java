package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.AccountingNote;
import com.lighthouse.aditum.repository.AccountingNoteRepository;
import com.lighthouse.aditum.service.dto.AccountingNoteDTO;
import com.lighthouse.aditum.service.mapper.AccountingNoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing AccountingNote.
 */
@Service
@Transactional
public class AccountingNoteService {

    private final Logger log = LoggerFactory.getLogger(AccountingNoteService.class);

    private final AccountingNoteRepository accountingNoteRepository;

    private final AccountingNoteMapper accountingNoteMapper;

    public AccountingNoteService(AccountingNoteRepository accountingNoteRepository, AccountingNoteMapper accountingNoteMapper) {
        this.accountingNoteRepository = accountingNoteRepository;
        this.accountingNoteMapper = accountingNoteMapper;
    }

    /**
     * Save a accountingNote.
     *
     * @param accountingNoteDTO the entity to save
     * @return the persisted entity
     */
    public AccountingNoteDTO save(AccountingNoteDTO accountingNoteDTO) {
        log.debug("Request to save AccountingNote : {}", accountingNoteDTO);
        AccountingNote accountingNote = accountingNoteMapper.toEntity(accountingNoteDTO);
        accountingNote = accountingNoteRepository.save(accountingNote);
        return accountingNoteMapper.toDto(accountingNote);
    }

    /**
     * Get all the accountingNotes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccountingNoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountingNotes");
        return accountingNoteRepository.findAll(pageable)
            .map(accountingNoteMapper::toDto);
    }

    /**
     * Get all the accountingNotes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccountingNoteDTO> findByHouse(Pageable pageable,Long houseId) {
        log.debug("Request to get all AccountingNotes");
        return accountingNoteRepository.findAllByHouseId(pageable,houseId)
            .map(accountingNoteMapper::toDto);
    }

    /**
     * Get one accountingNote by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AccountingNoteDTO findOne(Long id) {
        log.debug("Request to get AccountingNote : {}", id);
        AccountingNote accountingNote = accountingNoteRepository.findOne(id);
        return accountingNoteMapper.toDto(accountingNote);
    }

    /**
     * Delete the accountingNote by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AccountingNote : {}", id);
        AccountingNoteDTO a = this.findOne(id);
        a.setDeleted(1);
        this.save(a);
    }
}
