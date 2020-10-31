package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.HistoricalDefaulterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HistoricalDefaulter and its DTO HistoricalDefaulterDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, HouseMapper.class})
public interface HistoricalDefaulterMapper extends EntityMapper<HistoricalDefaulterDTO, HistoricalDefaulter> {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "house.id", target = "houseId")
    HistoricalDefaulterDTO toDto(HistoricalDefaulter historicalDefaulter);

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "houseId", target = "house")
    HistoricalDefaulter toEntity(HistoricalDefaulterDTO historicalDefaulterDTO);
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default HistoricalDefaulter fromId(Long id) {
        if (id == null) {
            return null;
        }
        HistoricalDefaulter historicalDefaulter = new HistoricalDefaulter();
        historicalDefaulter.setId(id);
        return historicalDefaulter;
    }
}
