package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.AdministrationConfigurationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AdministrationConfiguration and its DTO AdministrationConfigurationDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, })
public interface AdministrationConfigurationMapper extends EntityMapper <AdministrationConfigurationDTO, AdministrationConfiguration> {

    @Mapping(source = "company.id", target = "companyId")
    AdministrationConfigurationDTO toDto(AdministrationConfiguration administrationConfiguration);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    @Mapping(source = "companyId", target = "company")
    AdministrationConfiguration toEntity(AdministrationConfigurationDTO administrationConfigurationDTO);
    default AdministrationConfiguration fromId(Long id) {
        if (id == null) {
            return null;
        }
        AdministrationConfiguration administrationConfiguration = new AdministrationConfiguration();
        administrationConfiguration.setId(id);
        return administrationConfiguration;
    }
}
