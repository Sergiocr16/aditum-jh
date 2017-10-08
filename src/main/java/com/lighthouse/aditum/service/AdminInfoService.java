package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.AdminInfo;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.repository.AdminInfoRepository;
import com.lighthouse.aditum.repository.CompanyRepository;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.mapper.AdminInfoMapper;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private final CompanyMapper companyMapper;

    private final CompanyRepository companyRepository;

    public AdminInfoService(AdminInfoRepository adminInfoRepository, AdminInfoMapper adminInfoMapper,CompanyMapper companyMapper,CompanyRepository companyRepository) {
        this.adminInfoRepository = adminInfoRepository;
        this.adminInfoMapper = adminInfoMapper;
        this.companyMapper = companyMapper;
        this.companyRepository = companyRepository;
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
        if (adminInfoDTO.getCompanies() != null) {
            Set<Company> companies = new HashSet<>();
            adminInfoDTO.getCompanies().forEach(
                company -> companies.add(companyRepository.findOneById(company.getId()))
            );
            adminInfo.setCompanies(companies);
        }
        adminInfo.setImage_url(adminInfoDTO.getImage_url());
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

        return result.map(adminInfo -> {
            Set<Company> companies = new HashSet<>();
            adminInfo.setCompanies(adminInfo.getCompanies());
            return  adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo);
        });
    }

    @Transactional(readOnly = true)
    public Page<AdminInfoDTO> findAllByCompany(Pageable pageable,Long companyId) {
        log.debug("Request to get all AdminInfos");
        Company company = companyRepository.findOne(companyId);
        List<AdminInfo> targetList = new ArrayList<>(company.getAdminInfos());
        return  new PageImpl<AdminInfo>(targetList).map(adminInfo -> adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo));
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
        adminInfoDTO.setImage_url(adminInfo.getImage_url());
        Set<CompanyDTO> companies = new HashSet<>();
        adminInfo.getCompanies().forEach(company -> companies.add(companyMapper.companyToCompanyDTO(company)));
        adminInfoDTO.setCompanies(companies);
        return adminInfoDTO;
    }

    @Transactional(readOnly = true)
    public AdminInfoDTO findOneByUserId(Long id) {
        log.debug("Request to get AdminInfo : {}", id);
        AdminInfo adminInfo = adminInfoRepository.findOneByUserId(id);
        AdminInfoDTO adminInfoDTO = adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo);
        adminInfoDTO.setImage_url(adminInfo.getImage_url());
        Set<CompanyDTO> companies = new HashSet<>();
        List<Company> c = new ArrayList<Company>(adminInfo.getCompanies());
        adminInfo.getCompanies().forEach(company -> companies.add(companyMapper.companyToCompanyDTO(company)));

        adminInfoDTO.setCompanies(companies);
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
