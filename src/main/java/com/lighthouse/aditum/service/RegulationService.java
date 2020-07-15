package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Chapter;
import com.lighthouse.aditum.domain.Regulation;
import com.lighthouse.aditum.repository.RegulationRepository;
import com.lighthouse.aditum.service.dto.ArticleDTO;
import com.lighthouse.aditum.service.dto.ChapterDTO;
import com.lighthouse.aditum.service.dto.RegulationDTO;
import com.lighthouse.aditum.service.dto.SubsectionDTO;
import com.lighthouse.aditum.service.mapper.RegulationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Regulation.
 */
@Service
@Transactional
public class RegulationService {

    private final Logger log = LoggerFactory.getLogger(RegulationService.class);

    private final RegulationRepository regulationRepository;

    private final ChapterService chapterService;

    private final RegulationMapper regulationMapper;

    SubsectionService subsectionService;

    public RegulationService(SubsectionService subsectionService, ChapterService chapterService,RegulationRepository regulationRepository, RegulationMapper regulationMapper) {
        this.regulationRepository = regulationRepository;
        this.regulationMapper = regulationMapper;
        this.chapterService = chapterService;
        this.subsectionService = subsectionService;
    }

    /**
     * Save a regulation.
     *
     * @param regulationDTO the entity to save
     * @return the persisted entity
     */
    public RegulationDTO save(RegulationDTO regulationDTO) {
        log.debug("Request to save Regulation : {}", regulationDTO);

        Regulation regulation = regulationMapper.toEntity(regulationDTO);
        regulation.setFileName(regulationDTO.getFileName());
        String a = "a";
        regulation = regulationRepository.save(regulation);
        return regulationMapper.toDto(regulation);
    }

    /**
     * Get all the regulations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RegulationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Regulations");
        return regulationRepository.findByDeleted(pageable,0)
            .map(regulationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<RegulationDTO> findAllByCompanyId(Pageable pageable, Long companyId) {
        log.debug("Request to get all Regulations");
        return regulationRepository.findByDeletedAndCompanyId(pageable,0, companyId)
            .map(regulationMapper::toDto);
    }

    /**
     * Get one regulation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RegulationDTO getCompleteRegulation(Long id) {
        log.debug("Request to get Regulation : {}", id);
        Regulation regulation = regulationRepository.findOne(id);
        RegulationDTO regulationDTO = regulationMapper.toDto(regulation);
        regulationDTO.setChapters(chapterService.getCompleteChaptersByRegulation(id));
        for (ChapterDTO chapter : regulationDTO.getChapters()) {
            for (ArticleDTO articleDTO : chapter.getArticles()) {
                for (ArticleDTO reference : articleDTO.getReferences()) {
                    ChapterDTO chapterDTO = chapterService.findOne(reference.getChapterId());
                    reference.setRegulationName(this.findOne(chapterDTO.getRegulationId()).getName());
                    reference.setChapterName(chapterDTO.getName());
                    reference.setChapterDescription(chapterDTO.getDescription());
                    reference.setSubsections(subsectionService.getCompleteSubsectionsByArticle(reference.getId()));;
                }
            }
        }
        return regulationDTO;
    }

    @Transactional(readOnly = true)
    public RegulationDTO searchRegulation(RegulationDTO regulationDTO) {
        for (ChapterDTO chapter:regulationDTO.getChapters()) {
           String a = "a";
        }
        return null;
    }
    /**
     * Get one regulation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RegulationDTO findOne(Long id) {
        log.debug("Request to get Regulation : {}", id);
        Regulation regulation = regulationRepository.findOne(id);
        RegulationDTO regulationDTO = regulationMapper.toDto(regulation);
        regulationDTO.setFileName(regulation.getFileName());

        return regulationDTO;
    }
    /**
     * Delete the regulation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Regulation : {}", id);
        regulationRepository.delete(id);
    }
}
