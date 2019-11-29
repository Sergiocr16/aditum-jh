package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.SubsidiaryCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SubsidiaryCategory and its DTO SubsidiaryCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface SubsidiaryCategoryMapper extends EntityMapper<SubsidiaryCategoryDTO, SubsidiaryCategory> {

    @Mapping(source = "company.id", target = "companyId")
    SubsidiaryCategoryDTO toDto(SubsidiaryCategory subsidiaryCategory);

    @Mapping(source = "companyId", target = "company")
    SubsidiaryCategory toEntity(SubsidiaryCategoryDTO subsidiaryCategoryDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default SubsidiaryCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubsidiaryCategory subsidiaryCategory = new SubsidiaryCategory();
        subsidiaryCategory.setId(id);
        return subsidiaryCategory;
    }
}
