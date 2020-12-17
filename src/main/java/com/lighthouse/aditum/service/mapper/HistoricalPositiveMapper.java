package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.HistoricalPositiveDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HistoricalPositive and its DTO HistoricalPositiveDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, HouseMapper.class})
public interface HistoricalPositiveMapper extends EntityMapper<HistoricalPositiveDTO, HistoricalPositive> {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "house.id", target = "houseId")
    HistoricalPositiveDTO toDto(HistoricalPositive historicalPositive);

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "houseId", target = "house")
    HistoricalPositive toEntity(HistoricalPositiveDTO historicalPositiveDTO);
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default HistoricalPositive fromId(Long id) {
        if (id == null) {
            return null;
        }
        HistoricalPositive historicalPositive = new HistoricalPositive();
        historicalPositive.setId(id);
        return historicalPositive;
    }
}
