package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.RegulationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Regulation and its DTO RegulationDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface RegulationMapper extends EntityMapper<RegulationDTO, Regulation> {

    @Mapping(source = "company.id", target = "companyId")
    RegulationDTO toDto(Regulation regulation); 

    @Mapping(source = "companyId", target = "company")
    Regulation toEntity(RegulationDTO regulationDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default Regulation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Regulation regulation = new Regulation();
        regulation.setId(id);
        return regulation;
    }
}
