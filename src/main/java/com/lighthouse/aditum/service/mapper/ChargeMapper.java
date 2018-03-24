package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ChargeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Charge and its DTO ChargeDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, })
public interface ChargeMapper extends EntityMapper <ChargeDTO, Charge> {

    @Mapping(source = "house.id", target = "houseId")
    ChargeDTO toDto(Charge charge);

    @Mapping(source = "houseId", target = "house")
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    Charge toEntity(ChargeDTO chargeDTO);
    default Charge fromId(Long id) {
        if (id == null) {
            return null;
        }
        Charge charge = new Charge();
        charge.setId(id);
        return charge;
    }
}
