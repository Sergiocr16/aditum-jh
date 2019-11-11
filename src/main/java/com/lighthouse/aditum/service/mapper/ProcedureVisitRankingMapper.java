package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ProcedureVisitRankingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProcedureVisitRanking and its DTO ProcedureVisitRankingDTO.
 */
@Mapper(componentModel = "spring", uses = {ProcedureVisitsMapper.class})
public interface ProcedureVisitRankingMapper extends EntityMapper<ProcedureVisitRankingDTO, ProcedureVisitRanking> {

    @Mapping(source = "procedureVisits.id", target = "procedureVisitsId")
    ProcedureVisitRankingDTO toDto(ProcedureVisitRanking procedureVisitRanking); 

    @Mapping(source = "procedureVisitsId", target = "procedureVisits")
    ProcedureVisitRanking toEntity(ProcedureVisitRankingDTO procedureVisitRankingDTO);

    default ProcedureVisitRanking fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcedureVisitRanking procedureVisitRanking = new ProcedureVisitRanking();
        procedureVisitRanking.setId(id);
        return procedureVisitRanking;
    }
}
