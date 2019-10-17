package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.KeyWordsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity KeyWords and its DTO KeyWordsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KeyWordsMapper extends EntityMapper<KeyWordsDTO, KeyWords> {

    

    

    default KeyWords fromId(Long id) {
        if (id == null) {
            return null;
        }
        KeyWords keyWords = new KeyWords();
        keyWords.setId(id);
        return keyWords;
    }
}
