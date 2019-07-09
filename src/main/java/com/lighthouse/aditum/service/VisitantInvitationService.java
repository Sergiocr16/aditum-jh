package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.VisitantInvitation;
import com.lighthouse.aditum.repository.VisitantInvitationRepository;
import com.lighthouse.aditum.service.dto.VisitantInvitationDTO;
import com.lighthouse.aditum.service.mapper.VisitantInvitationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


/**
 * Service Implementation for managing VisitantInvitation.
 */
@Service
@Transactional
public class VisitantInvitationService {

    private final Logger log = LoggerFactory.getLogger(VisitantInvitationService.class);

    private final VisitantInvitationRepository visitantInvitationRepository;

    private final VisitantInvitationMapper visitantInvitationMapper;

    private final InvitationScheduleService invitationScheduleService;

    public VisitantInvitationService(InvitationScheduleService invitationScheduleService,VisitantInvitationRepository visitantInvitationRepository, VisitantInvitationMapper visitantInvitationMapper) {
        this.visitantInvitationRepository = visitantInvitationRepository;
        this.visitantInvitationMapper = visitantInvitationMapper;
        this.invitationScheduleService = invitationScheduleService;
    }

    /**
     * Save a visitantInvitation.
     *
     * @param visitantInvitationDTO the entity to save
     * @return the persisted entity
     */
    public VisitantInvitationDTO save(VisitantInvitationDTO visitantInvitationDTO) {
        log.debug("Request to save VisitantInvitation : {}", visitantInvitationDTO);
        VisitantInvitation visitantInvitation = visitantInvitationMapper.toEntity(visitantInvitationDTO);
        visitantInvitation = visitantInvitationRepository.save(visitantInvitation);
        return visitantInvitationMapper.toDto(visitantInvitation);
    }
    @Transactional(readOnly = true)
    public Page<VisitantInvitationDTO> findInvitedVisitorsByHouse(Long companyId, Long houseId, int timeFormat) {
        log.debug("Request to get all Visitants");
        List<VisitantInvitation> result = visitantInvitationRepository.findByCompanyIdAndHouseIdAndStatusAndHasscheduleOrCompanyIdAndHouseIdAndStatusAndHasschedule(companyId, houseId, 1, timeFormat,companyId, houseId, 2,timeFormat);
        Collections.reverse(result);
        return new PageImpl<VisitantInvitation>(result).map(visitant -> visitantInvitationMapper.toDto(visitant));
    }
    /**
     * Get all the visitantInvitations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VisitantInvitationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VisitantInvitations");
        return visitantInvitationRepository.findAll(pageable)
            .map(visitantInvitationMapper::toDto);
    }

    /**
     * Get one visitantInvitation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public VisitantInvitationDTO findOne(Long id) {
        log.debug("Request to get VisitantInvitation : {}", id);
        VisitantInvitation visitantInvitation = visitantInvitationRepository.findOne(id);
        return visitantInvitationMapper.toDto(visitantInvitation);
    }
    @Transactional(readOnly = true)
    public VisitantInvitationDTO findInvitedVisitorByHouse(String identificationNumber, Long houseId, Long companyId, int hasSchedule) {
        log.debug("Request to find if there is already a registered visitor with this identification number : {}", identificationNumber);
        VisitantInvitation visitant = visitantInvitationRepository.findByIdentificationnumberAndHouseIdAndCompanyIdAndHasschedule(identificationNumber, houseId, companyId,hasSchedule);
        VisitantInvitationDTO visitantDTO = visitantInvitationMapper.toDto(visitant);
        return visitantDTO;
    }
    /**
     * Delete the visitantInvitation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        VisitantInvitationDTO temporalVisitant = this.findOne(id);
        log.debug("Request to delete VisitantInvitation : {}", id);
        visitantInvitationRepository.delete(id);
      if(temporalVisitant.getHasschedule()==1){
          invitationScheduleService.delete(invitationScheduleService.findSchedulesByInvitation(id).get(0).getId());
      }


    }
}
