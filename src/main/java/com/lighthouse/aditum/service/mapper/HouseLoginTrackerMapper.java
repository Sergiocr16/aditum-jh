package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.HouseLoginTrackerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HouseLoginTracker and its DTO HouseLoginTrackerDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class})
public interface HouseLoginTrackerMapper extends EntityMapper<HouseLoginTrackerDTO, HouseLoginTracker> {

    @Mapping(source = "house.id", target = "houseId")
    HouseLoginTrackerDTO toDto(HouseLoginTracker houseLoginTracker);

    @Mapping(source = "houseId", target = "house")
    HouseLoginTracker toEntity(HouseLoginTrackerDTO houseLoginTrackerDTO);


    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }

    default HouseLoginTracker fromId(Long id) {
        if (id == null) {
            return null;
        }
        HouseLoginTracker houseLoginTracker = new HouseLoginTracker();
        houseLoginTracker.setId(id);
        return houseLoginTracker;
    }
}
