package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.RevisionTaskCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RevisionTaskCategory and its DTO RevisionTaskCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface RevisionTaskCategoryMapper extends EntityMapper<RevisionTaskCategoryDTO, RevisionTaskCategory> {

    @Mapping(source = "company.id", target = "companyId")
    RevisionTaskCategoryDTO toDto(RevisionTaskCategory revisionTaskCategory);

    @Mapping(source = "companyId", target = "company")
    RevisionTaskCategory toEntity(RevisionTaskCategoryDTO revisionTaskCategoryDTO);

    default RevisionTaskCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        RevisionTaskCategory revisionTaskCategory = new RevisionTaskCategory();
        revisionTaskCategory.setId(id);
        return revisionTaskCategory;
    }
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
