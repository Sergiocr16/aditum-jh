package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.VehiculeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Vehicule and its DTO VehiculeDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class})
public interface VehiculeMapper {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    VehiculeDTO vehiculeToVehiculeDTO(Vehicule vehicule);

    List<VehiculeDTO> vehiculesToVehiculeDTOs(List<Vehicule> vehicules);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    Vehicule vehiculeDTOToVehicule(VehiculeDTO vehiculeDTO);

    List<Vehicule> vehiculeDTOsToVehicules(List<VehiculeDTO> vehiculeDTOs);

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
}

