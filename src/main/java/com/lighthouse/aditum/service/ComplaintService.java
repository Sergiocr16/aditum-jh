package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Complaint;
import com.lighthouse.aditum.repository.ComplaintRepository;
import com.lighthouse.aditum.service.dto.ComplaintDTO;
import com.lighthouse.aditum.service.dto.NotificationRequestDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.ComplaintMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.List;


/**
 * Service Implementation for managing Complaint.
 */
@Service
@Transactional
public class ComplaintService {

    private final Logger log = LoggerFactory.getLogger(ComplaintService.class);

    private final ComplaintRepository complaintRepository;

    private final ComplaintMapper complaintMapper;

    private final ResidentService residentService;

    private final HouseService houseService;

    private final ComplaintCommentService complaintCommentService;

    private final ComplaintMailService complaintMailService;

    private final PushNotificationService pNotification;

    public ComplaintService(PushNotificationService pNotification,ComplaintMailService complaintMailService, @Lazy ComplaintCommentService complaintCommentService, HouseService houseService, ResidentService residentService, ComplaintRepository complaintRepository, ComplaintMapper complaintMapper) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
        this.residentService = residentService;
        this.houseService = houseService;
        this.complaintCommentService = complaintCommentService;
        this.complaintMailService = complaintMailService;
        this.pNotification = pNotification;
    }

    /**
     * Save a complaint.
     *
     * @param complaintDTO the entity to save
     * @return the persisted entity
     */

    @Async
    public ComplaintDTO newIndividualRelease(ComplaintDTO complaintDTO) throws URISyntaxException {
        ComplaintDTO result = null;
        if(complaintDTO.getToSend()==null){
            result = this.save(complaintDTO);
        }else{
            String [] ids = complaintDTO.getToSend().split(";");
            for (int i = 0; i < ids.length; i++) {
                ComplaintDTO c = complaintDTO;
                c.setHouseId(Long.parseLong(ids[i]));
                ResidentDTO pC = this.residentService.findPrincipalContactByHouse(c.getHouseId());
                if(pC!=null){
                    c.setResidentId(pC.getId());
                    result = this.save(c);
                }else{
                    List<ResidentDTO> rs = this.residentService.findOwnerByHouse(c.getHouseId()+"");
                    if(rs.size()>0){
                        c.setResidentId(rs.get(0).getId());
                        result = this.save(c);
                    }
                }
            }
        }
        return result;
    }
    public ComplaintDTO save(ComplaintDTO complaintDTO) throws URISyntaxException {
        log.debug("Request to save Complaint : {}", complaintDTO);
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint = complaintRepository.save(complaint);
        ComplaintDTO complaintDTO1 = complaintMapper.toDto(complaint);
        complaintDTO1.setResident(residentService.findOne(complaintDTO1.getResidentId()));
        complaintDTO1.setHouseNumber(houseService.findOneClean(complaintDTO1.getHouseId()).getHousenumber());
        complaintDTO1.setComplaintComments(this.complaintCommentService.findAllByComplaint(null,complaintDTO1.getId()));
        if(complaintDTO.getId()==null){
            this.complaintMailService.sendNewComplaintEmail(complaintDTO1);
        }
        return complaintDTO1;
    }

    /**
     * Get all the complaints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findAll(Pageable pageable, Long companyId, int category) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findByCompanyIdAndDeletedAndComplaintCategory(pageable , companyId, 0, category).map(complaint -> {
            ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
            complaintDTO.setResident(residentService.findOne(complaintDTO.getResidentId()));
            complaintDTO.setHouseNumber(houseService.findOneClean(complaintDTO.getHouseId()).getHousenumber());
            return complaintDTO;
        });
    }

    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findAllByResident(Pageable pageable, Long residentId, int category) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findByResidentIdAndDeletedAndComplaintCategory(pageable , residentId, 0, category).map(complaint -> {
            ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
            complaintDTO.setResident(residentService.findOne(complaintDTO.getResidentId()));
            complaintDTO.setHouseNumber(houseService.findOne(complaintDTO.getHouseId()).getHousenumber());
            return complaintDTO;
        });
    }

    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findAllByResidentAndStatus(Pageable pageable, Long residentId,int status, int category) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findByResidentIdAndDeletedAndStatusAndComplaintCategory(pageable , residentId, 0, status, category).map(complaint -> {
            ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
            complaintDTO.setResident(residentService.findOne(complaintDTO.getResidentId()));
            complaintDTO.setHouseNumber(houseService.findOne(complaintDTO.getHouseId()).getHousenumber());
            return complaintDTO;
        });
    }
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findAllByStatus(Pageable pageable, Long companyId, int status, int category) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findByCompanyIdAndDeletedAndStatusAndComplaintCategory(pageable , companyId, 0, status, category).map(complaint -> {
            ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
            complaintDTO.setResident(residentService.findOne(complaintDTO.getResidentId()));
            complaintDTO.setHouseNumber(houseService.findOne(complaintDTO.getHouseId()).getHousenumber());
            return complaintDTO;
        });
    }
    /**
     * Get one complaint by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ComplaintDTO findOne(Long id) {
        log.debug("Request to get Complaint : {}", id);
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaintRepository.findOne(id));
        complaintDTO.setResident(residentService.findOne(complaintDTO.getResidentId()));
        complaintDTO.setHouseNumber(houseService.findOneClean(complaintDTO.getHouseId()).getHousenumber());
        complaintDTO.setComplaintComments(this.complaintCommentService.findAllByComplaint(null,complaintDTO.getId()));
        return complaintDTO;
    }

    /**
     * Delete the complaint by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Complaint : {}", id);
        complaintRepository.delete(id);
    }
}
