package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.SubsidiaryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Subsidiary and its DTO SubsidiaryDTO.
 */
@Mapper(componentModel = "spring", uses = {SubsidiaryTypeMapper.class, HouseMapper.class})
public interface SubsidiaryMapper extends EntityMapper<SubsidiaryDTO, Subsidiary> {

    @Mapping(source = "subsidiaryType.id", target = "subsidiaryTypeId")
    @Mapping(source = "house.id", target = "houseId")
    SubsidiaryDTO toDto(Subsidiary subsidiary);

    @Mapping(source = "subsidiaryTypeId", target = "subsidiaryType")
    @Mapping(source = "houseId", target = "house")
    Subsidiary toEntity(SubsidiaryDTO subsidiaryDTO);

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }

    default Subsidiary fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setId(id);
        return subsidiary;
    }
}
