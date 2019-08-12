package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.InvitationSchedule;
import com.lighthouse.aditum.repository.InvitationScheduleRepository;
import com.lighthouse.aditum.service.dto.InvitationScheduleDTO;
import com.lighthouse.aditum.service.mapper.InvitationScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing InvitationSchedule.
 */
@Service
@Transactional
public class InvitationScheduleService {

    private final Logger log = LoggerFactory.getLogger(InvitationScheduleService.class);

    private final InvitationScheduleRepository invitationScheduleRepository;

    private final InvitationScheduleMapper invitationScheduleMapper;

    public InvitationScheduleService(InvitationScheduleRepository invitationScheduleRepository, InvitationScheduleMapper invitationScheduleMapper) {
        this.invitationScheduleRepository = invitationScheduleRepository;
        this.invitationScheduleMapper = invitationScheduleMapper;
    }

    /**
     * Save a invitationSchedule.
     *
     * @param invitationScheduleDTO the entity to save
     * @return the persisted entity
     */
    public InvitationScheduleDTO save(InvitationScheduleDTO invitationScheduleDTO) {
        log.debug("Request to save InvitationSchedule : {}", invitationScheduleDTO);
        InvitationSchedule invitationSchedule = invitationScheduleMapper.toEntity(invitationScheduleDTO);
        invitationSchedule = invitationScheduleRepository.save(invitationSchedule);
        return invitationScheduleMapper.toDto(invitationSchedule);
    }
    public List<InvitationScheduleDTO> findSchedulesByInvitation(Long invitationId) {
        log.debug("Request to get all Visitants in last month by house");
        return invitationScheduleRepository.findByVisitantInvitationId(invitationId).stream()
            .map(invitationScheduleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    public InvitationScheduleDTO findOneSchedulesByInvitation(Long invitationId) {
        log.debug("Request to get all Visitants in last month by house");
        return invitationScheduleMapper.toDto(invitationScheduleRepository.findOneByVisitantInvitationId(invitationId));
    }
    /**
     * Get all the invitationSchedules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvitationScheduleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvitationSchedules");
        return invitationScheduleRepository.findAll(pageable)
            .map(invitationScheduleMapper::toDto);
    }

    /**
     * Get one invitationSchedule by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public InvitationScheduleDTO findOne(Long id) {
        log.debug("Request to get InvitationSchedule : {}", id);
        InvitationSchedule invitationSchedule = invitationScheduleRepository.findOne(id);
        return invitationScheduleMapper.toDto(invitationSchedule);
    }

    /**
     * Delete the invitationSchedule by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvitationSchedule : {}", id);
        invitationScheduleRepository.delete(id);
    }
}
