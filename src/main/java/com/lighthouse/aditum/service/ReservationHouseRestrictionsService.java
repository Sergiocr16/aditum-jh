package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ReservationHouseRestrictions;
import com.lighthouse.aditum.repository.ReservationHouseRestrictionsRepository;
import com.lighthouse.aditum.service.dto.CommonAreaDTO;
import com.lighthouse.aditum.service.dto.ReservationHouseRestrictionsDTO;
import com.lighthouse.aditum.service.mapper.ReservationHouseRestrictionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ReservationHouseRestrictions.
 */
@Service
@Transactional
public class ReservationHouseRestrictionsService {

    private final Logger log = LoggerFactory.getLogger(ReservationHouseRestrictionsService.class);

    private final ReservationHouseRestrictionsRepository reservationHouseRestrictionsRepository;

    private final ReservationHouseRestrictionsMapper reservationHouseRestrictionsMapper;

    private final CommonAreaService commonAreaService;

    public ReservationHouseRestrictionsService(CommonAreaService commonAreaService, ReservationHouseRestrictionsRepository reservationHouseRestrictionsRepository, ReservationHouseRestrictionsMapper reservationHouseRestrictionsMapper) {
        this.reservationHouseRestrictionsRepository = reservationHouseRestrictionsRepository;
        this.reservationHouseRestrictionsMapper = reservationHouseRestrictionsMapper;
        this.commonAreaService = commonAreaService;
    }

    /**
     * Save a reservationHouseRestrictions.
     *
     * @param reservationHouseRestrictionsDTO the entity to save
     * @return the persisted entity
     */
    public ReservationHouseRestrictionsDTO save(ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO) {
        log.debug("Request to save ReservationHouseRestrictions : {}", reservationHouseRestrictionsDTO);
        ReservationHouseRestrictions reservationHouseRestrictions = reservationHouseRestrictionsMapper.toEntity(reservationHouseRestrictionsDTO);
        reservationHouseRestrictions = reservationHouseRestrictionsRepository.save(reservationHouseRestrictions);
        return reservationHouseRestrictionsMapper.toDto(reservationHouseRestrictions);
    }

    public void increaseQuantity(Long houseId, Long commonAreaId, ZonedDateTime lastTimeReservation) {
        CommonAreaDTO commonAreaDTO = this.commonAreaService.findOne(commonAreaId);
        if(commonAreaDTO.getHasReservationsLimit()==1) {
            ReservationHouseRestrictionsDTO reservationHouseRestrictions = this.findRestrictionByHouseAndCommonArea(houseId, commonAreaId);
            if (reservationHouseRestrictions != null) {
                reservationHouseRestrictions.setReservationQuantity(reservationHouseRestrictions.getReservationQuantity() + 1);
            } else {
                reservationHouseRestrictions = new ReservationHouseRestrictionsDTO();
                reservationHouseRestrictions.setCommonAreaId(commonAreaId);
                reservationHouseRestrictions.setReservationQuantity(1);
                reservationHouseRestrictions.setHouseId(houseId);
                reservationHouseRestrictions.setLastTimeReservation(lastTimeReservation);
            }
            this.save(reservationHouseRestrictions);
        }
    }

    public void decreaseQuantity(Long houseId, Long commonAreaId) {
        CommonAreaDTO commonAreaDTO = this.commonAreaService.findOne(commonAreaId);
        if(commonAreaDTO.getHasReservationsLimit()==1) {
            ReservationHouseRestrictionsDTO reservationHouseRestrictions = this.findRestrictionByHouseAndCommonArea(houseId, commonAreaId);
            if (reservationHouseRestrictions != null) {
                reservationHouseRestrictions.setReservationQuantity(reservationHouseRestrictions.getReservationQuantity() - 1);
            }
            this.save(reservationHouseRestrictions);
        }
    }
    /**
     * Get all the reservationHouseRestrictions.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReservationHouseRestrictionsDTO> findAll() {
        log.debug("Request to get all ReservationHouseRestrictions");
        return reservationHouseRestrictionsRepository.findAll().stream()
            .map(reservationHouseRestrictionsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<ReservationHouseRestrictionsDTO> findAllByCommonArea(Long commonAreaId) {
        log.debug("Request to get all ReservationHouseRestrictions");
        return reservationHouseRestrictionsRepository.findAllByCommonAreaId(commonAreaId).stream()
            .map(reservationHouseRestrictionsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reservationHouseRestrictions by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ReservationHouseRestrictionsDTO findOne(Long id) {
        log.debug("Request to get ReservationHouseRestrictions : {}", id);
        ReservationHouseRestrictions reservationHouseRestrictions = reservationHouseRestrictionsRepository.findOne(id);
        return reservationHouseRestrictionsMapper.toDto(reservationHouseRestrictions);
    }

    @Transactional(readOnly = true)
    public ReservationHouseRestrictionsDTO findRestrictionByHouseAndCommonArea(Long houseId,Long commonAreaId) {
        ReservationHouseRestrictions reservationHouseRestrictions = reservationHouseRestrictionsRepository.findByHouseIdAndCommonAreaId(houseId,commonAreaId);
        return reservationHouseRestrictionsMapper.toDto(reservationHouseRestrictions);
    }

    /**
     * Delete the reservationHouseRestrictions by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReservationHouseRestrictions : {}", id);
        reservationHouseRestrictionsRepository.delete(id);
    }
}