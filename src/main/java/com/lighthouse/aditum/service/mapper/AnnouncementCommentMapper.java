package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.AnnouncementCommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AnnouncementComment and its DTO AnnouncementCommentDTO.
 */
@Mapper(componentModel = "spring", uses = {ResidentMapper.class, AnnouncementMapper.class, AdminInfoMapper.class})
public interface AnnouncementCommentMapper extends EntityMapper<AnnouncementCommentDTO, AnnouncementComment> {

    @Mapping(source = "resident.id", target = "residentId")
    @Mapping(source = "announcement.id", target = "announcementId")
    @Mapping(source = "adminInfo.id", target = "adminInfoId")
    default AdminInfo adminInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setId(id);
        return adminInfo;
    }
    AnnouncementCommentDTO toDto(AnnouncementComment announcementComment);

    @Mapping(source = "residentId", target = "resident")

    default Resident residentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Resident resident = new Resident();
        resident.setId(id);
        return resident;
    }

    @Mapping(source = "announcementId", target = "announcement")
    @Mapping(source = "adminInfoId", target = "adminInfo")
    AnnouncementComment toEntity(AnnouncementCommentDTO announcementCommentDTO);

    default AnnouncementComment fromId(Long id) {
        if (id == null) {
            return null;
        }
        AnnouncementComment announcementComment = new AnnouncementComment();
        announcementComment.setId(id);
        return announcementComment;
    }


}
