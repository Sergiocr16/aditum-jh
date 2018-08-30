package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Complaint;
import com.lighthouse.aditum.repository.ComplaintRepository;
import com.lighthouse.aditum.service.dto.ComplaintDTO;
import com.lighthouse.aditum.service.mapper.ComplaintMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    public ComplaintService(HouseService houseService, ResidentService residentService, ComplaintRepository complaintRepository, ComplaintMapper complaintMapper) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
        this.residentService = residentService;
        this.houseService = houseService;
    }

    /**
     * Save a complaint.
     *
     * @param complaintDTO the entity to save
     * @return the persisted entity
     */
    public ComplaintDTO save(ComplaintDTO complaintDTO) {
        log.debug("Request to save Complaint : {}", complaintDTO);
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint = complaintRepository.save(complaint);
        return complaintMapper.toDto(complaint);
    }

    /**
     * Get all the complaints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findByCompanyIdAndDeleted(pageable , companyId, 0).map(complaint -> {
            ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
            complaintDTO.setResident(residentService.findOne(complaintDTO.getResidentId()));
            complaintDTO.setHouseNumber(houseService.findOne(complaintDTO.getHouseId()).getHousenumber());
            return complaintDTO;
        });

    }
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findAllByStatus(Pageable pageable, Long companyId, int status) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findByCompanyIdAndDeletedAndStatus(pageable , companyId, 0, status).map(complaint -> {
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
        complaintDTO.setHouseNumber(houseService.findOne(complaintDTO.getHouseId()).getHousenumber());
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
