package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ContractDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contract and its DTO ContractDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {

    @Mapping(source = "company.id", target = "companyId")
    ContractDTO toDto(Contract contract);

    @Mapping(source = "companyId", target = "company")
    Contract toEntity(ContractDTO contractDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default Contract fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }
}
