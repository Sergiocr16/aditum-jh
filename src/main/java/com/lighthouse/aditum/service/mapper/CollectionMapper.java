package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CollectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Collection and its DTO CollectionDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, })
public interface CollectionMapper extends EntityMapper <CollectionDTO, Collection> {

    @Mapping(source = "house.id", target = "houseId")
    CollectionDTO toDto(Collection collection);

    @Mapping(source = "houseId", target = "house")
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    Collection toEntity(CollectionDTO collectionDTO);
    default Collection fromId(Long id) {
        if (id == null) {
            return null;
        }
        Collection collection = new Collection();
        collection.setId(id);
        return collection;
    }
}
