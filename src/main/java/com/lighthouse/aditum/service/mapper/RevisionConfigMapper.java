package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.RevisionConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RevisionConfig and its DTO RevisionConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface RevisionConfigMapper extends EntityMapper<RevisionConfigDTO, RevisionConfig> {

    @Mapping(source = "company.id", target = "companyId")
    RevisionConfigDTO toDto(RevisionConfig revisionConfig);

    @Mapping(source = "companyId", target = "company")
    RevisionConfig toEntity(RevisionConfigDTO revisionConfigDTO);

    default RevisionConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        RevisionConfig revisionConfig = new RevisionConfig();
        revisionConfig.setId(id);
        return revisionConfig;
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
