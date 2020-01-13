package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.RevisionTaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RevisionTask and its DTO RevisionTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {RevisionMapper.class})
public interface RevisionTaskMapper extends EntityMapper<RevisionTaskDTO, RevisionTask> {

    @Mapping(source = "revision.id", target = "revisionId")
    RevisionTaskDTO toDto(RevisionTask revisionTask); 

    @Mapping(source = "revisionId", target = "revision")
    RevisionTask toEntity(RevisionTaskDTO revisionTaskDTO);

    default RevisionTask fromId(Long id) {
        if (id == null) {
            return null;
        }
        RevisionTask revisionTask = new RevisionTask();
        revisionTask.setId(id);
        return revisionTask;
    }
}
