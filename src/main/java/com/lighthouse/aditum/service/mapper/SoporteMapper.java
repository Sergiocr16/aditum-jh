package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.SoporteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Soporte and its DTO SoporteDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CompanyMapper.class})
public interface SoporteMapper extends EntityMapper<SoporteDTO, Soporte> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    SoporteDTO toDto(Soporte soporte); 

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    Soporte toEntity(SoporteDTO soporteDTO);
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default Soporte fromId(Long id) {
        if (id == null) {
            return null;
        }
        Soporte soporte = new Soporte();
        soporte.setId(id);
        return soporte;
    }
}
