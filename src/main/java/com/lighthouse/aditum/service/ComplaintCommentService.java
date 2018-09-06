package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ComplaintComment;
import com.lighthouse.aditum.repository.ComplaintCommentRepository;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.dto.ComplaintCommentDTO;
import com.lighthouse.aditum.service.dto.ComplaintDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.ComplaintCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ComplaintComment.
 */
@Service
@Transactional
public class ComplaintCommentService {

    private final Logger log = LoggerFactory.getLogger(ComplaintCommentService.class);

    private final ComplaintCommentRepository complaintCommentRepository;

    private final ComplaintCommentMapper complaintCommentMapper;

    private final ResidentService residentService;

    private final AdminInfoService adminInfoService;

    private final ComplaintMailService complaintMailService;

    private final ComplaintService complaintService;

    public ComplaintCommentService(ComplaintMailService complaintMailService, @Lazy ComplaintService complaintService, AdminInfoService adminInfoService, ResidentService residentService, ComplaintCommentRepository complaintCommentRepository, ComplaintCommentMapper complaintCommentMapper) {
        this.complaintCommentRepository = complaintCommentRepository;
        this.complaintCommentMapper = complaintCommentMapper;
        this.adminInfoService = adminInfoService;
        this.residentService = residentService;
        this.complaintMailService = complaintMailService;
        this.complaintService = complaintService;
    }

    /**
     * Save a complaintComment.
     *
     * @param complaintCommentDTO the entity to save
     * @return the persisted entity
     */
    public ComplaintCommentDTO save(ComplaintCommentDTO complaintCommentDTO) {
        log.debug("Request to save ComplaintComment : {}", complaintCommentDTO);
        ComplaintComment complaintComment = complaintCommentMapper.toEntity(complaintCommentDTO);
        complaintComment = complaintCommentRepository.save(complaintComment);
        ComplaintDTO complaintDTO = this.complaintService.findOne(complaintCommentDTO.getComplaintId());
        this.complaintMailService.sendComplaintEmailChangeStatus(complaintDTO);
        return complaintCommentMapper.toDto(complaintComment);
    }

    /**
     * Get all the complaintComments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ComplaintCommentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ComplaintComments");
        return complaintCommentRepository.findAll(pageable)
            .map(complaintCommentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ComplaintCommentDTO> findAllByComplaint(Pageable pageable,Long complaintId) {
        log.debug("Request to get all ComplaintComments");
        return complaintCommentRepository.findComplaintCommentByComplaintIdAndDeleted(pageable, complaintId, 0)
            .map(complaintComment -> {
                ComplaintCommentDTO complaintCommentDTO = complaintCommentMapper.toDto(complaintComment);
                if(complaintComment.getAdminInfo()!=null) {
                    complaintCommentDTO.setAdminInfoId(complaintComment.getAdminInfo().getId());
                }
                if(complaintComment.getResident()!=null) {
                    complaintCommentDTO.setResidentId(complaintComment.getResident().getId());
                }
                if(complaintCommentDTO.getResidentId()!=null){
                    complaintCommentDTO.setResident(residentService.findOne(complaintCommentDTO.getResidentId()));
                }else {
                    AdminInfoDTO adminInfoFound = adminInfoService.findOne(complaintCommentDTO.getAdminInfoId());
                    ResidentDTO adminAsResident = new ResidentDTO();
                    adminAsResident.setId(adminInfoFound.getId());
                    adminAsResident.setName(adminInfoFound.getName());
                    adminAsResident.setLastname(adminInfoFound.getLastname());
                    adminAsResident.setSecondlastname(adminInfoFound.getSecondlastname());
                    adminAsResident.setImage_url(adminInfoFound.getImage_url());
                    adminAsResident.setIdentificationnumber(adminInfoFound.getIdentificationnumber());
                    complaintCommentDTO.setResident(adminAsResident);
                }
                return complaintCommentDTO;
            });
    }

    /**
     * Get one complaintComment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ComplaintCommentDTO findOne(Long id) {
        log.debug("Request to get ComplaintComment : {}", id);
        ComplaintComment complaintComment = complaintCommentRepository.findOne(id);
        return complaintCommentMapper.toDto(complaintComment);
    }

    /**
     * Delete the complaintComment by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ComplaintComment : {}", id);
        ComplaintComment complaintComment = complaintCommentRepository.findOne(id);
        complaintComment.setDeleted(1);
        complaintCommentRepository.save(complaintComment);
        complaintCommentRepository.delete(id);
    }
}
