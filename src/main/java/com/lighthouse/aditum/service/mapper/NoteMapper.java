package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.NoteDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Note and its DTO NoteDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class})
public interface NoteMapper {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    NoteDTO noteToNoteDTO(Note note);

    List<NoteDTO> notesToNoteDTOs(List<Note> notes);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")

    Note noteDTOToNote(NoteDTO noteDTO);

    List<Note> noteDTOsToNotes(List<NoteDTO> noteDTOs);

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }

}
