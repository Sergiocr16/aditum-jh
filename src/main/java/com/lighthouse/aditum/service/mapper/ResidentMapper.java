package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ResidentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Resident and its DTO ResidentDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CompanyMapper.class, HouseMapper.class, })
public interface ResidentMapper extends EntityMapper <ResidentDTO, Resident> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "company.id", target = "companyId")

    @Mapping(source = "house.id", target = "houseId")
    ResidentDTO toDto(Resident resident);

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "companyId", target = "company")

    @Mapping(source = "houseId", target = "house")
    Resident toEntity(ResidentDTO residentDTO);

    default House fromHouseId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default Resident fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resident resident = new Resident();
        resident.setId(id);
        return resident;
    }
}
