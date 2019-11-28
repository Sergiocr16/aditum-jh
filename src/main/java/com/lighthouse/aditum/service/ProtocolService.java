package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Protocol;
import com.lighthouse.aditum.repository.ProtocolRepository;
import com.lighthouse.aditum.service.dto.ProtocolDTO;
import com.lighthouse.aditum.service.mapper.ProtocolMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Protocol.
 */
@Service
@Transactional
public class ProtocolService {

    private final Logger log = LoggerFactory.getLogger(ProtocolService.class);

    private final ProtocolRepository protocolRepository;

    private final ProtocolMapper protocolMapper;

    public ProtocolService(ProtocolRepository protocolRepository, ProtocolMapper protocolMapper) {
        this.protocolRepository = protocolRepository;
        this.protocolMapper = protocolMapper;
    }

    /**
     * Save a protocol.
     *
     * @param protocolDTO the entity to save
     * @return the persisted entity
     */
    public ProtocolDTO save(ProtocolDTO protocolDTO) {
        log.debug("Request to save Protocol : {}", protocolDTO);
        Protocol protocol = protocolMapper.toEntity(protocolDTO);
        protocol = protocolRepository.save(protocol);
        return protocolMapper.toDto(protocol);
    }

    /**
     * Get all the protocols.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProtocolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Protocols");
        return protocolRepository.findAll(pageable)
            .map(protocolMapper::toDto);
    }

    /**
     * Get one protocol by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProtocolDTO findOne(Long id) {
        log.debug("Request to get Protocol : {}", id);
        Protocol protocol = protocolRepository.findOne(id);
        return protocolMapper.toDto(protocol);
    }

    /**
     * Delete the protocol by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Protocol : {}", id);
        protocolRepository.delete(id);
    }
}
