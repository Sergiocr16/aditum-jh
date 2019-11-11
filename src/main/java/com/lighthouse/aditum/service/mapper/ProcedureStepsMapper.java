package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ProcedureStepsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProcedureSteps and its DTO ProcedureStepsDTO.
 */
@Mapper(componentModel = "spring", uses = {ProceduresMapper.class})
public interface ProcedureStepsMapper extends EntityMapper<ProcedureStepsDTO, ProcedureSteps> {

    @Mapping(source = "procedures.id", target = "proceduresId")
    ProcedureStepsDTO toDto(ProcedureSteps procedureSteps); 

    @Mapping(source = "proceduresId", target = "procedures")
    ProcedureSteps toEntity(ProcedureStepsDTO procedureStepsDTO);

    default ProcedureSteps fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcedureSteps procedureSteps = new ProcedureSteps();
        procedureSteps.setId(id);
        return procedureSteps;
    }
}
