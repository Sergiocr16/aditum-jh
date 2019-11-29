package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.MacroAdminAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MacroAdminAccount and its DTO MacroAdminAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {MacroCondominiumMapper.class, UserMapper.class})
public interface MacroAdminAccountMapper extends EntityMapper<MacroAdminAccountDTO, MacroAdminAccount> {

    @Mapping(source = "macroCondominium.id", target = "macroCondominiumId")
    @Mapping(source = "macroCondominium.name", target = "macroCondominiumName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    MacroAdminAccountDTO toDto(MacroAdminAccount macroAdminAccount); 

    @Mapping(source = "macroCondominiumId", target = "macroCondominium")
    @Mapping(source = "userId", target = "user")
    MacroAdminAccount toEntity(MacroAdminAccountDTO macroAdminAccountDTO);

    default MacroAdminAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        MacroAdminAccount macroAdminAccount = new MacroAdminAccount();
        macroAdminAccount.setId(id);
        return macroAdminAccount;
    }
}
