package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.JuntaDirectivaAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity JuntaDirectivaAccount and its DTO JuntaDirectivaAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, UserMapper.class})
public interface JuntaDirectivaAccountMapper extends EntityMapper<JuntaDirectivaAccountDTO, JuntaDirectivaAccount> {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    JuntaDirectivaAccountDTO toDto(JuntaDirectivaAccount juntaDirectivaAccount); 

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "userId", target = "user")
    JuntaDirectivaAccount toEntity(JuntaDirectivaAccountDTO juntaDirectivaAccountDTO);

    default JuntaDirectivaAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        JuntaDirectivaAccount juntaDirectivaAccount = new JuntaDirectivaAccount();
        juntaDirectivaAccount.setId(id);
        return juntaDirectivaAccount;
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
