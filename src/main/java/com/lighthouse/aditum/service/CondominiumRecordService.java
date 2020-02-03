package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CondominiumRecord;
import com.lighthouse.aditum.repository.CondominiumRecordRepository;
import com.lighthouse.aditum.service.dto.CondominiumRecordDTO;
import com.lighthouse.aditum.service.mapper.CondominiumRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing CondominiumRecord.
 */
@Service
@Transactional
public class CondominiumRecordService {

    private final Logger log = LoggerFactory.getLogger(CondominiumRecordService.class);

    private final CondominiumRecordRepository condominiumRecordRepository;

    private final CondominiumRecordMapper condominiumRecordMapper;

    public CondominiumRecordService(CondominiumRecordRepository condominiumRecordRepository, CondominiumRecordMapper condominiumRecordMapper) {
        this.condominiumRecordRepository = condominiumRecordRepository;
        this.condominiumRecordMapper = condominiumRecordMapper;
    }

    /**
     * Save a condominiumRecord.
     *
     * @param condominiumRecordDTO the entity to save
     * @return the persisted entity
     */
    public CondominiumRecordDTO save(CondominiumRecordDTO condominiumRecordDTO) {
        log.debug("Request to save CondominiumRecord : {}", condominiumRecordDTO);
        CondominiumRecord condominiumRecord = condominiumRecordMapper.toEntity(condominiumRecordDTO);
        condominiumRecord = condominiumRecordRepository.save(condominiumRecord);
        return condominiumRecordMapper.toDto(condominiumRecord);
    }

    /**
     * Get all the condominiumRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CondominiumRecordDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all CondominiumRecords");
        return condominiumRecordRepository.findAllByCompanyIdAndDeleted(pageable,companyId,0)
            .map(condominiumRecordMapper::toDto);
    }

    /**
     * Get one condominiumRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CondominiumRecordDTO findOne(Long id) {
        log.debug("Request to get CondominiumRecord : {}", id);
        CondominiumRecord condominiumRecord = condominiumRecordRepository.findOne(id);
        return condominiumRecordMapper.toDto(condominiumRecord);
    }

    /**
     * Delete the condominiumRecord by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CondominiumRecord : {}", id);
        condominiumRecordRepository.delete(id);
    }
}
