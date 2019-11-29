package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.JuntaDirectivaAccount;
import com.lighthouse.aditum.repository.JuntaDirectivaAccountRepository;
import com.lighthouse.aditum.service.dto.JuntaDirectivaAccountDTO;
import com.lighthouse.aditum.service.mapper.JuntaDirectivaAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing JuntaDirectivaAccount.
 */
@Service
@Transactional
public class JuntaDirectivaAccountService {

    private final Logger log = LoggerFactory.getLogger(JuntaDirectivaAccountService.class);

    private final JuntaDirectivaAccountRepository juntaDirectivaAccountRepository;

    private final JuntaDirectivaAccountMapper juntaDirectivaAccountMapper;

    public JuntaDirectivaAccountService(JuntaDirectivaAccountRepository juntaDirectivaAccountRepository, JuntaDirectivaAccountMapper juntaDirectivaAccountMapper) {
        this.juntaDirectivaAccountRepository = juntaDirectivaAccountRepository;
        this.juntaDirectivaAccountMapper = juntaDirectivaAccountMapper;
    }

    /**
     * Save a juntaDirectivaAccount.
     *
     * @param juntaDirectivaAccountDTO the entity to save
     * @return the persisted entity
     */
    public JuntaDirectivaAccountDTO save(JuntaDirectivaAccountDTO juntaDirectivaAccountDTO) {
        log.debug("Request to save JuntaDirectivaAccount : {}", juntaDirectivaAccountDTO);
        JuntaDirectivaAccount juntaDirectivaAccount = juntaDirectivaAccountMapper.toEntity(juntaDirectivaAccountDTO);
        juntaDirectivaAccount = juntaDirectivaAccountRepository.save(juntaDirectivaAccount);
        return juntaDirectivaAccountMapper.toDto(juntaDirectivaAccount);
    }

    /**
     * Get all the juntaDirectivaAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JuntaDirectivaAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JuntaDirectivaAccounts");
        return juntaDirectivaAccountRepository.findAll(pageable)
            .map(juntaDirectivaAccountMapper::toDto);
    }
    @Transactional(readOnly = true)
    public JuntaDirectivaAccountDTO findOneByUserId(Long id) {
        log.debug("Request to get Resident : {}", id);
        JuntaDirectivaAccount officer = juntaDirectivaAccountRepository.findOneByUserId(id);
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountMapper.toDto(officer);
        return juntaDirectivaAccountDTO;
    }
    /**
     * Get one juntaDirectivaAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public JuntaDirectivaAccountDTO findOne(Long id) {
        log.debug("Request to get JuntaDirectivaAccount : {}", id);
        JuntaDirectivaAccount juntaDirectivaAccount = juntaDirectivaAccountRepository.findOne(id);
        return juntaDirectivaAccountMapper.toDto(juntaDirectivaAccount);
    }
    @Transactional(readOnly = true)
    public JuntaDirectivaAccountDTO findByCompanyId(Long companyId) {
        log.debug("Request to get all Residents");
        JuntaDirectivaAccount JuntaDirectivaAccount = juntaDirectivaAccountRepository.findByCompanyId(companyId);
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountMapper.toDto(JuntaDirectivaAccount);
        return juntaDirectivaAccountDTO;
    }
    /**
     * Delete the juntaDirectivaAccount by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JuntaDirectivaAccount : {}", id);
        juntaDirectivaAccountRepository.delete(id);
    }
}
