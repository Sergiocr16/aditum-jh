package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ArticleCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ArticleCategory and its DTO ArticleCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArticleCategoryMapper extends EntityMapper<ArticleCategoryDTO, ArticleCategory> {

    

    

    default ArticleCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setId(id);
        return articleCategory;
    }
}
