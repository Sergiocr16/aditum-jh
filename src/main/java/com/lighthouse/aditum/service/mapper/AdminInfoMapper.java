package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AdminInfo and its DTO AdminInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface AdminInfoMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "company.id", target = "companyId")
    AdminInfoDTO adminInfoToAdminInfoDTO(AdminInfo adminInfo);

    List<AdminInfoDTO> adminInfosToAdminInfoDTOs(List<AdminInfo> adminInfos);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "companyId", target = "company")
    AdminInfo adminInfoDTOToAdminInfo(AdminInfoDTO adminInfoDTO);

    List<AdminInfo> adminInfoDTOsToAdminInfos(List<AdminInfoDTO> adminInfoDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
