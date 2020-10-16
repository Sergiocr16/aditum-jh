package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.PetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Pet and its DTO PetDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CompanyMapper.class})
public interface PetMapper extends EntityMapper<PetDTO, Pet> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    PetDTO toDto(Pet pet);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    Pet toEntity(PetDTO petDTO);

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default Pet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pet pet = new Pet();
        pet.setId(id);
        return pet;
    }
}
