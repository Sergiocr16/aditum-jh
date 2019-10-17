package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Announcement;
import com.lighthouse.aditum.repository.AnnouncementRepository;
import com.lighthouse.aditum.service.dto.AnnouncementDTO;
import com.lighthouse.aditum.service.mapper.AnnouncementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Announcement.
 */
@Service
@Transactional
public class AnnouncementService {

    private final Logger log = LoggerFactory.getLogger(AnnouncementService.class);

    private final AnnouncementRepository announcementRepository;

    private final AnnouncementMapper announcementMapper;

    private final AnnouncementCommentService announcementCommentService;

    private final AnnouncementMailService announcementMailService;

    public AnnouncementService(AnnouncementMailService announcementMailService, AnnouncementCommentService announcementCommentService, AnnouncementRepository announcementRepository, AnnouncementMapper announcementMapper) {
        this.announcementRepository = announcementRepository;
        this.announcementMapper = announcementMapper;
        this.announcementCommentService = announcementCommentService;
        this.announcementMailService = announcementMailService;
    }

    /**
     * Save a announcement.
     *
     * @param announcementDTO the entity to save
     * @return the persisted entity
     */
    public AnnouncementDTO save(AnnouncementDTO announcementDTO) {
        log.debug("Request to save Announcement : {}", announcementDTO);
        if(announcementDTO.getDescription()==null || announcementDTO.getDescription().equals("")){
            if(announcementDTO.getId()!=null){
                AnnouncementDTO oldAnnouncement = this.findOne(announcementDTO.getId());
                if (oldAnnouncement != null) {
                    announcementDTO.setDescription(oldAnnouncement.getDescription());
                }
            }
        }
        Announcement announcement = announcementMapper.toEntity(announcementDTO);
        announcement.setDeleted(0);
        announcement.setCompany(announcementMapper.companyFromId(announcementDTO.getCompanyId()));
        announcement = announcementRepository.save(announcement);
        AnnouncementDTO announcementDTO1 = announcementMapper.toDto(announcement);
        return announcementDTO1;
    }

    /**
     *  Get all the announcements.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnnouncementDTO> findAllAsAdmin(Pageable pageable, Long companyId) {
        log.debug("Request to get all Announcements");
        Page<AnnouncementDTO> announcementDTOS = announcementRepository.findByCompanyIdAndStatusAdmin(pageable,companyId,2,3).map(announcementMapper::toDto);
        announcementDTOS.getContent().forEach(announcement -> {
            announcement.setDescription("");
            announcement.setCommentsQuantity(announcementCommentService.countByAnnouncement(announcement.getId()));
        });
        return  announcementDTOS;
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementDTO> findAllSkectches(Pageable pageable, Long companyId) {
        log.debug("Request to get all Announcements");
        Page<AnnouncementDTO> announcementDTOS = announcementRepository.findByCompanyIdAndStatusAndDeleted(pageable,companyId,1,0).map(announcementMapper::toDto);
        announcementDTOS.getContent().forEach(announcement -> {
            announcement.setDescription("");
            announcement.setCommentsQuantity(announcementCommentService.countByAnnouncement(announcement.getId()));
        });
        return announcementDTOS;
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all Announcements");
        Page<AnnouncementDTO> announcementDTOS = announcementRepository.findByCompanyIdAndStatusAndDeleted(pageable,companyId,2,0).map(announcementMapper::toDto);
        announcementDTOS.getContent().forEach(announcement -> {
            announcement.setCommentsQuantity(announcementCommentService.countByAnnouncement(announcement.getId()));
        });
        return announcementDTOS;
    }

    /**
     *  Get one announcement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AnnouncementDTO findOne(Long id) {
        log.debug("Request to get Announcement : {}", id);
        Announcement announcement = announcementRepository.findOne(id);
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);
        announcementDTO.setCommentsQuantity(announcementCommentService.countByAnnouncement(announcement.getId()));
        return announcementDTO;
    }

    /**
     *  Delete the  announcement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Announcement : {}", id);
        Announcement announcement = this.announcementRepository.getOne(id);
        announcement.setDeleted(1);
        announcementRepository.save(announcement);
    }
}
