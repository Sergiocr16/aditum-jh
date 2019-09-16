package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ChapterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Chapter and its DTO ChapterDTO.
 */
@Mapper(componentModel = "spring", uses = {RegulationMapper.class})
public interface ChapterMapper extends EntityMapper<ChapterDTO, Chapter> {

    @Mapping(source = "regulation.id", target = "regulationId")
    ChapterDTO toDto(Chapter chapter); 

    @Mapping(source = "regulationId", target = "regulation")
    Chapter toEntity(ChapterDTO chapterDTO);

    default Chapter fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chapter chapter = new Chapter();
        chapter.setId(id);
        return chapter;
    }
}
