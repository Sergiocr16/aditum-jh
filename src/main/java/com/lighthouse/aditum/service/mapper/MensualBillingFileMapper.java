package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.MensualBillingFileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MensualBillingFile and its DTO MensualBillingFileDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface MensualBillingFileMapper extends EntityMapper<MensualBillingFileDTO, MensualBillingFile> {

    @Mapping(source = "company.id", target = "companyId")
    MensualBillingFileDTO toDto(MensualBillingFile mensualBillingFile);

    @Mapping(source = "companyId", target = "company")
    MensualBillingFile toEntity(MensualBillingFileDTO mensualBillingFileDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default MensualBillingFile fromId(Long id) {
        if (id == null) {
            return null;
        }
        MensualBillingFile mensualBillingFile = new MensualBillingFile();
        mensualBillingFile.setId(id);
        return mensualBillingFile;
    }
}
