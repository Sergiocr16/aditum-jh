package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.WaterConsumptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WaterConsumption and its DTO WaterConsumptionDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, ChargeMapper.class})
public interface WaterConsumptionMapper extends EntityMapper<WaterConsumptionDTO, WaterConsumption> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "charge.id", target = "chargeId")
    WaterConsumptionDTO toDto(WaterConsumption waterConsumption); 

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "chargeId", target = "charge")
    WaterConsumption toEntity(WaterConsumptionDTO waterConsumptionDTO);

    default WaterConsumption fromId(Long id) {
        if (id == null) {
            return null;
        }
        WaterConsumption waterConsumption = new WaterConsumption();
        waterConsumption.setId(id);
        return waterConsumption;
    }
}
