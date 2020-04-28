package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CompanyDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Company and its DTO CompanyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper {

    CompanyDTO companyToCompanyDTO(Company company);

    List<CompanyDTO> companiesToCompanyDTOs(List<Company> companies);

    Company companyDTOToCompany(CompanyDTO companyDTO);

    List<Company> companyDTOsToCompanies(List<CompanyDTO> companyDTOs);

    default Company fromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

}
