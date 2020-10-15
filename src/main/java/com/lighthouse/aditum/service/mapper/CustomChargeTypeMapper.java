package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CustomChargeTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomChargeType and its DTO CustomChargeTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface CustomChargeTypeMapper extends EntityMapper<CustomChargeTypeDTO, CustomChargeType> {

    @Mapping(source = "company.id", target = "companyId")
    CustomChargeTypeDTO toDto(CustomChargeType customChargeType);

    @Mapping(source = "companyId", target = "company")
    CustomChargeType toEntity(CustomChargeTypeDTO customChargeTypeDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default CustomChargeType fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomChargeType customChargeType = new CustomChargeType();
        customChargeType.setId(id);
        return customChargeType;
    }
}
