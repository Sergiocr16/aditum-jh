package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.EgressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Egress and its DTO EgressDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, })
public interface EgressMapper extends EntityMapper <EgressDTO, Egress> {

    @Mapping(source = "company.id", target = "companyId")
    EgressDTO toDto(Egress egress);

    @Mapping(source = "companyId", target = "company")
    Egress toEntity(EgressDTO egressDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default Egress fromId(Long id) {
        if (id == null) {
            return null;
        }
        Egress egress = new Egress();
        egress.setId(id);
        return egress;
    }
}
