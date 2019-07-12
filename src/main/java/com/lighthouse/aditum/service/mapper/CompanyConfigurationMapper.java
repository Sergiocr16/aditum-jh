package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CompanyConfigurationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CompanyConfiguration and its DTO CompanyConfigurationDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface CompanyConfigurationMapper extends EntityMapper<CompanyConfigurationDTO, CompanyConfiguration> {

    @Mapping(source = "company.id", target = "companyId")
    CompanyConfigurationDTO toDto(CompanyConfiguration companyConfiguration); 

    @Mapping(source = "companyId", target = "company")
    CompanyConfiguration toEntity(CompanyConfigurationDTO companyConfigurationDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default CompanyConfiguration fromId(Long id) {
        if (id == null) {
            return null;
        }
        CompanyConfiguration companyConfiguration = new CompanyConfiguration();
        companyConfiguration.setId(id);
        return companyConfiguration;
    }
}
