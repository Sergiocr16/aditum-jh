package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ComplaintCommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ComplaintComment and its DTO ComplaintCommentDTO.
 */
@Mapper(componentModel = "spring", uses = {ResidentMapper.class, AdminInfoMapper.class, ComplaintMapper.class})
public interface ComplaintCommentMapper extends EntityMapper<ComplaintCommentDTO, ComplaintComment> {

    @Mapping(source = "resident.id", target = "residentId")
    @Mapping(source = "adminInfo.id", target = "adminInfoId")
    @Mapping(source = "complaint.id", target = "complaintId")
    ComplaintCommentDTO toDto(ComplaintComment complaintComment);

    @Mapping(source = "residentId", target = "resident")
    @Mapping(source = "adminInfoId", target = "adminInfo")
    @Mapping(source = "complaintId", target = "complaint")
    ComplaintComment toEntity(ComplaintCommentDTO complaintCommentDTO);

    default AdminInfo adminInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setId(id);
        return adminInfo;
    }

    default ComplaintComment fromId(Long id) {
        if (id == null) {
            return null;
        }
        ComplaintComment complaintComment = new ComplaintComment();
        complaintComment.setId(id);
        return complaintComment;
    }
}
