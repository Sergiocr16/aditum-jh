package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.BalanceByAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BalanceByAccount and its DTO BalanceByAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BalanceByAccountMapper extends EntityMapper <BalanceByAccountDTO, BalanceByAccount> {
    
    
    default BalanceByAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        BalanceByAccount balanceByAccount = new BalanceByAccount();
        balanceByAccount.setId(id);
        return balanceByAccount;
    }
}
