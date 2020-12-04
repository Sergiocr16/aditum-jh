package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ActivityResident;
import com.lighthouse.aditum.repository.ActivityResidentRepository;
import com.lighthouse.aditum.service.dto.ActivityResidentDTO;
import com.lighthouse.aditum.service.dto.NotificationRequestDTO;
import com.lighthouse.aditum.service.mapper.ActivityResidentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


/**
 * Service Implementation for managing ActivityResident.
 */
@Service
@Transactional
public class ActivityResidentService {

    private final Logger log = LoggerFactory.getLogger(ActivityResidentService.class);

    private final ActivityResidentRepository activityResidentRepository;

    private final ActivityResidentMapper activityResidentMapper;

    public ActivityResidentService(ActivityResidentRepository activityResidentRepository, ActivityResidentMapper activityResidentMapper) {
        this.activityResidentRepository = activityResidentRepository;
        this.activityResidentMapper = activityResidentMapper;
    }

    /**
     * Save a activityResident.
     *
     * @param activityResidentDTO the entity to save
     * @return the persisted entity
     */
    public ActivityResidentDTO save(ActivityResidentDTO activityResidentDTO) {
        log.debug("Request to save ActivityResident : {}", activityResidentDTO);
        ActivityResident activityResident = activityResidentMapper.toEntity(activityResidentDTO);
        activityResident = activityResidentRepository.save(activityResident);
        return activityResidentMapper.toDto(activityResident);
    }

    public ActivityResidentDTO save(NotificationRequestDTO n, Long userId) {
        ActivityResidentDTO activityResidentDTO = new ActivityResidentDTO();
        activityResidentDTO.setDate(ZonedDateTime.now());
        activityResidentDTO.setDescription(n.getBody());
        activityResidentDTO.setSeen(0);
        activityResidentDTO.setTitle(n.getTitle());
        activityResidentDTO.setUser(userId);
        return this.save(activityResidentDTO);
    }


    public ActivityResidentDTO save(String title, String description, Long userId) {
        ActivityResidentDTO activityResidentDTO = new ActivityResidentDTO();
        activityResidentDTO.setDate(ZonedDateTime.now());
        activityResidentDTO.setDescription(description);
        activityResidentDTO.setSeen(0);
        activityResidentDTO.setTitle(title);
        activityResidentDTO.setUser(userId);
        return this.save(activityResidentDTO);
    }

    /**
     * Get all the activityResidents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ActivityResidentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityResidents");
        return activityResidentRepository.findAll(pageable)
            .map(activityResidentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResidentDTO> findAllByUserId(Pageable pageable, Long userId) {
        log.debug("Request to get all ActivityResidents");
        return activityResidentRepository.findAllByUser(pageable, userId)
            .map(activityResidentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResidentDTO> getLastActivityNotSeeingByUser(Pageable pageable, Long userId) {
        log.debug("Request to get all ActivityResidents");
        return activityResidentRepository.findTopByUserAndSeen(pageable, userId,0)
            .map(activityResidentMapper::toDto);
    }

    /**
     * Get one activityResident by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ActivityResidentDTO findOne(Long id) {
        log.debug("Request to get ActivityResident : {}", id);
        ActivityResident activityResident = activityResidentRepository.findOne(id);
        return activityResidentMapper.toDto(activityResident);
    }

    /**
     * Delete the activityResident by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ActivityResident : {}", id);
        activityResidentRepository.delete(id);
    }
}
