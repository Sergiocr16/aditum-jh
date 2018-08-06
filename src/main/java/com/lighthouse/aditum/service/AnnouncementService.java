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

    public AnnouncementService(AnnouncementRepository announcementRepository, AnnouncementMapper announcementMapper) {
        this.announcementRepository = announcementRepository;
        this.announcementMapper = announcementMapper;
    }

    /**
     * Save a announcement.
     *
     * @param announcementDTO the entity to save
     * @return the persisted entity
     */
    public AnnouncementDTO save(AnnouncementDTO announcementDTO) {
        log.debug("Request to save Announcement : {}", announcementDTO);
        Announcement announcement = announcementMapper.toEntity(announcementDTO);
        announcement = announcementRepository.save(announcement);
        return announcementMapper.toDto(announcement);
    }

    /**
     *  Get all the announcements.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnnouncementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Announcements");
        return announcementRepository.findAll(pageable)
            .map(announcementMapper::toDto);
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
        return announcementMapper.toDto(announcement);
    }

    /**
     *  Delete the  announcement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Announcement : {}", id);
        announcementRepository.delete(id);
    }
}
