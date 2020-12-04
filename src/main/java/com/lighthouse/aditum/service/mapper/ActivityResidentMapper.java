package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ActivityResidentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityResident and its DTO ActivityResidentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActivityResidentMapper extends EntityMapper<ActivityResidentDTO, ActivityResident> {

    

    

    default ActivityResident fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityResident activityResident = new ActivityResident();
        activityResident.setId(id);
        return activityResident;
    }
}
