package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.repository.BancoRepository;
import com.lighthouse.aditum.service.dto.BancoDTO;
import com.lighthouse.aditum.service.mapper.BancoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Banco.
 */
@Service
@Transactional
public class BancoService {

    private final Logger log = LoggerFactory.getLogger(BancoService.class);

    private final BancoRepository bancoRepository;

    private final BancoMapper bancoMapper;

    public BancoService(BancoRepository bancoRepository, BancoMapper bancoMapper) {
        this.bancoRepository = bancoRepository;
        this.bancoMapper = bancoMapper;
    }

    /**
     * Save a banco.
     *
     * @param bancoDTO the entity to save
     * @return the persisted entity
     */

    public BancoDTO save(BancoDTO bancoDTO) {
        log.debug("Request to save Banco : {}", bancoDTO);
        Banco banco = bancoMapper.toEntity(bancoDTO);
        banco.setCompany(bancoMapper.companyFromId(bancoDTO.getCompanyId()));
        banco = bancoRepository.save(banco);
        return bancoMapper.toDto(banco);
    }
    /**
     *  Get all the bancos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BancoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bancos");
        return bancoRepository.findAll(pageable)
            .map(bancoMapper::toDto);
    }

    /**
     *  Get one banco by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BancoDTO findOne(Long id) {
        log.debug("Request to get Banco : {}", id);
        Banco banco = bancoRepository.findOne(id);
        return bancoMapper.toDto(banco);
    }

    /**
     *  Delete the  banco by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Banco : {}", id);
        bancoRepository.delete(id);
    }
}
