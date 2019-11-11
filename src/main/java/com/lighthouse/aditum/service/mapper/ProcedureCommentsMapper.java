package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ProcedureCommentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProcedureComments and its DTO ProcedureCommentsDTO.
 */
@Mapper(componentModel = "spring", uses = {ProceduresMapper.class, AdminInfoMapper.class})
public interface ProcedureCommentsMapper extends EntityMapper<ProcedureCommentsDTO, ProcedureComments> {

    @Mapping(source = "procedures.id", target = "proceduresId")
    @Mapping(source = "adminInfo.id", target = "adminInfoId")
    ProcedureCommentsDTO toDto(ProcedureComments procedureComments);

    @Mapping(source = "proceduresId", target = "procedures")
    @Mapping(source = "adminInfoId", target = "adminInfo")
    ProcedureComments toEntity(ProcedureCommentsDTO procedureCommentsDTO);

    default AdminInfo adminInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setId(id);
        return adminInfo;
    }

    default ProcedureComments fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcedureComments procedureComments = new ProcedureComments();
        procedureComments.setId(id);
        return procedureComments;
    }
}
