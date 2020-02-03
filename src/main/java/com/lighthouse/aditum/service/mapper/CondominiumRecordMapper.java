package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CondominiumRecordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CondominiumRecord and its DTO CondominiumRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface CondominiumRecordMapper extends EntityMapper<CondominiumRecordDTO, CondominiumRecord> {

    @Mapping(source = "company.id", target = "companyId")
    CondominiumRecordDTO toDto(CondominiumRecord condominiumRecord);

    @Mapping(source = "companyId", target = "company")
    CondominiumRecord toEntity(CondominiumRecordDTO condominiumRecordDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default CondominiumRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        CondominiumRecord condominiumRecord = new CondominiumRecord();
        condominiumRecord.setId(id);
        return condominiumRecord;
    }
}
