package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CommonAreaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CommonArea and its DTO CommonAreaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommonAreaMapper extends EntityMapper <CommonAreaDTO, CommonArea> {
    
    
    default CommonArea fromId(Long id) {
        if (id == null) {
            return null;
        }
        CommonArea commonArea = new CommonArea();
        commonArea.setId(id);
        return commonArea;
    }
}
