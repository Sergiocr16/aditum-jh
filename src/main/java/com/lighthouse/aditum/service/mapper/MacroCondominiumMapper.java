package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.MacroCondominiumDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MacroCondominium and its DTO MacroCondominiumDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface MacroCondominiumMapper extends EntityMapper<MacroCondominiumDTO, MacroCondominium> {

    

    

    default MacroCondominium fromId(Long id) {
        if (id == null) {
            return null;
        }
        MacroCondominium macroCondominium = new MacroCondominium();
        macroCondominium.setId(id);
        return macroCondominium;
    }
}
