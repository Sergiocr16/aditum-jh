package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ProcedureVisitsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProcedureVisits and its DTO ProcedureVisitsDTO.
 */
@Mapper(componentModel = "spring", uses = {ProceduresMapper.class})
public interface ProcedureVisitsMapper extends EntityMapper<ProcedureVisitsDTO, ProcedureVisits> {

    @Mapping(source = "procedures.id", target = "proceduresId")
    ProcedureVisitsDTO toDto(ProcedureVisits procedureVisits); 

    @Mapping(source = "proceduresId", target = "procedures")
    ProcedureVisits toEntity(ProcedureVisitsDTO procedureVisitsDTO);

    default ProcedureVisits fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcedureVisits procedureVisits = new ProcedureVisits();
        procedureVisits.setId(id);
        return procedureVisits;
    }
}
