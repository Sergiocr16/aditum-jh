package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.MacroOfficerAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MacroOfficerAccount and its DTO MacroOfficerAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, MacroCondominiumMapper.class})
public interface MacroOfficerAccountMapper extends EntityMapper<MacroOfficerAccountDTO, MacroOfficerAccount> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "macroCondominium.id", target = "macroCondominiumId")
    @Mapping(source = "macroCondominium.name", target = "macroCondominiumName")
    MacroOfficerAccountDTO toDto(MacroOfficerAccount macroOfficerAccount); 

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "macroCondominiumId", target = "macroCondominium")
    MacroOfficerAccount toEntity(MacroOfficerAccountDTO macroOfficerAccountDTO);

    default MacroOfficerAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        MacroOfficerAccount macroOfficerAccount = new MacroOfficerAccount();
        macroOfficerAccount.setId(id);
        return macroOfficerAccount;
    }
}
