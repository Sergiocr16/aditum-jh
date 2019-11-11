package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ProceduresDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Procedures and its DTO ProceduresDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface ProceduresMapper extends EntityMapper<ProceduresDTO, Procedures> {

    @Mapping(source = "company.id", target = "companyId")
    ProceduresDTO toDto(Procedures procedures);

    @Mapping(source = "companyId", target = "company")
    Procedures toEntity(ProceduresDTO proceduresDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default Procedures fromId(Long id) {
        if (id == null) {
            return null;
        }
        Procedures procedures = new Procedures();
        procedures.setId(id);
        return procedures;
    }
}
