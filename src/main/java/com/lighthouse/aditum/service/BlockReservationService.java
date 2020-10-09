package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.BlockReservation;
import com.lighthouse.aditum.repository.BlockReservationRepository;
import com.lighthouse.aditum.service.dto.BlockReservationDTO;
import com.lighthouse.aditum.service.mapper.BlockReservationMapper;
import jdk.nashorn.internal.ir.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing BlockReservation.
 */
@Service
@Transactional
public class BlockReservationService {

    private final Logger log = LoggerFactory.getLogger(BlockReservationService.class);

    private final BlockReservationRepository blockReservationRepository;

    private final BlockReservationMapper blockReservationMapper;

    public BlockReservationService(BlockReservationRepository blockReservationRepository, BlockReservationMapper blockReservationMapper) {
        this.blockReservationRepository = blockReservationRepository;
        this.blockReservationMapper = blockReservationMapper;
    }

    /**
     * Save a blockReservation.
     *
     * @param blockReservationDTO the entity to save
     * @return the persisted entity
     */
    public BlockReservationDTO save(BlockReservationDTO blockReservationDTO) {
        log.debug("Request to save BlockReservation : {}", blockReservationDTO);
        BlockReservation blockReservation = blockReservationMapper.toEntity(blockReservationDTO);
        blockReservation = blockReservationRepository.save(blockReservation);
        return blockReservationMapper.toDto(blockReservation);
    }

    /**
     * Get all the blockReservations.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BlockReservationDTO> findAll() {
        log.debug("Request to get all BlockReservations");
        return blockReservationRepository.findAll().stream()
            .map(blockReservationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one blockReservation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BlockReservationDTO findOne(Long id) {
        log.debug("Request to get BlockReservation : {}", id);
        BlockReservation blockReservation = blockReservationRepository.findOne(id);
        return blockReservationMapper.toDto(blockReservation);
    }

    @Transactional(readOnly = true)
    public BlockReservationDTO findOneByHouseId(Long houseId) {
        log.debug("Request to get BlockReservation by house: {}", houseId);
        BlockReservation blockReservation = blockReservationRepository.findOneByHouseId(houseId);
        if(blockReservation!=null){
            return blockReservationMapper.toDto(blockReservation);
        }else{
            BlockReservationDTO b = new BlockReservationDTO();
            b.setBlocked(0);
            b.setComments(null);
            b.setHouseId(houseId);
            return b;
        }
    }
    /**
     * Delete the blockReservation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BlockReservation : {}", id);
        blockReservationRepository.delete(id);
    }
}
