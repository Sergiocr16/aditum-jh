package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.AdminInfo;
import com.lighthouse.aditum.repository.AdminInfoRepository;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.mapper.AdminInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing AdminInfo.
 */
@Service
@Transactional
public class AdminInfoService {

    private final Logger log = LoggerFactory.getLogger(AdminInfoService.class);

    private final AdminInfoRepository adminInfoRepository;

    private final AdminInfoMapper adminInfoMapper;

    public AdminInfoService(AdminInfoRepository adminInfoRepository, AdminInfoMapper adminInfoMapper) {
        this.adminInfoRepository = adminInfoRepository;
        this.adminInfoMapper = adminInfoMapper;
    }

    /**
     * Save a adminInfo.
     *
     * @param adminInfoDTO the entity to save
     * @return the persisted entity
     */
    public AdminInfoDTO save(AdminInfoDTO adminInfoDTO) {
        log.debug("Request to save AdminInfo : {}", adminInfoDTO);
        AdminInfo adminInfo = adminInfoMapper.adminInfoDTOToAdminInfo(adminInfoDTO);
        adminInfo = adminInfoRepository.save(adminInfo);
        AdminInfoDTO result = adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo);
        return result;
    }

    /**
     *  Get all the adminInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AdminInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdminInfos");
        Page<AdminInfo> result = adminInfoRepository.findAll(pageable);
        return result.map(adminInfo -> adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo));
    }

    /**
     *  Get one adminInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AdminInfoDTO findOne(Long id) {
        log.debug("Request to get AdminInfo : {}", id);
        AdminInfo adminInfo = adminInfoRepository.findOne(id);
        AdminInfoDTO adminInfoDTO = adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo);
        return adminInfoDTO;
    }

    @Transactional(readOnly = true)
    public AdminInfoDTO findOneByUserId(Long id) {
        log.debug("Request to get AdminInfo : {}", id);
        AdminInfo adminInfo = adminInfoRepository.findOneByUserId(id);
        AdminInfoDTO adminInfoDTO = adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo);
        return adminInfoDTO;
    }

    /**
     *  Delete the  adminInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AdminInfo : {}", id);
        adminInfoRepository.delete(id);
    }
}
