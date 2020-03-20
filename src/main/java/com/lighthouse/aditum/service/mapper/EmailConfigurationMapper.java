package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.EmailConfigurationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EmailConfiguration and its DTO EmailConfigurationDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface EmailConfigurationMapper extends EntityMapper<EmailConfigurationDTO, EmailConfiguration> {

    @Mapping(source = "company.id", target = "companyId")
    EmailConfigurationDTO toDto(EmailConfiguration emailConfiguration);

    @Mapping(source = "companyId", target = "company")
    EmailConfiguration toEntity(EmailConfigurationDTO emailConfigurationDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default EmailConfiguration fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setId(id);
        return emailConfiguration;
    }
}
