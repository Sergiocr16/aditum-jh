package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.SubsidiaryTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SubsidiaryType and its DTO SubsidiaryTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {SubsidiaryCategoryMapper.class, CompanyMapper.class})
public interface SubsidiaryTypeMapper extends EntityMapper<SubsidiaryTypeDTO, SubsidiaryType> {

    @Mapping(source = "subsidiaryCategory.id", target = "subsidiaryCategoryId")
    @Mapping(source = "company.id", target = "companyId")
    SubsidiaryTypeDTO toDto(SubsidiaryType subsidiaryType);

    @Mapping(source = "subsidiaryCategoryId", target = "subsidiaryCategory")
    @Mapping(source = "companyId", target = "company")
    SubsidiaryType toEntity(SubsidiaryTypeDTO subsidiaryTypeDTO);

    default SubsidiaryType fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubsidiaryType subsidiaryType = new SubsidiaryType();
        subsidiaryType.setId(id);
        return subsidiaryType;
    }
}
