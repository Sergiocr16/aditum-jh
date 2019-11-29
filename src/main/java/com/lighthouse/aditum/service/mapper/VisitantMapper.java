package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.VisitantDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Visitant and its DTO VisitantDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VisitantMapper {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    VisitantDTO visitantToVisitantDTO(Visitant visitant);

    List<VisitantDTO> visitantsToVisitantDTOs(List<Visitant> visitants);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    Visitant visitantDTOToVisitant(VisitantDTO visitantDTO);

    List<Visitant> visitantDTOsToVisitants(List<VisitantDTO> visitantDTOs);

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
