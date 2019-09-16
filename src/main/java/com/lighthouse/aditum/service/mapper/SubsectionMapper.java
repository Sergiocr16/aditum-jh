package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.SubsectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Subsection and its DTO SubsectionDTO.
 */
@Mapper(componentModel = "spring", uses = {ArticleMapper.class})
public interface SubsectionMapper extends EntityMapper<SubsectionDTO, Subsection> {

    @Mapping(source = "article.id", target = "articleId")
    SubsectionDTO toDto(Subsection subsection); 

    @Mapping(source = "articleId", target = "article")
    Subsection toEntity(SubsectionDTO subsectionDTO);

    default Subsection fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subsection subsection = new Subsection();
        subsection.setId(id);
        return subsection;
    }
}
