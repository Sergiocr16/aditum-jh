package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.MacroVisitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MacroVisit and its DTO MacroVisitDTO.
 */
@Mapper(componentModel = "spring", uses = {MacroCondominiumMapper.class, CompanyMapper.class, HouseMapper.class})
public interface MacroVisitMapper extends EntityMapper<MacroVisitDTO, MacroVisit> {

    @Mapping(source = "macroCondominium.id", target = "macroCondominiumId")
    @Mapping(source = "macroCondominium.name", target = "macroCondominiumName")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "house.housenumber", target = "houseHousenumber")
    MacroVisitDTO toDto(MacroVisit macroVisit); 

    @Mapping(source = "macroCondominiumId", target = "macroCondominium")
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "houseId", target = "house")
    MacroVisit toEntity(MacroVisitDTO macroVisitDTO);
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default MacroVisit fromId(Long id) {
        if (id == null) {
            return null;
        }
        MacroVisit macroVisit = new MacroVisit();
        macroVisit.setId(id);
        return macroVisit;
    }
}
