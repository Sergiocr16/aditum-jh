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

    public ComplaintService(ComplaintRepository complaintRepository, ComplaintMapper complaintMapper) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
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
    public Page<ComplaintDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findAll(pageable)
            .map(complaintMapper::toDto);
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
        Complaint complaint = complaintRepository.findOne(id);
        return complaintMapper.toDto(complaint);
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
