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
    VehiculeDTO toDto(Vehicule vehicule);

    List<VehiculeDTO> vehiculesToVehiculeDTOs(List<Vehicule> vehicules);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    Vehicule toEntity(VehiculeDTO vehiculeDTO);

    List<Vehicule> vehiculeDTOsToVehicules(List<VehiculeDTO> vehiculeDTOs);

    default Vehicule fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vehicule vehicule = new Vehicule();
        vehicule.setId(id);
        return vehicule;
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

