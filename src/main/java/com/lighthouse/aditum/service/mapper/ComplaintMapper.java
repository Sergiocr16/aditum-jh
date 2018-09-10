package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ComplaintDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Complaint and its DTO ComplaintDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CompanyMapper.class, ResidentMapper.class})
public interface ComplaintMapper extends EntityMapper<ComplaintDTO, Complaint> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "resident.id", target = "residentId")
    ComplaintDTO toDto(Complaint complaint); 

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "residentId", target = "resident")
    Complaint toEntity(ComplaintDTO complaintDTO);

    default Complaint fromId(Long id) {
        if (id == null) {
            return null;
        }
        Complaint complaint = new Complaint();
        complaint.setId(id);
        return complaint;
    }
}
