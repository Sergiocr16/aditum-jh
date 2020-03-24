package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CommonArea;
import com.lighthouse.aditum.repository.CommonAreaRepository;
import com.lighthouse.aditum.service.dto.CommonAreaDTO;
import com.lighthouse.aditum.service.mapper.CommonAreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing CommonArea.
 */
@Service
@Transactional
public class CommonAreaService {

    private final Logger log = LoggerFactory.getLogger(CommonAreaService.class);

    private final CommonAreaRepository commonAreaRepository;

    private final CommonAreaMapper commonAreaMapper;

    public CommonAreaService(CommonAreaRepository commonAreaRepository, CommonAreaMapper commonAreaMapper) {
        this.commonAreaRepository = commonAreaRepository;
        this.commonAreaMapper = commonAreaMapper;
    }

    /**
     * Save a commonArea.
     *
     * @param commonAreaDTO the entity to save
     * @return the persisted entity
     */
    public CommonAreaDTO save(CommonAreaDTO commonAreaDTO) {
        log.debug("Request to save CommonArea : {}", commonAreaDTO);
        CommonArea commonArea = commonAreaMapper.toEntity(commonAreaDTO);
        commonArea = commonAreaRepository.save(commonArea);
        return commonAreaMapper.toDto(commonArea);
    }

    /**
     *  Get all the commonAreas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommonAreaDTO> findAll(Pageable pageable,int companyId) {
        log.debug("Request to get all CommonAreas");
        return commonAreaRepository.findByCompanyIdAndDeleted(pageable,companyId,0)
            .map(commonAreaMapper::toDto);
    }

    /**
     *  Get one commonArea by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CommonAreaDTO findOne(Long id) {
        log.debug("Request to get CommonArea : {}", id);
        CommonArea commonArea = commonAreaRepository.findOne(id);
        CommonAreaDTO commonAreaDTO =  commonAreaMapper.toDto(commonArea);
        commonAreaDTO.setDaysToReserveIfFree(commonArea.getDaysToReserveIfFree());
        return commonAreaDTO;
    }

    /**
     *  Delete the  commonArea by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonArea : {}", id);
        commonAreaRepository.delete(id);
    }
}
