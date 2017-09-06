package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.OfficerAccountDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OfficerAccount and its DTO OfficerAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface OfficerAccountMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    OfficerAccountDTO officerAccountToOfficerAccountDTO(OfficerAccount officerAccount);

    List<OfficerAccountDTO> officerAccountsToOfficerAccountDTOs(List<OfficerAccount> officerAccounts);

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "userId", target = "user")
    OfficerAccount officerAccountDTOToOfficerAccount(OfficerAccountDTO officerAccountDTO);

    List<OfficerAccount> officerAccountDTOsToOfficerAccounts(List<OfficerAccountDTO> officerAccountDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
