package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.HouseSecurityDirectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HouseSecurityDirection and its DTO HouseSecurityDirectionDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CompanyMapper.class})
public interface HouseSecurityDirectionMapper extends EntityMapper<HouseSecurityDirectionDTO, HouseSecurityDirection> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    HouseSecurityDirectionDTO toDto(HouseSecurityDirection houseSecurityDirection);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    HouseSecurityDirection toEntity(HouseSecurityDirectionDTO houseSecurityDirectionDTO);

    default HouseSecurityDirection fromId(Long id) {
        if (id == null) {
            return null;
        }
        HouseSecurityDirection houseSecurityDirection = new HouseSecurityDirection();
        houseSecurityDirection.setId(id);
        return houseSecurityDirection;
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
