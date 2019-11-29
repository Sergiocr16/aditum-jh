package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ResolutionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Resolution and its DTO ResolutionDTO.
 */
@Mapper(componentModel = "spring", uses = {ArticleMapper.class, KeyWordsMapper.class, ArticleCategoryMapper.class, CompanyMapper.class, AdminInfoMapper.class})
public interface ResolutionMapper extends EntityMapper<ResolutionDTO, Resolution> {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "adminInfo.id", target = "adminInfoId")
    ResolutionDTO toDto(Resolution resolution); 

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "adminInfoId", target = "adminInfo")


    default AdminInfo adminInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setId(id);
        return adminInfo;
    }


    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default Resolution fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resolution resolution = new Resolution();
        resolution.setId(id);
        return resolution;
    }
}
