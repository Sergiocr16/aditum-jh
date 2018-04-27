package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.BancoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Banco and its DTO BancoDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, })
public interface BancoMapper extends EntityMapper <BancoDTO, Banco> {

    @Mapping(source = "company.id", target = "companyId")
    BancoDTO toDto(Banco banco);

    @Mapping(source = "companyId", target = "company")

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    Banco toEntity(BancoDTO bancoDTO);
    default Banco fromId(Long id) {
        if (id == null) {
            return null;
        }
        Banco banco = new Banco();
        banco.setId(id);
        return banco;
    }
}
