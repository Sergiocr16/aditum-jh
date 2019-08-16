package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ResolutionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Resolution and its DTO ResolutionDTO.
 */
@Mapper(componentModel = "spring", uses = {ArticleMapper.class, KeyWordsMapper.class, ArticleCategoryMapper.class})
public interface ResolutionMapper extends EntityMapper<ResolutionDTO, Resolution> {

    

    

    default Resolution fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resolution resolution = new Resolution();
        resolution.setId(id);
        return resolution;
    }
}
