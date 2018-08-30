package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.ComplaintComment;
import com.lighthouse.aditum.repository.ComplaintCommentRepository;
import com.lighthouse.aditum.service.dto.ComplaintCommentDTO;
import com.lighthouse.aditum.service.mapper.ComplaintCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public ComplaintCommentService(ComplaintCommentRepository complaintCommentRepository, ComplaintCommentMapper complaintCommentMapper) {
        this.complaintCommentRepository = complaintCommentRepository;
        this.complaintCommentMapper = complaintCommentMapper;
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
    public Page<ComplaintCommentDTO> findAllByComplaint(Pageable pageable) {
        log.debug("Request to get all ComplaintComments");
        return complaintCommentRepository.findAll(pageable)
            .map(complaintCommentMapper::toDto);
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
        complaintCommentRepository.delete(id);
    }
}
