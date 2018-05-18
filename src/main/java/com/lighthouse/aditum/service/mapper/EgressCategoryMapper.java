package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.EgressCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EgressCategory and its DTO EgressCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, })
public interface EgressCategoryMapper extends EntityMapper <EgressCategoryDTO, EgressCategory> {

    @Mapping(source = "company.id", target = "companyId")
    EgressCategoryDTO toDto(EgressCategory egressCategory);

    @Mapping(source = "companyId", target = "company")
    EgressCategory toEntity(EgressCategoryDTO egressCategoryDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default EgressCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        EgressCategory egressCategory = new EgressCategory();
        egressCategory.setId(id);
        return egressCategory;
    }
}
