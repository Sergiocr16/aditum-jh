package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.OfficerARDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OfficerAR and its DTO OfficerARDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, UserMapper.class, HouseMapper.class})
public interface OfficerARMapper extends EntityMapper<OfficerARDTO, OfficerAR> {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    OfficerARDTO toDto(OfficerAR officerAR); 

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "userId", target = "user")
    OfficerAR toEntity(OfficerARDTO officerARDTO);

    default OfficerAR fromId(Long id) {
        if (id == null) {
            return null;
        }
        OfficerAR officerAR = new OfficerAR();
        officerAR.setId(id);
        return officerAR;
    }
}
