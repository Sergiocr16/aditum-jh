package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.VisitantInvitationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity VisitantInvitation and its DTO VisitantInvitationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VisitantInvitationMapper extends EntityMapper<VisitantInvitationDTO, VisitantInvitation> {

    

    

    default VisitantInvitation fromId(Long id) {
        if (id == null) {
            return null;
        }
        VisitantInvitation visitantInvitation = new VisitantInvitation();
        visitantInvitation.setId(id);
        return visitantInvitation;
    }
}
