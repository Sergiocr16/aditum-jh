package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.SecurityCompanyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SecurityCompany and its DTO SecurityCompanyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SecurityCompanyMapper extends EntityMapper <SecurityCompanyDTO, SecurityCompany> {
    
    
    default SecurityCompany fromId(Long id) {
        if (id == null) {
            return null;
        }
        SecurityCompany securityCompany = new SecurityCompany();
        securityCompany.setId(id);
        return securityCompany;
    }
}
