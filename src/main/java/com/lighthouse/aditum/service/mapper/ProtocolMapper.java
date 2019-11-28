package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ProtocolDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Protocol and its DTO ProtocolDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface ProtocolMapper extends EntityMapper<ProtocolDTO, Protocol> {

    @Mapping(source = "company.id", target = "companyId")
    ProtocolDTO toDto(Protocol protocol);

    @Mapping(source = "companyId", target = "company")
    Protocol toEntity(ProtocolDTO protocolDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default Protocol fromId(Long id) {
        if (id == null) {
            return null;
        }
        Protocol protocol = new Protocol();
        protocol.setId(id);
        return protocol;
    }
}
