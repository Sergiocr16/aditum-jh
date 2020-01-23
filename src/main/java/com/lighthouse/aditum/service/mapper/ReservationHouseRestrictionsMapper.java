package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ReservationHouseRestrictionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReservationHouseRestrictions and its DTO ReservationHouseRestrictionsDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CommonAreaMapper.class})
public interface ReservationHouseRestrictionsMapper extends EntityMapper<ReservationHouseRestrictionsDTO, ReservationHouseRestrictions> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "commonArea.id", target = "commonAreaId")
    ReservationHouseRestrictionsDTO toDto(ReservationHouseRestrictions reservationHouseRestrictions);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "commonAreaId", target = "commonArea")
    ReservationHouseRestrictions toEntity(ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }

    default ReservationHouseRestrictions fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReservationHouseRestrictions reservationHouseRestrictions = new ReservationHouseRestrictions();
        reservationHouseRestrictions.setId(id);
        return reservationHouseRestrictions;
    }
}
