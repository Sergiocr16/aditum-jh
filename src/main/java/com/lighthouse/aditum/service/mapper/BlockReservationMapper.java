package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.BlockReservationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BlockReservation and its DTO BlockReservationDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class})
public interface BlockReservationMapper extends EntityMapper<BlockReservationDTO, BlockReservation> {

    @Mapping(source = "house.id", target = "houseId")
    BlockReservationDTO toDto(BlockReservation blockReservation);

    @Mapping(source = "houseId", target = "house")
    BlockReservation toEntity(BlockReservationDTO blockReservationDTO);

    default BlockReservation fromId(Long id) {
        if (id == null) {
            return null;
        }
        BlockReservation blockReservation = new BlockReservation();
        blockReservation.setId(id);
        return blockReservation;
    }

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
}
