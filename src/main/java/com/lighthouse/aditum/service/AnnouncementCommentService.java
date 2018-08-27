package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.AnnouncementComment;
import com.lighthouse.aditum.repository.AnnouncementCommentRepository;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.dto.AnnouncementCommentDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.AnnouncementCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing AnnouncementComment.
 */
@Service
@Transactional
public class AnnouncementCommentService {

    private final Logger log = LoggerFactory.getLogger(AnnouncementCommentService.class);

    private final AnnouncementCommentRepository announcementCommentRepository;

    private final AnnouncementCommentMapper announcementCommentMapper;

    private final ResidentService residentService;

    private final AdminInfoService adminInfoService;

    public AnnouncementCommentService( AdminInfoService adminInfoService, ResidentService residentService, AnnouncementCommentRepository announcementCommentRepository, AnnouncementCommentMapper announcementCommentMapper) {
        this.announcementCommentRepository = announcementCommentRepository;
        this.announcementCommentMapper = announcementCommentMapper;
        this.residentService = residentService;
        this.adminInfoService=adminInfoService;
    }

    /**
     * Save a announcementComment.
     *
     * @param announcementCommentDTO the entity to save
     * @return the persisted entity
     */
    public AnnouncementCommentDTO save(AnnouncementCommentDTO announcementCommentDTO) {
        log.debug("Request to save AnnouncementComment : {}", announcementCommentDTO);
        AnnouncementComment announcementComment = announcementCommentMapper.toEntity(announcementCommentDTO);
        announcementComment.setResident(announcementCommentMapper.residentFromId(announcementCommentDTO.getResidentId()));
        announcementComment.setAdminInfo(announcementCommentMapper.adminInfoFromId(announcementCommentDTO.getAdminInfoId()));
        announcementComment.setDeleted(announcementCommentDTO.getDeleted());
        announcementComment.setEditedDate(announcementCommentDTO.getEditedDate());
        announcementComment = announcementCommentRepository.save(announcementComment);
        return announcementCommentMapper.toDto(announcementComment);
    }

    /**
     * Get all the announcementComments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnnouncementCommentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnnouncementComments");
        return announcementCommentRepository.findAll(pageable)
            .map(announcementCommentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public int countByAnnouncement(Long id) {
       return announcementCommentRepository.countAnnouncementCommentByAnnouncement_IdAndDeleted(id,0);
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementCommentDTO> findByAnnouncement(Pageable pageable,Long id) {
        log.debug("Request to get all AnnouncementComments");
       Page<AnnouncementCommentDTO> announcementCommentDTOS = announcementCommentRepository.findAnnouncementCommentByAnnouncement_IdAndDeleted(pageable,id,0).map(announcementComment -> {
            AnnouncementCommentDTO announcementCommentDTO = announcementCommentMapper.toDto(announcementComment);
            if(announcementComment.getAdminInfo()!=null) {
                announcementCommentDTO.setAdminInfoId(announcementComment.getAdminInfo().getId());
            }
           if(announcementComment.getResident()!=null) {
               announcementCommentDTO.setResidentId(announcementComment.getResident().getId());
           }
           announcementCommentDTO.setEditedDate(announcementComment.getEditedDate());
            return announcementCommentDTO;
        });

       announcementCommentDTOS.getContent().forEach(announcementCommentDTO -> {
           if(announcementCommentDTO.getResidentId()!=null){
               announcementCommentDTO.setResident(residentService.findOne(announcementCommentDTO.getResidentId()));
           }else {
               AdminInfoDTO adminInfoFound = adminInfoService.findOne(announcementCommentDTO.getAdminInfoId());
               ResidentDTO adminAsResident = new ResidentDTO();
               adminAsResident.setId(adminInfoFound.getId());
               adminAsResident.setName(adminInfoFound.getName());
               adminAsResident.setLastname(adminInfoFound.getLastname());
               adminAsResident.setSecondlastname(adminInfoFound.getSecondlastname());
               adminAsResident.setImage_url(adminInfoFound.getImage_url());
               adminAsResident.setIdentificationnumber(adminInfoFound.getIdentificationnumber());
               announcementCommentDTO.setResident(adminAsResident);

           }

       });
       return announcementCommentDTOS;
    }


    /**
     * Get one announcementComment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
//    @Transactional(readOnly = true)
//    public Optional<AnnouncementCommentDTO> findOne(Long id) {
//        log.debug("Request to get AnnouncementComment : {}", id);
//        return announcementCommentRepository.findOne(id)
//            .map(announcementCommentMapper::toDto);
//    }

    /**
     * Delete the announcementComment by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AnnouncementComment : {}", id);
        AnnouncementComment announcementComment = this.announcementCommentRepository.findOne(id);
        announcementComment.setDeleted(1);
        announcementCommentRepository.save(announcementComment);
    }
}
