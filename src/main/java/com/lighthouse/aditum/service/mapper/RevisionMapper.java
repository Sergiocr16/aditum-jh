package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.RevisionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Revision and its DTO RevisionDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface RevisionMapper extends EntityMapper<RevisionDTO, Revision> {

    @Mapping(source = "company.id", target = "companyId")
    RevisionDTO toDto(Revision revision);

    @Mapping(target = "revisionTasks", ignore = true)
    @Mapping(source = "companyId", target = "company")
    Revision toEntity(RevisionDTO revisionDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default Revision fromId(Long id) {
        if (id == null) {
            return null;
        }
        Revision revision = new Revision();
        revision.setId(id);
        return revision;
    }
}
