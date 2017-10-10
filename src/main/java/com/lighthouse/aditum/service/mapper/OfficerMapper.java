package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.OfficerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Officer and its DTO OfficerDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface OfficerMapper {

    @Mapping(source = "company.id", target = "companyId")
    OfficerDTO officerToOfficerDTO(Officer officer);

    List<OfficerDTO> officersToOfficerDTOs(List<Officer> officers);

    @Mapping(source = "companyId", target = "company")
    Officer officerDTOToOfficer(OfficerDTO officerDTO);

    List<Officer> officerDTOsToOfficers(List<OfficerDTO> officerDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
