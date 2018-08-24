package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CommonAreaReservations;
import com.lighthouse.aditum.repository.CommonAreaReservationsRepository;
import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;
import com.lighthouse.aditum.service.mapper.CommonAreaReservationsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing CommonAreaReservations.
 */
@Service
@Transactional
public class CommonAreaReservationsService {

    private final Logger log = LoggerFactory.getLogger(CommonAreaReservationsService.class);

    private final CommonAreaReservationsRepository commonAreaReservationsRepository;

    private final CommonAreaReservationsMapper commonAreaReservationsMapper;

    public CommonAreaReservationsService(CommonAreaReservationsRepository commonAreaReservationsRepository, CommonAreaReservationsMapper commonAreaReservationsMapper) {
        this.commonAreaReservationsRepository = commonAreaReservationsRepository;
        this.commonAreaReservationsMapper = commonAreaReservationsMapper;
    }

    /**
     * Save a commonAreaReservations.
     *
     * @param commonAreaReservationsDTO the entity to save
     * @return the persisted entity
     */
    public CommonAreaReservationsDTO save(CommonAreaReservationsDTO commonAreaReservationsDTO) {
        log.debug("Request to save CommonAreaReservations : {}", commonAreaReservationsDTO);
        CommonAreaReservations commonAreaReservations = commonAreaReservationsMapper.toEntity(commonAreaReservationsDTO);
        commonAreaReservations = commonAreaReservationsRepository.save(commonAreaReservations);
        return commonAreaReservationsMapper.toDto(commonAreaReservations);
    }

    /**
     *  Get all the commonAreaReservations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommonAreaReservations");
        return commonAreaReservationsRepository.findAll(pageable)
            .map(commonAreaReservationsMapper::toDto);
    }

    /**
     *  Get one commonAreaReservations by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CommonAreaReservationsDTO findOne(Long id) {
        log.debug("Request to get CommonAreaReservations : {}", id);
        CommonAreaReservations commonAreaReservations = commonAreaReservationsRepository.findOne(id);
        return commonAreaReservationsMapper.toDto(commonAreaReservations);
    }

    /**
     *  Delete the  commonAreaReservations by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonAreaReservations : {}", id);
        commonAreaReservationsRepository.delete(id);
    }
}
