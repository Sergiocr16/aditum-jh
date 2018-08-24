package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CommonAreaReservations and its DTO CommonAreaReservationsDTO.
 */
@Mapper(componentModel = "spring", uses = {CommonAreaMapper.class, })
public interface CommonAreaReservationsMapper extends EntityMapper <CommonAreaReservationsDTO, CommonAreaReservations> {

    @Mapping(source = "commonArea.id", target = "commonAreaId")
    CommonAreaReservationsDTO toDto(CommonAreaReservations commonAreaReservations); 

    @Mapping(source = "commonAreaId", target = "commonArea")
    CommonAreaReservations toEntity(CommonAreaReservationsDTO commonAreaReservationsDTO); 
    default CommonAreaReservations fromId(Long id) {
        if (id == null) {
            return null;
        }
        CommonAreaReservations commonAreaReservations = new CommonAreaReservations();
        commonAreaReservations.setId(id);
        return commonAreaReservations;
    }
}
