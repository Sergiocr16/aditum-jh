package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.WaterConsumptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WaterConsumption and its DTO WaterConsumptionDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class})
public interface WaterConsumptionMapper extends EntityMapper<WaterConsumptionDTO, WaterConsumption> {

    @Mapping(source = "house.id", target = "houseId")
    WaterConsumptionDTO toDto(WaterConsumption waterConsumption);

    @Mapping(source = "houseId", target = "house")
    WaterConsumption toEntity(WaterConsumptionDTO waterConsumptionDTO);

    default WaterConsumption fromId(Long id) {
        if (id == null) {
            return null;
        }
        WaterConsumption waterConsumption = new WaterConsumption();
        waterConsumption.setId(id);
        return waterConsumption;
    }

    default House housefromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
}
