package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.AnnouncementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Announcement and its DTO AnnouncementDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, })
public interface AnnouncementMapper extends EntityMapper <AnnouncementDTO, Announcement> {

    @Mapping(source = "company.id", target = "companyId")
    AnnouncementDTO toDto(Announcement announcement);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    @Mapping(source = "companyId", target = "company")
    Announcement toEntity(AnnouncementDTO announcementDTO);
    default Announcement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Announcement announcement = new Announcement();
        announcement.setId(id);
        return announcement;
    }
}
