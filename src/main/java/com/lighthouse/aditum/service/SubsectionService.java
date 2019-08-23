package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Subsection;
import com.lighthouse.aditum.repository.SubsectionRepository;
import com.lighthouse.aditum.service.dto.SubsectionDTO;
import com.lighthouse.aditum.service.mapper.SubsectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing Subsection.
 */
@Service
@Transactional
public class SubsectionService {

    private final Logger log = LoggerFactory.getLogger(SubsectionService.class);

    private final SubsectionRepository subsectionRepository;

    private final SubsectionMapper subsectionMapper;

    public SubsectionService(SubsectionRepository subsectionRepository, SubsectionMapper subsectionMapper) {
        this.subsectionRepository = subsectionRepository;
        this.subsectionMapper = subsectionMapper;
    }

    /**
     * Save a subsection.
     *
     * @param subsectionDTO the entity to save
     * @return the persisted entity
     */
    public SubsectionDTO save(SubsectionDTO subsectionDTO) {
        log.debug("Request to save Subsection : {}", subsectionDTO);
        Subsection subsection = subsectionMapper.toEntity(subsectionDTO);
        subsection = subsectionRepository.save(subsection);
        return subsectionMapper.toDto(subsection);
    }

    /**
     * Get all the subsections.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SubsectionDTO> findAll(Pageable pageable,Long articleId) {
        log.debug("Request to get all Subsections");
        return subsectionRepository.findByArticleIdAndDeleted(pageable,articleId,0)
            .map(subsectionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<SubsectionDTO> getCompleteSubsectionsByArticle(Long articleId) {
        log.debug("Request to get all Subsections");
        List<Subsection> subsections =  subsectionRepository.findByArticleIdAndDeleted(articleId,0);
        List<SubsectionDTO> subsectionDTOS = new ArrayList<>();
        for (Subsection subsection: subsections) {
            SubsectionDTO subsectionDTO = subsectionMapper.toDto(subsection);
            subsectionDTOS.add(subsectionDTO);
        }
        return subsectionDTOS;
    }

    /**
     * Get one subsection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SubsectionDTO findOne(Long id) {
        log.debug("Request to get Subsection : {}", id);
        Subsection subsection = subsectionRepository.findOne(id);
        return subsectionMapper.toDto(subsection);
    }

    /**
     * Delete the subsection by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Subsection : {}", id);
        subsectionRepository.delete(id);
    }
}
