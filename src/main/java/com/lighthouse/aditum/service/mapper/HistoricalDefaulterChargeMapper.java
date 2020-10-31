package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.HistoricalDefaulterChargeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HistoricalDefaulterCharge and its DTO HistoricalDefaulterChargeDTO.
 */
@Mapper(componentModel = "spring", uses = {HistoricalDefaulterMapper.class})
public interface HistoricalDefaulterChargeMapper extends EntityMapper<HistoricalDefaulterChargeDTO, HistoricalDefaulterCharge> {

    @Mapping(source = "historicalDefaulter.id", target = "historicalDefaulterId")
    HistoricalDefaulterChargeDTO toDto(HistoricalDefaulterCharge historicalDefaulterCharge); 

    @Mapping(source = "historicalDefaulterId", target = "historicalDefaulter")
    HistoricalDefaulterCharge toEntity(HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO);

    default HistoricalDefaulterCharge fromId(Long id) {
        if (id == null) {
            return null;
        }
        HistoricalDefaulterCharge historicalDefaulterCharge = new HistoricalDefaulterCharge();
        historicalDefaulterCharge.setId(id);
        return historicalDefaulterCharge;
    }
}
