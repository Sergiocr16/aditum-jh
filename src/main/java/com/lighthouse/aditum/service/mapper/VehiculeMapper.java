package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.VehiculeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vehicule and its DTO VehiculeDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CompanyMapper.class, })
public interface VehiculeMapper extends EntityMapper <VehiculeDTO, Vehicule> {

    @Mapping(source = "house.id", target = "houseId")

    @Mapping(source = "company.id", target = "companyId")
    VehiculeDTO toDto(Vehicule vehicule);

    @Mapping(source = "houseId", target = "house")

    @Mapping(source = "companyId", target = "company")
    Vehicule toEntity(VehiculeDTO vehiculeDTO);

    default House fromHouseId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default Vehicule fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vehicule vehicule = new Vehicule();
        vehicule.setId(id);
        return vehicule;
    }
}
