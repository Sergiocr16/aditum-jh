package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.MensualBillingFile;
import com.lighthouse.aditum.repository.MensualBillingFileRepository;
import com.lighthouse.aditum.service.dto.MensualBillingFileDTO;
import com.lighthouse.aditum.service.mapper.MensualBillingFileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing MensualBillingFile.
 */
@Service
@Transactional
public class MensualBillingFileService {

    private final Logger log = LoggerFactory.getLogger(MensualBillingFileService.class);

    private final MensualBillingFileRepository mensualBillingFileRepository;

    private final MensualBillingFileMapper mensualBillingFileMapper;

    public MensualBillingFileService(MensualBillingFileRepository mensualBillingFileRepository, MensualBillingFileMapper mensualBillingFileMapper) {
        this.mensualBillingFileRepository = mensualBillingFileRepository;
        this.mensualBillingFileMapper = mensualBillingFileMapper;
    }

    /**
     * Save a mensualBillingFile.
     *
     * @param mensualBillingFileDTO the entity to save
     * @return the persisted entity
     */
    public MensualBillingFileDTO save(MensualBillingFileDTO mensualBillingFileDTO) {
        log.debug("Request to save MensualBillingFile : {}", mensualBillingFileDTO);
        MensualBillingFile mensualBillingFile = mensualBillingFileMapper.toEntity(mensualBillingFileDTO);
        mensualBillingFile = mensualBillingFileRepository.save(mensualBillingFile);
        return mensualBillingFileMapper.toDto(mensualBillingFile);
    }

    /**
     * Get all the mensualBillingFiles.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MensualBillingFileDTO> findAll(Long companyId, int month,String year) {
        log.debug("Request to get all MensualBillingFiles");
        return mensualBillingFileRepository.findAllByCompanyIdAndMonthAndYearAndDeleted(companyId,month+"",year,0).stream()
            .map(mensualBillingFileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<MensualBillingFileDTO> findAllPrivacy(Long companyId, int month,String year) {
        log.debug("Request to get all MensualBillingFiles");
        return mensualBillingFileRepository.findAllByCompanyIdAndMonthAndYearAndStatusAndDeleted(companyId,month+"",year,"true",0).stream()
            .map(mensualBillingFileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    /**
     * Get one mensualBillingFile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MensualBillingFileDTO findOne(Long id) {
        log.debug("Request to get MensualBillingFile : {}", id);
        MensualBillingFile mensualBillingFile = mensualBillingFileRepository.findOne(id);
        return mensualBillingFileMapper.toDto(mensualBillingFile);
    }

    /**
     * Delete the mensualBillingFile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MensualBillingFile : {}", id);
        MensualBillingFile mensualBillingFile = mensualBillingFileRepository.findOne(id);
        mensualBillingFile.setDeleted(1);
        mensualBillingFileRepository.save(mensualBillingFile);
    }
}
