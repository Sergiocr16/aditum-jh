package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.AccessDoorDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AccessDoor and its DTO AccessDoorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccessDoorMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "watch.id", target = "watchId")
    AccessDoorDTO accessDoorToAccessDoorDTO(AccessDoor accessDoor);

    List<AccessDoorDTO> accessDoorsToAccessDoorDTOs(List<AccessDoor> accessDoors);

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "watchId", target = "watch")
    AccessDoor accessDoorDTOToAccessDoor(AccessDoorDTO accessDoorDTO);

    List<AccessDoor> accessDoorDTOsToAccessDoors(List<AccessDoorDTO> accessDoorDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default Watch watchFromId(Long id) {
        if (id == null) {
            return null;
        }
        Watch watch = new Watch();
        watch.setId(id);
        return watch;
    }
}
