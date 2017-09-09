package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.RHAccountDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity RHAccount and its DTO RHAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CompanyMapper.class, })
public interface RHAccountMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    RHAccountDTO rHAccountToRHAccountDTO(RHAccount rHAccount);

    List<RHAccountDTO> rHAccountsToRHAccountDTOs(List<RHAccount> rHAccounts);

    @Mapping(source = "userId", target = "user")
    RHAccount rHAccountDTOToRHAccount(RHAccountDTO rHAccountDTO);

    List<RHAccount> rHAccountDTOsToRHAccounts(List<RHAccountDTO> rHAccountDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
