package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.BalanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Balance and its DTO BalanceDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, })
public interface BalanceMapper extends EntityMapper <BalanceDTO, Balance> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "house.housenumber", target = "houseHousenumber")
    BalanceDTO toDto(Balance balance);

    @Mapping(source = "houseId", target = "house")
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    Balance toEntity(BalanceDTO balanceDTO);
    default Balance fromId(Long id) {
        if (id == null) {
            return null;
        }
        Balance balance = new Balance();
        balance.setId(id);
        return balance;
    }
}
