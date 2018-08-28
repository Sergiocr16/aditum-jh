package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CommonAreaSchedule;
import com.lighthouse.aditum.repository.CommonAreaScheduleRepository;
import com.lighthouse.aditum.service.dto.CommonAreaScheduleDTO;
import com.lighthouse.aditum.service.mapper.CommonAreaScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CommonAreaSchedule.
 */
@Service
@Transactional
public class CommonAreaScheduleService {

    private final Logger log = LoggerFactory.getLogger(CommonAreaScheduleService.class);

    private final CommonAreaScheduleRepository commonAreaScheduleRepository;

    private final CommonAreaScheduleMapper commonAreaScheduleMapper;

    public CommonAreaScheduleService(CommonAreaScheduleRepository commonAreaScheduleRepository, CommonAreaScheduleMapper commonAreaScheduleMapper) {
        this.commonAreaScheduleRepository = commonAreaScheduleRepository;
        this.commonAreaScheduleMapper = commonAreaScheduleMapper;
    }

    /**
     * Save a commonAreaSchedule.
     *
     * @param commonAreaScheduleDTO the entity to save
     * @return the persisted entity
     */
    public CommonAreaScheduleDTO save(CommonAreaScheduleDTO commonAreaScheduleDTO) {
        log.debug("Request to save CommonAreaSchedule : {}", commonAreaScheduleDTO);
        CommonAreaSchedule commonAreaSchedule = commonAreaScheduleMapper.toEntity(commonAreaScheduleDTO);
        commonAreaSchedule = commonAreaScheduleRepository.save(commonAreaSchedule);
        return commonAreaScheduleMapper.toDto(commonAreaSchedule);
    }

    /**
     *  Get all the commonAreaSchedules.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CommonAreaScheduleDTO> findAll() {
        log.debug("Request to get all CommonAreaSchedules");
        return commonAreaScheduleRepository.findAll().stream()
            .map(commonAreaScheduleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    public List<CommonAreaScheduleDTO> findSchedulesByCommonArea(Long commonAreaId) {
        log.debug("Request to get all Visitants in last month by house");
        return commonAreaScheduleRepository.findByCommonAreaId(commonAreaId).stream()
            .map(commonAreaScheduleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

    }
    /**
     *  Get one commonAreaSchedule by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CommonAreaScheduleDTO findOne(Long id) {
        log.debug("Request to get CommonAreaSchedule : {}", id);
        CommonAreaSchedule commonAreaSchedule = commonAreaScheduleRepository.findOne(id);
        return commonAreaScheduleMapper.toDto(commonAreaSchedule);
    }

    /**
     *  Delete the  commonAreaSchedule by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonAreaSchedule : {}", id);
        commonAreaScheduleRepository.delete(id);
    }
}
