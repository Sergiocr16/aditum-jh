package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ResidentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Resident and its DTO ResidentDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface ResidentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "house.id", target = "houseId")
    ResidentDTO residentToResidentDTO(Resident resident);

    List<ResidentDTO> residentsToResidentDTOs(List<Resident> residents);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "houseId", target = "house")
    Resident residentDTOToResident(ResidentDTO residentDTO);

    List<Resident> residentDTOsToResidents(List<ResidentDTO> residentDTOs);

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
}
