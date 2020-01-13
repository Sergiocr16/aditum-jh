package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.RevisionTaskCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RevisionTaskCategory and its DTO RevisionTaskCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RevisionTaskCategoryMapper extends EntityMapper<RevisionTaskCategoryDTO, RevisionTaskCategory> {

    

    

    default RevisionTaskCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        RevisionTaskCategory revisionTaskCategory = new RevisionTaskCategory();
        revisionTaskCategory.setId(id);
        return revisionTaskCategory;
    }
}
