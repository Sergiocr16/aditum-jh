package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.RevisionConfigTaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RevisionConfigTask and its DTO RevisionConfigTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {RevisionTaskCategoryMapper.class, RevisionConfigMapper.class})
public interface RevisionConfigTaskMapper extends EntityMapper<RevisionConfigTaskDTO, RevisionConfigTask> {

    @Mapping(source = "revisionTaskCategory.id", target = "revisionTaskCategoryId")
    @Mapping(source = "revisionConfig.id", target = "revisionConfigId")
    RevisionConfigTaskDTO toDto(RevisionConfigTask revisionConfigTask); 

    @Mapping(source = "revisionTaskCategoryId", target = "revisionTaskCategory")
    @Mapping(source = "revisionConfigId", target = "revisionConfig")
    RevisionConfigTask toEntity(RevisionConfigTaskDTO revisionConfigTaskDTO);

    default RevisionConfigTask fromId(Long id) {
        if (id == null) {
            return null;
        }
        RevisionConfigTask revisionConfigTask = new RevisionConfigTask();
        revisionConfigTask.setId(id);
        return revisionConfigTask;
    }
}
