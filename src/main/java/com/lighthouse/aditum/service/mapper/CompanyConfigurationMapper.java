package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CompanyConfigurationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CompanyConfiguration and its DTO CompanyConfigurationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyConfigurationMapper {

    @Mapping(source = "company.id", target = "companyId")
    CompanyConfigurationDTO companyConfigurationToCompanyConfigurationDTO(CompanyConfiguration companyConfiguration);

    List<CompanyConfigurationDTO> companyConfigurationsToCompanyConfigurationDTOs(List<CompanyConfiguration> companyConfigurations);

    @Mapping(source = "companyId", target = "company")
    CompanyConfiguration companyConfigurationDTOToCompanyConfiguration(CompanyConfigurationDTO companyConfigurationDTO);

    List<CompanyConfiguration> companyConfigurationDTOsToCompanyConfigurations(List<CompanyConfigurationDTO> companyConfigurationDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
