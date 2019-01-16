package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.HistoricalChargeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HistoricalCharge and its DTO HistoricalChargeDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, })
public interface HistoricalChargeMapper extends EntityMapper <HistoricalChargeDTO, HistoricalCharge> {

    @Mapping(source = "house.id", target = "houseId")
    HistoricalChargeDTO toDto(HistoricalCharge historicalCharge);

    @Mapping(source = "houseId", target = "house")
    HistoricalCharge toEntity(HistoricalChargeDTO historicalChargeDTO);
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default HistoricalCharge fromId(Long id) {
        if (id == null) {
            return null;
        }
        HistoricalCharge historicalCharge = new HistoricalCharge();
        historicalCharge.setId(id);
        return historicalCharge;
    }
}
