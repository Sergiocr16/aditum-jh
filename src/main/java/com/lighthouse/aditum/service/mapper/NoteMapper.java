package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.NoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Note and its DTO NoteDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CompanyMapper.class, })
public interface NoteMapper extends EntityMapper <NoteDTO, Note> {

    @Mapping(source = "house.id", target = "houseId")

    @Mapping(source = "company.id", target = "companyId")
    NoteDTO toDto(Note note);

    @Mapping(source = "houseId", target = "house")

    @Mapping(source = "companyId", target = "company")
    Note toEntity(NoteDTO noteDTO);

    default House fromHouseId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
    default Note fromId(Long id) {
        if (id == null) {
            return null;
        }
        Note note = new Note();
        note.setId(id);
        return note;
    }
}
