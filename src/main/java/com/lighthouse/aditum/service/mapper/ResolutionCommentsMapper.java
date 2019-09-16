package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ResolutionCommentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ResolutionComments and its DTO ResolutionCommentsDTO.
 */
@Mapper(componentModel = "spring", uses = {AdminInfoMapper.class, ResolutionMapper.class})
public interface ResolutionCommentsMapper extends EntityMapper<ResolutionCommentsDTO, ResolutionComments> {

    @Mapping(source = "adminInfo.id", target = "adminInfoId")
    @Mapping(source = "resolution.id", target = "resolutionId")
    ResolutionCommentsDTO toDto(ResolutionComments resolutionComments); 

    @Mapping(source = "adminInfoId", target = "adminInfo")
    @Mapping(source = "resolutionId", target = "resolution")
    ResolutionComments toEntity(ResolutionCommentsDTO resolutionCommentsDTO);

    default ResolutionComments fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResolutionComments resolutionComments = new ResolutionComments();
        resolutionComments.setId(id);
        return resolutionComments;
    }
}
