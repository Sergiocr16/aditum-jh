package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ComplaintDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Complaint and its DTO ComplaintDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class})
public interface ComplaintMapper extends EntityMapper<ComplaintDTO, Complaint> {

    @Mapping(source = "house.id", target = "houseId")
    ComplaintDTO toDto(Complaint complaint);

    @Mapping(source = "houseId", target = "house")
    Complaint toEntity(ComplaintDTO complaintDTO);

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }

    default Complaint fromId(Long id) {
        if (id == null) {
            return null;
        }
        Complaint complaint = new Complaint();
        complaint.setId(id);
        return complaint;
    }
}
