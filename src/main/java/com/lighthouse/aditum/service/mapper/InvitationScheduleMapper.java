package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.InvitationScheduleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvitationSchedule and its DTO InvitationScheduleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InvitationScheduleMapper extends EntityMapper<InvitationScheduleDTO, InvitationSchedule> {

    

    

    default InvitationSchedule fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvitationSchedule invitationSchedule = new InvitationSchedule();
        invitationSchedule.setId(id);
        return invitationSchedule;
    }
}
