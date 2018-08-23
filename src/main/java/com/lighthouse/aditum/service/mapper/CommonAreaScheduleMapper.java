package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CommonAreaScheduleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CommonAreaSchedule and its DTO CommonAreaScheduleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommonAreaScheduleMapper extends EntityMapper <CommonAreaScheduleDTO, CommonAreaSchedule> {
    
    
    default CommonAreaSchedule fromId(Long id) {
        if (id == null) {
            return null;
        }
        CommonAreaSchedule commonAreaSchedule = new CommonAreaSchedule();
        commonAreaSchedule.setId(id);
        return commonAreaSchedule;
    }
}
