package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ActivosFijosDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivosFijos and its DTO ActivosFijosDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface ActivosFijosMapper extends EntityMapper<ActivosFijosDTO, ActivosFijos> {

    @Mapping(source = "company.id", target = "companyId")
    ActivosFijosDTO toDto(ActivosFijos activosFijos); 

    @Mapping(source = "companyId", target = "company")
    ActivosFijos toEntity(ActivosFijosDTO activosFijosDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default ActivosFijos fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivosFijos activosFijos = new ActivosFijos();
        activosFijos.setId(id);
        return activosFijos;
    }
}
