package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.OfficerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Officer and its DTO OfficerDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, })
public interface OfficerMapper extends EntityMapper <OfficerDTO, Officer> {

    @Mapping(source = "company.id", target = "companyId")
    OfficerDTO toDto(Officer officer);

    @Mapping(source = "companyId", target = "company")
    Officer toEntity(OfficerDTO officerDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default Officer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Officer officer = new Officer();
        officer.setId(id);
        return officer;
    }
}
