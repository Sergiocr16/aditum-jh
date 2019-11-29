package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.EmergencyDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Emergency and its DTO EmergencyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmergencyMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "house.id", target = "houseId")
    EmergencyDTO emergencyToEmergencyDTO(Emergency emergency);

    List<EmergencyDTO> emergenciesToEmergencyDTOs(List<Emergency> emergencies);

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "houseId", target = "house")
    Emergency emergencyDTOToEmergency(EmergencyDTO emergencyDTO);

    List<Emergency> emergencyDTOsToEmergencies(List<EmergencyDTO> emergencyDTOs);

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
